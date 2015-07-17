package edu.gatech.saadclass.project4.school;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class School {
    private HashMap<Integer, CourseCatalog> course_catalogs = new HashMap<>();
    private Integer current_term;
    private HashMap<String, Instructor> instructors = new HashMap<>();
    private Integer school_id;
    private HashMap<Integer, Student> students = new HashMap<>();
    private HashMap<Integer, Term> terms = new HashMap<>();
    private String title;

    /**
     * The constructor
     * 
     * @param title the title
     * @param current_term the current term
     */
    public School(String title, int current_term) {
        this.title = title;
        this.current_term = current_term;
    }

    /**
     * Get course catalogs
     * 
     * @return the courseCatalogs
     */
    public HashMap<Integer, CourseCatalog> getCourse_catalogs() {
        return course_catalogs;
    }

    /**
     * Get the current term
     * 
     * @return the current term
     */
    public Integer getCurrent_term() {
        return current_term;
    }

    /**
     * Get instructors
     * 
     * @return the instructors
     */
    public HashMap<String, Instructor> getInstructors() {
        return instructors;
    }

    /**
     * Get the school id
     * 
     * @return the school id
     */
    public Integer getSchool_id() {
        return school_id;
    }

    /**
     * Get the students
     * 
     * @return the students
     */
    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    /**
     * Get the terms
     * 
     * @return the terms
     */
    public HashMap<Integer, Term> getTerms() {
        return terms;
    }

    /**
     * Get the title
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Load the student course file
     * 
     * @param file the file
     */
    public void loadStudentCoursePreferences(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            for (String line; scanner.hasNextLine(); ) {
                line = scanner.nextLine();
                //Get Version and Program
                if (line.startsWith("%") || line.startsWith("ANON")) {
                    continue;
                }
                String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 3);
                if (tokens.length != 3) {
                    throw new RuntimeException("Please check students file for correct format.");
                } else {
                    Integer studentId = Integer.parseInt(tokens[0].trim().replaceAll("[,\"]", ""));
                    Integer courseReferenceNumber = Integer.parseInt(tokens[1]);
                    Integer preference = Integer.parseInt(tokens[2]);

                    CourseOffering courseOffering = terms.get(current_term).getCourse_offerings().get(courseReferenceNumber);
                    getStudents().get(studentId).getPreferred_courses().putIfAbsent(preference, courseOffering);
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
     * Load the student course preferences
     * 
     * @param filename the file name
     */
    public void loadStudentCoursePreferences(String filename) {
        loadStudentCoursePreferences(new File(filename));
    }

    /**
     * Load the student grades
     * 
     * @param file the file
     */
    public void loadStudentGrades(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            for (String line; scanner.hasNextLine(); ) {
                line = scanner.nextLine();
                //Get Version and Program
                if (line.startsWith("%") || line.startsWith("ANON")) {
                    continue;
                }
                String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 8);
                if (tokens.length != 8) {
                    throw new RuntimeException("Please check course grades file for correct format.");
                } else {
                    Integer studentId = Integer.parseInt(tokens[0].trim().replaceAll("[,\"]", ""));
                    Student student = getStudents().get(studentId);

                    Integer termKey = Integer.parseInt(tokens[1].trim());
                    Term currentTerm;
                    if (getTerms().containsKey(termKey)) {
                        currentTerm = getTerms().get(termKey);
                    } else {
                        currentTerm = new Term(this, termKey);
                        terms.put(termKey, currentTerm);
                    }

                    String subject = tokens[3].trim();
                    Integer number = Integer.parseInt(tokens[4].trim());
                    Integer referenceNumber = Integer.parseInt(tokens[5].trim());
					//String title = tokens[6].trim();
                    String section;

                    // Section is hard-coded so classes can be looked up
                    if (referenceNumber == 30545) {
                        section = "O02";
                    } else {
                        section = "O01";
                    }

                    //Build a consistent Id like in full course import
                    String courseId = subject + number + "-" + section;

                    //Hard code since this is the only catalog loaded
                    Course course = course_catalogs.get(201402).getCourses().get(courseId);

                    //Based on grade load into complete or withdraw classes
                    String grade = tokens[7].trim();

                    if (grade.isEmpty()) {
                        student.addComplete_course(course);
                    }
                    if (grade == "W") {
                        student.addWithdraw_course(course);
                    }
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
     * Load student grades
     * 
     * @param filename the filename
     */
    public void loadStudentGrades(String filename) {
        loadStudentGrades(new File(filename));
    }

    /**
     * Load students
     * 
     * @param program the program
     * @param file the file
     */
    public void loadStudents(Program program, File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            for (String line; scanner.hasNextLine(); ) {
                line = scanner.nextLine();
                //Get Version and Program
                if (line.startsWith("%") || line.startsWith("ANON")) {
                    continue;
                }
                String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 4);
                if (tokens.length != 4) {
                    throw new RuntimeException("Please check students file for correct format.");
                } else {
                    Integer studentId = Integer.parseInt(tokens[0].trim().replaceAll("[,\"]", ""));
                    String firstName = tokens[1].trim();
                    String lastName = tokens[2].trim();
                    String specialization = tokens[3].trim();

                    Student student = new Student(studentId, firstName, lastName, program, specialization);
                    //student.postToURL();
                    students.put(studentId, student);
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
     * Load students
     * 
     * @param program the program
     * @param filename the file name
     */
    public void loadStudents(Program program, String filename) {
        loadStudents(program, new File(filename));
    }

    /**
     * Set course catalogs
     * 
     * @param course_catalogs the courseCatalogs to set
     */
    public void setCourse_catalogs(HashMap<Integer, CourseCatalog> course_catalogs) {
        this.course_catalogs = course_catalogs;
    }

    /**
     * Set instructors
     * 
     * @param instructors the instructors to set
     */
    public void setInstructors(HashMap<String, Instructor> instructors) {
        this.instructors = instructors;
    }

    /**
     * Set students
     * 
     * @param students the students to set
     */
    public void setStudents(HashMap<Integer, Student> students) {
        this.students = students;
    }

    /**
     * Set terms
     * 
     * @param terms the terms to set
     */
    public void setTerms(HashMap<Integer, Term> terms) {
        this.terms = terms;
    }

    /**
     * Set title
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
