package com.example.httpserver.servlet;

import com.example.httpserver.HttpRequest;
import com.example.httpserver.HttpResponse;
import java.io.IOException;

public class DiscardServlet implements HttpServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        // empty
    }
}
