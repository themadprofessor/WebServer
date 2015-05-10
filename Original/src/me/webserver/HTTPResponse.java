package me.webserver;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by root on 10/04/15.
 */
public class HTTPResponse {
    private final String endline = "\r\n";
    MediaType contentType;
    String body;

    public HTTPResponse(MediaType contentType, String body) {
        this.contentType = contentType;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK").append(endline);
        builder.append("Date: ").append(LocalDate.now()).append(" ").append(LocalTime.now()).append(endline);
        builder.append("Content-Type: ").append(contentType).append(endline);
        builder.append(endline);
        if (body != null) {
            builder.append(body);
        }
        return builder.toString();
    }
}
