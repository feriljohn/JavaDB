package MainMethods;

import com.sun.net.httpserver.HttpServer;

import ApiLayer.BookHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/books", new BookHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }
}