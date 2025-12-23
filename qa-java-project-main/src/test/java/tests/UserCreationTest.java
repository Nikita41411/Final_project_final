package tests;

import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserCreationTest extends BaseTest {

    private String userEmail;
    private String userPassword = "password123";
    private String userName = "Test User";
    private String accessToken;

    @Before
    public void setUpTest() {
        userEmail = generateUniqueEmail();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccessfully() {
        Response response = createUser(userEmail, userPassword, userName);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(userEmail.toLowerCase()))
                .body("user.name", equalTo(userName))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        accessToken = response.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicateUserShouldFail() {
        createUser(userEmail, userPassword, userName);

        createUser(userEmail, userPassword, userName)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailShouldFail() {
        JsonObject json = new JsonObject();
        json.addProperty("password", userPassword);
        json.addProperty("name", userName);

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/register")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordShouldFail() {
        JsonObject json = new JsonObject();
        json.addProperty("email", userEmail);
        json.addProperty("name", userName);

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/register")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameShouldFail() {
        JsonObject json = new JsonObject();
        json.addProperty("email", userEmail);
        json.addProperty("password", userPassword);

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/register")
                .then()
                .statusCode(403);
    }

    @Step("Создание пользователя")
    private Response createUser(String email, String password, String name) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("name", name);

        return given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/register");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }

    @Step("Удаление пользователя")
    private void deleteUser(String token) {
        given()
                .spec(requestSpec)
                .header("Authorization", formatToken(token))
                .delete("/auth/user")
                .then()
                .statusCode(202);
    }
}