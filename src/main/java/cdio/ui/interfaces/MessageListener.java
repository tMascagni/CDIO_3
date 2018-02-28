package cdio.ui.interfaces;

import java.util.EventListener;

public interface MessageListener extends EventListener {
    void messageEventOccurred(Object obj, String text);
}
