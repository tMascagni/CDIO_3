package cdio.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class CommandPanel extends JPanel {

    private final JTextArea textArea;

    public CommandPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Command Log", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Sans Serif", Font.BOLD, 15)));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
    }

    public void appendText(String text) {
        textArea.append(text + "\n");
    }

}