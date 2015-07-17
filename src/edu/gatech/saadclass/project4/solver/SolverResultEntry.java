package edu.gatech.saadclass.project4.solver;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class SolverResultEntry {
	@SuppressWarnings("unused")
    private Integer course_reference_number;
    @SuppressWarnings("unused")
	private Integer student_id;
	@SuppressWarnings("unused")
    private Integer rank;

	/**
	 * The constructor. This class is used to return results.
	 * 
	 * @param course_reference_number the course reference number
	 * @param student_id the student id
	 * @param rank the rank
	 */
    public SolverResultEntry(int course_reference_number, int student_id, int rank) {
        this.course_reference_number = course_reference_number;
        this.student_id = student_id;
        this.rank = rank;
    }
}
