package cdio.main.test;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.panel.CameraPanel;

import javax.swing.*;


public class MainVideoFeedTest {

    public static void main(String[] args) throws IDroneController.DroneControllerException {
        DroneController.getInstance().startDrone();
        JFrame frame = new JFrame();
        frame.add(new CameraPanel(DroneController.getInstance().getDrone()));
        frame.setVisible(true);
    }

}
