package me.webserver;

import me.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by User on 27/04/2015.
 */
public class ListeningThread implements Runnable {
    private ServerSocket server;
    private Plugin plugin;
    private PluginSecurityManager securityManager;

    public ListeningThread(Plugin plugin, PluginSecurityManager securityManager) {
        this.plugin = plugin;
        this.securityManager = securityManager;
        this.securityManager.setEnabled(false);
        try {
            server = new ServerSocket(plugin.getListeningPort());
        } catch (IOException e) {
            Log.err(e);
        }
    }

    @Override
    public void run() {
        securityManager.setEnabled(false);
        while (!server.isClosed()) {
            try {
                Log.out("Thread " + Thread.currentThread().getId() + " Is Awaiting A Client");
                Socket socket = server.accept();
                Thread thread = new Thread(new PluginThread(plugin, socket, securityManager));
                thread.start();
            } catch (IOException e) {
                Log.err(e);
            }
        }
    }
}
