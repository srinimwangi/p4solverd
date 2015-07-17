package edu.gatech.saadclass.project4.school;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class CourseCatalog {
	private HashMap<String, Course> courses = new HashMap<>();
	private HashMap<String, Program> programs = new HashMap<>();
	private School school;
    @SuppressWarnings("unused")
    private Integer school_id;
    private Integer version;

    /**
     * The constructor
     * 
     * @param school the school
     */
    public CourseCatalog(School school) {
		this.school = school;
		this.school_id = school.getSchool_id();
    }
	
	/**
	 * Add program
	 * 
	 * @param program the program to add
	 */
    public void addProgram(Program program) {
		programs.put(program.getName(), program);
	}

	/**
	 * Get courses
	 * 
	 * @return collection of courses
	 */
	public HashMap<String, Course> getCourses() {
		return courses;
	}
    
	/**
	 * Get programs
	 * 
	 * @return collection of programs
	 */
    public HashMap<String, Program> getPrograms() {
		return this.programs;
	}

    /**
     * Get school
     * 
     * @return the school
     */
	public School getSchool() {
		return school;
	}

	/**
	 * Get version
	 * 
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Load course catalog
	 * 
	 * @param file the file
	 */
    public void loadCourseCatalog(File file) {
		Scanner scanner = null;
        try {
            scanner = new Scanner(file);
			Program program = null;
            for (String line; scanner.hasNextLine(); ) {
                line = scanner.nextLine();
				//Get Version and Program
				if (line.startsWith("%") || line.startsWith("Version")) {
	                line = scanner.nextLine();
					String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					this.version = Integer.parseInt(tokens[0].trim());
					program = new Program(this);
					program.setName(tokens[1].trim());
	                line = scanner.nextLine();
				}

				if (line.startsWith("%") || line.startsWith("Subj")) {
					continue;
				}

				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 8);
                if (tokens.length != 8) {
                    throw new RuntimeException("Please check courses file for correct format.");
                } else {
                    String subject = tokens[0].trim();
                    Integer number = Integer.parseInt(tokens[1].trim());
                    String section = tokens[2].trim();
                    String campus = tokens[3].trim();
                    String basis = tokens[4].trim();
                    Integer hours = Integer.parseInt(tokens[5].trim());
                    String title = tokens[6].trim();
                    String attributes = tokens[7].trim();
                    String id = subject + number + "-" + section;
    				Course course = new Course(this, id, subject, number, section, campus,
    						basis, hours, title, attributes);
    				courses.put(id, course);
					program.getCourses().put(id, course);
                }

				if (!program.getName().trim().isEmpty()){
					programs.put(program.getName(), program);
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
     * Load course catalog
     * 
     * @param filename the file name
     */
    public void loadCourseCatalog(String filename) {
        loadCourseCatalog(new File(filename));
    }

    /**
     * Load foundational courses
     * 
     * @param file the file
     */
	public void loadFoundationalCourses(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			//Program program = null;
			for (String line; scanner.hasNextLine(); ) {
				line = scanner.nextLine();
				//Get Version
				if (line.startsWith("%") || line.startsWith("Version")) {
					line = scanner.nextLine();
					//String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					// TODO: need to refactor and handle multiple versions
					// this.version = Integer.parseInt(tokens[0].trim());
					line = scanner.nextLine();
				}

				if (line.startsWith("%") || line.startsWith("Subj")) {
					continue;
				}

				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 3);
				if (tokens.length != 3) {
					throw new RuntimeException("Please check foundational file for correct format.");
				} else {
					String subject = tokens[0].trim();
					Integer number = Integer.parseInt(tokens[1].trim());
					String section = tokens[2].trim();
					courses.get(subject + number + '-' + section).setFoundational(true);
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
	 * Set version
	 * 
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
}
