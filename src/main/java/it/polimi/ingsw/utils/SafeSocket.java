package it.polimi.ingsw.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;

/*
    Thread-safe decorator for Socket class
 */

public class SafeSocket {
    private final Socket socket;

    public SafeSocket(Socket socket) {
        this.socket = socket;
    }

    public synchronized void close() throws IOException {
        socket.close();
    }

    public synchronized boolean isClosed() {
        return socket.isClosed();
    }

    public synchronized InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public synchronized OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public synchronized InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public synchronized void setSoTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

}

