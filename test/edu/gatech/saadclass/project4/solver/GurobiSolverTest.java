package edu.gatech.saadclass.project4.solver;

import edu.gatech.saadclass.project4.school.CourseCatalog;
import edu.gatech.saadclass.project4.school.Program;
import edu.gatech.saadclass.project4.school.School;
import edu.gatech.saadclass.project4.school.Term;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * GurobiSolver Tester.
 *
 * @author <Authors name>
 * @since <pre>Apr 20, 2015</pre>
 * @version 1.0
 */
public class GurobiSolverTest {
   private CourseCatalog courseCatalog;
   private String fileDir;
   private GurobiSolver gurobiSolver;
   private School school;
   private Program program;

   @Before
   /**
    * @throws java.lang.Exception
    */
   public void setUp() throws Exception {
      school = new School("Georgia Institute of Technology", 201508);
      school.getCourse_catalogs().put(201402, new CourseCatalog(school));
      school.getTerms().put(201508, new Term(school, school.getCurrent_term()));
      program = new Program(courseCatalog);
      gurobiSolver = new GurobiSolver();
      fileDir = new String("test" + File.separator + "Resources"
              + File.separator);
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception {
      courseCatalog = null;
      program = null;
      gurobiSolver = null;
      school = null;
      fileDir = null;
   }

   /**
    *
    * Method: solve()
    *
    */
   @Test
   public void testSolve() throws Exception {
      //TODO: Migrate to static data set.
      String comment = "Test solver with database load. This will fail when database is changed.";
      assertEquals(comment, 239629, (int) gurobiSolver.solve().getSatisfaction());
   }

   @Test
   public void testSolve2() throws Exception {
      String comment = "Test solver with sample data.";
      school.getCourse_catalogs().get(201402).loadCourseCatalog(new File(fileDir +
              "CourseCatalog201402.csv"));
      school.getCourse_catalogs().get(201402).loadFoundationalCourses(new File(fileDir +
              "Foundational.csv"));
      school.getCourse_catalogs().get(201402).getPrograms().get("OMSCS").loadSpecializations(new File(
              fileDir + "Specializations.csv"));
      school.loadStudents(program, new File(fileDir + "proofStudents.csv"));
      school.loadStudentGrades(new File(fileDir + "proofOMSCSCourseGrades_Final.csv"));
      school.getTerms().get(school.getCurrent_term()).loadCourseOfferings(new File(fileDir + "proof201508.csv"));
      school.loadStudentCoursePreferences(new File(fileDir + "proofPreferences.csv"));
      assertEquals(comment, 5, school.getTerms().get(school.getCurrent_term()).getCourse_offerings().size());
      assertEquals(comment, 201402, (int) school.getCourse_catalogs().get(201402).getVersion());
      assertEquals(comment, 12, school.getStudents().size());
      assertEquals(comment, 712, (int) gurobiSolver.solve(school).getSatisfaction());
   }
}
