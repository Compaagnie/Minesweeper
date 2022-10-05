package PAC.Roguelike.Powerups;

import GridPAC.Roguelike.RoguelikeGrid;

public class Radar implements PowerUp
{
    public final int ENERGY_COST = 3; // todo

    @Override
    public int getCost()
    {
        return ENERGY_COST;
    }

    @Override
    public boolean use(RoguelikeGrid grid, int position){ grid.radarReveal(position); return true; }


}
