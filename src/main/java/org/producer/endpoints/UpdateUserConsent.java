package org.producer.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.producer.DB;
import org.producer.UserRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;

// Handler to process requests to "/"
public class UpdateUserConsent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
            System.out.println("Update user");
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

            // Read the request body
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JsonObject jsonObject;
            try {
                jsonObject = gson.fromJson(body.toString(), JsonObject.class);
            } catch (JsonSyntaxException e) {
                String response = "{\"error\": \"Invalid JSON format\"}";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            boolean cookieConsent = jsonObject.get("cookie_consent").getAsBoolean();
            boolean dataConsent = jsonObject.get("data_consent").getAsBoolean();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            try (var con =  DB.connect()){
                UserRepo userRepo = new UserRepo(con);
                boolean success = userRepo.updateConsent(userId, cookieConsent, dataConsent);
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