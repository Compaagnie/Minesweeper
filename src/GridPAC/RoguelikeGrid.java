package GridPAC;

import PAC.GameView;

import java.awt.*;

public class RoguelikeGrid extends Grid
{
    Runnable onWinCallback;
    public RoguelikeGrid(GameView gameView, Dimension _dimension, int _bombCount)
    {
        super(gameView, _dimension, _bombCount);
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
        //TODO :
        //  - show bonuses
        //  - launch other game
        System.out.println("Roguelike:nextlevel");
        onWinCallback.run();
    }
}
