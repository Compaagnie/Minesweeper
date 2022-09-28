package GridPAC;

import Buttons.CellButton;
import PAC.GameView;

import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel
{
    protected GameView gameView;
    protected GridModel gridModel;

    protected CellButton[] buttonArray;

    protected final Icon redIcon;

    public Grid(GameView gameView, Dimension _dimension, int _bombCount)
    {
        this.gameView = gameView;

        gridModel = new GridModel(_dimension, _bombCount, this::cellChanged);

        this.setLayout(new GridBagLayout());

        buttonArray = new CellButton[_dimension.width * _dimension.height];

        buttonCreation();

        redIcon = new ImageIcon("textures/redIcon.png");
    }

    private void buttonCreation()
    {
        GridBagConstraints buttonPlacementConstraint = new GridBagConstraints();
        buttonPlacementConstraint.fill = GridBagConstraints.BOTH;
        buttonPlacementConstraint.weightx = 1;
        buttonPlacementConstraint.weighty = 1;
        for (int y = 0; y < this.gridModel.getDimension().height; y++)
        {
            buttonPlacementConstraint.gridy = y;
            for (int x = 0; x < this.gridModel.getDimension().width; x++)
            {
                buttonPlacementConstraint.gridx = x;

                CellButton button = new CellButton(x + y * this.gridModel.getDimension().width, this);
                buttonArray[x + y * this.gridModel.getDimension().width] = button;
                this.add(button, buttonPlacementConstraint);
            }
        }
    }

    public void restartGame()
    {
        this.gridModel.restartGame();
        this.gameView.gameTimer.restart();
        this.gameView.gameTimer.stop();
        this.gameView.updateFlagNb();
    }

    public void propagateReveal(int position)
    {
        this.gridModel.propagateReveal(position);
    }

    public void revealCell(int position)
    {
        this.gameView.gameTimer.start();
        this.gridModel.revealCell(position);
    }

    public boolean hasFlag(int position){
        return this.gridModel.hasFlag(position);
    }

    public void addFlag(int position)
    {
        gridModel.addFlag(position);
        this.gameView.updateFlagNb();
    }

    public void removeFlag(Integer position)
    {
        gridModel.removeFlag(position);
        this.gameView.updateFlagNb();
    }

    public int getCellCount() { return gridModel.getCellCount(); }

    public int getCell(int position) { return gridModel.getCell(position); }

    public void cellChanged(CellChangeEvent e)
    {
        if (e.finish)
        {
            this.gameView.gameTimer.stop();
            if(e.won) onGameWin();
            else onGameLost();
        }
        //update UI for cell
        else if (e.reveal)
        {
            this.buttonArray[e.position].revealButton();
        }
        else
        {
            this.buttonArray[e.position].resetButton();
        }
    }

    public void onGameWin()
    {

    }

    public void onGameLost()
    {

    }

    public int getFlagNumber()
    {
        return gridModel.getFlagArray().size();
    }

    public int getBombCount()
    {
        return gridModel.getBombCount();
    }

    public Boolean isOver() { return gridModel.isOver(); }
}
