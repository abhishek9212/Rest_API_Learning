import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BasicJavaServer {

    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create context for different endpoints
        server.createContext("/", new RootHandler());
        server.createContext("/hello", new HelloHandler());
        server.createContext("/api/users", new UsersHandler());

        // Set executor (null means default executor)
        server.setExecutor(null);

        // Start the server
        server.start();
        System.out.println("Server started on http://localhost:8000");
    }

    // Handler for root path "/"
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Abhishek ye hai Java HTTP Server!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Handler for "/hello" path
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response;

            if ("GET".equals(method)) {
                response = "Hello, World! This is a GET request.";
            } else {
                response = "Hello! Method: " + method;
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Handler for "/api/users" - basic REST endpoint
    static class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    response = "[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]";
                    break;
                case "POST":
                    response = "{\"message\":\"User created successfully\"}";
                    break;
                case "PUT":
                    response = "{\"message\":\"User updated successfully\"}";
                    break;
                case "DELETE":
                    response = "{\"message\":\"User deleted successfully\"}";
                    break;
                default:
                    response = "{\"error\":\"Method not supported\"}";
                    exchange.sendResponseHeaders(405, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
            }

            // Set JSON content type
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
