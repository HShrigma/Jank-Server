package server;

import java.io.*;
import java.net.*;

public class JankServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting Server...");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Try: curl http://localhost:8080");
            // Accept ONE connection
            Socket clientSocket = serverSocket.accept();

            OutputStream out = clientSocket.getOutputStream();
            out.write("HTTP/1.1 200 OK\r\n\r\nHello from raw Java!".getBytes());

            //clean up
            clientSocket.close();
        }

    }
}