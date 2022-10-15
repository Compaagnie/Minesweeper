package GridPAC;

import Buttons.CellButton;
import PAC.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Grid extends JPanel
{
    public final Dimension dimensions;
    private final GameView gameView;
    protected GridModel gridModel;

    protected CellButton[] buttonArray;

    protected final Icon redIcon;

    protected ArrayList<Consumer<GridEvent>> eventListeners = new ArrayList<>();

    public Grid(GameView _gameView, Dimension _dimension, int _bombCount)
    {
        this.gameView = _gameView;
        dimensions = _dimension;
        gridModel = new GridModel(_dimension, _bombCount, this::cellChanged);

        this.setLayout(new GridBagLayout());

        buttonArray = new CellButton[_dimension.width * _dimension.height];

        buttonCreation();

        redIcon = new ImageIcon("textures/redIcon.png");
    }

    private void buttonCreation()
    {
        GridBagConstraints buttonPlacementConstraint = new GridBagConstraints();
        buttonPlacementConstraint.weightx = 1;
        buttonPlacementConstraint.weighty = 1;
        buttonPlacementConstraint.fill = GridBagConstraints.BOTH;
        for (int y = 0; y < this.gridModel.getDimension().height; y++)
        {
            buttonPlacementConstraint.gridy = y;
            for (int x = 0; x < this.gridModel.getDimension().width; x++)
            {
                buttonPlacementConstraint.gridx = x;

                CellButton button = new CellButton(x + y * this.gridModel.getDimension().width, this);
                button.setSize(button.getPreferredSize());
                buttonArray[x + y * this.gridModel.getDimension().width] = button;
                this.add(button, buttonPlacementConstraint);
                //System.out.println("Created button " + (x + y * this.gridModel.getDimension().width));
            }
        }
        revalidate();
    }

    public void restartGame()
    {
        this.gridModel.restartGame();
        this.setGameViewStatus("");
        this.triggerEventListeners("restart");
    }

    public void propagateReveal(int position)
    {
        this.gridModel.propagateReveal(position);
    }

    public void revealCell(int position)
    {
        this.triggerEventListeners("reveal");
        this.gridModel.revealCell(position);
    }

    public void revealCellOnPointerPosition()
    {
        CellButton button = (CellButton) this.getComponentAt(MouseInfo.getPointerInfo().getLocation());
        gridModel.revealCell(button.position);
    }
    public boolean hasFlag(int position){
        return this.gridModel.hasFlag(position);
    }

    public void addFlag(int position)
    {
        gridModel.addFlag(position);
        triggerEventListeners("flag");
    }

    public void removeFlag(Integer position)
    {
        gridModel.removeFlag(position);
        triggerEventListeners("flag");
    }

    public void toggleFlagOnPointerPosition(){
        CellButton button = (CellButton) this.getComponentAt(MouseInfo.getPointerInfo().getLocation());
        button.toggleFlag();
    }

    public int getCellCount() { return gridModel.getCellCount(); }

    public int getCell(int position) { return gridModel.getCell(position); }

    public void cellChanged(CellChangeEvent e)
    {
        if(e.flagToggle) buttonArray[e.position].toggleFlag();
        else if (e.finish)
        {
            if(!isOver())
            {
                triggerEventListeners("over");
                if(e.won) onGameWin();
                else onGameLost();
            }
        }
        //update UI for cell
        else if (e.reveal) this.buttonArray[e.position].revealButton();
        else this.buttonArray[e.position].resetButton();
    }

    public void onGameWin()
    {
        setGameViewStatus("Won");
    }

    public void onGameLost()
    {
        setGameViewStatus("Lost");
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

    public boolean isGenerated(){return gridModel.isGenerated();}
    public void addEventListener(Consumer<GridEvent> listener){ this.eventListeners.add(listener); }
    public void triggerEventListeners(String command)
    {
        for(Consumer<GridEvent> listener: this.eventListeners) listener.accept(new GridEvent(command));
    }

    protected void setGameViewStatus(String status)
    {
        if(this.gameView != null) this.gameView.setGameStatus(status);
    }
}
