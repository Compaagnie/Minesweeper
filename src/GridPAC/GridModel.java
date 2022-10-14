package GridPAC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class GridModel
{
    protected final Dimension dimension;
    protected final int bombCount;
    protected Boolean gridGenerated = false;
    protected Boolean gameOver = false;

    private Boolean doFlagCheck = true;
    protected Integer[] CellArray;
    protected ArrayList<Integer> CellRevealedArray; // TODO : hashset ?
    protected ArrayList<Integer> FlagArray;
    protected Consumer<CellChangeEvent> onCellChange;


    public GridModel(Dimension _dimension, int _bombCount, Consumer<CellChangeEvent> cellChangeListener)
    {
        this.onCellChange = cellChangeListener;
        this.dimension = _dimension;
        this.bombCount = _bombCount;

        CellArray = new Integer[_dimension.width * _dimension.height];
        Arrays.fill(CellArray, 0);
        CellRevealedArray = new ArrayList<>();
        FlagArray = new ArrayList<>();
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
        onCellChange.accept(new CellChangeEvent(this, clickPosition, "reveal"));
    }

    public void revealCell(int position)
    {
        if (!isGenerated())
        {
            gridCreation(getBombCount(), position);
            setGridGenerated(true);
            propagateReveal(position);
        }
        else if (getCell(position) == CellContent.BOMB)
        {
            gameIsLost(position);
        }
        else if (getCell(position) == CellContent.EMPTY)
        {
            propagateReveal(position);
        }
        else
        {
            removeTopButton(position);
        }
    }

    public void propagateReveal(int cell)
    {
        if (!hasFlag(cell))
        {
            ArrayList<Integer> neighbours = this.getNeighbours(cell);
            this.removeTopButton(cell);

            if (getCell(cell) == CellContent.EMPTY)
            {
                for (int n : neighbours)
                {
                    if (!CellRevealedArray.contains(n)) propagateReveal(n);
                }
            }
            else if (getCell(cell) == CellContent.BOMB)
            {
                this.gameIsLost(cell);
            }
            else
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
                        if (!CellRevealedArray.contains(n)) propagateReveal(n);
                    }
                }
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

    public void gameWonRevealCheck(int position)
    {
        if (CellArray.length - CellRevealedArray.size() == bombCount)
        //if (CellRevealedArray.size() + FlagArray.size() == CellArray.length - 1)
        {
            /*
            for(int neigh : getNeighbours(position))
            {
                if(!CellRevealedArray.contains(neigh) && !FlagArray.contains(neigh)) {
                    String revealInstr;
                    System.out.println(neigh);
                    if (neigh == CellContent.BOMB)
                        revealInstr = "flag";
                    else
                        revealInstr = "reveal";
                    onCellChange.accept(new CellChangeEvent(this, neigh, revealInstr));
                }
            }
            gameOver = true;
            */
            doFlagCheck = false;
            for (int otherCell = 0; otherCell < getCellCount(); otherCell++)
            {
                if (!CellRevealedArray.contains(otherCell) && !FlagArray.contains(otherCell)) {
                    onCellChange.accept(new CellChangeEvent(this, otherCell, "flag"));
                }
            }
            onCellChange.accept(new CellChangeEvent(this, position, "win"));
            gameOver = true;
        }
    }

    public void gameWonFlagCheck()
    {
        if (doFlagCheck && CellRevealedArray.size() + FlagArray.size() == CellArray.length)
        {
            onCellChange.accept(new CellChangeEvent(this, -1, 1, true, true));
            gameOver = true;
        }
    }

    public void gameIsLost(int losingCell)
    {
        gameOver = true;
        doFlagCheck = false;
        for (int otherCell = 0; otherCell < getCellCount(); otherCell++)
        {
            if ( otherCell == losingCell || ( hasFlag(otherCell) && getCell(otherCell) != CellContent.BOMB))
            {
                onCellChange.accept(new CellChangeEvent(this, otherCell, 1));

//                TopButtonArray[otherCell].setIcon(redIcon); // TODO : this
            }
            else if (!hasFlag(otherCell))
            {
                if (!CellRevealedArray.contains(otherCell)) 
                {
                    CellRevealedArray.add(otherCell);
                }
                onCellChange.accept(new CellChangeEvent(this, otherCell, "reveal"));
            }
        }
        onCellChange.accept(new CellChangeEvent(this, -1, "lost"));
        gameOver = true;
    }

    public void removeTopButton(int position)
    {
        if (!CellRevealedArray.contains(position)) 
            CellRevealedArray.add(position);
        onCellChange.accept(new CellChangeEvent(this, position, "reveal"));
        gameWonRevealCheck(position);
    }

    public void restartGame()
    {
        FlagArray.clear();
        CellRevealedArray.clear();
        setGridGenerated(false);
        gameOver = false;
        doFlagCheck = true;
        for (int i = 0; i < CellArray.length; i++)
        {
            recoverCell(i);
            CellArray[i] = 0;
        }
    }

    public void recoverCell(int position)
    {
        onCellChange.accept(new CellChangeEvent(this, position, "reset"));
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Boolean isGenerated() {
        return gridGenerated;
    }

    public Boolean isOver() {return gameOver;}

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
        gameWonFlagCheck();
    }

    public void removeFlag(Integer position)
    {
        FlagArray.remove(position);
    }

    public int getBombCount() {
        return this.bombCount;
    }

}
