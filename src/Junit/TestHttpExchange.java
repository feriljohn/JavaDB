package Junit;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class TestHttpExchange extends HttpExchange {
    private String method;
    private URI uri;
    private Headers requestHeaders;
    private InputStream requestBody;
    private Headers responseHeaders;
    private int responseCode;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    @SuppressWarnings("unused")
    private long responseLength;

    public TestHttpExchange() {

    }

    public TestHttpExchange(String method, URI uri, Headers requestHeaders, InputStream requestBody,
            Headers responseHeaders) {
        this.method = method;
        this.uri = uri;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
        this.responseHeaders = responseHeaders;
    }

    @Override
    public String getRequestMethod() {
        return method;
    }

    public void setRequestMethod(String method) {
        this.method = method;
    }

    public void setRequestHeader(URI uri) {
        this.uri = uri;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public URI getRequestURI() {
        return uri;
    }

    @Override
    public InputStream getRequestBody() {
        return requestBody;
    }

    @Override
    public OutputStream getResponseBody() {
        return outputStream;
    }

    @Override
    public void sendResponseHeaders(int responseCode, long responseLength) throws IOException {
        this.responseCode = responseCode;
        this.responseLength = responseLength;
        this.responseHeaders = getResponseHeaders();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        throw new UnsupportedOperationException("Unimplemented method 'getRemoteAddress'");
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        throw new UnsupportedOperationException("Unimplemented method 'getLocalAddress'");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Unimplemented method 'getProtocol'");
    }

    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'getAttribute'");
    }

    @Override
    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException("Unimplemented method 'setAttribute'");
    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {
        throw new UnsupportedOperationException("Unimplemented method 'setStreams'");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        throw new UnsupportedOperationException("Unimplemented method 'getPrincipal'");
    }

    @Override
    public HttpContext getHttpContext() {
        throw new UnsupportedOperationException("Unimplemented method 'getHttpContext'");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = new ByteArrayInputStream(requestBody.getBytes());
    }
}
