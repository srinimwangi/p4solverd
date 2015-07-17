package edu.gatech.saadclass.project4.school;

import edu.gatech.saadclass.project4.utils.CachedObjectFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Instructor {
    private List<String> courses = new ArrayList<>();
    private String first_name;
    private int id;
    private Integer instructor_id;
    private String last_name;
    private School school;
    @SuppressWarnings("unused")
	private Integer school_id;

    /**
     * The constructor
     * 
     * @param school the school
     * @param last_name the last name
     * @param first_name the first name
     */
    public Instructor(School school, String last_name, String first_name) {
        super();
        this.school = school;
        this.school_id = school.getSchool_id();
        this.last_name = last_name;
        this.first_name = first_name;
    }

    /**
     * Convert REST API data to Java class data
     * @param cof
     */
    public void convertRawToNative(CachedObjectFactory cof) {
        school_id = cof.getSchool().getSchool_id();
        school = cof.getSchool();
    }

    /**
     * Get courses
     * 
     * @return collection of courses
     */
    public HashSet<Course> getCourses() {
        HashMap<String, Course> allCourses = school.getCourse_catalogs().get(school.getCurrent_term()).getCourses();
        HashSet<Course> courses = new HashSet<>();
        courses.addAll(this.courses.stream().map(allCourses::get).collect(Collectors.toList()));
        return courses;
    }

    /**
     * Get first name
     * 
     * @return the first name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Get id
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get instructor id
     * 
     * @return the instructor id
     */
    public Integer getInstructor_id() {
        return instructor_id;
    }

    /**
     * Get last name
     * 
     * @return the last name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Set courses
     * 
     * @param courses the collection of courses
     */
    public void setCourses(HashSet<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses.stream().map(course -> course.getSubject()
                + course.getNumber() + '-' + course.getSection()).collect(Collectors.toList()));
    }

    /**
     * Set first name
     * 
     * @param first_name the first name
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Set instructor id
     * 
     * @param instructor_id the instructor id
     */
    public void setInstructor_id(Integer instructor_id) {
        this.instructor_id = instructor_id;
    }

    /**
     * Set last name
     * 
     * @param last_name the last name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * toString override
     */
    @Override
	public String toString() {
        return last_name + ", " + first_name;
    }
}
