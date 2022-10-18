package CustomComponents.Buttons;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import GridPAC.CellContent;
import GridPAC.Grid;


public class CellButton extends JButton
{
    private static final int sideLength = 16;
    private static final int minSideLength = 16;
    private static final int maxSideLength = 16;
    private Image texture;
    public final int position;
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
        this.setTexture(ButtonTextures.getTopTexture(CellContent.EMPTY));
        this.setIconTextGap(0);

        CellButton thisButton = this;

        hiddenMouseListener = new MouseAdapter(){
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
        };

        revealedMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1) onClickRevealed();
            }
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
        if (value == CellContent.BOMB) this.setTexture(ButtonTextures.getBottomTexture(9));
        else this.setTexture(ButtonTextures.getBottomTexture(value));
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
        int size = Math.min(this.getHeight(), this.getWidth());
        return new Dimension(size, size);
    }

    private void onClickHidden()
    {
        grid.revealCell(this.position);
    }

    private void onClickRevealed()
    {
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
        this.setTexture(ButtonTextures.getTopTexture(CellContent.EMPTY));
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
                this.setTexture(ButtonTextures.getTopTexture(CellContent.EMPTY));
                grid.removeFlag(this.position);
            }
            else if (grid.getFlagNumber() < grid.getBombCount())
            {
                this.setTexture(ButtonTextures.getTopTexture(1));
                grid.addFlag(this.position);
            }
        }
    }
}
