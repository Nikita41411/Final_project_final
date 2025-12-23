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

public class UserUpdateTest extends BaseTest {

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
    @DisplayName("Изменение email с авторизацией")
    public void updateEmailWithAuthorization() {
        String newEmail = generateUniqueEmail();

        updateUserWithAuth(accessToken, newEmail, userPassword, userName)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail.toLowerCase()));
    }

    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void updateNameWithAuthorization() {
        String newName = "Updated Name";

        updateUserWithAuth(accessToken, userEmail, userPassword, newName)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение пароля с авторизацией")
    public void updatePasswordWithAuthorization() {
        String newPassword = "newpassword123";

        updateUserWithAuth(accessToken, userEmail, newPassword, userName)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        // Логинимся с новым паролем
        login(userEmail, newPassword)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных без авторизации")
    public void updateUserWithoutAuthorizationShouldFail() {
        JsonObject json = new JsonObject();
        json.addProperty("email", "updated@test.com");
        json.addProperty("password", "newpassword");
        json.addProperty("name", "Updated Name");

        given()
                .spec(requestSpec)
                .body(GSON.toJson(json))
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Обновление пользователя с авторизацией")
    private Response updateUserWithAuth(String token, String email, String password, String name) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("name", name);

        return given()
                .spec(requestSpec)
                .header("Authorization", formatToken(token))
                .body(GSON.toJson(json))
                .patch("/auth/user");
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