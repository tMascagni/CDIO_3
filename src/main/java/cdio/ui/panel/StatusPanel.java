package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class StatusPanel extends JPanel {

    private DroneStatusPanel droneStatusPanel;
    private HoopStatusPanel hoopStatusPanel;
    private DroneDataPanel droneDataPanel;
    private KeyPanel keyPanel;

    private JPanel droneStatusHoopStatusPanel;

    public StatusPanel() {
        droneStatusPanel = new DroneStatusPanel();
        hoopStatusPanel = new HoopStatusPanel();
        droneDataPanel = new DroneDataPanel();

        droneStatusHoopStatusPanel = new JPanel();
        droneStatusHoopStatusPanel.setLayout(new BorderLayout());
        droneStatusHoopStatusPanel.add(droneStatusPanel, BorderLayout.NORTH);
        droneStatusHoopStatusPanel.add(hoopStatusPanel, BorderLayout.SOUTH);

        keyPanel = new KeyPanel();

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

        /* ---------------------------- Next Col ---------------------------- */
        gbc.gridx++;
        add(keyPanel, gbc);
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

    public void setAltitude(int altitude) {
        droneDataPanel.setAltitude(altitude);
    }

}