package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class DroneStatusPanel extends JPanel {

    private JLabel lblVideoManager, lblNavManager, lblWiFiStrength, lblWiFiStrengthValue;
    private JRadioButton btnVideoManager, btnNavManager, btnWiFiStrength;

    public DroneStatusPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Drone", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));

        lblVideoManager = new JLabel("    Video Manager");
        lblVideoManager.setFont(lblVideoManager.getFont().deriveFont(lblVideoManager.getFont().getStyle() | Font.BOLD));
        lblNavManager = new JLabel("    Nav Manager");
        lblNavManager.setFont(lblNavManager.getFont().deriveFont(lblNavManager.getFont().getStyle() | Font.BOLD));
        lblWiFiStrength = new JLabel("    WiFi Strength:   ");
        lblWiFiStrength.setFont(lblWiFiStrength.getFont().deriveFont(lblWiFiStrength.getFont().getStyle() | Font.BOLD));

        lblWiFiStrengthValue = new JLabel("-1");

        btnVideoManager = new JRadioButton();
        btnNavManager = new JRadioButton();
        btnWiFiStrength = new JRadioButton();

        btnVideoManager.setBackground(Color.WHITE);
        btnNavManager.setBackground(Color.WHITE);
        btnWiFiStrength.setBackground(Color.WHITE);

        btnVideoManager.setEnabled(false);
        btnNavManager.setEnabled(false);
        btnWiFiStrength.setEnabled(false);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(btnVideoManager, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(4, 0, 0, 0);
        add(lblVideoManager, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(btnNavManager, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblNavManager, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(btnWiFiStrength, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblWiFiStrength, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblWiFiStrengthValue, gbc);
    }

    public void setVideoManagerConnection(boolean isConnected) {
        btnVideoManager.setSelected(isConnected);
    }

    public void setNavManagerConnection(boolean isConnected) {
        btnNavManager.setSelected(isConnected);
    }

    public void setWiFiConnection(long strength) {
        lblWiFiStrengthValue.setText(strength + " ");

        if (strength == 500) {
            btnWiFiStrength.setSelected(true);
        } else {
            btnWiFiStrength.setSelected(false);
        }
    }

}