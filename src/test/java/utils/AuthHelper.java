package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;
import java.util.HashMap;

public class AuthHelper {
    private static String adminToken;
    private static String userToken;

    // Login endpoint - adjust if your API differs (e.g. /authenticate, /login)
    private static final String LOGIN_ENDPOINT = "/auth/login";
    // Trying the endpoint used in the curl command seen in logs, or standard ones.
    // The user's curl attempted /api/authenticate and /api/auth/login.
    // Based on standard Spring Boot or similar, let's try to assume a standard
    // structure or check if we can find the controller.
    // Since I can't see backend code, I'll support both or try one.
    // The user's curl "http://localhost:8080/api/auth/login" failed with Method Not
    // Allowed or similar perhaps?
    // Wait, the curl output in Step 28/29 was messy.

    // Let's implement a robust login that tries to get a token.

    public static String getAdminToken() {
        if (adminToken == null) {
            adminToken = login(ConfigLoader.getProperty("admin.username"), ConfigLoader.getProperty("admin.password"));
        }
        return adminToken;
    }

    public static String getUserToken() {
        if (userToken == null) {
            userToken = login(ConfigLoader.getProperty("user.username"), ConfigLoader.getProperty("user.password"));
        }
        return userToken;
    }

    private static String login(String username, String password) {
        String baseUrl = ConfigLoader.getProperty("api.base.url");
        // Remove trailing /api if present because we might need to append /authenticate
        // or /auth/login
        // Actually config says api.base.url=http://localhost:8080/api

        // Let's try /authenticate first (common in JWT tutorials)
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        try {
            Response response = RestAssured.given()
                    .contentType("application/json")
                    .body(body)
                    .post(baseUrl + "/authenticate");

            if (response.getStatusCode() == 200) {
                // Adjust extraction based on response structure.
                // Often it's { "token": "..." } or just the string.
                String token = response.jsonPath().getString("token");
                if (token == null)
                    token = response.jsonPath().getString("jwt");
                if (token == null)
                    token = response.getBody().asString(); // Fallback
                return token;
            }

            // If 404, try /auth/login
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(body)
                    .post(baseUrl + "/auth/login");

            if (response.getStatusCode() == 200) {
                String token = response.jsonPath().getString("token");
                if (token == null)
                    token = response.jsonPath().getString("accessToken");
                return token;
            }

            System.err.println("Failed to login. Status: " + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fallback to hardcoded token from config if dynamic login fails
        System.out.println("Dynamic login failed, using fallback token from config for user: " + username);
        if (username.equals(ConfigLoader.getProperty("user.username"))) {
            return ConfigLoader.getProperty("user.token");
        } else if (username.equals(ConfigLoader.getProperty("admin.username"))) {
            return ConfigLoader.getProperty("admin.token");
        }

        return null;
    }
}
