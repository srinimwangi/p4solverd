package edu.gatech.saadclass.project4.school;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Program {
	@SuppressWarnings("unused")
	private CourseCatalog course_catalog;
	private HashMap<String, Course> courses = new HashMap<String, Course>();
	private int id;
	private String name;
	private HashMap<String, Specialization> specializations = new HashMap<String, Specialization>();
	private HashMap<Integer, Student> students = new HashMap<Integer, Student>();
	@SuppressWarnings("unused")
	private Integer version;

	/**
	 * The constructor
	 * 
	 * @param courseCatalog the course catalog
	 */
	public Program(CourseCatalog courseCatalog) {
		super();
		this.course_catalog = courseCatalog;
	}

	/**
	 * Add course
	 * 
	 * @param course_id the course id
	 * @param course the course
	 */
	public void addCourse(String course_id, Course course) {
		this.courses.putIfAbsent(course_id, course);
	}
	
	/**
	 * Convert REST API call data to Java class data
	 */
	public void convertRawToNative() {

	}

	/**
	 * Get courses
	 * 
	 * @return hashmap of course names and courses
	 */
	public HashMap<String, Course> getCourses() {
		return courses;
	}

	/**
	 * Get id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get specializations
	 * @return the hashmap of names and specializations
	 */
	public HashMap<String, Specialization> getSpecializations() {
		return specializations;
	}

	/**
	 * Get students
	 * 
	 * @return hashmap of student ids and students
	 */
	public HashMap<Integer, Student> getStudents() {
		return students;
	}

	/**
	 * Load specializations
	 *
	 * @param file the file
	 */
	public void loadSpecializations(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			Specialization specialization = null;
			for (String line; scanner.hasNextLine(); ) {
				line = scanner.nextLine();
				//Get Version and Specialization
				if (line.startsWith("%") || line.startsWith("Version")) {
					line = scanner.nextLine();
					String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					this.version = Integer.parseInt(tokens[0].trim());
					specialization = new Specialization(this);
					specialization.setTitle(tokens[1].trim());
					line = scanner.nextLine();
				}

				if (line.startsWith("%") || line.startsWith("Subj")) {
					continue;
				}

				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 4);
				if (tokens.length != 4) {
					throw new RuntimeException("Please check specializations file for correct format.");
				} else {
					String subject = tokens[0].trim();
					Integer number = Integer.parseInt(tokens[1].trim());
					String section = tokens[2].trim();
					String title = tokens[3].trim();

					Course course = new Course(subject + number.toString() + '-' + section, subject, number, section, title);
					specialization.getCourses().put(subject + number.toString() + '-' + section, course);
					if (!courses.containsKey(subject + number.toString() + '-' + section)){
						courses.put(subject + number.toString() + '-' + section, course);
					}
				}
				if (!specialization.getTitle().trim().isEmpty()){
					specializations.put(specialization.getTitle(), specialization);
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
	 * Load specialization constraints
	 *
	 * @param file the file
	 */
	public void loadSpecializationConstraints(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			Specialization specialization = null;
			for (String line; scanner.hasNextLine(); ) {
				line = scanner.nextLine();
				//Get Version and Specialization
				if (line.startsWith("%") || line.startsWith("Version")) {
					line = scanner.nextLine();
					String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					this.version = Integer.parseInt(tokens[0].trim());
					specialization = this.getSpecializations().get(tokens[1].trim());
					specialization.setTitle(tokens[1].trim());
					line = scanner.nextLine();
				}

				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				String name = tokens[0].trim();
				Integer required = Integer.parseInt(tokens[1].trim());
				HashSet<Course> group = new HashSet<>();
				for (int i = 0; i < tokens.length - 2; i++) {
					if (tokens[i+2].trim().length() > 0) {
						group.add(this.getCourses().get(tokens[i+2].trim()));
					}
				}
				Constraint constraint = new Constraint(specialization, name, required);
				constraint.setGroup(group);
				specialization.getConstraints().putIfAbsent(name, constraint);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	/**
	 * Set courses
	 * 
	 * @param courses the courses
	 */
	public void setCourses(HashMap<String, Course> courses) {
		this.courses = courses;
	}

	/**
	 * Set name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set specializations
	 * 
	 * @param specializations the specializations to set
	 */
	public void setSpecializations(HashMap<String, Specialization> specializations) {
		this.specializations = specializations;
	}
}