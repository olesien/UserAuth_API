package org.producer.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.producer.DB;
import org.producer.User;
import org.producer.UserRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

// Handler to process requests to "/"
public class AddUser implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // Read the request body and parse it into a User object
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            // Deserialize
            User user;
            try {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                user = gson.fromJson(body.toString(), User.class);
            } catch (JsonSyntaxException e) {
                String response = "{\"error\": \"Invalid JSON format\"}";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            try (var con =  DB.connect()){
                UserRepo userRepo = new UserRepo(con);
                System.out.println("Add user");
                User newUser = userRepo.createUser(user.getName(), user.getEmail(), user.getAge(), user.getGender(), user.getPassword(), false, false);
                System.out.println("Added user");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String response = gson.toJson(newUser);
                System.out.println(response);
                // Send response
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }


    }
}