import PAC.Grid;

import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame
{
    static JTextField bombNumberPrompt = new JTextField("99", 10);
    Minesweeper()
    {
        super("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(900,600));

        Grid grid = new Grid(new Dimension(30,16));
        this.add(grid);
        this.add(bombNumberPrompt, BorderLayout.NORTH);
        pack();
        this.setVisible(true);
    }

}
