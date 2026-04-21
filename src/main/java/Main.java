import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;
import controllers.AuthController;
import controllers.ElectionController;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final BufferedReader in;
        private final PrintWriter out;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String requestLine = in.readLine();
                if (requestLine != null) {
                    String[] requestParts = requestLine.split(" ");
                    String method = requestParts[0];
                    String path = requestParts[1];

                    Map<String, String> headers = new HashMap<>();
                    String headerLine;
                    while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                        String[] headerParts = headerLine.split(": ", 2);
                        if (headerParts.length == 2) {
                            headers.put(headerParts[0], headerParts[1]);
                        }
                    }

                    String body = "";
                    if (headers.containsKey("Content-Length")) {
                        int contentLength = Integer.parseInt(headers.get("Content-Length"));
                        char[] buffer = new char[contentLength];
                        in.read(buffer, 0, contentLength);
                        body = new String(buffer);
                    }

                    handleRequest(method, path, headers, body);
                }
            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void handleRequest(String method, String path, Map<String, String> headers, String body) {
            try {
                if (method.equals("POST") && path.equals("/api/login")) {
                    handleLogin(body);
                } else if (method.equals("POST") && path.equals("/api/logout")) {
                    handleLogout(body);
                } else if (method.equals("POST") && path.equals("/api/validate-session")) {
                    handleValidateSession(body);
                } else if (method.equals("POST") && path.equals("/api/register-candidate")) {
                    handleRegisterCandidate(body);
                } else if (method.equals("POST") && path.equals("/api/register-voter")) {
                    handleRegisterVoter(body);
                } else if (method.equals("POST") && path.equals("/api/cast-vote")) {
                    handleCastVote(body);
                } else if (method.equals("GET") && path.equals("/api/results")) {
                    handleGetResults();
                } else if (method.equals("GET") && path.equals("/api/candidates")) {
                    handleGetCandidates();
                } else if (method.equals("GET") && path.equals("/api/voters")) {
                    handleGetVoters();
                } else {
                    sendResponse(404, "Not Found");
                }
            } catch (Exception e) {
                sendResponse(500, "Internal Server Error");
            }
        }

        private void handleLogin(String body) {
            try {
                String[] params = body.split("&");
                String username = "";
                String password = "";

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("username")) {
                            username = URLDecoder.decode(keyValue[1], "UTF-8");
                        } else if (keyValue[0].equals("password")) {
                            password = URLDecoder.decode(keyValue[1], "UTF-8");
                        }
                    }
                }

                Map<String, Object> response = AuthController.login(username, password);
                sendJsonResponse(response.containsKey("error") ? 401 : 200, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleLogout(String body) {
            try {
                String[] params = body.split("&");
                String sessionId = "";

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("sessionId")) {
                        sessionId = URLDecoder.decode(keyValue[1], "UTF-8");
                    }
                }

                Map<String, Object> response = AuthController.logout(sessionId);
                sendJsonResponse(response.containsKey("error") ? 401 : 200, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleValidateSession(String body) {
            try {
                String[] params = body.split("&");
                String sessionId = "";

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("sessionId")) {
                        sessionId = URLDecoder.decode(keyValue[1], "UTF-8");
                    }
                }

                Map<String, Object> response = AuthController.validateSession(sessionId);
                sendJsonResponse(response.containsKey("error") ? 401 : 200, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleRegisterCandidate(String body) {
            try {
                String[] params = body.split("&");
                String name = "";

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("name")) {
                        name = URLDecoder.decode(keyValue[1], "UTF-8");
                    }
                }

                Map<String, Object> response = ElectionController.registerCandidate(name);
                sendJsonResponse(response.containsKey("error") ? 400 : 201, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleRegisterVoter(String body) {
            try {
                String[] params = body.split("&");
                String name = "";

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("name")) {
                        name = URLDecoder.decode(keyValue[1], "UTF-8");
                    }
                }

                Map<String, Object> response = ElectionController.registerVoter(name);
                sendJsonResponse(response.containsKey("error") ? 400 : 201, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleCastVote(String body) {
            try {
                String[] params = body.split("&");
                int voterId = 0;
                int candidateId = 0;

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("voterId")) {
                            voterId = Integer.parseInt(URLDecoder.decode(keyValue[1], "UTF-8"));
                        } else if (keyValue[0].equals("candidateId")) {
                            candidateId = Integer.parseInt(URLDecoder.decode(keyValue[1], "UTF-8"));
                        }
                    }
                }

                Map<String, Object> response = ElectionController.castVote(voterId, candidateId);
                sendJsonResponse(response.containsKey("error") ? 400 : 200, response);
            } catch (Exception e) {
                sendResponse(400, "Bad Request");
            }
        }

        private void handleGetResults() {
            try {
                Map<String, Object> response = ElectionController.getResults();
                sendJsonResponse(200, response);
            } catch (Exception e) {
                sendResponse(500, "Internal Server Error");
            }
        }

        private void handleGetCandidates() {
            try {
                Map<String, Object> response = ElectionController.getCandidates();
                sendJsonResponse(200, response);
            } catch (Exception e) {
                sendResponse(500, "Internal Server Error");
            }
        }

        private void handleGetVoters() {
            try {
                Map<String, Object> response = ElectionController.getVoters();
                sendJsonResponse(200, response);
            } catch (Exception e) {
                sendResponse(500, "Internal Server Error");
            }
        }

        private void sendJsonResponse(int statusCode, Map<String, Object> data) {
            StringBuilder json = new StringBuilder();
            json.append("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(entry.getKey()).append("\":");
                if (entry.getValue() instanceof String) {
                    json.append("\"").append(entry.getValue()).append("\"");
                } else {
                    json.append(entry.getValue());
                }
                first = false;
            }
            json.append("}");

            sendResponse(statusCode, json.toString(), "application/json");
        }

        private void sendResponse(int statusCode, String body) {
            sendResponse(statusCode, body, "text/plain");
        }

        private void sendResponse(int statusCode, String body, String contentType) {
            String statusText = getStatusText(statusCode);
            out.println("HTTP/1.1 " + statusCode + " " + statusText);
            out.println("Content-Type: " + contentType);
            out.println("Access-Control-Allow-Origin: *");
            out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
            out.println("Access-Control-Allow-Headers: Content-Type, Authorization");
            out.println("Content-Length: " + body.length());
            out.println();
            out.print(body);
            out.flush();
        }

        private String getStatusText(int statusCode) {
            switch (statusCode) {
                case 200: return "OK";
                case 400: return "Bad Request";
                case 401: return "Unauthorized";
                case 404: return "Not Found";
                case 500: return "Internal Server Error";
                default: return "Unknown";
            }
        }
    }
}
