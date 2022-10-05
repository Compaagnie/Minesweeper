package PAC.Roguelike.Powerups;

import GridPAC.Roguelike.RoguelikeGrid;

public class ColumnReveal implements PowerUp
{
    @Override
    public int getCost() {
        return 0; // todo design check
    }

    @Override
    public boolean use(RoguelikeGrid grid, int position)
    {
        grid.columnReveal(position);
        return true;
    }
}
