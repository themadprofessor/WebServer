package me.httphandler;

import me.util.Log;
import me.webserver.Plugin;

import java.io.*;
import java.net.Socket;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by User on 27/04/2015.
 */
public class Main extends Plugin {
    private MediaType type;
    private final String endline = "\r\n";

    public void handle(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            String[] split = line.split(" ");
            HTTPRequest request = HTTPRequest.fromString(split[0]);
            if (request == null) {
                socket.getOutputStream().write(("HTTP/1.1 501 Not Implemented" + endline).getBytes());
            }
            boolean storeLines = request.equals(HTTPRequest.TRACE);
            StringBuilder builder = null;
            if (storeLines) {
                builder = new StringBuilder();
            }
            request.request = split[1];

            while (!line.isEmpty()) {
                if (storeLines) {
                    builder.append(line).append(endline);
                }
                if (line.startsWith("Accept: ")) {
                    line = line.replace("Accept: ", "");
                    String[] split1 = line.split(",");
                    for (String s : split1) {
                        MediaType type = MediaType.fromString(s);
                        if (type != null) request.accepted.add(type);
                    }
                } else if (line.startsWith("Connection: ")) {
                    line = line.replace("Connection: ", "");
                    if (line.equals("keep-alive")) {
                        socket.setKeepAlive(true);
                    } else {
                        socket.setKeepAlive(false);
                    }
                }
                line = reader.readLine();
            }
            Log.out("Received [" + request.toString() + "] Request From [" + socket.getInetAddress() + "] For [" + request.request + ']');

            HTTPResponse response;
            switch (request) {
                case GET:
                    File file;
                    if (request.request.equals("/")) {
                        file = new File("assets/html/index.html");
                    } else {
                        file = new File("assets/html" + request.request.replace("%20", " "));
                    }
                    type = MediaType.fromString(file.getName().substring(file.getName().lastIndexOf('.')).replace(".", ""));
                    if (type == null) {
                        type = request.accepted.get(0);
                    }
                    switch (type.dataType) {
                        case TEXT:
                            response = new HTTPResponse(type, readFile(file));
                            sendReponse(response, socket.getOutputStream());
                            break;
                        case IMAGE:
                        case VIDEO:
                        case APPLICATION:
                            response = new HTTPResponse(type, null);
                            sendReponse(response, socket.getOutputStream());
                            sendFileAsBytes(file, socket.getOutputStream());
                            break;
                    }
                    break;
                case HEAD:
                    response = new HTTPResponse(MediaType.PLAIN, null);
                    sendReponse(response, socket.getOutputStream());
                    break;
                case TRACE:
                    socket.getOutputStream().write(builder.toString().getBytes());
                    break;
                case OPTIONS:
                    socket.getOutputStream().write(buildOptionsResponse().getBytes());
                    break;
            }
            socket.getOutputStream().flush();
            request.accepted.clear();
            Log.out("Sent Response");
            reader.close();
        } catch (IOException e) {
            Log.err(e);
        }
    }

    public int getListeningPort() {
        return 80;
    }

    private String readFile(File file) throws IOException {
        try {
            StringBuilder builder = new StringBuilder();
            List<String> lines = Files.readAllLines(file.toPath());
            lines.forEach(line -> builder.append(line).append("\n"));
            return builder.toString();
        } catch (MalformedInputException e) {
            Log.err(file.toString());
            Log.err(e);
            return null;
        }
    }

    private void sendReponse(HTTPResponse response, OutputStream stream) throws IOException {
        stream.write(response.toString().getBytes());
    }

    private void sendFileAsBytes(File file, OutputStream stream) throws IOException {
        FileInputStream in = new FileInputStream(file);
        int bytesRead;
        byte[] buffer = new byte[512];
        bytesRead = in.read(buffer);
        while (bytesRead != -1) {
            stream.write(buffer);
            bytesRead = in.read(buffer, 0, bytesRead);
        }
        in.close();
    }

    private String buildOptionsResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK").append(endline);
        builder.append("Allow: ");
        for (int i = 0; i < HTTPRequest.values().length -1; i++) {
            builder.append(HTTPRequest.values()[i]).append(',');
        }
        builder.append(HTTPRequest.values()[HTTPRequest.values().length - 1]);

        return builder.toString();
    }
}
