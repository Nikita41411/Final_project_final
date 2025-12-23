package tests;

import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserOrdersTest extends BaseTest {

    private String userEmail;
    private String userPassword = "password123";
    private String userName = "Test User";
    private String accessToken;
    private String bunId;
    private String mainId;

    @Before
    public void setUpTest() {
        userEmail = generateUniqueEmail();

        Response response = createUser(userEmail, userPassword, userName);
        accessToken = response.then().extract().path("accessToken");

        Response ingredientsResponse = given()
                .spec(requestSpec)
                .get("/ingredients");

        bunId = ingredientsResponse.then().extract().path("data.find { it.type == 'bun' }._id");
        mainId = ingredientsResponse.then().extract().path("data.find { it.type == 'main' }._id");

        createOrderForUser();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getUserOrdersWithAuthorization() {
        getUserOrders(accessToken)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", not(empty()))
                .body("total", greaterThan(0))
                .body("totalToday", greaterThan(0));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getUserOrdersWithoutAuthorizationShouldFail() {
        given()
                .spec(requestSpec)
                .get("/orders")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Создание заказа для пользователя")
    private void createOrderForUser() {
        if (bunId != null && mainId != null) {
            List<String> ingredients = Arrays.asList(bunId, mainId);

            given()
                    .spec(requestSpec)
                    .header("Authorization", formatToken(accessToken))
                    .body(createOrderJson(ingredients))
                    .post("/orders")
                    .then()
                    .statusCode(200)
                    .body("success", equalTo(true));
        }
    }

    @Step("Получение заказов пользователя")
    private Response getUserOrders(String token) {
        return given()
                .spec(requestSpec)
                .header("Authorization", formatToken(token))
                .get("/orders");
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