package edu.gatech.saadclass.project4.school;

import java.util.HashMap;

public class Specialization {
    private HashMap<String, Constraint> constraints = new HashMap<>();
    private HashMap<String, Course> courses = new HashMap<>();
    private int id;
    private Program internal_program;
    @SuppressWarnings("unused")
	private int program;
    private String title;

    public Specialization(Program program) {
        this.internal_program = program;
    }

    public void convertRawToNative() {

    }

    /**
     * @return the constraints
     */
    public HashMap<String, Constraint> getConstraints() {
        return constraints;
    }

    /**
     * @return the courses
     */
    public HashMap<String, Course> getCourses() {
        return courses;
    }

    public int getId() {
        return id;
    }

    public Program getInternal_program() {
        return internal_program;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param constraints the constraints to set
     */
    public void setConstraints(HashMap<String, Constraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(HashMap<String, Course> courses) {
        this.courses = courses;
    }

    public void setInternal_program(Program internal_program) {
        this.internal_program = internal_program;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
