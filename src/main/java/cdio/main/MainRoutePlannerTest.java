package cdio.main;

import cdio.algorithms.Algorithms;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.routing.RoutePlanner;

public final class MainRoutePlannerTest {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        RoutePlanner.flightControl(droneCommander);
    }

}