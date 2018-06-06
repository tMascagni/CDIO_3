package cdio.ui;

import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.KeyHandler;
import cdio.handler.interfaces.IKeyHandler;
import cdio.ui.panel.CameraPanel;
import cdio.ui.panel.CommandPanel;
import cdio.ui.panel.StatusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class MainFrame extends JFrame {

    private int width = 1280;
    private int height = 720;

    private final boolean IS_KEYS_ENABLED = false;

    private CommandPanel commandPanel;
    private StatusPanel statusPanel;
    private CameraPanel frontCamPanel;

    private final IDroneCommander droneController;

    private final IKeyHandler keyHandler = KeyHandler.getInstance();

    public MainFrame(IDroneCommander droneController) {
        super("DroneX - Group 3 - CDIO Project");
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
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

        if (IS_KEYS_ENABLED) {
            addKeyListener(keyHandler);
            getRootPane().addKeyListener(keyHandler);
        } else {
            droneController.addMessage("KEYBOARD DISABLED!");
        }

        commandPanel.setFocusable(false);
        statusPanel.setFocusable(false);
        setFocusable(true);

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {

            }

            public void focusLost(FocusEvent e) {
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

        updateStatusPanel();
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
    }

    private void initFrame() {
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        requestFocus();
    }

    private void updateStatusPanel() {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    statusPanel.setPitch((int) droneController.getPitch());
                    statusPanel.setRoll((int) droneController.getRoll());
                    statusPanel.setYaw((int) droneController.getYaw());
                    statusPanel.setCorrectedYaw((int) droneController.getCorrectYaw(droneController.getYaw()));
                    statusPanel.setAltitude((int) droneController.getAltitude());
                    statusPanel.setBattery(droneController.getBattery());
                    statusPanel.setSpeed(droneController.getSpeed());

                    List<String> messages = droneController.getNewMessages();

                    for (String str : messages) {
                        commandPanel.appendText("############### Drone Command ###############");
                        commandPanel.appendText(str);
                    }

                    messages.clear();
                } catch (IDroneCommander.DroneCommanderException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100);
    }

}