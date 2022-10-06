import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final DataInputStream inputFromClient;
    private final DataOutputStream outputToClient;

    private final TextArea serverLogArea;

    private Boolean authorised = false;


    private int totReq;

    private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private String handlerLogPrefix = date.format(timestamp) + " ClientHandler";


    public ClientHandler(Socket socket, DataInputStream inputFromClient, DataOutputStream outputToClient, TextArea serverLogArea) {
        this.socket = socket;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
        this.serverLogArea = serverLogArea;
    }


    public void run() {
        handlerLogPrefix += socket.getInetAddress() + ":" + socket.getPort() + " : ";

        while (true) {
            try {

                if (!authorised) {
                    int studId = inputFromClient.readInt();

                    if (studId > -1) {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign1", "root", "");
                        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);


                        serverLogArea.append(handlerLogPrefix + "Attempting Client Authentication \n");
                        String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = '%d'" , studId);
                        ResultSet rs = st.executeQuery(query);

                        if (rs.next() && rs.getInt("STUD_ID") == studId) {
                            authorised = true;
                            serverLogArea.append(handlerLogPrefix + "Successfully Authenticated Student : " + studId + "\n");

                            outputToClient.writeUTF("Authentication");
                            outputToClient.writeBoolean(true);

                            totReq = rs.getInt("TOT_REQ") + 1;
                            String updateReqQuery = String.format("UPDATE `students` set `TOT_REQ`= '%d'", totReq);
                            st.executeUpdate(updateReqQuery);
                            serverLogArea.append(handlerLogPrefix + "Student  : " + studId + " has a total of " + totReq + " login requests" + "\n");

                        } else  {
                            serverLogArea.append(handlerLogPrefix + "Failed to authenticate Student : " + studId + "\n");
                            //System.out.println("sending error message");
                            outputToClient.writeUTF("Message");
                            outputToClient.writeUTF("Sorry " + studId + ". You are not a registered student.Try again later or exit");
                        }
                        rs.close();
                        st.close();
                        con.close();
                    }
                }

                if (authorised) {
                    double radius = inputFromClient.readDouble();

                    if (radius > -1) {
                        double area = 0;

                        serverLogArea.append(handlerLogPrefix + "Radius received from client: " + radius + '\n');

                        // Compute area
                        area = radius * radius * Math.PI;


                        // Send area back to the client
                        outputToClient.writeUTF("Area");
                        outputToClient.writeDouble(area);
                        serverLogArea.append(handlerLogPrefix + "Area Calculated: " + area + '\n');


                    }
                }
            } catch (IOException e) {
                try {
                    this.inputFromClient.close();
                    this.outputToClient.close();
                } catch (IOException x) {
                    throw new RuntimeException(x);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                ;
            }
        }
    }
}
