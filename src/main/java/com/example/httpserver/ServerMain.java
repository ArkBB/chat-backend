package com.example.httpserver;


import com.example.httpserver.controller.SearchController;
import com.example.httpserver.controller.SiteController;
import com.example.httpserver.servlet.AnnotationServlet;
import com.example.httpserver.servlet.DiscardServlet;
import com.example.httpserver.servlet.HttpServlet;
import java.io.IOException;
import java.util.List;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteController(), new SearchController());
        HttpServlet servlet = new AnnotationServlet(controllers);
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(servlet);
        servletManager.add("/favicon.ico",new DiscardServlet());
        HttpServer server = new HttpServer(PORT,servletManager);
        server.start();
    }

}
