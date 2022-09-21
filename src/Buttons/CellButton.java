package Buttons;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import GridPAC.CellContent;
import GridPAC.Grid;


public class CellButton extends JButton
{
    private static final int sideLength = 48;
    private static final int minSideLength = 16;
    private static final int maxSideLength = 64;
    private Image texture;
    public int position;
    protected Grid grid;

    protected MouseListener hiddenMouseListener;
    protected MouseListener revealedMouseListener;

    public CellButton(int position, Grid g)
    {
        super();
        this.position = position;
        this.grid = g;

        //this.setMaximumSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(minSideLength, minSideLength));
        this.setMaximumSize(new Dimension(maxSideLength, maxSideLength));
        this.setPreferredSize(new Dimension(sideLength, sideLength));

        this.addActionListener(e -> onClickHidden());
        this.setTexture(ButtonTextures.getTexture(CellContent.EMPTY));
        this.setIconTextGap(0);

        CellButton thisButton = this;

        hiddenMouseListener = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (!grid.hasFlag(thisButton.position))
                    {
                        grid.revealCell(thisButton.position);
                    }
                }
                else if (e.getButton() == MouseEvent.BUTTON3)
                {
                    thisButton.toggleFlag();
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
        };

        revealedMouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1) onClickRevealed();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
        this.addMouseListener(hiddenMouseListener);
    }

    public void setTexture(Image texture)
    {
        this.texture = texture;
        this.repaint();
    }

    public void setTextureFromValue(int value)
    {
        if (value == CellContent.BOMB) this.setTexture(ButtonTextures.textures[9]);
        else this.setTexture(ButtonTextures.textures[value]);
    }

    @Override
    public void paintComponent(Graphics pen)
    {
        super.paintComponent(pen);
        pen.drawImage(texture, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(this.getHeight(), this.getHeight());
    }

    private void onClickHidden()
    {
        grid.revealCell(this.position);
    }

    private void onClickRevealed()
    {
        System.out.println("reveal");
        grid.propagateReveal(this.position);
    }

    public void revealButton()
    {
        this.setTextureFromValue(grid.getCell(this.position));
        this.removeListeners();
        this.addMouseListener(revealedMouseListener);
    }

    public void resetButton()
    {
        this.setTexture(ButtonTextures.textures[CellContent.EMPTY]);
        this.removeListeners();
        this.addMouseListener(hiddenMouseListener);
    }

    public void removeListeners()
    {
        for (MouseListener listener: this.getMouseListeners()) this.removeMouseListener(listener);
    }

    public void toggleFlag()
    {
        if(!grid.isOver())
        {
            if (grid.hasFlag(this.position))
            {
                this.setTexture(ButtonTextures.textures[CellContent.EMPTY]);
                grid.removeFlag(this.position);
            }
            else
            {
                this.setTexture(ButtonTextures.getFlagTexture());
                grid.addFlag(this.position);
            }
        }
    }
}
