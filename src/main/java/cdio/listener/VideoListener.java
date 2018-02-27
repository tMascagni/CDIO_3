package cdio.listener;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoListener extends JFrame {

    private IDroneController controller = DroneController.getInstance();
    private BufferedImage image = null;

    public VideoListener(final ARDrone drone) {
        super("Videofeed");

        setSize(640, 360);
        setVisible(true);


        try {
            controller.getDrone().getVideoManager().addImageListener(new ImageListener() {
                public void imageUpdated(BufferedImage newImage) {
                    image = newImage;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            repaint();
                        }
                    });
                }
            });
        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }


    }

    public void paint(Graphics g) {
        if (image != null)
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }


}
