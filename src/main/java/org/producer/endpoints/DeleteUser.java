package org.producer.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.producer.DB;
import org.producer.User;
import org.producer.UserRepo;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

// Handler to process requests to "/"
public class DeleteUser implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            String path = exchange.getRequestURI().getPath(); // e.g., /deleteuser/123
            String[] segments = path.split("/"); // Splits the path into parts
            if (segments.length < 3) {
                // If the ID is not present, return a 400 Bad Request
                String response = "{\"error\": \"User ID is required\"}";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            // The ID should be in the last segment (e.g., 123)
            int userId;
            try {
                userId = Integer.parseInt(segments[2]); // Extract and parse the user ID
            } catch (NumberFormatException e) {
                // If the ID is not a valid number, return a 400 Bad Request
                String response = "{\"error\": \"Invalid User ID\"}";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            try (var con =  DB.connect()){
                UserRepo userRepo = new UserRepo(con);
                boolean success = userRepo.deleteUser(userId);
                String response = "{\"message\": \"Success!\"}";
                if (success) {
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "{\"message\": \"Fail!\"}";
                    exchange.sendResponseHeaders(400, response.length());
                }
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}