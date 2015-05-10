package me.webserver;

import me.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 27/04/2015.
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        PluginSecurityManager securityManager = new PluginSecurityManager(false);
        System.setSecurityManager(securityManager);
        ArrayList<Plugin> plugins = new ArrayList<>();
        File pluginDir = new File("plugins");
        PluginLoader loader = new PluginLoader(pluginDir);
        File[] files = pluginDir.listFiles(pathname -> {
            String name = pathname.getName();
            return name.substring(name.length() - 3).equals("jar");
        });
        for (File file : files) {
            try {
                plugins.add(loader.loadPluginJar(file));
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                Log.err(e);
            }
        }
        Log.out("Loaded [" + plugins.size() + "] Plugins");

        ArrayList<Thread> listeningThreads = new ArrayList<>();
        plugins.forEach(plugin -> {
            Thread thread = new Thread(new ListeningThread(plugin, securityManager));
            thread.start();
            listeningThreads.add(thread);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> listeningThreads.forEach(Thread::interrupt)));
    }
}
