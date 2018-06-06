package cdio.algorithms;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.model.QRCodeData;
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
            QRCodeData qrCodeData = droneCommander.searchForQRCode();

            /*
             * Hvis den korrekte QR kode ikke blev fundet, så
             * vil qrCodeData være lig med null, så derfor
             * laver vi en ny rotation, i håb om at finde den
             * korrekte QR kode. (den næste kode)
             */
            if (qrCodeData == null) {
                droneCommander.hoverDrone(5000);
                qrCodeData = droneCommander.searchForQRCode();
            }


            QRCodeData qrCodeData1 = null;

            if (qrCodeData == null) {
                droneCommander.addMessage("qrCodeData is null! : correct code was not found : rotating to first code in list ");
                // still null after rotation
                for (int key : droneCommander.getQrCodeMap().keySet()) {
                    QRCodeData data = droneCommander.getQrCodeMap().get(key);

                    if (data != null) {
                        qrCodeData1 = data;
                        droneCommander.addMessage("Rotating to: " + qrCodeData1.getFoundYaw());
                        break;
                    }

                }

                if (qrCodeData1 != null) {
                    //droneCommander.rotateDrone((int) qrCodeData1.getFoundYaw());
                }

            } else {
                droneCommander.addMessage("qrCodeData is NOT null : correct code was found : doing nothing");
            }

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
            QRCodeData qrCodeData = droneCommander.searchForQRCode();

            /*
             * Hvis den korrekte QR kode ikke blev fundet, så
             * vil qrCodeData være lig med null, så derfor
             * laver vi en ny rotation, i håb om at finde den
             * korrekte QR kode. (den næste kode)
             */
            if (qrCodeData == null) {
                qrCodeData = droneCommander.searchForQRCode();
            }

            /*
             * Hvis vi ikke har fundet nogle QR koder efter ANDEN søgning,
             * (360 grader rundt), så roterer vi hen til graden af den kode der har den
             * største højde.
             */
            try {
                qrCodeData = droneCommander.getQrCodeWithGreatestHeight();
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

}