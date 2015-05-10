package me.webserver;

import me.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by root on 10/04/15.
 */
public class ListeningThread implements Runnable {
    public boolean stop;
    private ServerSocket serverSocket;
    private ArrayList<ProcessingThread> threads;

    public ListeningThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        threads = new ArrayList<>();

        String name = Thread.currentThread().getName();
        while (!stop) {
            Log.out("Thread [" + name + "] Is Waiting For A Request");
            try {
                Socket socket = serverSocket.accept();
                ProcessingThread processingThread = new ProcessingThread(socket);
                Thread thread = new Thread(processingThread);
                thread.start();
                threads.add(processingThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        threads.forEach(thread -> thread.stop = true);
    }
}
