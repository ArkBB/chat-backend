package com.example.httpserver;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private final PrintWriter writer;
    private int statusCode;
    private StringBuilder body = new StringBuilder();
    private String contentType;

    public HttpResponse(PrintWriter writer) {
        this.writer = writer;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void writeBody(String body) {
        this.body.append(body);
    }

    public void flush() {
        int contentLength = body.toString().getBytes(StandardCharsets.UTF_8).length;
        writer.println("HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode));
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + contentLength);
        writer.println();
        writer.print(body.toString());
        writer.flush();
    }

    private String getReasonPhrase(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown Status";
        }
    }
}
