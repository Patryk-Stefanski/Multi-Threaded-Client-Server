import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import Views.ServerView;

public class Server extends JFrame {

    public static void main(String[] args) {
        new Server();
    }


    public Server() {
        // Place text area on the frame
        setTitle("Server");
        // Text area for displaying contents
        ServerView serverView = new ServerView();
        setContentPane(serverView.getConfiguredServerView());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            serverView.serverLogArea.append("Server started at " + new Date() + '\n');

            while (true) {
                // Listen for a connection request
                Socket socket = serverSocket.accept();

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String serverLogPrefix = date.format(timestamp) + " Server:";
                serverLogPrefix += socket.getLocalAddress() + ":" + socket.getLocalPort() + " : ";

                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                serverView.serverLogArea.append(serverLogPrefix + "Server connected \n");

                outputToClient.writeUTF("Message");
                outputToClient.writeUTF("Connection established with Server : " + socket.getLocalAddress() + ":" + socket.getLocalPort() + "\n");


                serverView.serverLogArea.append(serverLogPrefix + "Starting new client handler \n");
                // Connect to a client Thread
                Thread thread = new ClientHandler(socket, inputFromClient, outputToClient, serverView.serverLogArea);
                thread.start();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }


    }


}