package com.example.httpserver.servlet;

import com.example.httpserver.HttpRequest;
import com.example.httpserver.HttpResponse;
import com.example.httpserver.error.PageNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationServlet implements HttpServlet {

    private final Map<String, ControllerMethod> pathMap;

    public AnnotationServlet(List<Object> controllers) {
        this.pathMap = new HashMap<>();
        initializePathMap(controllers);
    }

    private void initializePathMap(List<Object> controllers) {

        for (Object controller : controllers) {
            for (Method method : controller.getClass().getDeclaredMethods()) {
                if(method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String path = mapping.value();
                    if(pathMap.containsKey(path)) {
                        throw new IllegalArgumentException("Path " + path + " is duplicated.");
                    }
                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }

    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        ControllerMethod controllerMethod = pathMap.get(path);

        if(controllerMethod == null) {
            throw new PageNotFoundException("request url = " + path);
        }

        controllerMethod.invoke(request, response);


    }

    private static class ControllerMethod {
        private final Object controller;
        private final Method method;

        public ControllerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }


        public void invoke(HttpRequest request, HttpResponse response){
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for(int i = 0; i < parameterTypes.length; i++) {
                if(parameterTypes[i] == HttpRequest.class) {
                    args[i] = request;
                }
                else if(parameterTypes[i] == HttpResponse.class) {
                    args[i] = response;
                } else {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
                }
            }

            try {
                method.invoke(controller,args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
