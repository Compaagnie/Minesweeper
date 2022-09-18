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
        //this.setMaximumSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(64,64));
        this.setPreferredSize(new Dimension(64,64));
        this.setIconTextGap(0);
    }
}
