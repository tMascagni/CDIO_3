package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class DroneStatusPanel extends JPanel {

    private JLabel lblIsStarted, lblIsAutonomous, lblIsFlying, lblIsStopped;
    private JLabel lblIsStartedStatus, lblIsAutonomousStatus, lblIsFlyingStatus, lblIsStoppedStatus;
    private JRadioButton isStarted, isAutonomous, isFlying, isStopped;

    public DroneStatusPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Drone", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));

        lblIsStarted = new JLabel("    Started");
        lblIsStarted.setFont(lblIsStarted.getFont().deriveFont(lblIsStarted.getFont().getStyle() | Font.BOLD));
        lblIsAutonomous = new JLabel("    Autonomous");
        lblIsAutonomous.setFont(lblIsAutonomous.getFont().deriveFont(lblIsAutonomous.getFont().getStyle() | Font.BOLD));
        lblIsFlying = new JLabel("    Flying");
        lblIsFlying.setFont(lblIsFlying.getFont().deriveFont(lblIsFlying.getFont().getStyle() | Font.BOLD));
        lblIsStopped = new JLabel("    Stopped");
        lblIsStopped.setFont(lblIsStopped.getFont().deriveFont(lblIsStopped.getFont().getStyle() | Font.BOLD));

        lblIsStartedStatus = new JLabel("None");
        lblIsAutonomousStatus = new JLabel("None");
        lblIsFlyingStatus = new JLabel("None");
        lblIsStoppedStatus = new JLabel("None");

        isStarted = new JRadioButton();
        isAutonomous = new JRadioButton();
        isFlying = new JRadioButton();
        isStopped = new JRadioButton();

        isStarted.setBackground(Color.WHITE);
        isAutonomous.setBackground(Color.WHITE);
        isFlying.setBackground(Color.WHITE);
        isStopped.setBackground(Color.WHITE);

        isStarted.setEnabled(false);
        isAutonomous.setEnabled(false);
        isFlying.setEnabled(false);
        isStopped.setEnabled(false);

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
        add(isStarted, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(4, 0, 0, 0);
        add(lblIsStarted, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(4, 15, 0, 0);
        add(lblIsStartedStatus, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isAutonomous, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblIsAutonomous, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 15, 0, 0);
        add(lblIsAutonomousStatus, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isFlying, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblIsFlying, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 15, 0, 0);
        add(lblIsFlyingStatus, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isStopped, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(1, 0, 0, 0);
        add(lblIsStopped, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 15, 0, 0);
        add(lblIsStoppedStatus, gbc);
    }

}
