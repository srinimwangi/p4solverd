package edu.gatech.saadclass.project4.solver;

import edu.gatech.saadclass.project4.school.CourseOffering;
import edu.gatech.saadclass.project4.school.School;
import edu.gatech.saadclass.project4.school.Student;
import edu.gatech.saadclass.project4.school.Term;
import edu.gatech.saadclass.project4.utils.CachedObjectFactory;
import gurobi.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class GurobiSolver extends Solver {
    private GRBEnv env;
    private GRBModel model;

    public GurobiSolver() {
        try {
            this.env = new GRBEnv("project4.log");
            this.env.set(GRB.IntParam.OutputFlag, 0);
            this.model = new GRBModel(this.env);
            this.model.getEnv().set(GRB.IntParam.Threads, 1);
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    @Override
	public void finalize() {
        try {
            this.model.dispose();
            this.env.dispose();
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    @Override
	public SolverResult solve() {
        CachedObjectFactory cof = new CachedObjectFactory();
        ArrayList<Integer> student_ids = cof.getStudentIds();
        ArrayList<Integer> crns = cof.getCrns();
        HashMap<Integer, Student> students = cof.getStudents();
        Term term = cof.getCurrentTerm();
        return solve(student_ids, crns, students, term);
    }

    public SolverResult solve(School school) {
        ArrayList<Integer> student_ids = new ArrayList<>(school.getStudents().keySet());
        ArrayList<Integer> crns = new ArrayList<>(school.getTerms().get(school.getCurrent_term()).getCourse_offerings().keySet());
        HashMap<Integer, Student> students = school.getStudents();
        Term term = school.getTerms().get(school.getCurrent_term());
        return solve(student_ids, crns, students, term);
    }

    public SolverResult solve(ArrayList<Integer> student_ids, ArrayList<Integer> crns, HashMap<Integer, Student> students,
                              Term term) {
        if (!this.getSolved()) {
            try {

                Boolean write = false;
                Boolean console = false;
                PrintWriter writer = null;

                try {
                    if (write) writer = new PrintWriter("project4lp.out", "UTF-8");

                    //Add the variables

                    //We want to maximize satisfaction
                    GRBVar satisfaction = model.addVar(0, GRB.INFINITY, 0.0, GRB.INTEGER, "SATISFACTION");
                    GRBVar[][] y = new GRBVar[student_ids.size()][crns.size()];

                    //Variables for all students and classes
                    for (int i = 0; i < student_ids.size(); i++) {
                        for (int j = 0; j < crns.size(); j++) {
                            String label = "y_" + student_ids.get(i) + "_" + crns.get(j);
                            y[i][j] = this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, label);
                            //System.out.println(label);
                            if (write) writer.println(label);
                        }
                    }

                    // Integrate new variables
                    this.model.update();

                    // Set objective function to maximize satisfaction
                    GRBLinExpr expr = new GRBLinExpr();
                    expr.addTerm(1.0, satisfaction);
                    this.model.setObjective(expr, GRB.MAXIMIZE);

                    //Constraints

                    // Students can take limited classes based on their foundational courses completed
                    for (int i = 0; i < student_ids.size(); i++) {
                        GRBLinExpr lhs = new GRBLinExpr();
                        for (int j = 0; j < crns.size(); j++) {
                            lhs.addTerm(1.0, y[i][j]);
                            if (console) System.out.print("y_" + i + "_" + j + " + ");
                            if (write) writer.print("y_" + i + "_" + j + " + ");
                        }
                        String label = "student_course_limit_" + student_ids.get(i);
                        model.addConstr(lhs,
                                GRB.LESS_EQUAL,
                                students
                                        .get(student_ids.get(i))
                                        .getCourse_limit(),
                                label);
                        if (console) System.out.print("<= " + students
                                .get(student_ids.get(i))
                                .getCourse_limit());
                        if (write) writer.print("<= " + students
                                .get(student_ids.get(i))
                                .getCourse_limit());
                        if (console) System.out.println(label);
                        if (write) writer.println(label);
                    }

                    // Add capacity limits per class
                    for (int j = 0; j < crns.size(); j++) {
                        GRBLinExpr lhs = new GRBLinExpr();
                        for (int i = 0; i < student_ids.size(); i++) {
                            lhs.addTerm(1.0, y[i][j]);
                            if (console) System.out.print("y_" + i + "_" + j + " + ");
                            if (write) writer.print("y_" + i + "_" + j + " + ");
                        }
                        String label = "capacity_" + crns.get(j);
                        model.addConstr(lhs,
                                GRB.LESS_EQUAL,
                                term
                                        .getCourse_offerings()
                                        .get(crns.get(j))
                                        .getCapacity(),
                                label);
                        if (console) System.out.print("<= " + term
                                .getCourse_offerings()
                                .get(crns.get(j))
                                .getCapacity());
                        if (write) writer.print("<= " + term
                                .getCourse_offerings()
                                .get(crns.get(j))
                                .getCapacity());
                        if (console) System.out.println(label);
                        if (write) writer.println(label);
                    }

                    //TODO Add a config file for course history weighting per hour or course
                    //TODO Add config file for preference weighting
                    // e.g. 8 for a 1 preference, 5 for a 2, 3 for a 3, 2 - 4, 1 - 5
                    // Satisfaction should be a combination of seniority and preference
                    // multiply student hours completed by preference weight
                    GRBLinExpr lhs = new GRBLinExpr();
                    for (int i = 0; i < student_ids.size(); i++) {
                        for (int j = 0; j < crns.size(); j++) {
                            for (Entry<Integer, CourseOffering> courseOfferingEntry : students
                                    .get(student_ids.get(i))
                                    .getPreferred_courses()
                                    .entrySet()) {
                                if (crns.get(j) ==
                                        courseOfferingEntry.getValue().getCourse_registration_number()) {
                                    Integer preference = 0;
                                    switch (courseOfferingEntry.getKey()) {
                                        case 1:
                                            preference = 8;
                                            break;
                                        case 2:
                                            preference = 5;
                                            break;
                                        case 3:
                                            preference = 3;
                                            break;
                                        case 4:
                                            preference = 2;
                                            break;
                                        case 5:
                                            preference = 1;
                                            break;
                                    }
                                    lhs.addTerm((double) preference *
                                                    (students
                                                            .get(student_ids.get(i))
                                                            .getComplete_hours() + 1),
                                            y[i][j]);
                                    if (console) System.out.print(preference *
                                            (students
                                                    .get(student_ids.get(i))
                                                    .getComplete_hours() + 1) + "y_" + i + "_" + j);
                                    if (write) writer.print(preference *
                                            (students
                                                    .get(student_ids.get(i))
                                                    .getComplete_hours() + 1) + "y_" + i + "_" + j);
                                }
                            }
                        }

                    }
                    lhs.addTerm(-1.0, satisfaction);
                    if (console) System.out.print("- " + satisfaction);
                    if (write) writer.print("- " + satisfaction);
                    String label = "pref_ALL";
                    model.addConstr(lhs, GRB.EQUAL, 0.0, label);
                    if (console) System.out.print("= " + 0);
                    if (write) writer.print("= " + 0);
                    if (console) System.out.println(label);
                    if (write) writer.println(label);
                    if (write) writer.close();

                    //optimize
                    this.model.optimize();
                    this.result = new SolverResult(term.getTerm_id());
                    this.solved = true;

                    //PrintWriter writer;
                    try {
                        result.setSatisfaction((int)Math.round(satisfaction.get(GRB.DoubleAttr.X)));
                        if (write) writer = new PrintWriter("project4.out", "UTF-8");
                        System.out.println("Satisfaction = " + (int)Math.round(model.get(GRB.DoubleAttr.ObjVal)));
                        if (write) writer.println("Satisfaction = " + (int)Math.round(model.get(GRB.DoubleAttr.ObjVal)));
                        System.out.println(satisfaction.get(GRB.StringAttr.VarName)
                        		+ " " + (int)Math.round(satisfaction.get(GRB.DoubleAttr.X)));
                        if (write) writer.println(satisfaction.get(GRB.StringAttr.VarName)
                                + " " + (int)Math.round(satisfaction.get(GRB.DoubleAttr.X)));

                        for (int i = 0; i < student_ids.size(); i++) {
                            for (int j = 0; j < crns.size(); j++) {
                                if ((int)Math.round(y[i][j].get(GRB.DoubleAttr.X)) > 0) {
                                    this.result.addStudentForCourse(crns.get(j), student_ids.get(i),
                                            students.get(student_ids.get(i))
                                                    .getPreferred_course_rank(crns.get(j)));
                                }
                                if (console) System.out.println(y[i][j].get(GRB.StringAttr.VarName)
                                		+ " " + (int)Math.round(y[i][j].get(GRB.DoubleAttr.X)));
                                if (write) writer.println(y[i][j].get(GRB.StringAttr.VarName)
                                        + " " + (int) Math.round(y[i][j].get(GRB.DoubleAttr.X)));
                            }
                        }
                        if (write) writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Write the lp
                if (write) model.write("project4.lp");

                // Dispose of model and environment
                model.dispose();
                env.dispose();

            } catch (GRBException e) {
                unsolve();
                System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            } catch (NullPointerException npe) {
                unsolve();
                System.out.println("NullPointerException, most likely Program is null");
                npe.printStackTrace();
            }

        }

        return this.result;
    }

    protected void unsolve() {
        this.result = null;
    }
}
