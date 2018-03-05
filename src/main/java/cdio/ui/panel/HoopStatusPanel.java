package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class HoopStatusPanel extends JPanel {

    private JRadioButton btnHoop1, btnHoop2, btnHoop3, btnHoop4, btnHoop5, btnHoop6, btnHoop7, btnHoop8;
    private JLabel lblFound, lblHoop, lblLocation, lblIsPassed;
    private JLabel lblHoop1, lblHoop2, lblHoop3, lblHoop4, lblHoop5, lblHoop6, lblHoop7, lblHoop8;
    private JLabel lblHoopLoc1, lblHoopLoc2, lblHoopLoc3, lblHoopLoc4, lblHoopLoc5, lblHoopLoc6, lblHoopLoc7, lblHoopLoc8;
    private JRadioButton btnIsPassed1, btnIsPassed2, btnIsPassed3, btnIsPassed4, btnIsPassed5, btnIsPassed6, btnIsPassed7, btnIsPassed8;

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

        btnHoop1 = new JRadioButton();
        btnHoop2 = new JRadioButton();
        btnHoop3 = new JRadioButton();
        btnHoop4 = new JRadioButton();
        btnHoop5 = new JRadioButton();
        btnHoop6 = new JRadioButton();
        btnHoop7 = new JRadioButton();
        btnHoop8 = new JRadioButton();

        btnHoop1.setBackground(Color.WHITE);
        btnHoop2.setBackground(Color.WHITE);
        btnHoop3.setBackground(Color.WHITE);
        btnHoop4.setBackground(Color.WHITE);
        btnHoop5.setBackground(Color.WHITE);
        btnHoop6.setBackground(Color.WHITE);
        btnHoop7.setBackground(Color.WHITE);
        btnHoop8.setBackground(Color.WHITE);

        lblHoop1 = new JLabel("1");
        lblHoop2 = new JLabel("2");
        lblHoop3 = new JLabel("3");
        lblHoop4 = new JLabel("4");
        lblHoop5 = new JLabel("5");
        lblHoop6 = new JLabel("6");
        lblHoop7 = new JLabel("7");
        lblHoop8 = new JLabel("8");

        lblHoopLoc1 = new JLabel("(0, 1)");
        lblHoopLoc2 = new JLabel("(2, 3)");
        lblHoopLoc3 = new JLabel("(4, 5)");
        lblHoopLoc4 = new JLabel("(6, 7)");
        lblHoopLoc5 = new JLabel("(8, 9)");
        lblHoopLoc6 = new JLabel("(10, 11)");
        lblHoopLoc7 = new JLabel("(12, 13)");
        lblHoopLoc8 = new JLabel("(14, 15)");

        btnIsPassed1 = new JRadioButton();
        btnIsPassed2 = new JRadioButton();
        btnIsPassed3 = new JRadioButton();
        btnIsPassed4 = new JRadioButton();
        btnIsPassed5 = new JRadioButton();
        btnIsPassed6 = new JRadioButton();
        btnIsPassed7 = new JRadioButton();
        btnIsPassed8 = new JRadioButton();

        btnIsPassed1.setBackground(Color.WHITE);
        btnIsPassed2.setBackground(Color.WHITE);
        btnIsPassed3.setBackground(Color.WHITE);
        btnIsPassed4.setBackground(Color.WHITE);
        btnIsPassed5.setBackground(Color.WHITE);
        btnIsPassed6.setBackground(Color.WHITE);
        btnIsPassed7.setBackground(Color.WHITE);
        btnIsPassed8.setBackground(Color.WHITE);

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

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop1, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop1, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc1, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed1, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop2, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop2, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc2, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed2, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop3, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop3, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc3, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed3, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop4, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop4, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc4, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed4, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop5, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop5, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc5, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed5, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop6, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop6, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc6, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed6, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop7, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop7, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc7, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed7, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 6, 0, 0);
        add(btnHoop8, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoop8, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(4, leftOffset + 10, 0, 0);
        add(lblHoopLoc8, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, leftOffset + 10, 0, 0);
        add(btnIsPassed8, gbc);
    }

}
