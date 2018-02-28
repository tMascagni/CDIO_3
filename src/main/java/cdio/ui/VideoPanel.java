package cdio.ui;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoPanel extends JPanel {

    private BufferedImage currentImage = null;

    private final IARDrone drone;

    public VideoPanel(IARDrone drone, boolean isFrontCamera) {
        this.drone = drone;
        setBackground(Color.WHITE);

        if (isFrontCamera) {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Video (Front)", TitledBorder.CENTER, TitledBorder.CENTER));
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
        } else {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Video (Bottom)", TitledBorder.CENTER, TitledBorder.CENTER));
            drone.getCommandManager().setVideoChannel(VideoChannel.VERT);
        }

        drone.getVideoManager().addImageListener(newImage -> {
            currentImage = newImage;
            SwingUtilities.invokeLater(this::repaint);
        });
    }

    @Override
    public void paint(Graphics g) {
        if (currentImage != null)
            g.drawImage(currentImage, 0, 0, currentImage.getWidth(), currentImage.getHeight(), null);
    }

}