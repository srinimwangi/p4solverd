package edu.gatech.saadclass.project4.school;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Course {
	private String attributes;
    @SuppressWarnings("unused")
	private String basis;
    @SuppressWarnings("unused")
	private String campus;
	private CourseCatalog course_catalog;
    @SuppressWarnings("unused")
    private Integer course_catalog_id;
	private String course_code;
	private String course_id;
	private String course_number;
    @SuppressWarnings("unused")
	private HashMap<Integer, CourseOffering> course_offering = new HashMap<>();
    private Integer credits;
    private Integer foundational;
    private Integer hours;
    private int id;
    @SuppressWarnings("unused")
    private Integer instructor;
    @SuppressWarnings("unused")
	private Integer instructor_secondary;
    @SuppressWarnings("unused")
	private HashSet<String> internal_attributes = new HashSet<>();
    private boolean internal_foundational;
    private Integer number;
    @SuppressWarnings("unused")
	private HashMap<String, Course> prerequisites = new HashMap<>();
    @SuppressWarnings("unused")
    private HashMap<Integer, Student> qualified_assistants = new HashMap<>();
    @SuppressWarnings("unused")
    private HashSet<Instructor> qualified_instructors = new HashSet<>();
	private String section;
    private String subject;
	private String title;

	/**
	 * The constructor
	 * 
	 * @param course_catalog the course catalog
	 * @param course_id the course id
	 * @param subject the subject
	 * @param number the number
	 * @param section the section
	 * @param campus the campus
	 * @param basis the basis
	 * @param hours the hours
	 * @param title the title
	 * @param attributes the attributes
	 */
    public Course(CourseCatalog course_catalog, String course_id, String subject, Integer number, String section,
			String campus, String basis, Integer hours, String title,
			String attributes) {
		super();
		this.course_catalog = course_catalog;
		this.course_catalog_id = course_catalog.getVersion();
		this.course_id = course_id;
		this.subject = subject;
		this.number = number;
		this.section = section;
		this.campus = campus;
		this.basis = basis;
		this.hours = hours;
		this.title = title;
		this.internal_attributes = parseAttributes(attributes);
	}

    /**
     * The constructor
     * 
     * @param course_id the course id
     * @param subject the subject
     * @param number the number
     * @param section the section
     * @param title the title
     */
	public Course(String course_id, String subject, Integer number, String section, String title) {
		this.course_id = course_id;
		this.subject = subject;
		this.number = number;
		this.section = section;
		this.title = title;
	}

	/**
	 * toString override
	 */
	@Override
	public String toString() {
        return String.format("Course: [ course_id: %s, Subject: %s, Number: %d, Foundational: %s ]",
				course_id,
				this.subject,
				this.number,
				isFoundational());
    }

	/**
	 * Convert REST API data to Java classes
	 */
	public void convertRawToNative() {
		internal_foundational = !(foundational == 0);
		course_id = course_code + course_number + section;
		internal_attributes = parseAttributes(attributes);
		subject = title;
		hours = credits;
	}

	/**
	 * Get course catalog
	 * 
	 * @return the course catalog
	 */
	public CourseCatalog getCourse_catalog() {
		return course_catalog;
	}

	/**
	 * Get course id
	 * 
	 * @return the course id
	 */
	public String getCourse_id() {
		return course_id;
	}

	/**
	 * Get hours
	 * 
	 * @return the hours
	 */
	public Integer getHours() {
		return hours;
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
	 * Get course number
	 * @return the course number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * Get section
	 * 
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * Get subject
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Get title
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Is Foundational
	 * 
	 * @return foundational boolean
	 */
	public Boolean isFoundational() {
		return internal_foundational;
	}

	/**
	 * Parse attributes
	 * 
	 * @param s attributes
	 * @return collection of attributes
	 */
	private HashSet<String> parseAttributes(String s){
		HashSet<String> attributes = new HashSet<>();
		if (s.length() > 0) {
			String[] tokens = s.split(",");
			attributes = new HashSet<>(Arrays.asList(tokens));
		}
		return attributes;
	}

	/**
	 * Set course catalog
	 * 
	 * @param course_catalog the course catalog
	 */
	public void setCourse_catalog(CourseCatalog course_catalog) {
		this.course_catalog = course_catalog;
	}

	/**
	 * Set course catalog id
	 * 
	 * @param course_catalog_id the course catalog id
	 */
	public void setCourse_catalog_id(Integer course_catalog_id) {
		this.course_catalog_id = course_catalog_id;
	}

	/**
	 * Set foundational
	 * 
	 * @param foundational boolean flag
	 */
	public void setFoundational(Boolean foundational) {
		this.internal_foundational = foundational;
	}

	/**
	 * Set title
	 * 
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
