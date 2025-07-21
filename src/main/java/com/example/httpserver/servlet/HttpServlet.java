package com.example.httpserver.servlet;

import com.example.httpserver.HttpRequest;
import com.example.httpserver.HttpResponse;
import java.io.IOException;

public interface HttpServlet {

    void service(HttpRequest request, HttpResponse response) throws IOException;

}