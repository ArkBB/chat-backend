package com.example.httpserver.controller;


import com.example.httpserver.HttpRequest;
import com.example.httpserver.HttpResponse;
import com.example.httpserver.servlet.Mapping;

public class SearchController {

    @Mapping("/search")
    public void search(HttpRequest request, HttpResponse response) {
        String query = request.getQueryParameter("q");

        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }

}
