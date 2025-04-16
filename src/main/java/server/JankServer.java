package server;

import java.io.*;
import java.net.*;

import server.*;

public class JankServer {
    public static void main(String[] args) throws IOException {
        HTTPParser.PrintServerSideInit();
        int maxRequests = 2;
        int reqsCount = 0;
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started. Try: curl http://localhost:8080");

        while (reqsCount < maxRequests) {
            // Accept connections on 1 thread using try w/ resources
            try (Socket clientSocket = serverSocket.accept()) {
                OutputStream out = clientSocket.getOutputStream();
                
                String res = HTTPParser.GetResponse200("Hello from raw Java using HTTPParserClass!\n");
                
                out.write(res.getBytes());

            } catch (IOException e) {
                System.err.println("Request failed: " + e.getMessage());
            }

            reqsCount++;
        }
        System.out.println("Reached max Requests: " + maxRequests + "\nServer is closing");
        serverSocket.close();
    }
}