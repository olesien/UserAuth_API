package org.producer;

import com.sun.net.httpserver.HttpServer;
import org.producer.endpoints.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleServer {
    static void start() throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Endpoints
            server.createContext("/", new RootHandler());
            server.createContext( "/getusers", new GetUsers() );
            server.createContext( "/adduser", new AddUser() );
            server.createContext( "/deleteuser", new DeleteUser() );
            server.createContext( "/updateuserconsent", new UpdateUserConsent() );

            // Start the server
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Server started on port 8000");


    }
}
