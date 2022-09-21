package GridPAC;

import Buttons.BottomButton;
import Buttons.TopButton;

import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel
{
    protected GridUI gridUI;
    protected GridModel gridModel;

    protected TopButton[] TopButtonArray;
    protected BottomButton[] BottomButtonArray;



    protected final Icon redIcon;

    public int getCellCount() { return gridModel.getCellCount(); }
    public int getCell(int position) { return gridModel.getCell(position); }

    public Grid(Dimension _dimension, int _bombCount)
    {
        gridUI = new GridUI(this);
        this.setUI(gridUI);

        gridModel = new GridModel(_dimension, _bombCount, this::cellChanged);

        this.setLayout(new GridBagLayout());

        CellArray = new Integer[_dimension.width * _dimension.height];
        Arrays.fill(CellArray, 0);
        FlagArray = new ArrayList<>();
        TopButtonArray = new TopButton[_dimension.width * _dimension.height];
        BottomButtonArray = new BottomButton[_dimension.width * _dimension.height];

        buttonCreation();
        redIcon = new ImageIcon("textures/redIcon.png");
    }

    private void buttonCreation()
    {
        GridBagConstraints buttonPlacementConstraint = new GridBagConstraints();
        buttonPlacementConstraint.fill = GridBagConstraints.BOTH;
        buttonPlacementConstraint.weightx = 1;
        buttonPlacementConstraint.weighty = 1;
        for (int y = 0; y < this.gridModel.getDimension().height; y++) {
            buttonPlacementConstraint.gridy = y;
            for(int x = 0; x < dimension.width; x++){
                buttonPlacementConstraint.gridx = x;

                TopButton topButton = new TopButton(x + y * this.gridModel.getDimension().width, this);
                TopButtonArray[x + y * this.gridModel.getDimension().width] = topButton;
                this.add(topButton, buttonPlacementConstraint);

                BottomButton bottomButton = new BottomButton(x + y * this.gridModel.getDimension().width, this);
                BottomButtonArray[x + y * this.gridModel.getDimension().width] = bottomButton;
                this.add(bottomButton, buttonPlacementConstraint);
            }
        }
    }

    public void restartGame()
    {
        this.gridModel.restartGame();
    }

    public void propagateReveal(int position)
    {
        this.gridModel.propagateReveal(position);
    }

    public void revealCell(int position)
    {
        this.gridModel.revealCell(position);
    }

    public boolean hasFlag(int position){
        return this.gridModel.hasFlag(position);
    }

    public void addFlag(int position)
    {
        gridModel.addFlag(position);
    }

    public void removeFlag(Integer position)
    {
        gridModel.removeFlag(position);
    }

    void setBottomButtonTexture()
    {
        for (int i = 0; i < this.gridModel.getCellCount(); i++)
        {
            this.BottomButtonArray[i].setTextureFromValue(this.gridModel.getCell(i));
        }
    }

    public void cellChanged(CellChangeEvent e)
    {
        //update UI for cell
        if (e.reveal) {
            this.TopButtonArray[e.position].setVisible(false);
            this.BottomButtonArray[e.position].setTextureFromValue(this.gridModel.getCell(e.position));
        }
        else {
            this.TopButtonArray[e.position].setVisible(true);
            this.BottomButtonArray[e.position].setTextureFromValue(0);
        }

    }



}
