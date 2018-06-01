package cdio.routing.room;

import java.util.ArrayList;
import java.util.List;

public class Room {

    double width;
    double height;

    List<WallMarking> wallMarkingList = new ArrayList<>();

    private static Room singleton = new Room();

    private Room() {
        width = 60.5;
        height = 40.3;

        wallMarkingList.add(new WallMarking(0, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(1, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(2, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(3, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(4, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(5, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(6, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(7, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(8, 12.2, 40.1));
        wallMarkingList.add(new WallMarking(9, 12.2, 40.1));
    }

    public static Room getInstance() {
        return singleton;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<WallMarking> getWallMarkingList() {
        return wallMarkingList;
    }

    public void setWallMarkingList(List<WallMarking> wallMarkingList) {
        this.wallMarkingList = wallMarkingList;
    }

}
