package test;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import steps.Students;

import java.util.Random;

public class StudentsTest {
    public Logger log = Logger.getLogger(StudentsTest.class);

    private Students steps;

    @DataProvider(name = "note")
    public Object[][] inputData() {
    	Random random = new Random();
	    int randomNum = random.nextInt(10);
	    boolean approved=false;
	    if (randomNum>=5) {approved=true;}
        return new Object[][] {{randomNum, approved}};
    }

    @BeforeMethod
    @Parameters({"uri"})
    public void test(String uri){
        steps = new Students(uri);
    }

    @Test
    public void getUsersTest(){
        steps.getStudentsAPIEndpoint();
        steps.getStudents();
        steps.isStatusCode(200);
        steps.showActualStudentsList();
    }

    @Test
    public void getUserTest(){
        steps.getStudentsAPIEndpoint();
        steps.getStudent("5");
        steps.isStatusCode(200);
        steps.userEmailShouldBe("marcelalasso@mail.com");
        steps.userFirstNameShouldBe("Marcela");
    }



    @Test(dataProvider = "note")
    public void putTest(int randomNum, boolean approved){
    	steps.getStudentsAPIEndpoint();
        steps.updateStudent("5", randomNum);
        steps.isStatusCode(200);
        steps.finalNoteChanged(randomNum);
        steps.approvedChanged(approved);
        log.info("MODIFIED STUDENT: ");
        steps.getStudent("5");
    }

}
