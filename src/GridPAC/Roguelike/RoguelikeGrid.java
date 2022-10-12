package GridPAC.Roguelike;

import GridPAC.Grid;

import java.awt.*;

public class RoguelikeGrid extends Grid
{
    Runnable onWinCallback;
    public RoguelikeGrid(Dimension _dimension, int _bombCount)
    {
        super(_dimension, _bombCount);
        gridModel = new RoguelikeGridModel(_dimension, _bombCount, this::cellChanged);
    }

    public void setOnWinCallback(Runnable callback) {this.onWinCallback = callback;}

    @Override
    public void onGameLost()
    {
        //TODO : show score
    }

    @Override
    public void onGameWin()
    {
        onWinCallback.run();
    }

    public void radarReveal(int position) { ((RoguelikeGridModel) gridModel).radarReveal(position); }
    public boolean bombReveal() { return ((RoguelikeGridModel) gridModel).bombReveal(); }

    public void lineReveal(int position) { ((RoguelikeGridModel) gridModel).lineReveal(position);}

    public void columnReveal(int position) { ((RoguelikeGridModel) gridModel).columnReveal(position); }
}
