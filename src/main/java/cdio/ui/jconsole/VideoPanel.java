package cdio.ui.jconsole;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoPanel extends JPanel {

    // private IDroneController controller = DroneController.getInstance();
    private BufferedImage image = null;

    public VideoPanel() {

        /*
        try {

            controller.getDrone().getVideoManager().addImageListener(newImage -> {
                image = newImage;
                SwingUtilities.invokeLater(() -> repaint());
            });

        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }
        */
    }

    public void paint(Graphics g) {
        if (image != null)
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

}