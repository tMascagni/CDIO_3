package controller;

import java.util.ArrayList;
import java.util.List;

public class Room {

    double with;
    double height;

    List<WallMarking> wallMarkingList = new ArrayList<>();


    private static Room singleton = new Room();

    private Room() {
        with = 60.5;
        height = 40.3;

        wallMarkingList.add(new WallMarking(0, 12.2,40.1));
        wallMarkingList.add(new WallMarking(1, 12.2,40.1));
        wallMarkingList.add(new WallMarking(2, 12.2,40.1));
        wallMarkingList.add(new WallMarking(3, 12.2,40.1));
        wallMarkingList.add(new WallMarking(4, 12.2,40.1));
        wallMarkingList.add(new WallMarking(5, 12.2,40.1));
        wallMarkingList.add(new WallMarking(6, 12.2,40.1));
        wallMarkingList.add(new WallMarking(7, 12.2,40.1));
        wallMarkingList.add(new WallMarking(8, 12.2,40.1));
        wallMarkingList.add(new WallMarking(9, 12.2,40.1));
    }

    public static Room getInstance( ) {
        return singleton;
    }

}
