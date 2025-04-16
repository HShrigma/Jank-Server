package server;

import java.io.BufferedReader;
import java.io.IOException;

public class HTTPParser {
    // Default welcome message to print to the host
    public static void PrintServerSideInit() {
        System.out.println("Starting Server...");
    }

    // Returns (required) HTTP OK response (status 200) + a message for the user
    public static String GetResponse200(String message) {
        return "HTTP/1.1 200 OK\r\n\r\n" + message;
    }
    public static String GetResponse400() {
        return "HTTP/1.1 400 Bad Request\r\n\r\n";
    }
    public static String GetResponse404() {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }
    public static String GetResponse405() {
        return "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
    }
    public static String GetResponse500() {
        return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
    }

    // Returns line from Buffered reader split by whitespace characters as vanilla String array
    // Use case in app: Extract HTTP method and path from request 
    public static String[] ParseRequest(BufferedReader in) throws IOException {
        String reqLine = in.readLine();
        if (reqLine == null)
            return null;
        return reqLine.split(" ");
    }
    // Simple POST data reading
    // Places Buffered Reader values in char[] buffer and returns String from buffer. 
    public static String ReadPostData(BufferedReader in, int contentLength) throws IOException{
        char[] buffer = new char[contentLength];
        in.read(buffer, 0, contentLength);

        return new String(buffer);
    }

}