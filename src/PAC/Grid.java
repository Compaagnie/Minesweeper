package PAC;

import Buttons.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Grid extends JPanel
{
    public Dimension dimension;
    public Boolean gridGenerated = false;
    public Integer[] CellArray;
    public ArrayList<Integer> FlagArray;
    public TopButton[] TopButtonArray;
    public BottomButton[] BottomButtonArray;

    public Grid(Dimension _dimension)
    {
        dimension = _dimension;
        this.setLayout(new GridBagLayout());

        CellArray = new Integer[_dimension.width * _dimension.height];
        Arrays.fill(CellArray, 0);
        FlagArray = new ArrayList<>();
        TopButtonArray = new TopButton[_dimension.width * _dimension.height];
        BottomButtonArray = new BottomButton[_dimension.width * _dimension.height];

        buttonCreation();

    }

    private void buttonCreation()
    {
        GridBagConstraints buttonPlacementConstraint = new GridBagConstraints();
        for(int y = 0; y < dimension.height; y++){
            buttonPlacementConstraint.gridy = y;
            for(int x = 0; x < dimension.width; x++){
                buttonPlacementConstraint.gridx = x;

                TopButton topButton = new TopButton(x + y * dimension.width, this);
                TopButtonArray[x + y * dimension.width] = topButton;
                this.add(topButton, buttonPlacementConstraint);

                BottomButton bottomButton = new BottomButton(x + y * dimension.width, this);
                BottomButtonArray[x + y * dimension.width] = bottomButton;
                this.add(bottomButton, buttonPlacementConstraint);
            }
        }
    }

    public void gridCreation(int bombNumber, int clickPosition)
    {
        Random r = new Random();
        ArrayList<Integer> excluded = this.getNeighbours(clickPosition);
        excluded.add(clickPosition);
        int cell;
        for(int i = 0; i < bombNumber;){
            // selecting a random cell in the grid
            cell = r.nextInt(this.dimension.height * this.dimension.width);
            if(CellArray[cell] != CellContent.BOMB && !excluded.contains(cell))
            {
                // Updating bomb count for neighbours
                CellArray[cell] = CellContent.BOMB;
                ArrayList<Integer> neighbours = this.getNeighbours(cell);
                for (Integer n : neighbours)
                {
                    if(CellArray[n] != CellContent.BOMB) CellArray[n] += 1;
                }
                i++; // Updating bomb placed count
            }
        }
    }

    public void propagateReveal(int cell)
    {
        if (!FlagArray.contains(cell))
        {
            ArrayList<Integer> neighbours = this.getNeighbours(cell);
            TopButtonArray[cell].setVisible(false);
            if (CellArray[cell] == CellContent.EMPTY)
            {
                for (int n : neighbours)
                {
                    if (TopButtonArray[n].isVisible()) propagateReveal(n);
                }
            } else if (CellArray[cell] != CellContent.BOMB) {
                int nbOfFlagsArround = 0;
                for (int n : neighbours)
                {
                    if (FlagArray.contains(n)) nbOfFlagsArround++;
                }
                if (nbOfFlagsArround == CellArray[cell])
                {
                    for (int n : neighbours)
                    {
                        if (TopButtonArray[n].isVisible()) propagateReveal(n);
                    }
                }
            } else {
                this.gameIsLost(cell);
            }
        }
    }

    public void gameIsLost(int losingCell){
        // set losing cell color to red
        for (int otherCell = 0; otherCell < CellArray.length; otherCell++)
        {
            if (FlagArray.contains(otherCell) && CellArray[otherCell] != CellContent.BOMB)
            {
                //set color to red
            }
            else if (!FlagArray.contains(otherCell))
            {
                TopButtonArray[otherCell].setVisible(false);
            }
        }
    }

    public ArrayList<Integer> getNeighbours(int cell)
    {
        ArrayList<Integer> neighbours = new ArrayList<>();
        int x = cell % dimension.width;
        int y = cell / dimension.width;

        // Vertical
        if (y > 0) neighbours.add(cell - dimension.width);
        if (y < dimension.height - 1) neighbours.add(cell + dimension.width);

        if(x > 0)
        {
            neighbours.add(cell -1); // Horizontal
            // Diagonal
            if(y > 0) neighbours.add(cell - 1 - dimension.width);
            if(y < dimension.height - 1) neighbours.add(cell - 1 + dimension.width);
        }
        if(x < dimension.width - 1)
        {
            neighbours.add(cell + 1); // Horizontal
            // Diagonal
            if(y > 0) neighbours.add(cell + 1 - dimension.width);
            if(y < dimension.height - 1) neighbours.add(cell + 1 + dimension.width);
        }

        return neighbours;
    }
}
