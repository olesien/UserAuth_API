package org.producer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8000
        SimpleServer.start();
    }
}