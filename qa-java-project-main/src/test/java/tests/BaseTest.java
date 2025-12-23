package tests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

import java.util.List;

public class BaseTest {

    protected static final Gson GSON = new Gson();
    protected static RequestSpecification requestSpec;
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    @BeforeClass
    public static void setUp() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    protected String generateUniqueEmail() {
        return "user_" + System.currentTimeMillis() + "@test.com";
    }

    protected String formatToken(String token) {
        if (token != null && !token.startsWith("Bearer ")) {
            return "Bearer " + token;
        }
        return token;
    }

    protected String createOrderJson(List<String> ingredients) {
        JsonObject json = new JsonObject();
        JsonArray ingredientsArray = new JsonArray();

        for (String ingredient : ingredients) {
            ingredientsArray.add(ingredient);
        }

        json.add("ingredients", ingredientsArray);
        return GSON.toJson(json);
    }

    protected String createUserJson(String email, String password, String name) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("name", name);
        return GSON.toJson(json);
    }
}