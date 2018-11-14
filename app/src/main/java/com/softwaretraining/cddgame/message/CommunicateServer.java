package com.softwaretraining.cddgame.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

public class CommunicateServer implements Callable {

    private final String serverName = "***************";
    private int port;

    private Socket socket;

    private final String action;
    private final String message;

    public CommunicateServer(String action, String message) {
        this.action = action;
        this.message = message;
    }

    @Override
    public Object call() {
        String result;
        try {
            connect();
            sendMessage();
            result = readMessage();
            close();
            System.out.println(result);
            return MessageFilter.filter(result);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "服务器暂时没有响应，请稍后重试";
        } catch (IOException e) {
            e.printStackTrace();
            return MessageFilter.filter(e.getMessage());
        }
    }

    private void sendMessage() throws IOException {
        sendMessage(message);
    }

    private void sendMessage(String message) throws IOException {
        OutputStream outToServer = socket.getOutputStream();
        outToServer.write(message.getBytes("UTF-8"));
    }

    private String readMessage() throws IOException {

        final int BUFFER_SIZE = 50000;
        char[] data = new char[BUFFER_SIZE];

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int length = bufferedReader.read(data);

        return String.valueOf(data, 0, length);

    }

    private void connect() throws IllegalArgumentException, IOException {
        switch (action) {
            case "login":
                port = 8280;
                break;
            case "register":
                port = 8281;
                break;
            case "wait":
                port = 8282;
                break;
            case "game":
                port = 8283;
                break;
            default:
                throw new IllegalArgumentException("Illegal action.");
        }
        try {
            Socket server = new Socket();
            InetSocketAddress address = new InetSocketAddress(serverName, port);
            server.connect(address, 3000);
            this.socket = server;
            socket.setSoTimeout(3000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Fail to ping server.");
            throw new SocketTimeoutException("服务器暂时没有响应，请稍后重试");
        }
    }

    private void close() throws IOException {
        socket.close();
    }

}
