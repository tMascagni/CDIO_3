package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.panel.CameraPanel;

import javax.swing.*;


public class MainVideoFeedTest {

    public static void main(String[] args) throws IDroneController.DroneControllerException {

        JFrame frame = new JFrame();
        frame.add(new CameraPanel(DroneController.getInstance().getDrone()));
        frame.setVisible(true);

    }

}
