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
            System.out.println("Server started. Test with:");
            System.out.println("GET: curl http://localhost:8080");
            System.out.println("POST: curl -X POST -d 'data' http://localhost:8080");
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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()) {

            // Parse request line (e.g., "GET / HTTP/1.1")
            String[] requestParts = HTTPParser.ParseRequest(in);
            if (requestParts == null)
                return;

            String method = requestParts[0];
            String path = requestParts[1];

            // Handle GET
            if ("GET".equalsIgnoreCase(method)) {
                HandleGET(out);
            }
            // Handle POST
            else if ("POST".equalsIgnoreCase(method)) {
                HandlePOST(in, out);
            }

            requestCount++;
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        }
    }

    private static synchronized void HandleGET(OutputStream out) throws IOException {
        String response = HTTPParser.GetResponse200(
                "GET request handled by thread: " + Thread.currentThread().threadId() + '\n');
        out.write(response.getBytes());
    }

    private static synchronized void HandlePOST(BufferedReader in, OutputStream out) throws IOException {
        // Read headers to find Content-Length
        int contentLength = 0;
        String line;
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }

        // Read POST data
        String postData = HTTPParser.ReadPostData(in, contentLength);
        String response = HTTPParser.GetResponse200(
                "POST data received by thread " + Thread.currentThread().threadId() +
                        ": " + postData + '\n');
        out.write(response.getBytes());
    }
}