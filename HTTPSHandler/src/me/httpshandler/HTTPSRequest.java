package me.httpshandler;

import java.util.ArrayList;

/**
 * Created by root on 10/04/15.
 */
public enum HTTPSRequest {
    GET, POST, HEAD, TRACE, OPTIONS;
    String request;
    ArrayList<MediaType> accepted;

    HTTPSRequest() {
        accepted = new ArrayList<>();
    }

    public static HTTPSRequest fromString(String string) {
        for (HTTPSRequest request : values()) {
            if (request.toString().equals(string)) {
                return request;
            }
        }
        return null;
    }
}
