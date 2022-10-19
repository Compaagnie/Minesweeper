package PAC;

import CustomComponents.Buttons.ButtonTextures;
import PAC.Roguelike.RogueLikeController;

import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame
{
    GameMenu gameMenu;
    GameView gameView;

    Boolean enterPressed;
    Boolean debug;

    public Minesweeper(Boolean debug)
    {
        super("A Rogue Minesweeper");
        enterPressed = false;
        ButtonTextures buttonTextures = new ButtonTextures();

        this.setPreferredSize(new Dimension(900, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.debug = debug;

        gameMenu = new GameMenu(this);
        pack();
        this.setVisible(true);
    }

    public GameView getGameView(){
        return this.gameView;
    }

    public GameMenu getGameMenu(){
        return this.gameMenu;
    }

    public void startGame(int width, int height, int bombCount)
    {
        this.remove(gameMenu);
        gameView = new GameView(this, width, height,  bombCount);
        gameView.setVisible(true);
    }

    public void startRoguelikeGame()
    {
        this.remove(gameMenu);
        RogueLikeController controller = new RogueLikeController(this, debug);
    }

    public void openMenu()
    {
        gameView.setVisible(false);
        gameMenu = new GameMenu(this);
    }

    public void setEnterPressed(Boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public Boolean getEnterPressed() {
        return enterPressed;
    }
    
    public void setGameView(GameView view) { this.gameView = view; }
}