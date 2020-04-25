package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.apache.log4j.Logger;
import org.junit.Assert;
import pojo.User;

import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.hamcrest.Matchers.equalTo;

public class Users {

    private String endpoint;
    private Response response;
    public Logger log = Logger.getLogger(Users.class);

    /**
     * Constructor.
     * @param uri String
     */
    public Users(String uri) {
        endpoint = uri + "/v1/users/";
    }

    /**
     * Get users endpoint print.
     */
    @Step("Given the users API endpoint")
    public void getUsersAPIEndpoint() {
        log.info(endpoint);
    }

    /**
     * GET Method users/:id.
     * @param id String
     */
    @Step("When I look for the user with id {0}")
    public void getUser(String id) {
        response = given()
                .contentType(ContentType.JSON)
            .when()
                .get(endpoint + id);
        log.info(response.asString());
    }

    /**
     * GET Method users (list of users).
     */
    @Step("When I look for users")
    public void getUsers() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(endpoint);
    }

    /**
     * POST Method create new user.
     * @param users list of {@link User}
     */
    @Step("When I create users")
    public void createUsers(List<User> users) {
        for (User user : users) {
            response = given()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post(endpoint);

            Assert.assertEquals(201, response.statusCode());
        }
    }

    /**
     * PUT Method update job title of a user.
     * @param id String
     * @param jobTitle String
     */
    @Step("When I update the job title of the user with id {0} with {1}")
    public void updateUser(String id, String jobTitle) {

        /*Option 1 (not a good practice)
         String requestBody = "{\r\n" +
                        "   \"job_title\":\"QC Analyst\", + \r\n" +
         "}";*/

        //Option 2
        User user = new User();
        user.setJob_title(jobTitle);

        given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .put(endpoint + id);
    }

    /**
     * DELETE Method, delete user by id.
     * @param id String
     */
    @Step("When I delete the user with id {0}")
    public void deleteUser(String id) {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(endpoint + id);
    }

    /**
     * Verify expected title.
     * @param jobTitle String
     */
    @Step("Then the job title will be equals to {0}")
    public void jobTitleChanged(String jobTitle) {
        then()
                .body("job_title", equalTo(jobTitle));
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
     * Print list of users.
     */
    @Step("Then I can see the list of users")
    public void showActualUsersList() {
        List<User> users = then()
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("$", User.class);

        log.info(users);
    }

    /**
     * Verify expected email.
     * @param email String
     */
    @Step("Then there should be a email filed with value {0}")
    public void userEmailShouldBe(String email) {
        response.then().body("email", equalTo(email));
    }
}
