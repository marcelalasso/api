package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.apache.log4j.Logger;
import org.junit.Assert;

import pojo.Student;

import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.hamcrest.Matchers.equalTo;

public class Students {

    private String endpoint;
    private Response response;
    public Logger log = Logger.getLogger(Students.class);

    /**
     * Constructor.
     * @param uri String
     */
    public Students(String uri) {
        endpoint = uri + "/v1/students/";
    }

    /**
     * Get students endpoint print.
     */
    @Step("Given the students API endpoint")
    public void getStudentsAPIEndpoint() {
        log.info("Endpoint= " + endpoint);
    }

    /**
     * GET Method student/:id.
     * @param id String
     */
    @Step("When I look for the student with id {0}")
    public void getStudent(String id) {
        response = given()
                .contentType(ContentType.JSON)
            .when()
                .get(endpoint + id);
        log.info("Student: " + response.asString());
    }

    /**
     * GET Method students (list of students).
     */
    @Step("When I look for student")
    public void getStudents() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(endpoint);
    }

    /**
     * PUT Method update final_note and approved.
     * @param id String
     * @param final_note int
     */
    @Step("When I update the final_note and approved of the student with id {0} with {1}")
    public void updateStudent(String id, int final_note) {
        Student student = new Student();
        student.setFinalNote(final_note);
        if (final_note>=5) {student.setApproved(true);}
        else{student.setApproved(false);} 
        given()
                .contentType(ContentType.JSON)
                .body(student)
            .when()
                .put(endpoint + id);
    }

  
    /**
     * Verify expected note.
     * @param final_note int
     */
    @Step("Then the final note will be equals to {0}")
    public void finalNoteChanged(int final_note) {
        then()
                .body("final_note", equalTo(final_note));
    }

    /**
     * Verify expected approved.
     * @param approved boolean
     */
    @Step("Then the approved will be equals to {true}")
    public void approvedChanged(boolean approved) {
        then()
                .body("approved", equalTo(approved));
    }

    
    /**
     * Verify expected status code.
     * @param statusCode int
     */
    @Step("Then the status code will be {0}")
    public void isStatusCode(int statusCode) {
        then()
                .statusCode(statusCode);
    }

    /**
     * Print list of students.
     */
    @Step("Then I can see the list of students")
    public void showActualStudentsList() {
        List<Student> students = then()
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$", Student.class);

        log.info("STUDENTS LIST: "+ students);
    }

    /**
     * Verify expected email.
     * @param email String
     */
    @Step("Then there should be a email filed with value {0}")
    public void userEmailShouldBe(String email) {
        response.then().body("email", equalTo(email));
    }

    /**
     * Verify expected first name.
     * @param first_name String
     */
    @Step("Then there should be a first_name filed with value {0}")
    public void userFirstNameShouldBe(String first_name) {
        response.then().body("first_name", equalTo(first_name));
    }


}
