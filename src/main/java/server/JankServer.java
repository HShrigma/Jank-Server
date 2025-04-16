package server;

import java.io.*;
import java.net.*;
import server.*;

public class JankServer {
    public static void main(String[] args) throws IOException {
        HTTPParser.PrintServerSideInit();
        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            System.out.println("Server started. Try: curl http://localhost:8080");
            // Accept ONE connection
            Socket clientSocket = serverSocket.accept();

            OutputStream out = clientSocket.getOutputStream();
            out.write(HTTPParser.GetResponse200("Hello from raw Java using HTTPParserClass!\n").getBytes());

            //clean up
            clientSocket.close();
        }

    }
}