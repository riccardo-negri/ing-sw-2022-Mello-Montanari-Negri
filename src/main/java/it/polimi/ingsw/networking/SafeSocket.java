package it.polimi.ingsw.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Thread-safe decorator for Socket class avoids concurrency problems when using socket methods
 */

public class SafeSocket {
    private final Socket socket;

    /**
     * create a safe socket
     * @param socket the normal socket used by the safe socket
     */
    public SafeSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * close the socket
     * @throws IOException if an exception occurs when closing the socket
     */
    public synchronized void close() throws IOException {
        socket.close();
    }

    /**
     * check if the socket is closed
     * @return if the socket is closed
     */
    public synchronized boolean isClosed() {
        return socket.isClosed();
    }

    /**
     * get the value of getInetAddress()
     * @return the ip the socket is connected to
     */
    public synchronized InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    /**
     * get the value of getPort()
     * @return the remote port the socket is connected to
     */
    public synchronized int getRemotePort() {
        return socket.getPort();
    }

    /**
     * get getOutputStream() value
     * @return the output stream connected to the socket
     * @throws IOException if an exception occurs while getting the output stream
     */
    public synchronized OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    /**
     * get getInputStream() value
     * @return the input stream connected to the socket
     * @throws IOException if an exception occurs while getting the input stream
     */
    public synchronized InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    /**
     * set the timeout after which the socket throws an exception
     * @param timeout timeout in milliseconds
     * @throws SocketException if an exception occurs while setting the timeout
     */
    public synchronized void setSoTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

}

