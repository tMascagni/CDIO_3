package cdio.ui.panel;

import cdio.cv.QRImg;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public final class HoopStatusPanel extends JPanel {

    private final int LENGTH = 8;

    private JLabel lblFound, lblHoop, lblLocation, lblIsPassed;

    private JRadioButton[] btnHoops = new JRadioButton[LENGTH];
    private JLabel[] lblHoops = new JLabel[LENGTH];
    private JLabel[] lblHoopLocations = new JLabel[LENGTH];
    private JRadioButton[] btnPassedHoop = new JRadioButton[LENGTH];
    private JRadioButton btnEnabled = new JRadioButton();

    public HoopStatusPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Hoop", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));

        lblFound = new JLabel("Found");
        lblFound.setFont(lblFound.getFont().deriveFont(lblFound.getFont().getStyle() | Font.BOLD));
        lblHoop = new JLabel("Hoop");
        lblHoop.setFont(lblHoop.getFont().deriveFont(lblHoop.getFont().getStyle() | Font.BOLD));
        lblLocation = new JLabel("Location");
        lblLocation.setFont(lblLocation.getFont().deriveFont(lblLocation.getFont().getStyle() | Font.BOLD));
        lblIsPassed = new JLabel("Passed");
        lblIsPassed.setFont(lblIsPassed.getFont().deriveFont(lblIsPassed.getFont().getStyle() | Font.BOLD));

        for (int i = 0; i < LENGTH; i++) {
            btnHoops[i] = new JRadioButton();
            btnHoops[i].setBackground(Color.WHITE);

            lblHoops[i] = new JLabel(i + "");
            lblHoopLocations[i] = new JLabel("LOC");

            btnPassedHoop[i] = new JRadioButton();
            btnPassedHoop[i].setBackground(Color.WHITE);

            btnHoops[i].setEnabled(false);
            btnPassedHoop[i].setEnabled(false);
        }

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        int leftOffset = 25;

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblFound, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblHoop, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblLocation, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblIsPassed, gbc);


        for (int i = 0; i < LENGTH; i++) {
            gbc.gridy++;

            gbc.gridx = 0;
            gbc.insets = new Insets(0, 6, 0, 0);
            add(btnHoops[i], gbc);

            gbc.gridx = 1;
            gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
            add(lblHoops[i], gbc);

            gbc.gridx = 2;
            gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
            add(lblHoopLocations[i], gbc);

            gbc.gridx = 3;
            gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
            add(btnPassedHoop[i], gbc);
        }

    }

    public void setRingFound(Map<Integer, QRImg> qrMap) {
        for (int qrNumber : qrMap.keySet()) {
            QRImg qrImg = qrMap.get(qrNumber);

            if (qrImg != null) {
                btnHoops[qrNumber].setSelected(true);
            }
        }
    }

    public void setRingPassed(int ringNumber) {
        btnPassedHoop[ringNumber].setSelected(true);
    }

}