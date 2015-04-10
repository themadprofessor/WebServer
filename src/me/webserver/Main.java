package me.webserver;

import me.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by stuart on 10/04/15.
 */
public class Main {
    ServerSocket serverSocket;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            serverSocket = new ServerSocket(80);
            Log.out("Entering Loop");
            while (true) {
                Log.out("Waiting For Request");
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ProcessingThread(socket));
                thread.start();
            }
        } catch (IOException e) {
            Log.err(e);
        }
    }
}
