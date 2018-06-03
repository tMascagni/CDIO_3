package cdio.routing;

import cdio.cv.QRDetector;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import javax.swing.*;
public final class RoutePlanner {

    private RoutePlanner() {

    }

    public static void flightControl(IDroneCommander droneCommander) {
       IARDrone  drone = null;
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
    });
        /* ######### TEST ######### */
        CommandManager cmd = drone.getCommandManager();
        int speed = 30; // percentage of max speed

        cmd.takeOff().doFor(5000);

        cmd.schedule(5000, new Runnable() {
            public void run()
            {
                cmd.goLeft(speed).doFor(1000);

            }
        });
        cmd.schedule(5000, new Runnable() {
            public void run()
            {
                try {
                    droneCommander.searchForQRCode();

                } catch (IDroneCommander.DroneCommanderException e) {
                    e.printStackTrace();
                }

            }
        });
        cmd.hover().doFor(2000);
        cmd.schedule(5000, new Runnable() {
            public void run()
            {
                cmd.goRight(speed).doFor(2000);

            }
        });
        cmd.schedule(5000, new Runnable() {
            public void run()
            {
                try {
                    droneCommander.searchForQRCode();

                } catch (IDroneCommander.DroneCommanderException e) {
                    e.printStackTrace();
                }

            }
        });
        cmd.hover().doFor(2000);
        cmd.landing();
        cmd.stop();

    }
}
