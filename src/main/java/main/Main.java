package main;

import server.Server;

import java.io.IOException;

public final class Main {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    /* Initialize drone */
                    Server.getInstance().initDrone();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}