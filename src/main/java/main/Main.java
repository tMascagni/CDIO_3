package main;

import controller.DroneControllerException;
import server.Server;

import java.awt.*;

public final class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Server.getInstance().initDrone();
            } catch (DroneControllerException e) {
                e.printStackTrace();
            }
        });
    }

}