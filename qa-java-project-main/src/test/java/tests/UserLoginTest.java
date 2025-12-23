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

public class UserLoginTest extends BaseTest {

    private String userEmail;
    private String userPassword = "password123";
    private String userName = "Test User";
    private String accessToken;

    @Before
    public void setUpTest() {
        userEmail = generateUniqueEmail();
        // Создаем пользователя перед тестами
        Response response = createUser(userEmail, userPassword, userName);
        accessToken = response.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginWithValidCredentials() {
        login(userEmail, userPassword)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(userEmail.toLowerCase()))
                .body("user.name", equalTo(userName))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным email")
    public void loginWithInvalidEmail() {
        JsonObject json = new JsonObject();
        json.addProperty("email", "wrong@email.com");
        json.addProperty("password", userPassword);

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithInvalidPassword() {
        JsonObject json = new JsonObject();
        json.addProperty("email", userEmail);
        json.addProperty("password", "wrongpassword");

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Step("Логин пользователя")
    private Response login(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        return given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .post("/auth/login");
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