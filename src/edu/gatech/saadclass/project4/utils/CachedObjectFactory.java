package edu.gatech.saadclass.project4.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.gatech.saadclass.project4.school.*;
import edu.gatech.saadclass.project4.serverimpl.Fetch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class CachedObjectFactory {
    private School school;
    private CourseCatalog course_catalog;
    private Program program;
    private Term current_term;
    private HashMap<Integer, Course> courses = new HashMap<>();
    private HashMap<String, Integer> course_lookup = new HashMap<>();
    private HashMap<Integer, Student> students = new HashMap<>();
    private ArrayList<Integer> student_ids = new ArrayList<>();
    private HashMap<Integer, CourseOffering> course_offerings = new HashMap<>();
    private ArrayList<Integer> crns = new ArrayList<>();
    private HashMap<Integer, Specialization> specializations = new HashMap<>();
    private HashMap<String, Integer> specializations_lookup = new HashMap<>();
    private HashMap<Integer, Program> programs = new HashMap<>();
    private HashMap<Integer, Instructor> instructors = new HashMap<>();

    // private static final String kBase = "http://localhost/";
    private static final String kBase = "https://v-project4.shib.al/";

    public CachedObjectFactory() {
        school = new School("Georgia Institute of Technology", 201508);
        current_term = new Term(school, school.getCurrent_term());
        init();
    }

    public CachedObjectFactory(School school) {
        this.school = school;
        current_term = new Term(school, school.getCurrent_term());
        init();
    }

    private void init() {
        System.out.println("Populating data model from API base: " + kBase);
        System.out.println("Depending on your network speed, this may take a while.");

        populateInstructors();
        populateCourses();
        populateCourseCatalogAndProgram();
        resyncCoursesFromCourseCatalog();
        populateCourseOfferings();
        populateStudents();
        populateSpecializations();
        populatePreferences();
        populateStudentCourses();
        populatePrograms();
        populateStudentCourseLimit();

        System.out.println("Population and object creation finished.");
    }

    public int populateCourses() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "courses")).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            Course c = gson.fromJson(array.get(i), Course.class);
            c.convertRawToNative();
            courses.put(c.getId(), c);
            course_lookup.put(c.getCourse_id(), c.getId());
        }

        return courses.size();
    }

    private void populateCourseCatalogAndProgram() {
        course_catalog = new CourseCatalog(school);
        program = new Program(course_catalog);
        course_catalog.addProgram(program);

        for (Course course : courses.values()) {
            program.addCourse(course.getCourse_id(), course);
        }
    }

    public int populateStudents() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "users")).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            Student s = gson.fromJson(array.get(i), Student.class);
            s.convertRawToNative();
            students.put(s.getStudent_id(), s);
            student_ids.add(s.getStudent_id());
        }

        return students.size();
    }

    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    public int populateCourseOfferings() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "courseofferings?term=" +
                current_term.getTerm_id())).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            CourseOffering s = gson.fromJson(array.get(i), CourseOffering.class);
            s.convertRawToNative(this);
            course_offerings.put(s.getCourse_registration_number(), s);
            current_term.addCourse_offering(s.getCourse_registration_number(), s);
            crns.add(s.getCourse_registration_number());
        }

        return students.size();
    }

    public int populateSpecializations() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "specializations")).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            Specialization s = gson.fromJson(array.get(i), Specialization.class);
            s.convertRawToNative();
            specializations.put(s.getId(), s);
            specializations_lookup.put(s.getTitle(), s.getId());
        }

        return specializations.size();
    }

    public int populateInstructors() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "instructors")).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            Instructor s = gson.fromJson(array.get(i), Instructor.class);
            s.convertRawToNative(this);
            instructors.put(s.getId(), s);
        }

        return instructors.size();
    }

    public int populatePrograms() {
        Gson gson = new Gson();
        String programsDump = Fetch.get(kBase + "programs");
        if (programsDump.substring(0, 1) == "[") {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(programsDump).getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                Program p = gson.fromJson(array.get(i), Program.class);
                p.convertRawToNative();
                programs.put(p.getId(), p);
            }
        } else {
            Program p = gson.fromJson(programsDump, Program.class);
            programs.put(p.getId(), p);
        }
        return programs.size();
    }

    public void populatePreferences() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(Fetch.get(kBase + "preferences?term=" +
                current_term.getTerm_id())).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            IntermediatePreference p = gson.fromJson(array.get(i), IntermediatePreference.class);
            students.get(p.anon_id).addPreferred_course(p.priority, course_offerings.get(p.course_registration_number));
        }
    }

    public void populateStudentCourses() {
        Gson gson = new Gson();
        String programsDump = Fetch.get(kBase + "coursegrades");
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(programsDump).getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            IntermediateCourseGrade c = gson.fromJson(array.get(i), IntermediateCourseGrade.class);
            String section = (c.course_reference != 30545) ? "O01" : "O02";
            String courseId = c.course_code + c.course_number + section;
            Course cc = courses.get(course_lookup.get(courseId));
            if (c.grade == "W") {
                students.get(c.anon_id).addWithdraw_course(cc);
            } else {
                students.get(c.anon_id).addComplete_course(cc);
            }
        }
    }

    public void populateStudentCourseLimit() {
        for (Student student : students.values()) {
            if (student.getComplete_courses().size() >= 2) {
                student.setCourse_limit(3);
            }
        }
    }

    private void resyncCoursesFromCourseCatalog() {
        for (Course course : courses.values()) {
            course.setCourse_catalog(course_catalog);
            course.setCourse_catalog_id(course_catalog.getSchool().getSchool_id());
        }
    }

    public Student getStudent(int studentId) {
        return students.get(studentId);
    }

    public ArrayList<Integer> getStudentIds() {
        return student_ids;
    }

    public CourseOffering getCourseOffering(int crn) {
        return course_offerings.get(crn);
    }

    public ArrayList<Integer> getCrns() {
        return crns;
    }

    public Course getCourse(int id) {
        return courses.get(id);
    }

    public School getSchool() {
        return school;
    }

    public Specialization getSpecializationByName(String name) {
        return specializations.get(specializations_lookup.get(name));
    }

    public Instructor getInstructor(int id) {
        return instructors.get(id);
    }

    public Term getCurrentTerm() {
        return current_term;
    }

}
