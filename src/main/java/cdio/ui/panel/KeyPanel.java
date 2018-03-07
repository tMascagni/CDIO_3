package cdio.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class KeyPanel extends JPanel {

    private JLabel lblW, lblS, lblQ, lblE, lblA, lblD, lblP, lblO, lblI, lblEnter, lblSpace, lblH, lblR, lblC, lblF, lblG, lblN, lblM;
    private JLabel lblWDesc, lblSDesc, lblQDesc, lblEDesc, lblADesc, lblDDesc, lblPDesc, lblODesc, lblIDesc, lblEnterDesc, lblSpaceDesc, lblHDesc, lblRDesc, lblCDesc, lblFDesc, lblGDesc, lblNDesc, lblMDesc;

    public KeyPanel() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Key", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));
        setBackground(Color.WHITE);

        lblW = new JLabel("W: ");
        lblW.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblS = new JLabel("S: ");
        lblS.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblQ = new JLabel("Q: ");
        lblQ.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblE = new JLabel("E: ");
        lblE.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblA = new JLabel("A: ");
        lblA.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblD = new JLabel("D: ");
        lblD.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblP = new JLabel("P: ");
        lblP.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblO = new JLabel("O: ");
        lblO.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblI = new JLabel("I: ");
        lblI.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblEnter = new JLabel("Enter: ");
        lblEnter.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblSpace = new JLabel("Space: ");
        lblSpace.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblH = new JLabel("H: ");
        lblH.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblR = new JLabel("R: ");
        lblR.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblC = new JLabel("C: ");
        lblC.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblF = new JLabel("F: ");
        lblF.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblG = new JLabel("G: ");
        lblG.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblN = new JLabel("N: ");
        lblN.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));
        lblM = new JLabel("M: ");
        lblM.setFont(lblW.getFont().deriveFont(lblW.getFont().getStyle() | Font.BOLD));

        lblWDesc = new JLabel("Forward");
        lblSDesc = new JLabel("Backward");
        lblQDesc = new JLabel("Up");
        lblEDesc = new JLabel("Down");
        lblADesc = new JLabel("Left");
        lblDDesc = new JLabel("Right");
        lblPDesc = new JLabel("Autonomous");
        lblODesc = new JLabel("Start");
        lblIDesc = new JLabel("Stop");
        lblEnterDesc = new JLabel("TakeOff");
        lblSpaceDesc = new JLabel("Land");
        lblHDesc = new JLabel("Hover");
        lblRDesc = new JLabel("Rotate");
        lblCDesc = new JLabel("Circle around object");
        lblFDesc = new JLabel("Flip");
        lblGDesc = new JLabel("Dance");
        lblNDesc = new JLabel("Increase speed");
        lblMDesc = new JLabel("Decrease speed");

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        int leftOffset = 10;
        int firstLeftOffset = 5;

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblW, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblWDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblS, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblSDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblQ, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblQDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblE, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblEDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblA, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblADesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblD, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 20, 0);
        add(lblDDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblP, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblPDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblO, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblODesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblI, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblIDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblEnter, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblEnterDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblSpace, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblSpaceDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblH, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblHDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblR, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblRDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblC, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblCDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblF, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblFDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblG, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblGDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblN, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblNDesc, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, firstLeftOffset, 0, 0);
        add(lblM, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, leftOffset, 0, 0);
        add(lblMDesc, gbc);
    }

}
