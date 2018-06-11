package cdio.routing;

import cdio.cv.QRImg;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.ui.MainFrame;
import yadankdrone.command.CommandManager;
import yadankdrone.video.ImageListener;

import javax.swing.*;
import java.util.ArrayList;

public final class RoutePlanner {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();
    private final static ImageListener cameraControl = null;
    private ArrayList<QRImg> qrImgs = new ArrayList<>();
    private final static IQRCodeHandler qrHandler = QRCodeHandler.getInstance() ;
    private long imageCount = 0;
    private final CommandManager commandManager;

    public RoutePlanner(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public static void routeFinder(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

/*        int x =  droneCommander.getLatestReceivedImage().getHeight();
        int y =  droneCommander.getLatestReceivedImage().getWidth();*/
        QRImg qr = null;
        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(1000);
            droneCommander.circleAroundObject();
            droneCommander.hoverDrone(1000);
            droneCommander.landDrone();
            droneCommander.stopDrone();


        } catch (DroneCommander.DroneCommanderException e) {
            e.printStackTrace();

        }

    }
}
