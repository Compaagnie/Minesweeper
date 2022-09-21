package GridPAC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class GridModel
{
    protected Dimension dimension;
    protected Boolean gridGenerated = false;
    protected Integer[] CellArray;
    protected Boolean[] CellRevealedArray;
    protected ArrayList<Integer> FlagArray;
    protected int bombCount;
    protected Consumer<CellChangeEvent> onCellChange;


    public GridModel(Dimension _dimension, int _bombCount, Consumer<CellChangeEvent> cellChangeListener)
    {
        this.onCellChange = cellChangeListener;
        this.dimension = _dimension;
        this.bombCount = _bombCount;

        CellArray = new Integer[_dimension.width * _dimension.height];
        CellRevealedArray = new Boolean[_dimension.width * _dimension.height];
        Arrays.fill(CellArray, 0);
        Arrays.fill(CellRevealedArray, false);
        FlagArray = new ArrayList<>();
    }

    public void revealCell(int position)
    {
        if (!isGenerated())
        {
            gridCreation(getBombCount(), position);
            setGridGenerated(true);
            propagateReveal(position);
        }
        if (getCell(position) == CellContent.BOMB)
        {
            gameIsLost(position);
        }
        else if (getCell(position) == CellContent.EMPTY)
        {
            propagateReveal(position);
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
            if(getCell(cell) != CellContent.BOMB && !excluded.contains(cell))
            {
                // Updating bomb count for neighbours
                setCell(cell, CellContent.BOMB);
                ArrayList<Integer> neighbours = this.getNeighbours(cell);
                for (Integer n : neighbours)
                {
                    if(getCell(n) != CellContent.BOMB) CellArray[n] += 1;
                }
                i++; // Updating bomb placed count
            }
        }

        onCellChange.accept(new CellChangeEvent(this, clickPosition, true));
    }

    public void propagateReveal(int cell)
    {
        if (!hasFlag(cell))
        {
            ArrayList<Integer> neighbours = this.getNeighbours(cell);
            this.removeTopButton(cell);

//            TopButtonArray[cell].setVisible(false); // TODO
            if (getCell(cell) == CellContent.EMPTY)
            {
                for (int n : neighbours)
                {
                    if (!CellRevealedArray[n]) propagateReveal(n);
                }
            }
            else if (getCell(cell) != CellContent.BOMB)
            {
                int nbOfFlagsAround = 0;
                for (int n : neighbours)
                {
                    if (hasFlag(n)) nbOfFlagsAround++;
                }
                if (nbOfFlagsAround == getCell(cell))
                {
                    for (int n : neighbours)
                    {
                        if (!CellRevealedArray[n]) propagateReveal(n);
                    }
                }
            } else
            {
                this.gameIsLost(cell);
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

    public void gameIsLost(int losingCell)
    {
        // set losing cell color to red
//        BottomButtonArray[losingCell].setIcon(redIcon); // TODO : this

        for (int otherCell = 0; otherCell < getCellCount(); otherCell++)
        {
            if (hasFlag(otherCell) || getCell(otherCell) != CellContent.BOMB)
            {
//                TopButtonArray[otherCell].setIcon(redIcon); // TODO : this

            }
            else if (!hasFlag(otherCell))
            {
//                TopButtonArray[otherCell].setVisible(false); // TODO : this
            }
        }
    }

    public void removeTopButton(int position)
    {
        CellRevealedArray[position] = true;
        onCellChange.accept(new CellChangeEvent(this, position, true));
    }

    public void restartGame()
    {
        for (int i = 0; i < CellArray.length; i++)
        {
//            TopButtonArray[i].setVisible(true); // TODO : this
            recoverCell(i);
            CellArray[i] = 0;
        }
        this.gridGenerated = false;
        FlagArray.clear();
    }

    public void recoverCell(int position)
    {
        CellRevealedArray[position] = false;
        onCellChange.accept(new CellChangeEvent(this, position, false));
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Boolean isGenerated() {
        return gridGenerated;
    }

    public void setGridGenerated(Boolean gridGenerated) {
        this.gridGenerated = gridGenerated;
    }

    public Integer getCell(int cell)
    {
        return CellArray[cell];
    }

    public void setCell(int position, int value) {
        CellArray[position] = value;
    }

    public int getCellCount()
    {
        return CellArray.length;
    }

    public ArrayList<Integer> getFlagArray()
    {
        return FlagArray;
    }

    public boolean hasFlag(Integer position)
    {
        return FlagArray.contains(position);
    }

    public void addFlag(int position)
    {
        FlagArray.add(position);
    }

    public void removeFlag(Integer position)
    {
        FlagArray.remove(position);
    }

    public int getBombCount() {
        return this.bombCount;
    }

    public void setBombCount(int newCount) {
        this.bombCount = newCount;
    }

}
