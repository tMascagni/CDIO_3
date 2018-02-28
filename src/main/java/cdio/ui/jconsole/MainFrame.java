package cdio.ui.jconsole;

import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {

    private CommandPanel textPanel;
    private StatusPanel statusPanel;
    private VideoPanel videoPanel;

    public MainFrame() {
        super("JConsole");

        textPanel = new CommandPanel();

        statusPanel = new StatusPanel();
        statusPanel.setSize(180, 360);
        statusPanel.setPreferredSize(new Dimension(180, 360));

        videoPanel = new VideoPanel();
        videoPanel.setSize(180, 360);
        videoPanel.setPreferredSize(new Dimension(180, 360));

        setSize(1280, 720);
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1920, 1080));

        initComponents();
        initFrame();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        add(textPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.EAST);
        //add(videoPanel, BorderLayout.EAST);
    }

    private void initFrame() {
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        requestFocus();
    }

}