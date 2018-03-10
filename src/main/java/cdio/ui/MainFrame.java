package cdio.ui;

import cdio.controller.interfaces.IDroneController;
import cdio.handler.KeyHandler;
import cdio.handler.TextHandler;
import cdio.handler.interfaces.IKeyHandler;
import cdio.ui.interfaces.MessageListener;
import cdio.ui.panel.CameraPanel;
import cdio.ui.panel.CommandPanel;
import cdio.ui.panel.StatusPanel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MainFrame extends JFrame implements MessageListener {

    private int width = 1280;
    private int height = 720;

    private CommandPanel commandPanel;
    private StatusPanel statusPanel;
    private CameraPanel frontCamPanel;

    private final IDroneController droneController;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    private final IKeyHandler keyHandler = KeyHandler.getInstance();

    public MainFrame(IDroneController droneController) {
        super(TextHandler.GUI_TITLE);
        this.droneController = droneController;
        setBackground(Color.WHITE);
        getRootPane().setBackground(Color.WHITE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Dimension preferredPanelSize = new Dimension(width / 2, height / 2);

        commandPanel = new CommandPanel();
        commandPanel.setPreferredSize(preferredPanelSize);
        commandPanel.setSize(preferredPanelSize);

        statusPanel = new StatusPanel();
        statusPanel.setPreferredSize(new Dimension((width / 2) / 3, height / 2));
        statusPanel.setSize(new Dimension(width / 2, height / 2));

        try {
            frontCamPanel = new CameraPanel(droneController.getDrone());
            frontCamPanel.setPreferredSize(preferredPanelSize);
            frontCamPanel.setSize(preferredPanelSize);
        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }

        keyHandler.setMessageListener(this);
        /*
        addKeyListener(keyHandler);
        getRootPane().addKeyListener(keyHandler);
        */
        commandPanel.setFocusable(false);
        statusPanel.setFocusable(false);
        setFocusable(true);

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                System.out.println("Focus GAINED:");
            }

            public void focusLost(FocusEvent e) {
                System.out.println("Focus LOST:");
                e.getComponent().requestFocus();
            }
        });

        setSize(width, height);
        setMinimumSize(preferredPanelSize);
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        initComponents();
        initFrame();
        requestFocus();
        requestFocusInWindow();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        /* ---------------------------- First Row ---------------------------- */
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(commandPanel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(statusPanel, gbc);

        /* ---------------------------- Next Row ---------------------------- */
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(frontCamPanel, gbc);

        /*
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(bottomCamPanel, gbc);
        */
    }

    private void initFrame() {
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        requestFocus();
    }

    @Override
    public void messageCommandStartEventOccurred(String title) {
        title = StringUtils.center(title, 14, " ");
        //String msg = "───────────[ " + title + " ]───────────";
        String msg = "--------------------------[ " + title + " ]--------------------------";
        commandPanel.appendText(msg);
    }

    @Override
    public void messageCommandEventOccurred(Object obj, String msg) {
        String formattedDate = df.format(new Date());
        commandPanel.appendText("[" + obj.getClass().getSimpleName() + ": " + formattedDate + "] " + msg);
    }

    @Override
    public void messageCommandEndEventOccurred() {
        commandPanel.appendText("----------------------------------------------------------------------");
    }

}