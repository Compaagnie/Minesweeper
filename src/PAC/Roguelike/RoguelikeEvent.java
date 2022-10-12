package PAC.Roguelike;

import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;

public class RoguelikeEvent
{
    public final JComponent newCenterComponent;

    public final RoguelikeGrid newGrid;

    public RoguelikeEvent(JComponent component)
    {
        this.newGrid = null;
        this.newCenterComponent = component;
    }

    public RoguelikeEvent(RoguelikeGrid grid)
    {
        this.newGrid = grid;
        this.newCenterComponent = grid;
    }
}
