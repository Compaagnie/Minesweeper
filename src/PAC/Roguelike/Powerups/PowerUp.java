package PAC.Roguelike.Powerups;

import GridPAC.Roguelike.RoguelikeGrid;

public interface PowerUp
{
    int getCost();
    boolean use(RoguelikeGrid grid, int position);
}
