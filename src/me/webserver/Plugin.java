package me.webserver;

import javax.net.ssl.SSLContext;
import java.net.Socket;

/**
 * Created by User on 27/04/2015.
 */
public abstract class Plugin {
    public abstract void handle(Socket socket);
    public abstract int getListeningPort();

    public void init(){};

    public SSLContext getSSLContext() {
        return null;
    }
}
