package me.webserver;

/**
 * Created by root on 10/04/15.
 */
public enum DataType {
    APPLICATION, AUDIO, IMAGE, MESSAGE, MODEL, MULTIPART, TEXT, VIDEO, UNKNOWN;

    public static DataType fromString(String string) {
        return valueOf(string.toUpperCase());
    }
}
