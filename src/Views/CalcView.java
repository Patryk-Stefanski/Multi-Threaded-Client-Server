package Views;

import javax.swing.*;
import java.awt.*;

public class CalcView {
    public JPanel calcPanel;
    public JLabel radiusLabel;
    public JLabel areaLabel;
    public JTextArea areaTextArea;
    public TextField radiusField;
    public JButton calcButton;

    public JButton getCalcButton() {
        return calcButton;
    }

    public JPanel getCalcPanel() {
        return calcPanel;
    }


    public void setCalcPanel(JPanel calcPanel) {
        this.calcPanel = calcPanel;
    }

    public void setCalcButton(JButton calcButton) {
        this.calcButton = calcButton;
    }


    public JLabel getRadiusLabel() {
        return radiusLabel;
    }

    public void setRadiusLabel(JLabel radiusLabel) {
        this.radiusLabel = radiusLabel;
    }

    public JLabel getAreaLabel() {
        return areaLabel;
    }

    public void setAreaLabel(JLabel areaLabel) {
        this.areaLabel = areaLabel;
    }

    public JTextArea getAreaTextArea() {
        return areaTextArea;
    }

    public void setAreaTextArea(JTextArea areaTextArea) {
        this.areaTextArea = areaTextArea;
    }

    public TextField getRadiusField() {
        return radiusField;
    }

    public void setRadiusField(TextField radiusField) {
        this.radiusField = radiusField;
    }

    public JPanel getConfiguredCalcView() {

        setCalcPanel(new JPanel());

        setAreaLabel(new JLabel("Area : "));
        setAreaTextArea(new JTextArea(1, 10));
        setRadiusLabel(new JLabel("Radius : "));
        setRadiusField(new TextField(8));
        setCalcButton(new JButton("Calculate Area"));

        getCalcPanel().setSize(300, 100);

        getCalcPanel().add(getRadiusLabel());
        getCalcPanel().add(getRadiusField());
        getCalcPanel().add(getAreaLabel());
        getCalcPanel().add(getAreaTextArea());
        getCalcPanel().add(getCalcButton());


        return getCalcPanel();
    }
}
