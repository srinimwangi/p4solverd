package edu.gatech.saadclass.project4.school;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private HashMap<String, Course> complete_courses = new HashMap<>();
    private Integer complete_hours;
    @SuppressWarnings("unused")
	private CourseCatalog course_catalog;
    private Integer course_limit;
    @SuppressWarnings("unused")
    private Integer course_wanted;
    @SuppressWarnings("unused")
    private String first_name;
    private int id;
    private Specialization internal_specialization;
    @SuppressWarnings("unused")
    private String last_name;
    private HashMap<Integer, CourseOffering> preferred_courses = new HashMap<>();
    private Program program;
    @SuppressWarnings("unused")
    private String specialization;
    private int student_id;
    private HashMap<String, Course> withdraw_courses = new HashMap<>();

    /**
     * The constructor
     * 
     * @param studentId the student id
     * @param first_name the first name
     * @param last_name the last name
     * @param program the program
     * @param specialization the specialization
     */
    public Student(Integer studentId, String first_name, String last_name, Program program, String specialization) {
        super();
        this.student_id = studentId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.program = program;
        this.internal_specialization = getSpecialization(specialization);
        this.course_limit = 2;
        this.complete_hours = 0;
    }

    /**
     * Add a complete course
     * 
     * @param course the course to add
     */
    public void addComplete_course(Course course) {
        String courseId = course.getSubject() + course.getNumber() + "-" + course.getSection();
        this.complete_courses.put(courseId, course);
        this.complete_hours += course.getHours();
        updateCourseLimit();
    }

    /**
     * Add a preferred course
     * 
     * @param priority the priority
     * @param courseOffering the course offering
     */
    public void addPreferred_course(Integer priority, CourseOffering courseOffering) {
        this.preferred_courses.putIfAbsent(priority, courseOffering);
    }

    /**
     * Add a withdraw course
     * 
     * @param course the course to add
     */
    public void addWithdraw_course(Course course) {
        String courseId = course.getSubject() + course.getNumber() + "-" + course.getSection();
        this.withdraw_courses.put(courseId, course);
    }

    /**
     * Convert the REST data to Java class data
     */
    public void convertRawToNative() {
        preferred_courses = new HashMap<>();
        withdraw_courses = new HashMap<>();
        complete_courses = new HashMap<>();
        this.course_limit = 2;
        this.complete_hours = 0;
    }

    /**
     * Get the complete courses
     * 
     * @return the completeCourses
     */
    public HashMap<String, Course> getComplete_courses() {
        return complete_courses;
    }

    /**
     * Get the complete hours
     * 
     * @return the hours
     */
    public Integer getComplete_hours() {
        return complete_hours;
    }

    /**
     * Get the course limit
     * 
     * @return the courseLimit
     */
    public Integer getCourse_limit() {
        return course_limit;
    }

    /**
     * Get the id
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the preferred courses
     * 
     * @return the preferredCourses
     */
    public HashMap<Integer, CourseOffering> getPreferred_courses() {
        return preferred_courses;
    }

    /**
     * Get the specialization
     * 
     * @param s the specialization name
     * @return the specialization
     */
    private Specialization getSpecialization(String s) {
        if (program != null) {
            this.internal_specialization = program.getSpecializations().get(s);
            return this.internal_specialization;
        }
        return null;
    }

    /**
     * Get the student id
     * 
     * @return the id
     */
    public int getStudent_id() {
        return student_id;
    }

    /**
     * Set the course limit
     * 
     * @param course_limit the courseLimit to set
     */
    public void setCourse_limit(Integer course_limit) {
        this.course_limit = course_limit;
    }

    /**
     * Update the course limit
     */
    private void updateCourseLimit(){
        if (this.complete_courses.size() >= 2) {
            this.course_limit = 3;
        }
    }

    /**
     * Get preferred course rank
     * @param course_id the course id
     * @return the rank
     */
    public Integer getPreferred_course_rank(Integer course_id) {
        for (Map.Entry<Integer, CourseOffering> courseOfferingEntry : preferred_courses.entrySet()) {
            if (courseOfferingEntry.getValue().getCourse_registration_number() == course_id) {
                return courseOfferingEntry.getKey();
            }
        }
        return 0;
    }
}