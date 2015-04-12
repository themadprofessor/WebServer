package me.webserver;

import me.util.Log;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by stuart on 10/04/15.
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        byte[] bytes = new byte[] {107,101,81,119,101,114,116,121,56,56};
        char[] data = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (char) bytes[i];
        }

        SSLContext context = null;
        try {
            KeyStore store = KeyStore.getInstance("JKS");
            store.load(new FileInputStream("assets/keystore.jks"), data);

            KeyManagerFactory managerFactory = KeyManagerFactory.getInstance("SunX509");
            managerFactory.init(store, data);

            context = SSLContext.getInstance("TLS");
            context.init(managerFactory.getKeyManagers(), null, null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
            Log.err(e);
        }

        ListeningThread httpListeningThread = null;
        ListeningThread httpsListeningThread = null;
        try {
            httpListeningThread = new ListeningThread(new ServerSocket(80));
            httpsListeningThread = new ListeningThread(context.getServerSocketFactory().createServerSocket(443));
            Thread httpListener = new Thread(httpListeningThread, "HTTP Listening Thread");
            Thread httpsListener = new Thread(httpsListeningThread, "HTTPS Listening Thread");
            httpListener.start();
            httpsListener.start();
        } catch (IOException e) {
            Log.err(e);
        }

        boolean stop = false;
        Scanner scanner = new Scanner(System.in);

        while (!stop) {
            String input = scanner.nextLine();
            if (input.equals("exit") || input.equals("stop") || input.equals("kill")) {
                stop = true;
            }
        }
        httpListeningThread.stop = true;
        httpsListeningThread.stop = true;
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 80));
            socket.close();
            socket.connect(new InetSocketAddress("127.0.0.1", 443));
            socket.close();
        } catch (IOException e) {
            Log.err(e);
        }
    }
}
