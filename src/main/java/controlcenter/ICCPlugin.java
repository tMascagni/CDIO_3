package controlcenter;

import de.yadrone.base.IARDrone;

import javax.swing.*;
import java.awt.*;

public interface ICCPlugin {
    void activate(IARDrone drone);

    void deactivate();

    String getTitle();

    String getDescription();

    boolean isVisual();

    Dimension getScreenSize();

    Point getScreenLocation();

    JPanel getPanel();
}