package com.example.httpserver;

import static com.example.chatserver.MyLogger.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path; //?q=hello&key2=value2
    private String version;

    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        parseRequestLine(reader);
        parseHeaders(reader);
        parseBody(reader);
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if(!headers.containsKey("Content-Length")){
            return;
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] bodyChars = new char[contentLength];
        int read = reader.read(bodyChars);
        if (read != contentLength) {
            throw new IOException("Fail to read entire body. Expected " + contentLength + " bytes, but read " + read);
        }

        String body = new String(bodyChars);
        log("Http Message Body: " + body);

        String contentType = headers.get("Content-Type");
        if(contentType.equals("application/x-www-form-urlencoded")){
            parseQueryParameters(body);
        }
    }

    // GET /search?q=hello HTTP/1.1
    // Host: localhost:12345

    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if(requestLine == null) {
            throw new IOException("EOF: No request line received");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }
        // GET , POST 등
        method = parts[0];
        // /search?q=hello
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        if(pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }

        // HTTP/1.1
        version = parts[2];

    }
    // q=hello
    // key1=value&key2=value2
    private void parseQueryParameters(String pathParts) throws IOException {
        String[] queryParts = pathParts.split("&");
        for(String queryPart : queryParts) {
            String[] keyValue = queryPart.split("=");
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";
            queryParameters.put(key,value);
        }
    }

    // Host: localhost:12345
    // Connection: keep-alive
    // Cache-Control: max-age=0
    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while(!(line = reader.readLine()).isEmpty()){
            String[] parts = line.split(":");
            //trim() 앞 뒤에 공백 제거
            headers.put(parts[0].trim(), parts[1].trim());
        }
    }

    public String getMethod() {
        return method;
    }



    public String getPath() {
        return path;
    }


    public String getVersion() {
        return version;
    }


    public String getQueryParameter(String key) {
        return queryParameters.get(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
