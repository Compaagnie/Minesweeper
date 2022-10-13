package PAC.Roguelike;

import GridPAC.Roguelike.RoguelikeGrid;
import PAC.Roguelike.PowerUps.PowerUp;

import javax.swing.*;

public class RoguelikeEvent
{
    public final JComponent newCenterComponent;
    public final RoguelikeGrid newGrid;
    public final PowerUp powerUpChange;
    public final boolean addedPowerUp;

    public RoguelikeEvent(JComponent component)
    {
        this.newGrid = null;
        this.newCenterComponent = component;
        this.powerUpChange = null;
        this.addedPowerUp = false;
    }

    public RoguelikeEvent(RoguelikeGrid grid)
    {
        this.newGrid = grid;
        this.newCenterComponent = grid;
        this.powerUpChange = null;
        this.addedPowerUp = false;
    }

    public RoguelikeEvent(PowerUp powerUp, boolean addedPowerUp)
    {
        this.newGrid = null;
        this.newCenterComponent = null;
        this.powerUpChange = powerUp;
        this.addedPowerUp = addedPowerUp;
    }
}
