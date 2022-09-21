package Buttons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import GridPAC.Grid;

import javax.imageio.ImageIO;

public class TopButton extends MineButton
{

    private static Image flag;
    private static Image empty;

    public TopButton(int position, Grid g)
    {
        super(position, g);

        try
        {
            empty = ImageIO.read(new File("textures/0.png"));
            flag = ImageIO.read(new File("textures/flag.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setTexture(empty);
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
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void toggleFlag()
    {
        if (grid.hasFlag(this.position))
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
