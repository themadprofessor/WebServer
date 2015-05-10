package me.webserver;

import me.util.Log;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Created by User on 27/04/2015.
 */
public class PluginLoader extends ClassLoader {
    private File pluginDir;
    private URLClassLoader cl;

    public PluginLoader(File pluginDir) {
        this.pluginDir = pluginDir;
    }

    public Plugin loadPluginFile(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Plugin)loadClass(name).newInstance();
    }

    public Plugin loadPluginJar(File jar) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        JarFile jarFile = new JarFile(jar);
        Enumeration e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + jar.getPath() + "!/") };
        cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if(!je.isDirectory()){
                if (je.getName().endsWith("plugin.txt")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(je)));
                    String line = reader.readLine();
                    reader.close();
                    Class c = cl.loadClass(line);
                    return (Plugin)c.newInstance();
                }
            }
        }
        throw new ClassCastException("Could Not File plugin.txt");
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class c = null;
        try {
            c = findLoadedClass(name);
            if (c == null) {
                c = findSystemClass(name);
            }
            if (c == null) {
                String filename = name.replace('.',File.separatorChar) + ".class";

                // Create a File object. Interpret the filename relative to the
                // directory specified for this ClassLoader.
                File f = new File(pluginDir, filename);

                // Get the length of the class file, allocate an array of bytes for
                // it, and read it in all at once.
                int length = (int) f.length();
                byte[] classbytes = new byte[length];
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                in.readFully(classbytes);
                in.close();

                // Now call an inherited method to convert those bytes into a Class
                c = defineClass(name, classbytes, 0, length);
            }
        } catch (IOException e) {
            Log.err(e);
        }

        return c;
    }
}
