package server;

public class HTTPParser {
    public static void PrintServerSideInit(){
        System.out.println("Starting Server...");
    }
    public static String GetResponse200(String message){
        return "HTTP/1.1 200 OK\r\n\r\n" + message;
    } 
}