package Junit;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.json.JSONObject;
import org.junit.Test;

import ApiLayer.BookHandler;


public class BookHandlerTest {


    @Test
    public void handleGetRequestByIdTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("GET");
        exchange.setRequestHeader(URI.create("/books/1"));
        BookHandler bookHandler = new BookHandler();
        bookHandler.handleGetBookById(exchange);
        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    public void handlePostRequestTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("POST");

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("book_name", "NewBook");
        requestBodyJson.put("dop", "2022-12-31");
        requestBodyJson.put("auth_Id", 1);
        requestBodyJson.put("gen_Id", 2);
        String requestBody = requestBodyJson.toString();
        exchange.setRequestBody(requestBody);
        BookHandler bookHandler = new BookHandler();
        bookHandler.handlePostRequest(exchange);
        assertEquals(201, exchange.getResponseCode());
    }

    @Test
    public void handlePutRequestTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("PUT");
        exchange.setRequestHeader(URI.create("/books/20"));

        String requestBody = "{" +
                "\"book_name\": \"Spider\"," +
                "\"dop\": \"2023-01-15\"," +
                "\"auth_Id\": 3," +
                "\"gen_Id\": 4" +
                "}";

        exchange.setRequestBody(requestBody);
        BookHandler bookHandler = new BookHandler();
        bookHandler.handlePutRequest(exchange);
        assertEquals(200, exchange.getResponseCode());

    }

    @Test
    public void handleDeleteRequestTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("DELETE");
        exchange.setRequestHeader(URI.create("/books/23"));
        BookHandler bookHandler = new BookHandler();
        bookHandler.handleDeleteRequest(exchange);
        assertEquals(200, exchange.getResponseCode());

    }

    @Test
    public void handleGetRequestByIdInValidTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("GET");
        exchange.setRequestHeader(URI.create("/books/90"));
        BookHandler bookHandler = new BookHandler();
        bookHandler.handleGetBookById(exchange);
        assertEquals(404, exchange.getResponseCode());
    }

    @Test
    public void handlePostRequestInValidTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("POST");

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("book_name", "NewBook");
        requestBodyJson.put("dop", "2025-12-31");
        requestBodyJson.put("auth_Id", 1);
        requestBodyJson.put("gen_Id", 2);
        String requestBody = requestBodyJson.toString();
        exchange.setRequestBody(requestBody);
        BookHandler bookHandler = new BookHandler();
        bookHandler.handlePostRequest(exchange);
        assertEquals(400, exchange.getResponseCode());
    }

    @Test
    public void handlePutRequestInValidTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("PUT");
        exchange.setRequestHeader(URI.create("/books/55"));

        String requestBody = "{" +
                "\"book_name\": \"Spider\"," +
                "\"dop\": \"2023-01-15\"," +
                "\"auth_Id\": 3," +
                "\"gen_Id\": 4" +
                "}";

        exchange.setRequestBody(requestBody);
        BookHandler bookHandler = new BookHandler();
        bookHandler.handlePutRequest(exchange);
        assertEquals(404, exchange.getResponseCode());

    }

    @Test
    public void handleDeleteRequestInValidTest() throws IOException, SQLException {
        TestHttpExchange exchange = new TestHttpExchange();
        exchange.setRequestMethod("DELETE");
        exchange.setRequestHeader(URI.create("/books/89"));
        BookHandler bookHandler = new BookHandler();
        bookHandler.handleDeleteRequest(exchange);
        assertEquals(404, exchange.getResponseCode());

    }

}
