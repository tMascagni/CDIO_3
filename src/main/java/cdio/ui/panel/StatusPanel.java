package cdio.ui.panel;

import cdio.cv.QRImg;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public final class StatusPanel extends JPanel {

    private DroneStatusPanel droneStatusPanel;
    private HoopStatusPanel hoopStatusPanel;
    private DroneDataPanel droneDataPanel;

    private JPanel droneStatusHoopStatusPanel;

    public StatusPanel() {
        droneStatusPanel = new DroneStatusPanel();
        hoopStatusPanel = new HoopStatusPanel();
        droneDataPanel = new DroneDataPanel();

        droneStatusHoopStatusPanel = new JPanel();
        droneStatusHoopStatusPanel.setLayout(new BorderLayout());
        droneStatusHoopStatusPanel.add(droneStatusPanel, BorderLayout.NORTH);
        droneStatusHoopStatusPanel.add(hoopStatusPanel, BorderLayout.SOUTH);

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        /* ---------------------------- Next Col ---------------------------- */
        gbc.fill = GridBagConstraints.BOTH;
        add(droneStatusHoopStatusPanel, gbc);

        /* ---------------------------- Next Col ---------------------------- */
        gbc.gridx++;
        add(droneDataPanel, gbc);
    }

    public void setBattery(int battery) {
        droneDataPanel.setBattery(battery);
    }

    public void setSpeed(int speed) {
        droneDataPanel.setSpeed(speed);
    }

    public void setPitch(int pitch) {
        droneDataPanel.setPitch(pitch);
    }

    public void setRoll(int roll) {
        droneDataPanel.setRoll(roll);
    }

    public void setYaw(int yaw) {
        droneDataPanel.setYaw(yaw);
    }

    public void setMaxAltitude(int maxAltitude) {
        droneDataPanel.setMaxAltitude(maxAltitude);
    }

    public void setMinAltitude(int minAltitude) {
        droneDataPanel.setMinAltitude(minAltitude);
    }

    public void setAltitude(int altitude) {
        droneDataPanel.setAltitude(altitude);
    }

    public void setRingFound(Map<Integer, QRImg> qrMap) {
        hoopStatusPanel.setRingFound(qrMap);
    }

    public void setRingPassed(int ringNumber) {
        hoopStatusPanel.setRingPassed(ringNumber);
    }

    public void setVideoManagerConnection(boolean isConnected) {
        droneStatusPanel.setVideoManagerConnection(isConnected);
    }

    public void setNavManagerConnection(boolean isConnected) {
        droneStatusPanel.setNavManagerConnection(isConnected);
    }

    public void setWiFiConnection(long strength) {
        droneStatusPanel.setWiFiConnection(strength);
    }

}