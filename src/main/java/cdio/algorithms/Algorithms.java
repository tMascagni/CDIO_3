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

            //Start med at søge efter en QR kode.

            QRImg qrImg = droneCommander.searchForQRCode();


            // Hvis den korrekte QR kode ikke blev fundet, så
            // vil qrImg være lig med null, så derfor
            // laver vi en ny rotation, i håb om at finde den
            // korrekte QR kode. (den næste kode)
            if (qrImg == null) {
                droneCommander.addMessage("Starting next rotation....");
                droneCommander.hoverDrone(2000);
                qrImg = droneCommander.searchForQRCode();
            }


            if (qrImg == null) {
                droneCommander.addMessage("qrImg is null : correct code was not found : rotating to tallest QR code!");
                // still null after rotation

                QRImg qrImgHeight = droneCommander.getTallestQRCode();

                if (qrImgHeight != null && qrImgHeight.getQrCodeData() != null) {
                    droneCommander.rotateDrone((int) qrImgHeight.getQrCodeData().getFoundYaw());
                } else {
                    droneCommander.addMessage("Not rotating because... no idea why.");
                }

            } else {
                droneCommander.addMessage("qrImg is NOT null : correct code was found : doing nothing");
            }

            droneCommander.hoverDrone(5000);
            droneCommander.landDrone();
            droneCommander.stopDrone();
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }

    public static void oneRingAlgoTest(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(5500);
            droneCommander.searchForQRCodeDetect(true);
            //droneCommander.pointToQRSpin(50);
            while (true) {
                droneCommander.hoverDrone(5000);

                droneCommander.flyToTargetQRCode(true, 100, 15); // fly hen til ring

                droneCommander.adjustHeightToCenterFromQR();

                //droneCommander.adjustToCenterFromQR(50);

                droneCommander.rejeHop();

                droneCommander.hoverDrone(5000);

                //droneCommander.landDrone();
                // igennem first ring

                float downAltitude = droneCommander.getAltitude() - 650;
                droneCommander.flyDownToAltitude(downAltitude);

                droneCommander.searchForQRCodeDetect(false);
            }


        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }

    public static void runAllRings(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });


        try {
            do {
                droneCommander.startDrone();
                droneCommander.initDrone();
                droneCommander.takeOffDrone();
                droneCommander.hoverDrone(7000);

                /*
                 * Start med at søge efter en QR kode.
                 */
                QRImg qrImg = droneCommander.searchForQRCodeDetect(false);

                /*
                 * Hvis den korrekte QR kode ikke blev fundet, så
                 * vil qrCodeData være lig med null, så derfor
                 * laver vi en ny rotation, i håb om at finde den
                 * korrekte QR kode. (den næste kode)
                 */
                if (qrImg == null) {
                    qrImg = droneCommander.searchForQRCodeDetect(false);
                }

                /*
                 * Hvis qrImg er forskellig fra null, og den er læst,
                 * så ved vi at det er den næste QRKode som vi skal have.
                 */
                if (qrImg != null) {
                    /*
                     * Vi sikrer os at dronens camera er ca. foran QR koden
                     */
                    droneCommander.adjustToCenterFromQR(50);

                    /*
                     * Vi flyver hen til QR koden
                     */
                    droneCommander.flyToTargetQRCode(true, 80, 10);

                    /*
                     * Flyv op til midten af ringen.
                     * Flyv op ind til midten
                     * af ringen er blevet detekteret.
                     */
                    droneCommander.adjustHeightToCenterFromQR();
                    droneCommander.flyUpAltitudePlus(650);

                    /*
                     * flyv igennem ring :)
                     */
                    droneCommander.flyForward(2000);

                    /*
                     * flyv ned til samme altitude som før.
                     */
                    float wantedAltitude = droneCommander.getAltitude() - 650;
                    droneCommander.flyDownToAltitude((int) wantedAltitude);

                } else {
                    /*
                     * Hvis qrImg er null så betyder det at dronen ikke fandt den
                     * korrekte QR kode.
                     *
                     * Flyv op til en ny højde og søg igen efter QRKoder.
                     */

                    // flyv op

                    // searchRotation

                    // igen searchRotation

                    // samme algo som ovenover

                    // ellers, så flyv hen til QR koden med den største højde.
                }

            } while (droneCommander.getTargetQRCode() < 8);
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

            droneCommander.landDrone();
        } catch (DroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }


}

/*


 try {
                qrImg = droneCommander.getTallestQRCode();
                droneCommander.rotateDrone((int) qrImg.getQrCodeData().getFoundYaw());
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

