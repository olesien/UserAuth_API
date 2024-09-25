package org.producer;

import com.sun.net.httpserver.HttpServer;
import org.producer.endpoints.GetUsers;
import org.producer.endpoints.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8000
        SimpleServer.start();
    }
}