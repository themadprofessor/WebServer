package me.webserver;

import me.util.Log;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by stuart on 10/04/15.
 */
public class ProcessingThread implements Runnable {
    private Socket socket;

    public ProcessingThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            HTTPRequest request = new HTTPRequest();
            String[] split = line.split(" ");
            if (split[0].equals("GET")) {
                request.type = HTTPRequestType.GET;
                request.request = split[1];
            }

            while (!line.isEmpty()) {
                Log.out(line); //Use to read request
                line = reader.readLine();
            }

            switch (request.type) {
                case GET :
                    File file;
                    if (request.request.equals("/")) {
                        file = new File("assets/html/index.html");
                    } else {
                        file = new File("assets/html" + request.request);
                    }
                    HTTPResponse response = new HTTPResponse("text/html", readFile(file));
                    socket.getOutputStream().write(response.toString().getBytes());
            }
            reader.close();
        } catch (IOException e) {
            Log.err(e);
        }
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read()) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> lines = Files.readAllLines(file.toPath());
        lines.forEach(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}
