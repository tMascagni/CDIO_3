package cdio.controller;

import cdio.controller.interfaces.IDroneCommander;
import cdio.controller.interfaces.IMainController;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class MainController implements IMainController {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    private static IMainController instance;

    private MainController() {

    }

    static {
        try {
            instance = new MainController();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton MainController!");
        }
    }

    public static synchronized IMainController getInstance() {
        return instance;
    }

    @Override
    public void start() throws MainControllerException {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        /* ######### TEST ######### */
        try {
            droneCommander.startDrone();
            droneCommander.initDrone();

            droneCommander.takeOffDrone();

            droneCommander.hoverDrone(5000);

            droneCommander.searchForQRCode();

            droneCommander.hoverDrone(5000);
            droneCommander.landDrone();
            droneCommander.stopDrone();

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }

}