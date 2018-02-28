package cdio.ui.jconsole;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class StatusPanel extends JPanel {

    private JLabel lblIsArrows, lblIsAutonomous, lblIsStarted, lblIsStopped;

    private JRadioButton isArrows;
    private JRadioButton isAutonomous;
    private JRadioButton isStarted, isStopped;

    public StatusPanel() {
        lblIsArrows = new JLabel("WADS/Arrow keys: ");
        lblIsAutonomous = new JLabel("Autonomous Mode: ");
        lblIsStarted = new JLabel("Started: ");
        lblIsStopped = new JLabel("Stopped: ");

        isArrows = new JRadioButton();
        isAutonomous = new JRadioButton();
        isStarted = new JRadioButton();
        isStopped = new JRadioButton();

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status", TitledBorder.CENTER, TitledBorder.CENTER));

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        /* ---------------------------- First Row ---------------------------- */
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblIsArrows, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(isArrows, gbc);

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
