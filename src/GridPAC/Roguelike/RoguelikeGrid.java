package GridPAC.Roguelike;

import GridPAC.CellChangeEvent;
import GridPAC.Grid;
import PAC.Roguelike.RoguelikeModel;
import PAC.Roguelike.RoguelikeView;

import java.awt.*;
import java.util.function.Supplier;

public class RoguelikeGrid extends Grid
{
    RoguelikeModel roguelikeModel;

    public RoguelikeGrid(RoguelikeModel _roguelikeModel, Dimension _dimension, int _bombCount, Supplier<Boolean> hasRevive)
    {
        super(null, _dimension, _bombCount);
        this.roguelikeModel = _roguelikeModel;
        gridModel = new RoguelikeGridModel(_dimension, _bombCount, this::cellChanged, hasRevive);
    }

    @Override
    public void onGameLost()
    {
        roguelikeModel.onLevelLost();
    }

    @Override
    public void onGameWin()
    {
        roguelikeModel.onLevelClear();
    }

    public void radarReveal(int position) { ((RoguelikeGridModel) gridModel).radarReveal(position); }
    public boolean bombReveal() { return ((RoguelikeGridModel) gridModel).bombReveal(); }

    public void lineReveal(int position) { ((RoguelikeGridModel) gridModel).lineReveal(position);}

    public void columnReveal(int position) { ((RoguelikeGridModel) gridModel).columnReveal(position); }

    @Override
    public void cellChanged(CellChangeEvent e)
    {
        if(e.flagToggle) buttonArray[e.position].toggleFlag();
        else if (e.finish)
        {
            if(!e.reveal) // The game should have been over, but we stopped : REVIVING PLAYER
            {
                System.out.println("REVIVE");
                roguelikeModel.onRevive();
                ((RoguelikeGridModel) gridModel).resolveCell(e.position);
            }
            else // the game is actually over, the player revealed the thing
            {
                if(!this.isOver()) // prevent multiple calls
                {
                    triggerEventListeners("over");
                    if(e.won) onGameWin();
                    else onGameLost();
                }
            }
        }
        //update UI for cell
        else if (e.reveal) this.buttonArray[e.position].revealButton();
        else this.buttonArray[e.position].resetButton();
    }
}
