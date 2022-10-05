package PAC.Roguelike.Powerups;

import GridPAC.Roguelike.RoguelikeGrid;

public class BombReveal implements PowerUp
{
    @Override
    public int getCost()
    {
        return 0; //todo
    }

    @Override
    public boolean use(RoguelikeGrid grid, int position)
    {
        grid.bombReveal();
        return true;
    }
}
