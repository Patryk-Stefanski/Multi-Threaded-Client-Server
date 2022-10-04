package Views;

import javax.swing.*;
import java.awt.*;

public class ClientView {
    public JTabbedPane tabPane;
    public JPanel logPanel;
    public TextArea clientLogArea;

    public JTabbedPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(JTabbedPane tabPane) {
        this.tabPane = tabPane;
    }

    public TextArea getClientLogArea() {
        return clientLogArea;
    }

    public void setClientLogArea(TextArea clientLogArea) {
        this.clientLogArea = clientLogArea;
    }

    public JPanel getLogPanel() {
        return logPanel;
    }

    public void setLogPanel(JPanel logPanel) {
        this.logPanel = logPanel;
    }

    public JTabbedPane getConfiguredClientView() {
        setTabPane(new JTabbedPane());
        setLogPanel(new JPanel());
        setClientLogArea(new TextArea(14, 60));

        getLogPanel().setSize(450, 270);
        getClientLogArea().setLocation(10, 10);
        getLogPanel().add(getClientLogArea());

        getTabPane().addTab("Client Logs", getLogPanel());


        return getTabPane();

    }


}
