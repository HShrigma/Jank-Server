package server;

import java.io.*;
import java.nio.file.*;

public class Router {
    public static void routeRequest(String method, String path, BufferedReader in, InputStream rawIn, OutputStream out)
            throws IOException {
        if (path.startsWith("/upload/")) {
            if ("GET".equalsIgnoreCase(method))
                handleFileDownload(path, out);
            else
                out.write(HTTPParser.GetResponse405().getBytes());
        } else {
            switch (path) {
                case "/":
                    handleGET(out);
                    break;
                case "/upload":
                    if ("POST".equalsIgnoreCase(method))
                        handlePOST(in, rawIn, out);
                    else
                        out.write(HTTPParser.GetResponse405().getBytes());
                    break;
                default:
                    out.write(HTTPParser.GetResponse404().getBytes());
            }
        }
    }

    private static void handleGET(OutputStream out) throws IOException {
        out.write(HTTPParser.GetResponse200("Hello from GET").getBytes());
    }

    private static void handlePOST(BufferedReader in, InputStream rawIn, OutputStream out) throws IOException {
        int contentLength = 0;
        String line;
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }

        byte[] fileData = new byte[contentLength];
        rawIn.read(fileData, 0, contentLength);

        String filename = "upload_" + System.currentTimeMillis() + ".dat";
        Path destination = Paths.get("src/main/resources/uploads", filename);
        Files.createDirectories(destination.getParent());
        Files.write(destination, fileData);

        out.write(HTTPParser.GetResponse200("Saved as " + filename).getBytes());
    }

    private static void handleFileDownload(String path, OutputStream out) throws IOException {
        Path file = Paths.get("src/main/resources/uploads", path.substring(8));
        if (!Files.exists(file)) {
            out.write(HTTPParser.GetResponse404().getBytes());
            return;
        }

        out.write(HTTPParser.GetFileResponse(file).getBytes());
        Files.copy(file, out);
    }
}
