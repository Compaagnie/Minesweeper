package PAC.Roguelike.PowerUps;

import GridPAC.Roguelike.RoguelikeGrid;

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
        shopCost = 1;
        energyCost = 1;
    }

    ActivePowerUp(int _energyCost)
    {
        shopCost = 1;
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
    public String getName(){ return String.valueOf(this.ordinal()); }

    @Override
    public boolean isActive() { return true; }

    public int getEnergyCost(){ return energyCost; }
}
