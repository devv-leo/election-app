package controllers;

import java.util.HashMap;
import java.util.Map;

public class FrontendController {
    public static Map<String, Object> login(String username, String password) {
        return AuthController.login(username, password);
    }

    public static Map<String, Object> logout(String sessionId) {
        return AuthController.logout(sessionId);
    }

    public static Map<String, Object> validateSession(String sessionId) {
        return AuthController.validateSession(sessionId);
    }
}
