package PAC.Roguelike;

import PAC.Minesweeper;
import PAC.Roguelike.PowerUps.ActivePowerUp;

public class RogueLikeController
{
    protected RoguelikeView view;
    protected RoguelikeModel model;

    public RogueLikeController(Minesweeper minesweeper, Boolean debug)
    {
        model = new RoguelikeModel(debug);
        view = new RoguelikeView(this, minesweeper);
        minesweeper.setGameView(view);
        minesweeper.add(view);
        view.init();
        view.setVisible(true);
        model.addChangeListener(e -> view.update());
        model.addEventListener(this::roguelikeEventHandler);
        model.init();
        view.setGrid(model.grid);
        view.setCenterComponent(model.grid);
    }

    public void roguelikeEventHandler(RoguelikeEvent e)
    {
//        System.out.println(e.newGrid + " " + e.newCenterComponent);
        if(e.newGrid != null) view.setGrid(e.newGrid);
        if(e.newCenterComponent != null) view.setCenterComponent(e.newCenterComponent);
        if(e.powerUpChange != null) view.handlePowerUpUpdate(e.powerUpChange, e.addedPowerUp);

    }

    public void executePowerUp(int powerUpSlot)
    {
        this.model.executePowerUp(powerUpSlot);
    }

    public int getCurrencyCount()
    {
        return model.getCurrencyCount();
    }

    public int getCurrentLevel()
    {
        return model.getCurrentLevel();
    }

    public int getBombCount() { return model.grid.getBombCount(); }

    public int getCurrentEnergy(){ return model.getCurrentEnergy(); }
    public int getMaxEnergy(){return model.getMaxEnergy(); }

    public void onRestart() { model.restart(); }

    public boolean isInShop(){return model.isInShop();}

    public void usePowerUp(ActivePowerUp powerUp){ model.usePowerUp(powerUp); }
}
