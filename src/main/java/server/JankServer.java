package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class JankServer {

    private static final int MAX_REQUESTS = 2;
    private static final int THREAD_POOL_SIZE = 5;
    private static int requestCount = 0;

    public static void main(String[] args) throws IOException {
        HTTPParser.PrintServerSideInit();
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Try: curl http://localhost:8080");
            while (requestCount < MAX_REQUESTS) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(() -> handleRequest(clientSocket));
            }
        } finally {
            pool.shutdown();
            System.out.println("Reached max requests: " + MAX_REQUESTS + "\nServer is closing");
        }
    }

    private static synchronized void handleRequest(Socket clientSocket) {
        try (OutputStream out = clientSocket.getOutputStream()) {
            String response = HTTPParser.GetResponse200(
                    "Handled by thread: " + Thread.currentThread().threadId() + "\n");
            out.write(response.getBytes());

            // Thread-safe counter increment
            requestCount++;
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        }
    }
}