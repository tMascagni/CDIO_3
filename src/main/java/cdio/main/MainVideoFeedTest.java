package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.VideoPanel;

public class MainVideoFeedTest {

    public static void main(String[] args) throws IDroneController.DroneControllerException {

        VideoPanel x = new VideoPanel(DroneController.getInstance().getDrone());

    }

}
