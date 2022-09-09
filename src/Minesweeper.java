import PAC.Grid;

import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame
{
    Grid grid;
    static JTextField bombNumberPrompt = new JTextField("99", 10);
    Minesweeper()
    {
        super("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(900,600));

        setUpGrid();

        JButton restartButton = new JButton("Restart Game");
        this.add(restartButton, BorderLayout.SOUTH);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        pack();
        this.setVisible(true);
    }

    private void setUpGrid() {
        grid = new Grid(new Dimension(30,16));
        this.add(grid);
        this.add(bombNumberPrompt, BorderLayout.NORTH);
    }
}
