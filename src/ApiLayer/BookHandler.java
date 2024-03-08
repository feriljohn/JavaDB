package ApiLayer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import BusinessLayer.BookService;
import EntityConstructors.BookCrud;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookHandler implements HttpHandler {

    private final BookService service;

    public BookHandler() {
        this.service = new BookService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET":
                    handleGetRequest(exchange);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "PUT":
                    handlePutRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange);
                    break;
                default:
                    handleErrorResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            handleErrorResponse(exchange, 500, "Internal Server Error");
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/books")) {
            handleGetAllBooks(exchange);
        } else {
            handleGetBookById(exchange);
        }
    }

    public void handlePostRequest(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/books/repair")) {
            handlePostRequestRepair(exchange);
        } else {
            handlePostByRequest(exchange);
        }
    }

    public void handleGetAllBooks(HttpExchange exchange) throws IOException {
        try {
            List<BookCrud> books = service.getAllBooks();
            JSONArray jsonArray = new JSONArray();

            for (BookCrud book : books) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("book_id", book.getBookId());
                jsonObject.put("book_name", book.getBookName());
                jsonObject.put("dop", book.getDop());
                jsonObject.put("auth_id", book.getAuthId());
                jsonObject.put("gen_id", book.getGenId());
                jsonArray.put(jsonObject);
            }

            String response = jsonArray.toString();
            sendResponse(exchange, 200, response);
        } catch (SQLException e) {
            handleErrorResponse(exchange, 500, "Error retrieving books from the database");
        } catch (Exception e) {
            handleErrorResponse(exchange, 500, "Unexpected error");
        }
    }

    public void handleGetBookById(HttpExchange exchange) throws IOException {
        try {
            int bookId = extractBookIdFromRequestURI(exchange.getRequestURI().getPath());
            List<BookCrud> books = service.getBookById(bookId);

            if (!books.isEmpty()) {
                BookCrud book = books.get(0);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("book_id", book.getBookId());
                jsonObject.put("book_name", book.getBookName());
                jsonObject.put("dop", book.getDop());
                jsonObject.put("auth_id", book.getAuthId());
                jsonObject.put("gen_id", book.getGenId());
                jsonObject.put("damaged", book.getDamageName());
                jsonObject.put("repair_status", book.getRepairStatus());
                sendResponse(exchange, 200, jsonObject.toString());
            } else {
                handleErrorResponse(exchange, 404, "Book not found");
            }
        } catch (NumberFormatException e) {
            handleErrorResponse(exchange, 400, "Invalid book ID");
        } catch (SQLException e) {
            handleErrorResponse(exchange, 500, "Internal server error");
        } catch (Exception e) {
            handleErrorResponse(exchange, 500, "Unexpected error");
        }
    }

    public void handlePostByRequest(HttpExchange exchange) throws IOException {
        try {
            String requestBody = Utils.convertInputStreamToString(exchange.getRequestBody());
            JSONObject json = new JSONObject(requestBody);

            String bookName = json.getString("book_name");
            String dopString = json.getString("dop");
            int authId = json.getInt("auth_Id");
            int genId = json.getInt("gen_Id");
            String damageName = json.getString("damage");
            String repairStatus = "";

            repairStatus = "Pending";
            if (damageName.contains("no damage")) {
                repairStatus = "No need to repair";
            }
            Date dopDate = convertStringToDate(dopString);

            if (service.addBook(bookName, dopDate, authId, genId, damageName, repairStatus)) {
                sendResponse(exchange, 201, "Book created successfully");
            } else {
                handleErrorResponse(exchange, 400, "Book cannot be added");
            }
        } catch (SQLException e) {
            handleErrorResponse(exchange, 500, "Internal server error");
        } catch (Exception e) {
            handleErrorResponse(exchange, 500, "Unexpected error");
        }
    }

    public void handlePostRequestRepair(HttpExchange exchange) throws IOException, SQLException {
        List<BookCrud> books = new ArrayList<>();
        books = service.manageRepair();

        try {
            service.updateStatus(books);

            String response = "repair status updated";
            sendResponse(exchange, 201, response);

        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Error creating book in the database";
            sendResponse(exchange, 400, errorMessage);
        }
    }

    public void handlePutRequest(HttpExchange exchange) throws IOException, SQLException {
        try {
            int bookIdUrl = extractBookIdFromRequestURI(exchange.getRequestURI().getPath());
            String requestBody = Utils.convertInputStreamToString(exchange.getRequestBody());
            JSONObject json = new JSONObject(requestBody);

            String bookName = json.getString("book_name");
            String dopString = json.getString("dop");
            int authorId = json.getInt("auth_Id");
            int genreId = json.getInt("gen_Id");
            String damage = json.getString("damage");
            String repairStatus = "";

            repairStatus = "Pending";
            if (damage.contains("no damage")) {
                repairStatus = "No need to repair";
            }

            Date dopDate = convertStringToDate(dopString);
            if (service.updateBook(bookIdUrl, bookName, dopDate, authorId, genreId, damage, repairStatus)) {
                sendResponse(exchange, 200, "Book updated successfully");
            } else {
                handleErrorResponse(exchange, 404, "Book cannot be updated");
            }
        } catch (SQLException e) {
            handleErrorResponse(exchange, 500, "Internal server error");
        } catch (Exception e) {
            handleErrorResponse(exchange, 500, "Unexpected error");
        }
    }

    public void handleDeleteRequest(HttpExchange exchange) throws IOException, SQLException {
        try {
            int bookId = extractBookIdFromRequestURI(exchange.getRequestURI().getPath());
            if (service.deleteBook(bookId)) {
                sendResponse(exchange, 200, "Book deleted successfully");
            } else {
                handleErrorResponse(exchange, 404, "Book not found");
            }
        } catch (SQLException e) {
            handleErrorResponse(exchange, 500, "Internal server error");
        } catch (Exception e) {
            handleErrorResponse(exchange, 500, "Unexpected error");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }

    private void handleErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        exchange.sendResponseHeaders(statusCode, errorMessage.length());
        exchange.getResponseBody().write(errorMessage.getBytes());
        exchange.getResponseBody().close();
    }

    private int extractBookIdFromRequestURI(String requestURI) {
        String[] parts = requestURI.split("/");
        if (parts.length >= 3) {
            return Integer.parseInt(parts[2].replaceAll("[^\\d]", ""));
        }
        throw new NumberFormatException("Invalid book ID in the request");
    }

    private static class Utils {
        public static String convertInputStreamToString(InputStream inputStream) throws IOException {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                return br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    private Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = sdf.parse(dateString);
        return new Date(utilDate.getTime());
    }
}

////welcome to 

///2nd commit
