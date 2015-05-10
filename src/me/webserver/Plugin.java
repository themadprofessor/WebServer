package me.webserver;

import java.net.Socket;

/**
 * Created by User on 27/04/2015.
 */
public interface Plugin {
    void init();
    void handle(Socket socket);
    int getListeningPort();
}
