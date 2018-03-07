package cdio.ui.interfaces;

import java.util.EventListener;

public interface MessageListener extends EventListener {
    void messageCommandStartEventOccurred(String title);
    void messageCommandEventOccurred(Object obj, String msg);
    void messageCommandEndEventOccurred();
}