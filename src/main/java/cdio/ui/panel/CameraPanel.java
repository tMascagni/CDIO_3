package cdio.ui.panel;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import cdio.cv.QRImg;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import yadankdrone.IARDrone;
import yadankdrone.command.VideoCodec;
import yadankdrone.video.ImageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class CameraPanel extends JPanel implements ImageListener {

    private BufferedImage image = null;
    private long timestampLastUpdate = 0;
    private boolean showWaiting = true;

    private QRDetector qrDetector = new QRDetector();
    private CVHelper cvHelper = new CVHelper();

    private IARDrone drone;

    public CameraPanel(final IARDrone drone) {
        this.drone = drone;
        setBackground(Color.WHITE);

        drone.getVideoManager().addImageListener(this);

        drone.getCommandManager().setVideoCodecFps(10);
        drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                drone.toggleCamera();
            }
        });

        // if for some time no video is received, a waiting screen shall be displayed
        new Thread(() -> {
            while (true) {
                showWaiting = System.currentTimeMillis() - timestampLastUpdate > 1000;
                SwingUtilities.invokeLater(this::repaint);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setImage(final BufferedImage image) {
        this.image = image;
        SwingUtilities.invokeLater(() -> repaint());
    }

    public void paint(Graphics g) {
        if ((image != null) && !showWaiting) {
            g.drawImage(drawWithConturs(image), 0, 0, image.getWidth(), image.getHeight(), null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 25));
            g.drawString("Video Feed", 120, 60);
            g.setFont(new Font("Monospaced", Font.PLAIN, 20));
            g.drawString("Waiting for video signal...", 120, 100);
            g.drawString("This could last up to one minute!", 120, 140);
            g.drawString("If afterwards no video is displayed,", 120, 160);
            g.drawString("try to change the video codec!", 120, 180);
        }
    }

    private BufferedImage drawWithConturs(BufferedImage image){
        Mat imgMat = cvHelper.buf2mat(image);

        ArrayList<QRImg> qrImgs = qrDetector.processAll(imgMat);
        for(QRImg qr : qrImgs) {
            Imgproc.drawContours(imgMat, Collections.singletonList(qr.getContour()), -1, new Scalar(15, 250, 200), 3);
            Imgproc.drawMarker(imgMat, qr.getPosition(), new Scalar(10, 250, 200), 0, 20, 5, 8);
        }

        return cvHelper.mat2buf(imgMat);
    }

    public void imageUpdated(BufferedImage image) {
        timestampLastUpdate = System.currentTimeMillis();
        setImage(image);
    }

}

