package cdio.main;

import cdio.algorithms.Algorithms;
import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;

public final class Main {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        Algorithms.finalAlgorithm(droneCommander);
    }

}