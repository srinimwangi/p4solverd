package edu.gatech.saadclass.project4.school;

import edu.gatech.saadclass.project4.utils.CachedObjectFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CourseOffering {
    @SuppressWarnings("unused")
    private Integer actual;
    @SuppressWarnings("unused")
    private String assistants;
    private Integer capacity;
    private int course;
    @SuppressWarnings("unused")
    private String course_id;
    private Integer course_registration_number;
    @SuppressWarnings("unused")
    private String days;
    private int id;
    private Integer instructor;
    private Integer instructor_secondary;
    private List<Instructor> instructors = new ArrayList<>();
    private HashSet<Student> internal_assistants = new HashSet<>();
    private Course internal_course;
    private Term internal_term;
    @SuppressWarnings("unused")
    private String location;
    @SuppressWarnings("unused")
    private Integer remaining;
    @SuppressWarnings("unused")
    private int term;
    @SuppressWarnings("unused")
    private Integer term_id;
    @SuppressWarnings("unused")
    private String time;
    @SuppressWarnings("unused")
    private Integer wait_list_actual;
    @SuppressWarnings("unused")
    private Integer wait_list_capacity;
    @SuppressWarnings("unused")
    private Integer wait_list_remaining;

    /**
     * The constructor
     * 
     * @param course the course
     * @param term the term
     * @param course_registration_number the course registration number
     */
    public CourseOffering(Course course, Term term, Integer course_registration_number) {
        this.internal_course = course;
        this.course_id = course.getCourse_id();
        this.internal_term = term;
        this.term_id = term.getTerm_id();
        this.course_id = course.getCourse_id();
        this.course_registration_number = course_registration_number;
    }

    /**
     * The constructor
     * 
     * @param course the course
     * @param term the term
     * @param course_registration_number the course registration number
     * @param days the days
     * @param time the time
     * @param capacity the capacity
     * @param actual the actual
     * @param remaining the remaining
     * @param wait_list_capacity the wait list capacity
     * @param wait_list_actual the wait list actual
     * @param wait_list_remaining the wait list remaining
     * @param instructors the instructors 
     * @param location the location
     */
    public CourseOffering(Course course, Term term, Integer course_registration_number, String days, String time,
                          Integer capacity, Integer actual, Integer remaining, Integer wait_list_capacity,
                          Integer wait_list_actual, Integer wait_list_remaining, String instructors, String location) {
        this.internal_course = course;
        this.course_id = course.getCourse_id();
        this.internal_term = term;
        this.term_id = term.getTerm_id();
        this.course_registration_number = course_registration_number;
        this.days = days;
        this.time = time;
        this.capacity = capacity;
        this.actual = actual;
        this.remaining = remaining;
        this.wait_list_capacity = wait_list_capacity;
        this.wait_list_actual = wait_list_actual;
        this.wait_list_remaining = wait_list_remaining;
        this.location = location;
        setInstructors(instructors);
    }

    /**
     * Add instructor
     * 
     * @param instructor the instructor
     */
    public void addInstructor(Instructor instructor) {
        if (!instructors.contains(instructor)) {
            instructors.add(instructor);
        }
    }

    /**
     * Conver REST API data to Java classes
     * 
     * @param cof cached object factory
     */
    public void convertRawToNative(CachedObjectFactory cof) {
        internal_course = cof.getCourse(course);
        course_id = internal_course.getCourse_id();

        internal_term = cof.getCurrentTerm();
        term_id = cof.getCurrentTerm().getTerm_id();

        instructors = new ArrayList<>();

        if (instructor != null) {
            Instructor a = cof.getInstructor(instructor);
            instructors.add(a);
        }

        if (instructor_secondary != null)
            instructors.add(cof.getInstructor(instructor_secondary));
    }

    /**
     * Get capacity
     * 
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Get course registration number
     * 
     * @return the courseRegistrationNumber
     */
    public Integer getCourse_registration_number() {
        return course_registration_number;
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
     * Get instructors
     * 
     * @return collection of instructors
     */
    public HashSet<Instructor> getInstructors() {
        HashSet<Instructor> instructors = new HashSet<>();
        for (Instructor instructor : this.instructors) {
            instructors.add(instructor);
        }
        return instructors;
    }

    /**
     * Get internal assistants
     * 
     * @return the internal_assistants
     */
    public HashSet<Student> getInternal_assistants() {
        return internal_assistants;
    }

    /**
     * Get internal course
     * 
     * @return the internal course
     */
    public Course getInternal_course() {
        return internal_course;
    }

    /**
     * Get internal term
     * 
     * @return the internal_term
     */
    public Term getInternal_term() {
        return internal_term;
    }

    /**
     * Set instructors
     * 
     * @param s the instructors
     */
    void setInstructors(String s) {
        s = s.replaceAll("\"", "").trim();
        //TODO: Set primary instructor
        s = s.replace("(P)", "").trim();
        s = s.replace("TBA", "").trim();
        if (s.length() > 0) {
            String[] tokens = s.split("\\.");
            for (String token : tokens) {
                String[] names = token.split(",", 2);
                Instructor instructor = new Instructor(internal_course.getCourse_catalog().getSchool(), names[0], names[1]);
                this.instructors.add(instructor);
            }
        }
    }

    /**
     * Set internal assistants
     * 
     * @param internal_assistants the internal_assistants to set
     */
    public void setInternal_assistants(HashSet<Student> internal_assistants) {
        this.internal_assistants = internal_assistants;
    }

    /**
     * Set internal course
     * 
     * @param internal_course the internal course
     */
    public void setInternal_course(Course internal_course) {
        this.internal_course = internal_course;
    }

    /**
     * Set internal term
     * 
     * @param internal_term the internal_term to set
     */
    public void setInternal_term(Term internal_term) {
        this.internal_term = internal_term;
    }
}
