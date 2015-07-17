package edu.gatech.saadclass.project4.school;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseCatalogTest {
    private School school;
	private CourseCatalog courseCatalog;
    private String fileDir;

    @Before
	/**
	 * @throws java.lang.Exception
	 */    
    public void setUp() throws Exception {
		school = new School("Georgia Institute of Technology", 201508);
    	courseCatalog = new CourseCatalog(school);
        fileDir = new String("test" + File.separator + "Resources"
                + File.separator);
    }

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		courseCatalog = null;
        fileDir = null;
    }

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.CourseCatalog#loadCourseCatalog(java.io.File)}.
	 */
	@Test
	public void testloadCourseCatalog() {
        String comment = "Test input file to load courses";
        courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
        assertEquals(comment, 19, courseCatalog.getCourses().size());
		assertEquals(comment, 201402, (int)courseCatalog.getVersion());
		assertEquals(comment, "OMSCS", courseCatalog.getPrograms().get("OMSCS").getName());
		assertEquals(comment, 19, courseCatalog.getPrograms().get("OMSCS").getCourses().size());
	}

	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.CourseCatalog#loadCourseCatalog(java.io.File)}.
	 */
	@Test
	public void testLoadFoundationalCourses() {
		String comment = "Test input file to load foundational courses";
		courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
		courseCatalog.loadFoundationalCourses(new File(fileDir + "Foundational.csv"));
		assertEquals(comment, 19, courseCatalog.getCourses().size());
		assertEquals(comment, 201402, (int)courseCatalog.getVersion());
		assertEquals(comment, "OMSCS", courseCatalog.getPrograms().get("OMSCS").getName());
		int foundationalCount = 0;
		for (Course course : courseCatalog.getPrograms().get("OMSCS").getCourses().values()) {
			if (course.isFoundational() != null && course.isFoundational()) {
				foundationalCount++;
			}
		}
		assertEquals(comment, 11, foundationalCount);
	}
}
