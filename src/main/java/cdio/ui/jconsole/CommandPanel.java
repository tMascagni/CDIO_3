package cdio.ui.jconsole;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class CommandPanel extends JPanel {

    private JTextArea textArea;

    public CommandPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Command Log", TitledBorder.CENTER, TitledBorder.CENTER));
        textArea.setFont(new Font("Sans Serif", Font.PLAIN, 14));

        setLayout(new BorderLayout());

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String text) {
        textArea.append(text + "\n");
    }

}