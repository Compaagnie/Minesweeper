package PAC;

import GridPAC.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Minesweeper extends JFrame
{
    Grid grid;

    GameMenu gameMenu;
    public Minesweeper()
    {
        super("PAC.Minesweeper");

        this.setPreferredSize(new Dimension(900,600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameMenu = new GameMenu(this);
        pack();
        this.setVisible(true);
    }




    public void startGame(int width, int height, int bombCount)
    {
        setUpGrid(width, height, bombCount);

        JButton restartButton = new JButton("Restart Game");
        this.add(restartButton, BorderLayout.SOUTH);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);

//        revalidate();
//        repaint();
    }


    private void setUpGrid(int width, int height, int bombCount)
    {
        grid = new Grid(new Dimension(width,height), bombCount);
        this.add(grid);
    }
}