package server;

import java.io.*;
// import java.nio.file.*;

public class Router {
    public static void routeRequest(String method, String path, BufferedReader in, OutputStream out)
            throws IOException {
        switch (path) {
            case "/":
                if ("GET".equalsIgnoreCase(method)) {
                    handleGET(out);
                } else {
                    out.write(HTTPParser.GetResponse405().getBytes());
                }
                break;
            case "/upload":
                if ("POST".equalsIgnoreCase(method)) {
                    handlePOST(in, out);
                } else {
                    out.write(HTTPParser.GetResponse405().getBytes());
                }
                break;
            default:
                out.write(HTTPParser.GetResponse404().getBytes());
        }
    }

    private static void handleGET(OutputStream out) throws IOException {
        out.write(HTTPParser.GetResponse200("Hello from GET").getBytes());
    }

    private static void handlePOST(BufferedReader in, OutputStream out) throws IOException {
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