package cdio.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class StatusPanel extends JPanel {

    private JLabel lblIsAutonomous, lblIsStarted, lblIsStopped;

    private JRadioButton isAutonomous;
    private JRadioButton isStarted, isStopped;

    public StatusPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));

        lblIsAutonomous = new JLabel("Autonomous: ");
        lblIsStarted = new JLabel("Started: ");
        lblIsStopped = new JLabel("Stopped: ");

        isAutonomous = new JRadioButton();
        isStarted = new JRadioButton();
        isStopped = new JRadioButton();

        isAutonomous.setBackground(Color.WHITE);
        isStarted.setBackground(Color.WHITE);
        isStopped.setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblIsAutonomous, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isAutonomous, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblIsStarted, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isStarted, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblIsStopped, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(isStopped, gbc);
    }

}
