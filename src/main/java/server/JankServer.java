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
            System.out.println("POST: curl -X POST --data-binary @file.dat http://localhost:8080/upload");
            System.out.println("DOWNLOAD: curl http://localhost:8080/upload/<filename>");

            while (requestCount < MAX_REQUESTS) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(() -> handleRequest(clientSocket));
            }
        } finally {
            pool.shutdown();
            System.out.println("Reached max requests: " + MAX_REQUESTS + "\nServer is closing");
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (
            InputStream rawIn = clientSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(rawIn));
            OutputStream out = clientSocket.getOutputStream()
        ) {
            String[] requestParts = HTTPParser.ParseRequest(in);
            if (requestParts == null || requestParts.length < 2) {
                out.write(HTTPParser.GetResponse400().getBytes());
                return;
            }

            // Route with both reader (headers) and rawIn (binary body)
            Router.routeRequest(requestParts[0], requestParts[1], in, rawIn, out);

        } catch (Exception e) {
            handleServerError(clientSocket, e);
        } finally {
            incrementRequestCount();
        }
    }

    private static void handleServerError(Socket socket, Exception e) {
        try {
            socket.getOutputStream().write(HTTPParser.GetResponse500().getBytes());
        } catch (IOException ioException) {
            System.err.println("Failed to send 500: " + ioException.getMessage());
        }
        System.err.println("Server error: " + e.getMessage());
    }

    private static synchronized void incrementRequestCount() {
        requestCount++;
    }
}
