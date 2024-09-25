package org.producer.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.producer.DB;
import org.producer.User;
import org.producer.UserRepo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;

// Handler to process requests to "/"
public class GetUsers implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        try (var con =  DB.connect()){
            UserRepo userRepo = new UserRepo(con);
            System.out.println("Getting users");
            ArrayList<User> users = userRepo.getUsers();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String response = gson.toJson(users);
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