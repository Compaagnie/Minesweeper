package PAC.Roguelike.PowerUps;

import GridPAC.Roguelike.RoguelikeGrid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public enum ActivePowerUp implements PowerUp
{
    //BOMB_REVEAL(1),
    //COLUMN_REVEAL(3),
    //LINE_REVEAL(3),
    //RADAR_REVEAL(2),
    BOMB_REVEAL(),
    COLUMN_REVEAL(),
    LINE_REVEAL(),
    RADAR_REVEAL(),

    COUNT;

    public final int shopCost;
    private final int energyCost;
    private int shortcut;

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
        else return "unassigned name";
    }

    @Override
    public String getDescription()
    {
        if(this == BOMB_REVEAL) return "Reveals random bombs that are not yet revealed";
        else if (this == COLUMN_REVEAL) return "Select then reveal a whole column";
        else if (this == LINE_REVEAL) return "Select then reveal a whole line";
        else if (this == RADAR_REVEAL) return "Reveals a cell and its neighbours";
        else return "unassigned description";
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
        try {
            return ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("textures/powerups/" + iconName))).getScaledInstance(imgDimensions.width, imgDimensions.height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isActive() { return true; }

    public int getEnergyCost(){ return energyCost; }

    public int getShortcut() { return shortcut;}
    public void setShortcut(int _shortcut) { this.shortcut = _shortcut; }

    public List<String> getKeyWords()
    {
        if(this == BOMB_REVEAL)
        {
            return List.of(new String[]{"bomb", "random"});
        }
        else if (this == COLUMN_REVEAL)
        {
            return List.of(new String[]{"column", "vertical"});
        }
        else if (this == LINE_REVEAL)
        {
            return List.of(new String[]{"line", "horizontal", "row"});
        }
        else if (this == RADAR_REVEAL)
        {
            return List.of(new String[]{"radar", "zone", "scan", "area", "can", "skin"});
        }
        else return List.of(new String[]{});
    }
}
