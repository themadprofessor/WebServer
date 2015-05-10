package me.httphandler;

import java.util.ArrayList;

/**
 * Created by root on 10/04/15.
 */
public enum  HTTPRequest {
    GET, POST, HEAD, TRACE, OPTIONS;
    String request;
    ArrayList<MediaType> accepted;

    HTTPRequest() {
        accepted = new ArrayList<>();
    }

    public static HTTPRequest fromString(String string) {
        for (HTTPRequest request : values()) {
            if (request.toString().equals(string)) {
                return request;
            }
        }
        return null;
    }
}
