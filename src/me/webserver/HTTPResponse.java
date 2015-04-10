package me.webserver;

/**
 * Created by root on 10/04/15.
 */
public class HTTPResponse {
    private final String endline = "\r\n";
    String contentType;
    String body;

    public HTTPResponse(String contentType, String body) {
        this.contentType = contentType;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK").append(endline);
        builder.append("Content-Type: ").append(contentType).append(endline);
        builder.append(endline);
        builder.append(body);
        return builder.toString();
    }
}
