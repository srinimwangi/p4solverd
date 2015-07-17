package edu.gatech.saadclass.project4.school;

import java.util.HashSet;

public class Constraint {
	@SuppressWarnings("unused")
	private Specialization specialization;
	@SuppressWarnings("unused")
	private String specialization_id;
	private String name;
	private Integer required;
	private HashSet<Course> group;

	/**
	 * The constructor
	 *
	 * @param specialization the specialization
	 * @param name the constraint name
	 * @param required required courses count
	 */
	public Constraint(Specialization specialization, String name, Integer required) {
		this.specialization = specialization;
		this.specialization_id = specialization.getTitle();
		this.name = name;
		this.required = required;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the required
	 */
	public Integer getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Integer required) {
		this.required = required;
	}

	/**
	 * @return the group
	 */
	public HashSet<Course> getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(HashSet<Course> group) {
		this.group = group;
	}
}
