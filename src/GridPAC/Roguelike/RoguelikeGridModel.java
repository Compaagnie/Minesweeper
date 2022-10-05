package GridPAC.Roguelike;

import GridPAC.CellChangeEvent;
import GridPAC.CellContent;
import GridPAC.GridModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class RoguelikeGridModel extends GridModel
{
    public RoguelikeGridModel(Dimension _dimension, int _bombCount, Consumer<CellChangeEvent> cellChangeListener)
    {
        super(_dimension, _bombCount, cellChangeListener);
    }
    public void resolveCell(int position) // Reveals the cell or put a flag on it depending on whether a bomb is there or not
    {
        if(getCell(position) != CellContent.BOMB) revealCell(position);
        else if (!hasFlag(position)) onCellChange.accept(new CellChangeEvent(this, position, "flag"));
    }

    public void radarReveal(int position)
    {
        resolveCell(position);
        for (int neigh : getNeighbours(position)) resolveCell(neigh);
        System.out.println("Used radar");
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
            resolveCell(bombArray.get((new Random()).nextInt(bombArray.size())));
            System.out.println("Used bomb reveal");
            return true;
        }
    }
}
