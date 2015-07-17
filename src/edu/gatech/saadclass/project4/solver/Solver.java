package edu.gatech.saadclass.project4.solver;

import java.util.HashMap;

import edu.gatech.saadclass.project4.school.School;

public abstract class Solver {
	protected Boolean solved = false;
	protected SolverResult result;
    protected School school;
    protected HashMap<Integer, Integer> prerequisiteMapping;
    
    public Solver() {
        unsolve();
    }

    public Boolean getSolved() {
		return solved;
	}

	public void setSolved(Boolean solved) {
		this.solved = solved;
	}

	public SolverResult getResult() {
		return result;
	}

	public void setResult(SolverResult result) {
		this.result = result;
	}

	protected void unsolve() {
        this.solved = false;
        this.result = new SolverResult(0);
    }

    /**
     * Finalize. Does any clean-up tasks that are needed before the Solver object
     * is garbage collected by Java, for Solver implementations which depend on
     * slightly more fragile underlying implementations that require manual clean-up,
     * JNI being the primary example. Callers are expected to make sure this is called
     * when done with the Solver.
     */
    @Override
	public void finalize() {

    }

    public Solver setInternal_school(School school) {
        this.school = school;
        unsolve();
        return this;
    }

    public School getSchool() {
        return this.school;
    }

    /**
     *
     * @return SolverResult object containing the results of the solved problems.
     */
    public SolverResult solve() {
        // As noted, this is a "dummy" solver. This doesn't really return anything.
        // Real solver should override this and implement something meaningful.
        return new SolverResult(201508);
    }
}
