package main;

import server.Server;

import java.awt.*;

public final class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> Server.getInstance().initDrone());
    }

}