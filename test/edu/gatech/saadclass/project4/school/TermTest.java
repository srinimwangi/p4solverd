package edu.gatech.saadclass.project4.school;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TermTest {
	private CourseCatalog courseCatalog;
	private String fileDir;
	private School school;
    private Term term = null;

    @Before
	/**
	 * @throws java.lang.Exception
	 */    
    public void setUp() throws Exception {
		school = new School("Georgia Institute of Technology", 201508);
		courseCatalog = new CourseCatalog(school);
        term = new Term(school, 201508);
        fileDir = new String("test" + File.separator + "Resources"
                + File.separator);
    }

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		term = null;
		school = null;
        fileDir = null;
    }

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.Term#loadCourseOfferings(java.io.File)}.
	 */
	@Test
	public void testLoadCourseOfferingsFile() {
        String comment = "Test input file to load courses";
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		courseCatalog.loadFoundationalCourses(new File(fileDir + "Foundational.csv"));
		term.loadCourseOfferings(new File(fileDir + "201508.csv"));
        assertEquals(comment, 18, term.getCourse_offerings().size());
        assertEquals(comment, 201402, (int)courseCatalog.getVersion());
	}

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.Term#loadCourseOfferings(java.io.File)}.
	 */
	@Test
	public void testLoadCourseOfferingsFile2() {
		String comment = "Test input file to load courses";
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		courseCatalog.loadFoundationalCourses(new File(fileDir + "Foundational.csv"));
		term.loadCourseOfferings(new File(fileDir + "proof201508.csv"));
		assertEquals(comment, 5, term.getCourse_offerings().size());
		assertEquals(comment, 201402, (int)courseCatalog.getVersion());
	}

	@Test
	public void testLoadCourseOfferingsString() {
		String comment = "Test input file string to load courses";
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		courseCatalog.loadFoundationalCourses(new File(fileDir + "Foundational.csv"));
		term.loadCourseOfferings(fileDir + "201508.csv");
		assertEquals(comment, 18, term.getCourse_offerings().size());
		assertEquals(comment, 201402, (int)courseCatalog.getVersion());
	}
}
