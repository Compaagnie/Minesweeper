package Buttons;


import javax.swing.*;
import java.awt.*;

import GridPAC.Grid;


public class MineButton extends JButton
{
    private static final int sideLength = 48;
    private static final int minSideLength = 16;
    private static final int maxSideLength = 64;

//    private Image tempImg;
    private Image texture;
    public int position;
    protected Grid grid;

    MineButton(int position, Grid g)
    {
        super();
        this.position = position;
        this.grid = g;
        //this.setMaximumSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(minSideLength, minSideLength));
        this.setMaximumSize(new Dimension(maxSideLength, maxSideLength));
        this.setPreferredSize(new Dimension(sideLength, sideLength));

        this.setIconTextGap(0);

        //this.addComponentListener(new ComponentListener()
//        {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                onResize();
//            }
//
//            @Override
//            public void componentMoved(ComponentEvent e) {}
//
//            @Override
//            public void componentShown(ComponentEvent e) {}
//
//            @Override
//            public void componentHidden(ComponentEvent e) {}
//        });
    }

//    public void onResize()
//    {
//        Icon myIcon = this.getIcon();
//        if (myIcon != null)
//        {
//            tempImg = ((ImageIcon) myIcon).getImage();
//            tempImg = tempImg.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
//            this.setIcon(new ImageIcon(tempImg));
//        }
//    }

    public void setTexture(Image texture)
    {
        this.texture = texture;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics pen)
    {
        super.paintComponent(pen);
        pen.drawImage(texture, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
