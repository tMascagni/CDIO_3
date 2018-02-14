package server;

import de.yadrone.base.ARDrone;

public interface IServer {
    void initDrone();
    void stopDrone();
    ARDrone getDrone();

    default void printConsole(String msg) {
        System.out.println(msg);
    }

}