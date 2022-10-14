package PAC;

import Buttons.ButtonTextures;

import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame
{
    GameMenu gameMenu;
    GameView gameView;

    Boolean enterPressed;

    public Minesweeper()
    {
        super("PAC.Minesweeper");
        enterPressed = false;
        ButtonTextures buttonTextures = new ButtonTextures();

        this.setPreferredSize(new Dimension(900, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


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
}