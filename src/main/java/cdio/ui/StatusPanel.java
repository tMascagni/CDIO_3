package cdio.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class StatusPanel extends JPanel {

    private DroneStatusPanel droneStatusPanel;
    private HoopStatusPanel hoopStatusPanel;

    public StatusPanel() {
        droneStatusPanel = new DroneStatusPanel();
        hoopStatusPanel = new HoopStatusPanel();

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.fill = GridBagConstraints.BOTH;
        add(droneStatusPanel, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;
        add(hoopStatusPanel, gbc);
    }

}
