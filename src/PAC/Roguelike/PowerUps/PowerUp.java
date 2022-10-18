package PAC.Roguelike.PowerUps;

import javax.swing.*;
import java.awt.*;

public interface PowerUp
{
    int getShopCost();
    int IMAGE_SIZE = 64;
    default Image getImage() { return null; };
    String getName();
    String getDescription();
    boolean isActive();
}
