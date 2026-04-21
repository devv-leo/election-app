package controllers;

import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private static Map<String, String> sessions = new HashMap<>();
    private static int userIdCounter = 1;

    public static Map<String, Object> login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Username and password required");
            return error;
        }

        String role = "VOTER";
        if (username.equals("admin") && password.equals("admin")) {
            role = "ADMIN";
        }

        String sessionId = "session_" + System.currentTimeMillis();
        sessions.put(sessionId, username + ":" + role);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("username", username);
        response.put("role", role);
        return response;
    }

    public static Map<String, Object> logout(String sessionId) {
        if (sessionId != null && sessions.containsKey(sessionId)) {
            sessions.remove(sessionId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Logged out successfully");
            return response;
        }
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid session");
        return error;
    }

    public static Map<String, Object> validateSession(String sessionId) {
        if (sessionId != null && sessions.containsKey(sessionId)) {
            String sessionData = sessions.get(sessionId);
            String[] parts = sessionData.split(":");
            Map<String, Object> response = new HashMap<>();
            response.put("username", parts[0]);
            response.put("role", parts[1]);
            return response;
        }
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid or expired session");
        return error;
    }

    public static int createUserId() {
        return userIdCounter++;
    }
}
