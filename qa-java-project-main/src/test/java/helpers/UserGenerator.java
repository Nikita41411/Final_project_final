package helpers;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.given;

public class UserGenerator {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    public static class UserData {
        private String email;
        private String password;
        private String name;

        public UserData(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getName() { return name; }
    }

    public static UserData generateRandomUser() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@test.com";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new UserData(email, password, name);
    }

    public static UserData createUserViaAPI() {
        UserData userData = generateRandomUser();

        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                userData.getEmail(),
                userData.getPassword(),
                userData.getName()
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/auth/register");

        // Сохраняем токен для последующего удаления
        if (response.statusCode() == 200) {
            String accessToken = response.body().jsonPath().getString("accessToken");
            userData = new UserData(userData.getEmail(), userData.getPassword(), userData.getName()) {
                private String token = accessToken;

                public String getToken() { return token; }
            };
        }

        return userData;
    }

    public static void deleteUser(String email, String password) {
        // Сначала получаем токен
        String loginBody = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}",
                email,
                password
        );

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginBody)
                .when()
                .post(BASE_URL + "/auth/login");

        if (loginResponse.statusCode() == 200) {
            String token = loginResponse.body().jsonPath().getString("accessToken");

            // Удаляем пользователя
            given()
                    .header("Authorization", token)
                    .when()
                    .delete(BASE_URL + "/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}