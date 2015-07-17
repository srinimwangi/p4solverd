package edu.gatech.saadclass.project4;

import edu.gatech.saadclass.project4.school.CourseCatalog;
import edu.gatech.saadclass.project4.school.School;
import edu.gatech.saadclass.project4.solver.GurobiSolver;
import edu.gatech.saadclass.project4.solver.SolverResult;

import java.io.File;

public class Main {
    private static int SOLVER_NAIVE = 0;
    private static int SOLVER_GUROBI = 1;

    public static void main(String[] args) {
        int solverType = SOLVER_GUROBI;

        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        School school = new School("Georgia Institute of Technology", 201508);
        CourseCatalog courseCatalog = new CourseCatalog(school);

        try {
            String fileDir;
            fileDir = new String("test" + File.separator + "Resources"
                    + File.separator);
            courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
        } catch (Exception e) {
            System.out.println("Fatal error while parsing course catalog, exception details:");
            e.printStackTrace();
            System.exit(1);
        }

        try {
           	school.loadStudentCoursePreferences(args[0]);
        } catch (Exception e) {
            System.out.println("Fatal error while parsing input data, exception details:");
            e.printStackTrace();
            System.exit(1);
        }

        // FIXME: Hardcoded for testing.
        SolverResult solverResult = new SolverResult(201508);

        if (solverType == SOLVER_GUROBI) {
            solverResult = new GurobiSolver().solve();
        } else {
            System.out.println("Incorrect solver type.");
            System.exit(1);
        }

        System.out.println("Program Summary");
        System.out.println("------------------------------------");
        System.out.println("Student Count: " + school.getStudents().size());
        System.out.println("Course Catalog Size: " + courseCatalog.getCourses().size());
        System.out.println("Solver Type: " + solverToString(solverType));
        System.out.println();

        System.out.println("Solution Summary");
        System.out.println("------------------------------------");

        if (solverResult.getSatisfaction() > 0) {
            System.out.println("Everyone is happy!");
            System.out.println("Satisfaction = " + solverResult.getSatisfaction());
        } else {
            System.out.println("We can't get no satisfaction.");
            System.exit(1);
        }

    }

    private static void printUsage() {
        System.out.println("The first argument must be a filename containing the student data.");
    }

    private static String solverToString(int solverId) {
        if (solverId == SOLVER_GUROBI) {
            return "Gurobi";
        } else if (solverId == SOLVER_NAIVE) {
            return "Naive";
        }
        return "Unknown";
    }
}
