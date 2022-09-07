package Buttons;


import javax.swing.*;
import java.awt.*;
import PAC.*;


public class MineButton extends JButton
{
    public int position;
    protected Grid grid;
    MineButton(int position, Grid g){
        super();
        this.position = position;
        this.grid = g;
        this.setMaximumSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(50,50));
        //this.setText(position.toString());
    }

    public void revealCell(){
        if (!grid.gridGenerated){
            grid.gridCreation(99, this.position);
            grid.gridGenerated = true;
            for (int i = 0; i < grid.CellArray.length; i++)
            {
                grid.BottomButtonArray[i].setText(Integer.toString(grid.CellArray[i]));
            }
        }

        grid.propagateReveal(position);
    }


}
