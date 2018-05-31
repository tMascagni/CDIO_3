package cdio.routing;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

public class RoutePlanner implements ImageListener {

    private final static IDroneController droneControl = DroneController.getInstance();
    private final static ImageListener cameraControl = null;

    private long imageCount = 0;

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {

        try {
            if (droneControl.getDrone().getNavDataManager().isConnected()) {

                droneControl.startDrone();
                droneControl.takeOffDrone();
                droneControl.doSearchRotation();

                if (cameraControl.equals(true)) {
                    System.out.println("found it");
                    droneControl.stopDrone();
                } else {
                    droneControl.stopDrone();
                }

            }
        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }

        if ((++imageCount % 2) == 0)
            return;
        int[] realDimension = new int[]{297, 420, 297, 420};
        //measure image
        LuminanceSource dimension = new BufferedImageLuminanceSource(bufferedImage);
        //dimension.crop(0,0,0,0).toString();
        if (dimension.crop(0, 0, 0, 0).toString().equals(realDimension)) {
            System.out.println(dimension.crop(0, 0, 0, 0));
        }
    }
}
