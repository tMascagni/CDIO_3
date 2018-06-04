package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class DroneDataPanel extends JPanel {

    private JLabel lblBattery, lblSpeed, lblPitch, lblRoll, lblYaw, lblYawCorrected, lblAltitude;
    private JLabel lblBatteryValue, lblSpeedValue, lblPitchValue, lblRollValue, lblYawValue, getLblYawCorrectedValue, lblAltitudeValue;

    public DroneDataPanel() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Data", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));
        setBackground(Color.WHITE);

        lblBattery = new JLabel("Battery: ");
        lblBattery.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblSpeed = new JLabel("Speed: ");
        lblSpeed.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblPitch = new JLabel("Pitch: ");
        lblPitch.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblRoll = new JLabel("Roll: ");
        lblRoll.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblYaw = new JLabel("Yaw: ");
        lblYaw.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblYawCorrected = new JLabel("C Yaw: ");
        lblYawCorrected.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));
        lblAltitude = new JLabel("Altitude: ");
        lblAltitude.setFont(lblBattery.getFont().deriveFont(lblBattery.getFont().getStyle() | Font.BOLD));

        lblBatteryValue = new JLabel("-1%");
        lblSpeedValue = new JLabel("-1%");
        lblPitchValue = new JLabel("-1");
        lblRollValue = new JLabel("-1");
        lblYawValue = new JLabel("-1");
        getLblYawCorrectedValue = new JLabel("-1");
        lblAltitudeValue = new JLabel("-1");

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int leftOffset = 5;
        int rightOffset = 5;
        int rightBorderOffset = 5;

        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 0, rightOffset);
        add(lblBattery, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, rightBorderOffset);
        add(lblBatteryValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 10, rightOffset);
        add(lblSpeed, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 10, rightBorderOffset);
        add(lblSpeedValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 0, rightOffset);
        add(lblPitch, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, rightBorderOffset);
        add(lblPitchValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 10, rightOffset);
        add(lblRoll, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 10, rightBorderOffset);
        add(lblRollValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 0, rightOffset);
        add(lblYaw, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, rightBorderOffset);
        add(lblYawValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 10, rightOffset);
        add(lblYawCorrected, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 10, rightBorderOffset);
        add(getLblYawCorrectedValue, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, leftOffset, 0, rightOffset);
        add(lblAltitude, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, rightBorderOffset);
        add(lblAltitudeValue, gbc);
    }

    public void setBattery(int battery) {
        lblBatteryValue.setText(battery + "%");
    }

    public void setSpeed(int speed) {
        lblSpeedValue.setText(speed + "%");
    }

    public void setPitch(int pitch) {
        lblPitchValue.setText(pitch + " ");
    }

    public void setRoll(int roll) {
        lblRollValue.setText(roll + " ");
    }

    public void setYaw(int yaw) {
        lblYawValue.setText(yaw + " ");
    }

    public void setCorrectedYaw(int correctedYaw) {
        getLblYawCorrectedValue.setText(correctedYaw + " ");
    }

    public void setAltitude(int altitude) {
        lblAltitudeValue.setText(altitude + " ");
    }

}