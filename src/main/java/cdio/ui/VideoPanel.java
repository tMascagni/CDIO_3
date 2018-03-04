package cdio.ui;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoPanel extends JFrame {
    private BufferedImage image = null;

    public VideoPanel(final IARDrone drone) {
        super("YADrone Tutorial");

        drone.getVideoManager().addImageListener(new ImageListener() {
            public void imageUpdated(BufferedImage newImage) {
                image = newImage;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        repaint();
                    }
                });
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                drone.stop();
                System.exit(0);
            }
        });


        setSize(640, 360);
        setPreferredSize(new Dimension(640, 360));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        //add(this);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g) {
        // if (image != null) {
        URL url = null;
        try {
            url = new URL("https://i.pinimg.com/736x/4c/71/54/4c7154368fcc32e580c7878e7607289c--logodesign-logotype.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    //}

}