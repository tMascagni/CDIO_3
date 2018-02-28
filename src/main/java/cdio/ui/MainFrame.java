package cdio.ui;

import cdio.controller.interfaces.IDroneController;
import cdio.handler.KeyHandler;
import cdio.handler.TextHandler;
import cdio.handler.interfaces.IKeyHandler;
import cdio.ui.interfaces.MessageListener;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MainFrame extends JFrame implements MessageListener {

    private int width = 1280;
    private int height = 720;

    private CommandPanel commandPanel;
    private StatusPanel statusPanel;
    private VideoPanel frontVideoPanel, bottomVideoPanel;

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

        commandPanel = new CommandPanel();
        statusPanel = new StatusPanel();

        try {
            frontVideoPanel = new VideoPanel(droneController.getDrone(), true);
            bottomVideoPanel = new VideoPanel(droneController.getDrone(), false);
        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }

        keyHandler.setMessageListener(this);
        addKeyListener(keyHandler);
        commandPanel.addKeyListener(keyHandler);
        statusPanel.addKeyListener(keyHandler);
        frontVideoPanel.addKeyListener(keyHandler);
        bottomVideoPanel.addKeyListener(keyHandler);

        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        initComponents();
        initFrame();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;

        gbc.weightx = 1.0;
        gbc.weighty = 0.1;

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

        gbc.weightx = 1.0;
        gbc.weighty = 0.1;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(frontVideoPanel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(bottomVideoPanel, gbc);
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
    public void messageEventOccurred(Object obj, String text) {
        String formattedDate = df.format(new Date());
        commandPanel.appendText("[" + obj.getClass().getSimpleName() + ": " + formattedDate + "] " + text);
    }

}