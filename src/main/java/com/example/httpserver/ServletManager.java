package com.example.httpserver;

import com.example.httpserver.error.PageNotFoundException;
import com.example.httpserver.servlet.HttpServlet;
import com.example.httpserver.servlet.InternalErrorServlet;
import com.example.httpserver.servlet.NotFoundServlet;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;
    private HttpServlet notFoundErrorServlet = new NotFoundServlet();
    private HttpServlet interalErrorServlet = new InternalErrorServlet();

    public ServletManager() {
    }

    public void add(String path, HttpServlet servlet){
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet servlet){
        this.defaultServlet = servlet;
    }

    public void setNotFoundErrorServlet(HttpServlet servlet){
        this.notFoundErrorServlet = servlet;
    }

    public void setInteralErrorServlet(HttpServlet servlet){
        this.interalErrorServlet = servlet;
    }

    public void execute(HttpRequest request, HttpResponse response) throws Exception {
        try{
            HttpServlet servlet = servletMap.getOrDefault(request.getPath(), defaultServlet);
            if(servlet == null){
                throw new PageNotFoundException("request url= " + request.getPath());
            }
            servlet.service(request, response);
        } catch (PageNotFoundException e){
            e.printStackTrace();
            notFoundErrorServlet.service(request, response);
        } catch (Exception e){
            e.printStackTrace();
            interalErrorServlet.service(request, response);
        }
    }

}
