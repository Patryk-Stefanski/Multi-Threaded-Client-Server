package Views;

import javax.swing.*;

public class LoginView {
    public JPanel loginView;
    public JButton loginButton;
    public JTextField loginField;
    public JLabel studId;

    public JPanel getLoginView() {
        return loginView;
    }

    public void setLoginView(JPanel loginView) {
        this.loginView = loginView;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JTextField getLoginField() {
        return loginField;
    }

    public void setLoginField(JTextField loginField) {
        this.loginField = loginField;
    }

    public JLabel getStudIdLabel() {
        return studId;
    }

    public void setStudIdLabel(JLabel usernameLabel) {
        this.studId = usernameLabel;
    }

    public JPanel getConfiguredView() {
        setLoginView(new JPanel());
        setLoginButton(new JButton("Login"));
        setLoginField(new JTextField(8));
        setStudIdLabel(new JLabel("Student ID :"));

        getLoginView().setSize(350, 150);

        getLoginButton().setLocation(130, 90);
        getStudIdLabel().setLocation(55, 40);
        getLoginField().setLocation(165, 40);

        getLoginView().add(getStudIdLabel());
        getLoginView().add(getLoginField());
        getLoginView().add(getLoginButton());

        return getLoginView();
    }
}
