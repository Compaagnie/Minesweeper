package PAC.Roguelike;

import GridPAC.Roguelike.RoguelikeGrid;
import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.PowerUps.PassivePowerUp;
import Shop.Shop;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import static java.awt.Toolkit.getDefaultToolkit;

public class RoguelikeModel
{
    //TODO : move from view to here

    protected int currentLevel = 0;
    protected int currencyCount = 0;
    protected ArrayList<ActivePowerUp> activePowerUps = new ArrayList<>();
    protected int passivePowerUps = 0;
    private Integer energy = Integer.MAX_VALUE; // todo : design check
    private int energy_MAX = Integer.MAX_VALUE; // todo : design check

    protected RoguelikeGrid grid;

    protected ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    protected ArrayList<Consumer<RoguelikeEvent>> eventListeners = new ArrayList<>();

    public RoguelikeModel()
    {
        // todo : remove debug power ups :
        activePowerUps.add(ActivePowerUp.RADAR_REVEAL);
//        activePowerUps.add(ActivePowerUp.BOMB_REVEAL);
//        activePowerUps.add(ActivePowerUp.LINE_REVEAL);
//        activePowerUps.add(ActivePowerUp.COLUMN_REVEAL);

        setupCurrentLevelGrid();
    }

    public void addChangeListener(ChangeListener listener) {this.changeListeners.add(listener);}

    public void addEventListener(Consumer<RoguelikeEvent> listener) {this.eventListeners.add(listener);}

    public void triggerChangeListeners()
    {
        for(ChangeListener listener : this.changeListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    public void triggerEventListeners(RoguelikeEvent event)
    {
        for(Consumer<RoguelikeEvent> listener : this.eventListeners)
            listener.accept(event);
    }

    public void executePowerUp(int powerUpSlot)
    {
        if(grid == null) return;
        if(powerUpSlot < 0 || powerUpSlot >= activePowerUps.size())
        {
            getDefaultToolkit().beep();
            System.out.println("Not yet bound to power up : " + powerUpSlot);
        }
        else
        {
            ActivePowerUp activePowerUp = activePowerUps.get(powerUpSlot);
            Point mouse = grid.getMousePosition();
            if(activePowerUp.getEnergyCost() <= energy && mouse != null && grid.contains(mouse))
            {
                float absolute_x = grid.dimensions.width * mouse.x/ (float) grid.getWidth();
                float absolute_y = grid.dimensions.height * mouse.y/ (float) grid.getHeight();

                int currentMouseCell =   ((int)absolute_x + (int)absolute_y * grid.dimensions.width);
                if(activePowerUp.use(this.grid, currentMouseCell))
                {
                    energy -= activePowerUp.getEnergyCost();
                }
            }
        }
    }

    protected void refillEnergy()
    {
        this.energy = this.energy_MAX;
    }

    protected void updateLevel()
    {
        ++this.currentLevel;
        triggerChangeListeners();
    }

    public void updateCurrency(int modifier)
    {
        // todo : magic number to constant & design check
        // todo : add animations
        if(modifier > 0)
        {
            if(has(PassivePowerUp.DOUBLE_COIN))
            {
                modifier *= 2;
            }
            if(has(PassivePowerUp.DOUBLE_EDGED_SWORD))
            {
                modifier *= 1.5;
            }
        }

        this.currencyCount += modifier;
        triggerChangeListeners();
    }

    public void setupCurrentLevelGrid()
    {
//        removeCenterComponent();
        this.updateLevel();
        this.grid = null;
        this.grid = getGridFromLevel(currentLevel);
        triggerEventListeners(new RoguelikeEvent(grid));
    }

    protected RoguelikeGrid getGridFromLevel(int level)
    {
        // TODO : non-rectangular grids ?
        // todo : magic number to constant & design check

        int BOMB_PERCENT = 10 + 2*level;
        int size = (level+1) * 4;
        if(has(PassivePowerUp.DOUBLE_EDGED_SWORD)) BOMB_PERCENT *= 1.5f;

        int bomb_count = (int) (size * size * (BOMB_PERCENT / 100f));
        RoguelikeGrid grid = new RoguelikeGrid(new Dimension(size, size), bomb_count);
        grid.setOnWinCallback(this::onLevelClear);
        return grid;
    }

    public void onLevelClear()
    {
        updateCurrency(+3); // Todo : currency formula
        refillEnergy();
        grid = null;

        Shop shop; // free stage store : choose one of 3 power ups
        if(!has(PassivePowerUp.SHOP_AHEAD)) shop = new Shop(this, true, this::nextLevel);
        else
        {
            shop = new Shop(this, true, this::createShop);
            remove(PassivePowerUp.SHOP_AHEAD);
        }
        triggerEventListeners(new RoguelikeEvent(shop));
    }

    public void createShop()
    {
        Shop shop = new Shop(this, false, this::nextLevel);
        triggerEventListeners(new RoguelikeEvent(shop));
    }

    public void nextLevel()
    {
        this.setupCurrentLevelGrid();
        triggerChangeListeners();
    }

    public void add(PassivePowerUp powerUp)
    {
        // set masked bit to 1
        this.passivePowerUps = this.passivePowerUps | powerUp.mask;
//        DEBUGupdatePassivePowerUpLabel();
    }

    public boolean has(PassivePowerUp powerUp)
    {
        // check if the masked bit is 1
        return (this.passivePowerUps & powerUp.mask) != 0;
    }

    public void remove(PassivePowerUp powerUp)
    {
        // set the masked bit to 0
        this.passivePowerUps = this.passivePowerUps & ~powerUp.mask;
//        DEBUGupdatePassivePowerUpLabel();
    }

    public void add(ActivePowerUp powerUp)
    {
        this.activePowerUps.add(powerUp);
    }

    public boolean has(ActivePowerUp powerUp)
    {
        return this.activePowerUps.contains(powerUp);
    }

    public void remove(ActivePowerUp powerUp)
    {
        this.activePowerUps.remove(powerUp);
    }

    public int getCurrencyCount() { return currencyCount; }

    public int getCurrentLevel() { return currentLevel; }
}
