import java.awt.geom.Area;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.*;

import Views.LoginView;
import Views.CalcView;
import Views.ClientView;


public class Client extends JFrame {
    // IO streams
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    private final ClientView clientView = new ClientView();
    private final LoginView loginView = new LoginView();

    private final CalcView calcView = new CalcView();

    private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private String clientLogPrefix = date.format(timestamp) + " Client : ";

    public static void main(String[] args) throws UnknownHostException {
        new Client();
    }

    private static int studId;

    public Client() throws UnknownHostException {
        //Setup Client GUI
        loginView.getConfiguredView();
        clientView.getConfiguredClientView();
        JTabbedPane cv = clientView.getConfiguredClientView();
        cv.add("Login", loginView.getConfiguredView());
        setContentPane(cv);

        setTitle("Client");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        calcView.getConfiguredCalcView();

        loginView.loginButton.addActionListener(new authListener());

        clientLogPrefix += InetAddress.getLocalHost().toString() + ":" ;

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            while (true) {

                //Read "header" message from server to determine what type of data is meant to be read next

                String readMessageType = fromServer.readUTF();

                if (readMessageType.contains("Message")) {
                    clientView.clientLogArea.append(clientLogPrefix + fromServer.readUTF());
                }

                if (readMessageType.contains("Authentication")) {
                    boolean authorised = fromServer.readBoolean();
                    if (authorised) {
                        //if user is logged in then remove login page and add the calculate page
                        cv.remove(1);
                        cv.addTab("Calculator", calcView.getConfiguredCalcView());
                        calcView.getCalcButton().addActionListener(new calcListener());
                        clientView.clientLogArea.append(clientLogPrefix + "Successfully logged in student : " + studId + "\n");
                    }
                }

                if (readMessageType.contains("Area")) {
                    //populate area field with calculated value from client handler
                    double area = fromServer.readDouble();
                    calcView.areaTextArea.setText(String.valueOf(area));
                    clientView.clientLogArea.append(clientLogPrefix + "Area value has been received from server : " + area + "\n");
                }
            }
        } catch (IOException ex) {
        }
    }


    //Listener for login button
    private class authListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg) {
            // Check that loginfield is not empty and only contains numbers
            if (!loginView.loginField.getText().isEmpty() && loginView.loginField.getText().matches("[0-9]+")) {
                clientView.clientLogArea.append(clientLogPrefix + "Trying to log in \n");
                try {
                    //Send student ID to server for authentication
                    studId = Integer.parseInt(loginView.loginField.getText());
                    toServer.writeInt(studId);

                } catch (SecurityException e) {
                    System.err.println(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                clientView.clientLogArea.append(clientLogPrefix + "Username field is empty or the value is not a number \n");
            }
        }
    }


    //Listener for calculate button
    private class calcListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg) {
            //Check that radius field is not empty and only contains numbers and decimals (double)
            if (!calcView.radiusField.getText().isEmpty() && calcView.radiusField.getText().matches("(-?\\d*\\.?\\d+)")) {
                try {
                    double sendMessage = Double.parseDouble(calcView.radiusField.getText());
                    toServer.writeDouble(sendMessage);

                    clientView.clientLogArea.append(clientLogPrefix + "Radius value sent to Server : " + sendMessage + " \n");
                } catch (SecurityException e) {
                    System.err.println(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                clientView.clientLogArea.append(clientLogPrefix + "Radius field is empty or the value is not a number \n");
            }
        }
    }


}