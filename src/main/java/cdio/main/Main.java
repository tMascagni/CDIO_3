package cdio.main;

import cdio.algorithms.Algorithms;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;

public final class Main {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        Algorithms.testAl(droneCommander);
    }

}