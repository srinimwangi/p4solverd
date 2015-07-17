package edu.gatech.saadclass.project4.school;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Term {
	private HashMap<Integer, CourseOffering> course_offerings = new HashMap<>();
    private School school;
    @SuppressWarnings("unused")
	private Integer school_id;
    private Integer term_id;

    /**
     * The constructor
     * 
     * @param school the school
     * @param term_id the term id
     */
	public Term(School school, int term_id) {
		this.school = school;
		this.school_id = school.getSchool_id();
		this.term_id = term_id;
	}

	/**
	 * Add course offering
	 * 
	 * @param course_registration_number the course registration number
	 * @param courseOffering the course offering
	 */
	public void addCourse_offering(Integer course_registration_number, CourseOffering courseOffering) {
		this.course_offerings.putIfAbsent(course_registration_number, courseOffering);
	}
	
	/**
	 * Get course offerings
	 * 
	 * @return the hashmap of course registration numbers and course offerings
	 */
	public HashMap<Integer, CourseOffering> getCourse_offerings() {
		return course_offerings;
	}

	/**
	 * Get Term id
	 * 
	 * @return the term id
	 */
    public Integer getTerm_id() {
		return term_id;
	}
    
    /**
     * Load course offerings
     * 
     * @param file the file
     */
    public void loadCourseOfferings(File file) {
		Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            for (String line; scanner.hasNextLine(); ) {
                line = scanner.nextLine();
				//Get Version and Program
                if (line.startsWith("%") || line.startsWith("Version")) {
	                line = scanner.nextLine();
					String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 2);
					this.term_id = Integer.parseInt(tokens[0].trim());
	                line = scanner.nextLine();
				}

				if (line.startsWith("%") || line.startsWith("CRN")) {
					continue;
				}

				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 19);
                if (tokens.length != 19) {
                    throw new RuntimeException("Please check course offerings file for correct format.");
                } else {
                    Integer courseRegistrationNumber = Integer.parseInt(tokens[0].trim());
                    String subject = tokens[1].trim();
                    Integer number = Integer.parseInt(tokens[2].trim());
                    String section = tokens[3].trim();
                    String campus = tokens[4].trim();
                    String basis = tokens[5].trim();
                    Integer hours = Integer.parseInt(tokens[6].trim());
                    String title = tokens[7].trim();
                    String days = tokens[8].trim();
                    String time = tokens[9].trim();
                    Integer capacity = Integer.parseInt(tokens[10].trim());
                    Integer actual = Integer.parseInt(tokens[11].trim());
                    Integer remaining = Integer.parseInt(tokens[12].trim());
                    Integer waitListCapacity = Integer.parseInt(tokens[13].trim());
                    Integer waitListActual = Integer.parseInt(tokens[14].trim());
                    Integer waitListRemaining = Integer.parseInt(tokens[15].trim());
                    String instructors = tokens[16].trim();
                    String location = tokens[17].trim();
                    String attributes = tokens[18].trim();
                    String id = subject + number + "-" + section;
					CourseCatalog courseCatalog;
					if (this.school.getCourse_catalogs().get(term_id) == null){
						courseCatalog = new CourseCatalog(school);
					} else {
						courseCatalog = this.school.getCourse_catalogs().get(term_id);
					}
					Course course = new Course(courseCatalog, id, subject, number, section, campus, basis, hours, title, attributes);
    				CourseOffering courseOffering = new CourseOffering(course, this, courseRegistrationNumber, days, time, capacity, actual, remaining, waitListCapacity, waitListActual, waitListRemaining, instructors, location);
    				addCourse_offering(courseOffering.getCourse_registration_number(), courseOffering);
                }

            }
	        scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
        	scanner.close();
        }
	}

    /**
     *  Load course offerings
     *  
     * @param filename the filename
     */
	public void loadCourseOfferings(String filename) {
    	loadCourseOfferings(new File(filename));
    }
}
