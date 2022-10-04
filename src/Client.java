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

    private final String clientLogPrefix = date.format(timestamp) + " Client : ";

    public static void main(String[] args) {
        new Client();
    }


    public Client() {
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

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            String connectionEstablished = fromServer.readUTF();

            if (!connectionEstablished.equals("")) {
                clientView.clientLogArea.append(connectionEstablished);
            }

            boolean authorisation = false;
            while (!authorisation) {
                authorisation = fromServer.readBoolean();
                if (authorisation) {
                    cv.remove(1);
                    cv.addTab("Calculator", calcView.getConfiguredCalcView());
                    calcView.getCalcButton().addActionListener(new calcListener());
                }
            }
        } catch (IOException ex) {
        }
    }


    private class authListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg) {
            if (loginView.loginField.getText() != null && Integer.parseInt(loginView.loginField.getText()) > -1) {
                clientView.clientLogArea.append(clientLogPrefix + "Trying to log in \n");
                try {
                    int sendStudId = Integer.parseInt(loginView.loginField.getText());

                    toServer.writeInt(sendStudId);
                } catch (SecurityException e) {
                    System.err.println(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private class calcListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg) {
            clientView.clientLogArea.append(clientLogPrefix + "Calc button works \n");
            if (calcView.radiusField.getText() != null && !Double.isNaN(Double.parseDouble(calcView.radiusField.getText()))) {
                clientView.clientLogArea.append(clientLogPrefix + "Trying to Calculate  \n");
                try {
                    double sendMessage = Double.parseDouble(calcView.radiusField.getText());
                    clientView.clientLogArea.append(clientLogPrefix + "Sending Radius to Server");

                    toServer.writeDouble(sendMessage);

                    calcView.areaTextArea.setText(String.valueOf(fromServer.readDouble()));
                } catch (SecurityException e) {
                    System.err.println(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}