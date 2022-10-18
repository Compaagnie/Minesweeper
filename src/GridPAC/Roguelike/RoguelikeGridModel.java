package GridPAC.Roguelike;

import GridPAC.CellChangeEvent;
import GridPAC.CellContent;
import GridPAC.GridModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoguelikeGridModel extends GridModel
{
    Supplier<Boolean> hasRevive;
    private final int BOMB_REVEAL_COUNT = 3;

    public RoguelikeGridModel(Dimension _dimension, int _bombCount, Consumer<CellChangeEvent> cellChangeListener, Supplier<Boolean> _hasRevive)
    {
        super(_dimension, _bombCount, cellChangeListener);
        this.hasRevive = _hasRevive;
    }

    /** Reveals the cell or put a flag on it depending on whether a bomb is there or not */
    public void resolveCell(int position)
    {
        if(getCell(position) != CellContent.BOMB) revealCell(position);
        else if (!hasFlag(position)) onCellChange.accept(new CellChangeEvent(this, position, "flag"));
    }

    public void radarReveal(int position)
    {
        resolveCell(position);
        for (int neigh : getNeighbours(position)) resolveCell(neigh);
    }

    public void columnReveal(int position)
    {
        int x = position % dimension.width;
        for(int y = 0; y < dimension.height; ++y) resolveCell(x + dimension.width * y);
    }

    public void lineReveal(int position)
    {
        int y = position / dimension.width;
        for(int x = 0; x < dimension.width; ++x) resolveCell(x + dimension.width * y);
    }

    public boolean bombReveal()
    {
        ArrayList<Integer> bombArray = new ArrayList<>();
        for(int i = 0; i < CellArray.length; ++i)
        {
            if(CellArray[i] == CellContent.BOMB && !hasFlag(i)) bombArray.add(i);
        }

        if(bombArray.size() == 0) return false;
        else
        {
            if(bombArray.size() < BOMB_REVEAL_COUNT)
            {
                for(Integer cell : bombArray) resolveCell(cell);
            }
            else
            {
                for(int i = 0; i < BOMB_REVEAL_COUNT; ++i)
                {
                    int toResolve;
                    do
                    {
                        toResolve = (new Random()).nextInt(bombArray.size());
                    } while (hasFlag(toResolve));
                    resolveCell(bombArray.get(toResolve));
                }
            }
            return true;
        }
    }

    @Override
    public void gameIsLost(int losingCell)
    {
        if(hasRevive.get())
        {
            onCellChange.accept(new CellChangeEvent(this, losingCell, "revive"));
        }
        else
        {
            for (int otherCell = 0; otherCell < getCellCount(); otherCell++)
            {
                if (otherCell == losingCell || ( hasFlag(otherCell) && getCell(otherCell) != CellContent.BOMB))
                {
                    onCellChange.accept(new CellChangeEvent(this, otherCell, "lost"));
                }
                else if (!hasFlag(otherCell))
                {
                    if (!CellRevealedArray.contains(otherCell)) {
                        CellRevealedArray.add(otherCell);
                    }
                    onCellChange.accept(new CellChangeEvent(this, otherCell, "reveal"));
                }
            }
            onCellChange.accept(new CellChangeEvent(this, -1, "lost"));
            gameOver = true;
        }
    }
}
