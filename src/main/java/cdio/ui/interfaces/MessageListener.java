package cdio.ui.interfaces;

import java.util.EventListener;

public interface MessageListener extends EventListener {
    void messageCommandStartEventOccurred(String title);
    void messageCommandEventOccurred(Object obj, String msg);
    void messageCommandEndEventOccurred();
    void setBattery(int battery);
    void setSpeed(int speed);
    void setPitch(int pitch);
    void setRoll(int roll);
    void setYaw(int yaw);
    void setAltitude(int altitude);
}