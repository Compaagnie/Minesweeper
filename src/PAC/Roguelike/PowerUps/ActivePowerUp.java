package PAC.Roguelike.PowerUps;

import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;

public enum ActivePowerUp implements PowerUp
{
    BOMB_REVEAL(1),
    COLUMN_REVEAL(3),
    LINE_REVEAL(3),
    RADAR_REVEAL(2),

    COUNT;

    public final int shopCost;
    private final int energyCost;

    ActivePowerUp()
    {
        shopCost = 10;
        energyCost = 1;
    }

    ActivePowerUp(int _energyCost)
    {
        shopCost = 10;
        energyCost = _energyCost;
    }

    ActivePowerUp(int _energyCost, int _shopCost)
    {
        shopCost = _shopCost;
        energyCost = _energyCost;
    }

    public boolean use(RoguelikeGrid grid, int position)
    {
        if(this == BOMB_REVEAL) return grid.bombReveal();
        else if (this == COLUMN_REVEAL) grid.columnReveal(position);
        else if (this == LINE_REVEAL) grid.lineReveal(position);
        else if (this == RADAR_REVEAL) grid.radarReveal(position);
        else return false;
        return true;
    }

    @Override
    public int getShopCost()
    {
        return shopCost;
    }

    @Override
    public String getName()
    {
        if(this == BOMB_REVEAL) return "Bomb reveal";
        else if (this == COLUMN_REVEAL) return "Column reveal";
        else if (this == LINE_REVEAL) return "Line reveal";
        else if (this == RADAR_REVEAL) return "Radar";
        else return String.valueOf(this.ordinal());
    }

    @Override
    public Image getImage()
    {
        String iconName;
        Dimension imgDimensions = new Dimension(PowerUp.IMAGE_SIZE, PowerUp.IMAGE_SIZE);
        if(this == BOMB_REVEAL) iconName = "bomb_reveal.png";
        else if (this == COLUMN_REVEAL) iconName = "column_reveal.png";
        else if (this == LINE_REVEAL) iconName = "line_reveal.png";
        else if (this == RADAR_REVEAL) iconName = "radar.png";
        else return null;
        return new ImageIcon("textures/powerups/" + iconName).getImage().getScaledInstance(imgDimensions.width,imgDimensions.height, Image.SCALE_SMOOTH);
    }

    @Override
    public boolean isActive() { return true; }

    public int getEnergyCost(){ return energyCost; }
}
