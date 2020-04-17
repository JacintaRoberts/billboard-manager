package server;

import helpers.Helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

                System.out.println("Connected to " + socket.getInetAddress());

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                /* General notes: always flush streams at the correct time + interleave the operations so that they
                / are in the same order. Write, read on client = Read, write on server (flush in between).*/


                // TODO: Need to make every object that needs to be sent across "implement Serializable" to be able
                //  to break up into bytes (see example MyClass)
                MyClass o = (MyClass) ois.readObject(); // Cast to MyClass
                System.out.println("Received from client: " + o);
                System.out.println("o.getVal(): " + o.getVal());

//                System.out.println(ois.readUTF());
//                oos.writeUTF("Hi, thanks for sending me two UTF-8 strings!");
//                oos.flush();
//                System.out.println(ois.readUTF());
//                System.out.println(ois.readUTF());
//                oos.flush();

                oos.close();
                ois.close();
                // Read a single byte from the stream
                //InputStream inputStream = socket.getInputStream();
                //int num = inputStream.read();
                //System.out.println("Read byte: " + num);

                //OutputStream outputStream = socket.getOutputStream();
                //outputStream.write(num + 1);

                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Exception caught: " + e);
        }


    }
}

