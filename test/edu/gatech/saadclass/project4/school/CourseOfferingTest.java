package edu.gatech.saadclass.project4.school;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseOfferingTest {

	private CourseOffering courseOffering = null;
	private School school = null;

    @Before
	/**
	 * @throws java.lang.Exception
	 */    
    public void setUp() throws Exception {
		school = new School("Georgia Institute of Technology", 201508);
		CourseCatalog courseCatalog = new CourseCatalog(school);
		Term term = new Term(school, 201508);
		Course course = new Course(courseCatalog, "CS6310-O01", "CS", 6310, "O01", "O", "ALP", 3, "Zip A Dee Doo Da", "THRY");
    	courseOffering = new CourseOffering(course, term, 91419);
    }

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		courseOffering = null;
    }
	
	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.CourseOffering#setInstructors(String)}.
	 */
	@Test
	public void testSetInstructors(){
        String comment = "Test set instructors";
		//HashSet<Instructor> instructors = new HashSet<Instructor>();
        courseOffering.setInstructors("Orso, A.");
        //courseOffering.setInstructors(instructors);
        assertEquals(comment, 1, courseOffering.getInstructors().size());
	}
	
	/**
	 * Test method for {@link edu.gatech.saadclass.project4.school.CourseOffering#setInstructors(String)}.
	 */
	@Test
	public void testSetInstructors2(){
        String comment = "Test set instructors";
		//HashSet<Instructor> instructors = new HashSet<Instructor>();
        courseOffering.setInstructors("Lee, W., Ahamad, M.");
        //courseOffering.setInstructors(instructors);
        assertEquals(comment, 2, courseOffering.getInstructors().size());
	}
}
