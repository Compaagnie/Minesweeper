package Buttons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import GridPAC.Grid;

import javax.imageio.ImageIO;

public class TopButton extends MineButton
{

    private static Image flag;
    private static Image empty;

    public TopButton(int position, Grid g)
    {
        super(position, g);

        TopButton thisButton = this;
        this.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (!grid.hasFlag(thisButton.position))
                    {
                        grid.revealCell(thisButton.position);
                        thisButton.setVisible(false);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    toggleFlag();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void toggleFlag(){
        if (grid.FlagArray.contains(this.position))
        {
            this.setTexture(empty);
            grid.removeFlag((Integer) this.position);
        }
        else
        {
            this.setTexture(flag);
            grid.addFlag(this.position);
        }

    }



}
