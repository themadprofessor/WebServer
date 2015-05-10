package me.webserver;

import java.net.Socket;

/**
 * Created by User on 27/04/2015.
 */
public class PluginThread implements Runnable {
    private Plugin plugin;
    private Socket socket;
    private PluginSecurityManager securityManager;

    public PluginThread(Plugin plugin, Socket socket, PluginSecurityManager securityManager) {
        this.plugin = plugin;
        this.socket = socket;
        this.securityManager = securityManager;
    }

    @Override
    public void run() {
        securityManager.setEnabled(true);
        plugin.init();
        try {
            plugin.handle(socket);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
