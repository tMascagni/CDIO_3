package cdio.ui.jconsole;

import javax.swing.*;
import java.awt.*;

public final class TextPanel extends JPanel {

    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea();

        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        setLayout(new BorderLayout());

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String text) {
        textArea.append(text + "\n");
    }

    public void setText(String text) {
        textArea.setText(text);
    }

}