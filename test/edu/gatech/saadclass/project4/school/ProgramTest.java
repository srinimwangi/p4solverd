package edu.gatech.saadclass.project4.school;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ProgramTest {
    private CourseCatalog courseCatalog;
    private String fileDir;
    private Program program;
    private School school;

    @Before
    /**
     * @throws java.lang.Exception
     */
    public void setUp() throws Exception {
        school = new School("Georgia Institute of Technology", 201508);
        courseCatalog = new CourseCatalog(school);
        program = new Program(courseCatalog);
        fileDir = new String("test" + File.separator + "Resources"
                + File.separator);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        program = null;
        courseCatalog = null;
        school = null;
        fileDir = null;
    }

    @Test
    public void testLoadSpecializations() {
        String comment = "Test input file to load specializations";
        courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
        program.loadSpecializations(new File(fileDir + "Specializations.csv"));
        assertEquals(comment, 7, program.getSpecializations().size());
        assertEquals(comment, 65, program.getCourses().size());
    }

    @Test
    public void testLoadSpecializationConstraints() throws Exception {
        String comment = "Test input file to load specializations";
        courseCatalog.loadCourseCatalog(new File(fileDir + "CourseCatalog201402.csv"));
        program.loadSpecializations(new File(fileDir + "Specializations.csv"));
        program.loadSpecializationConstraints(new File(fileDir + "SpecializationConstraints.csv"));
        assertEquals(comment, 7, program.getSpecializations().size());
        assertEquals(comment, 3, program.getSpecializations().get("Machine Learning").getConstraints().size());
        assertEquals(comment, 1,
                (int) program.getSpecializations()
                        .get("Machine Learning")
                        .getConstraints()
                        .get("Algorithms")
                        .getRequired());
        assertEquals(comment, 7,
                program.getSpecializations()
                        .get("Machine Learning")
                        .getConstraints()
                        .get("Algorithms")
                        .getGroup()
                        .size());
    }
}
