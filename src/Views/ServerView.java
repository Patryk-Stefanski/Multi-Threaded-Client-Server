package Views;

import javax.swing.*;
import java.awt.*;

public class ServerView {
    public JPanel serverView;
    public TextArea serverLogArea;

    public TextArea getServerLogArea() {
        return serverLogArea;
    }

    public void setServerLogArea(TextArea serverLogArea) {
        this.serverLogArea = serverLogArea;
    }

    public JPanel getServerView() {
        return serverView;
    }

    public void setServerView(JPanel serverView) {
        this.serverView = serverView;
    }


    public JPanel getConfiguredServerView() {
        setServerView(new JPanel());
        setServerLogArea(new TextArea(16, 60));

        getServerView().setSize(500, 300);


        getServerView().add(getServerLogArea());

        return getServerView();
    }
}
