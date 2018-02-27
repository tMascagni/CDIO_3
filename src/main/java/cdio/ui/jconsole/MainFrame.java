package cdio.ui.jconsole;

import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {

    private TextPanel textPanel;

    public MainFrame() {
        super("JConsole");

        textPanel = new TextPanel();

        setSize(1280, 720);
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1920, 1080));

        initModels();
        initFrame();
    }

    private void initModels() {
        setLayout(new BorderLayout());

        add(textPanel, BorderLayout.CENTER);
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