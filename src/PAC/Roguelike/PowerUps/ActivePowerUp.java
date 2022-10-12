package PAC.Roguelike.PowerUps;

import GridPAC.Roguelike.RoguelikeGrid;
import PAC.Roguelike.PowerUps.PowerUp;

public enum ActivePowerUp implements PowerUp
{
    BOMB_REVEAL,
    COLUMN_REVEAL,
    LINE_REVEAL,
    RADAR_REVEAL,

    COUNT;

    public final int shopCost;
    private final int energyCost;

    ActivePowerUp()
    {
        shopCost = 1;
        energyCost = 1;
    }

    ActivePowerUp(int _shopCost)
    {
        shopCost = _shopCost;
        energyCost = 1;
    }

    ActivePowerUp(int _shopCost, int _energyCost)
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
    public String getName(){ return String.valueOf(this.ordinal()); }

    @Override
    public boolean isActive() { return true; }

    public int getEnergyCost(){ return energyCost; }
}
