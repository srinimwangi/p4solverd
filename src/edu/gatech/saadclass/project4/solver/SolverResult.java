package edu.gatech.saadclass.project4.solver;

import java.util.ArrayList;

public class SolverResult {
    private ArrayList<SolverResultEntry> results;
    private Integer satisfaction;
    @SuppressWarnings("unused")
	private Integer term;

    public SolverResult(Integer term) {
        results = new ArrayList<>();
        this.term = term;
    }

    public void addStudentForCourse(Integer courseReferenceNumber, Integer studentId, Integer rank) {
        results.add(new SolverResultEntry(courseReferenceNumber, studentId, rank));
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer factor) {
        satisfaction = factor;
    }
}
