package com.example.httpserver.servlet;

import com.example.httpserver.HttpRequest;
import com.example.httpserver.HttpResponse;
import java.io.IOException;

public class InternalErrorServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatusCode(500);
        response.writeBody("<h1>Internal Error</h1>");
    }
}
