package test;

import server.Server;

import java.io.IOException;

public class TestDriverWeek3 {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server server = Server.getInstance();

                    System.err.println("test starts");

                    server.initDrone();
                    System.out.println("battery: " + server.getNavData().getBattery());
                    server.hoverAndWait(200);
                    server.land();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}