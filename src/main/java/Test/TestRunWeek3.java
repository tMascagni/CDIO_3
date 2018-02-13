package Test;

import server.Server;

import java.io.IOException;

public class TestRunWeek3 {



    public static void main(String[] args) {

        Server server = Server.getInstance();

        System.err.println("test starts");

        try {
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
}
