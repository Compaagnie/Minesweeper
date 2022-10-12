package PAC.Roguelike.PowerUps;

import javax.swing.*;

public interface PowerUp
{
    int getShopCost();
    default Icon getIcon() { return null; }; // todo : implement this
    String getName();
    boolean isActive();
}
