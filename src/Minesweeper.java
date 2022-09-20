import PAC.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Minesweeper extends JFrame
{
    Grid grid;
    Minesweeper()
    {
        super("Minesweeper");

        createMenu();

        this.setPreferredSize(new Dimension(900,600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);
    }

    private void createMenu()
    {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2,2));
        this.add(menuPanel);

        JButton presetButton1 = new JButton("8x8 : 10 bombs");
        presetButton1.addActionListener( e -> { menuPanel.setVisible(false); startGame(8,8,10);});
        JButton presetButton2 = new JButton("16x16 : 40 bombs");
        presetButton2.addActionListener(e -> { menuPanel.setVisible(false); startGame(16,16,40);});
        JButton presetButton3 = new JButton("30x16 : 99 bombs");
        presetButton3.addActionListener(e -> { menuPanel.setVisible(false); startGame(30,16,99); });

        JButton presetButtonCustom = new JButton("? : Custom");
        presetButtonCustom.addActionListener(e -> { menuPanel.setVisible(false); createCustomPresetMenu(); });

        menuPanel.add(presetButton1);
        menuPanel.add(presetButton2);
        menuPanel.add(presetButton3);
        menuPanel.add(presetButtonCustom);
    }

    private void createCustomPresetMenu()
    {
        JPanel selectionPanel = new JPanel();
        this.add(selectionPanel);
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));

        JPanel dimensionSubPanel = new JPanel();
        dimensionSubPanel.setPreferredSize(new Dimension(300, 40));
        dimensionSubPanel.setLayout(new BoxLayout(dimensionSubPanel, BoxLayout.LINE_AXIS));
        JLabel sizeLabel = new JLabel("Size:");
        JTextField widthField = new JTextField();
        JLabel xLabel = new JLabel("x");
        JTextField heightField = new JTextField();
        dimensionSubPanel.add(sizeLabel);
        dimensionSubPanel.add(widthField);
        dimensionSubPanel.add(xLabel);
        dimensionSubPanel.add(heightField);

        selectionPanel.add(dimensionSubPanel);

        JPanel bombSubPanel = new JPanel();
        bombSubPanel.setPreferredSize(new Dimension(300, 40));
        bombSubPanel.setLayout(new BoxLayout(bombSubPanel, BoxLayout.LINE_AXIS));
        JLabel bombLabel = new JLabel("Bombs :");
        JTextField bombTextField = new JTextField();
        bombSubPanel.add(bombLabel);
        bombSubPanel.add(bombTextField);

        selectionPanel.add(bombSubPanel);

        JButton confirmButton = new JButton("Start");
        confirmButton.addActionListener(e ->
            {
                try
                {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    int bombCount = Integer.parseInt(bombTextField.getText());

                    // Preventing most crashes (infinite bomb positioning loop)
                    // TODO : true check with exception
                    if(bombCount < width * height)
                    {
                        startGame(width, height, bombCount);
                        selectionPanel.setVisible(false);
                    }
                }
                catch (NumberFormatException exception){} // Do nothing
            });
        selectionPanel.add(confirmButton);

//        revalidate();
//        repaint();
    }


    private void startGame(int width, int height, int bombCount)
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