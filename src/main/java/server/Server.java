package server;

import helpers.Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        // Clients connect to the server with a port number (server listens) - TCP/IP
        // Read port number and ip address from network.props
        final String networkPropsFilePath = "src\\main\\resources\\network.props";
        final int port = Helpers.getPort(networkPropsFilePath);
        final String ip = Helpers.getIp(networkPropsFilePath);

        //TODO: May want to handle this IOException better (if fatal error close and restart maybe?)
        try {
            // Bind port number and begin listening
            ServerSocket serverSocket = new ServerSocket(port);
            // Socket is what will be used to communicate on, loop to keep receiving connections
            for (;;) {
                Socket socket = serverSocket.accept();

                // Read a single byte from the stream
                InputStream inputStream = socket.getInputStream();
                System.out.println("Read byte: " + inputStream.read());

                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Exception caught: " + e);
        }


    }
}

