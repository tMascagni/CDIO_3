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

    private CommandPanel commandPanel, c1, c2;
    private StatusPanel statusPanel;

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

        statusPanel = new StatusPanel();
        statusPanel.setPreferredSize(new Dimension((width / 2) / 3, height / 2));
        statusPanel.setSize(new Dimension((width / 2) / 3, height / 2));

        commandPanel = new CommandPanel();
        commandPanel.setPreferredSize(preferredPanelSize);
        commandPanel.setSize(preferredPanelSize);

        c1 = new CommandPanel();
        c1.setPreferredSize(preferredPanelSize);
        c1.setSize(preferredPanelSize);

        c2 = new CommandPanel();
        c2.setPreferredSize(preferredPanelSize);
        c2.setSize(preferredPanelSize);

        keyHandler.setMessageListener(this);
        addKeyListener(keyHandler);
        getRootPane().addKeyListener(keyHandler);
        commandPanel.addKeyListener(keyHandler);
        c1.addKeyListener(keyHandler);
        c2.addKeyListener(keyHandler);
        statusPanel.addKeyListener(keyHandler);

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
        add(c1, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(c2, gbc);
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
        commandPanel.appendText("────────────────[ " + title + " ]────────────────");
    }

    @Override
    public void messageCommandEventOccurred(Object obj, String msg) {
        String formattedDate = df.format(new Date());
        commandPanel.appendText("[" + obj.getClass().getSimpleName() + ": " + formattedDate + "] " + msg);
    }

    @Override
    public void messageCommandEndEventOccurred() {
        commandPanel.appendText("────────────────────────────────────");
    }

}