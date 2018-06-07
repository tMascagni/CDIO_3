package cdio.algorithms;

import cdio.cv.QRImg;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class Algorithms {

    private Algorithms() {

    }

    public static void runSingleRing(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(5000);

            /*
             * Start med at søge efter en QR kode.
             */
            QRImg qrImg = droneCommander.searchForQRCode();

            /*
             * Hvis den korrekte QR kode ikke blev fundet, så
             * vil qrImg være lig med null, så derfor
             * laver vi en ny rotation, i håb om at finde den
             * korrekte QR kode. (den næste kode)
             */
            if (qrImg == null) {
                droneCommander.addMessage("Starting next rotation....");
                droneCommander.hoverDrone(2000);
                qrImg = droneCommander.searchForQRCode();
            }

            /*
            if (qrImg == null) {
                droneCommander.addMessage("qrImg is null! : correct code was not found : rotating to first code in list ");
                // still null after rotation
                for (int key : droneCommander.getQrCodeMap().keySet()) {
                    QRImg qrImgObj = droneCommander.getQrCodeMap().get(key);

                    if (qrImgObj != null) {
                        qrImg = qrImgObj;
                        droneCommander.addMessage("Rotating to: " + qrImg.getQrCodeData().getFoundYaw());
                        break;
                    }

                }

              */
                /*
                if (qrImg != null || qrImg.getQrCodeData() != null) {
                    //droneCommander.rotateDrone((int) qrImg.getQrCodeData().getFoundYaw());
                }
                */
/*
        } else{
            droneCommander.addMessage("qrImg is NOT null : correct code was found : doing nothing");
        }
*/
            droneCommander.hoverDrone(5000);
            droneCommander.landDrone();
            droneCommander.stopDrone();
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }

    public static void runAllRings(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(7000);

            /*
             * Start med at søge efter en QR kode.
             */
            QRImg qrImg = droneCommander.searchForQRCode();

            /*
             * Hvis den korrekte QR kode ikke blev fundet, så
             * vil qrCodeData være lig med null, så derfor
             * laver vi en ny rotation, i håb om at finde den
             * korrekte QR kode. (den næste kode)
             */
            if (qrImg == null || qrImg.getQrCodeData() == null) {
                qrImg = droneCommander.searchForQRCode();
            }

            /*
             * Hvis vi ikke har fundet nogle QR koder efter ANDEN søgning,
             * (360 grader rundt), så roterer vi hen til graden af den kode der har den
             * største højde.
             */

            try {
                qrImg = droneCommander.getQrCodeWithGreatestHeight();
            } catch (IDroneCommander.DroneCommanderException e) {
                // Hvis denne exception forekommer, er det fordi at der
                // aldrig er blevet fundet nogle QR koder i begge rotationer.
                e.printStackTrace();
            }

            /* TODO: Flyv hen til denne QRCode, og roter rundt om det indtil QR koden har den
             * største bredde (width) da det så betyder at vi står lige foran den. */

            /* Flyv op */

            /* Flyv igennem ring */

            /* Og så gør dette igen. */

        } catch (DroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }

    public static void test(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(5000);

            QRImg qrImg = droneCommander.searchForQRCode();

            if (qrImg == null) {
                droneCommander.addMessage("Found no QR code!");
            }

            droneCommander.hoverDrone(5000);
            droneCommander.landDrone();
            droneCommander.stopDrone();
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }

}