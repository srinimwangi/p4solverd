package edu.gatech.saadclass.project4.school;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SchoolTest {
    private String fileDir;
    private School school = null;

    @Before
	/**
	 * @throws java.lang.Exception
	 */    
    public void setUp() throws Exception {
    	school = new School("Georgia Institute of Technology", 201508);
        fileDir = new String("test" + File.separator + "Resources"
                + File.separator);
    }

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		school = null;
        fileDir = null;
    }

	@Test
	public void testLoadStudentCoursePreferences() throws Exception {
		String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		school.getCourse_catalogs().put(courseCatalog.getVersion(), courseCatalog);
		Program program = new Program(courseCatalog);
		school.getTerms().put(201508, new Term(school, school.getCurrent_term()));
		Term term = school.getTerms().get(201508);
		term.loadCourseOfferings(new File(fileDir + "201508.csv"));
		assertEquals(comment, 18, term.getCourse_offerings().size());
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
		school.loadStudents(program, new File(fileDir + "Students.csv"));
		school.loadStudentGrades(new File(fileDir + "OMSCSCourseGrades_Final.csv"));
		assertEquals(comment, 2656, school.getStudents().size());
		//TODO: add tests to see if courses loaded correctly
		//assertEquals(comment, 201508, (int)courseCatalog.getVersion());
		school.loadStudentCoursePreferences(new File(fileDir + "preferences.csv"));
		//school.postToURL();
		//term.getCourse_offerings().values().forEach(edu.gatech.saadclass.project4.school.CourseOffering::postToURL);
		//school.getStudents().values().forEach(edu.gatech.saadclass.project4.school.Student::postToURL);
	}

	@Test
	public void testLoadStudentCoursePreferences2() throws Exception {
		String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		school.getCourse_catalogs().put(courseCatalog.getVersion(), courseCatalog);
		Program program = new Program(courseCatalog);
		school.getTerms().put(201508,new Term(school, school.getCurrent_term()));
		Term term = school.getTerms().get(201508);
		term.loadCourseOfferings(new File(fileDir + "proof201508.csv"));
		assertEquals(comment, 5, term.getCourse_offerings().size());
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
		school.loadStudents(program, new File(fileDir + "proofStudents.csv"));
		school.loadStudentGrades(new File(fileDir + "proofOMSCSCourseGrades_Final.csv"));
		assertEquals(comment, 12, school.getStudents().size());
		//TODO: add tests to see if courses loaded correctly
		//assertEquals(comment, 201508, (int)courseCatalog.getVersion());
		school.loadStudentCoursePreferences(new File(fileDir + "proofPreferences.csv"));
	}

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.School#loadStudentGrades(java.io.File)}.
	 */
	@Test
	public void testLoadStudentGradesFile() {
		String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		school.getCourse_catalogs().put(courseCatalog.getVersion(), courseCatalog);
		Program program = new Program(courseCatalog);
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
		school.loadStudents(program, new File(fileDir + "Students.csv"));
		school.loadStudentGrades(new File(fileDir + "OMSCSCourseGrades_Final.csv"));
		assertEquals(comment, 2656, school.getStudents().size());
		//TODO: add tests to see if courses loaded correctly
		//assertEquals(comment, 201508, (int)courseCatalog.getVersion());
	}

	@Test
	public void testLoadStudentGradesFile2() {
		String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		school.getCourse_catalogs().put(courseCatalog.getVersion(), courseCatalog);
		Program program = new Program(courseCatalog);
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
		school.loadStudents(program, new File(fileDir + "proofStudents.csv"));
		school.loadStudentGrades(new File(fileDir + "proofOMSCSCourseGrades_Final.csv"));
		assertEquals(comment, 12, school.getStudents().size());
		//TODO: add tests to see if courses loaded correctly
		//assertEquals(comment, 201508, (int)courseCatalog.getVersion());
	}

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.School#loadStudents(edu.gatech.saadclass.project4.school.Program, java.io.File)}.
	 */
	@Test
	public void testLoadStudentsFile() {
        String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		Program program = new Program(courseCatalog);
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
        school.loadStudents(program, new File(fileDir + "Students.csv"));
        assertEquals(comment, 2656, school.getStudents().size());
        //assertEquals(comment, 201508, (int)courseCatalog.getVersion());
	}

	@Test
	public void testLoadStudentsFile2() {
		String comment = "Test input file to load student";
		CourseCatalog courseCatalog = new CourseCatalog(school);
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		Program program = new Program(courseCatalog);
		program.loadSpecializations(new File(fileDir + "Specializations.csv"));
		school.loadStudents(program, new File(fileDir + "proofStudents.csv"));
		assertEquals(comment, 12, school.getStudents().size());
		//assertEquals(comment, 201508, (int)courseCatalog.getVersion());
	}
}
