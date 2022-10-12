package PAC;

import GridPAC.Grid;
import GridPAC.GridEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameView extends JPanel
{
    protected Minesweeper minesweeper;
    protected Grid grid;

    public Timer gameTimer;
    protected JLabel flagFoundLabel;
    protected JPanel gameInfoPanel;

    protected JScrollPane gridScrollPane;

    public GameView(){super();}

    public GameView(Minesweeper minesweeper, int width, int height, int bombCount)
    {
        super();
        this.minesweeper = minesweeper;
        this.setLayout(new BorderLayout());

        this.minesweeper.add(this);

        this.setupGrid(width, height, bombCount);


        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.PAGE_AXIS));
        this.add(gameInfoPanel, BorderLayout.EAST);

        flagFoundLabel = new JLabel("Flag: 0/" + grid.getBombCount());
        gameInfoPanel.add(flagFoundLabel);

        JLabel timeSpentLabel = new JLabel("Time: 00:00:00");
        gameInfoPanel.add(timeSpentLabel);
        
        ActionListener timerAction = new ActionListener()
        {
            int seconds = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                timeSpentLabel.setText("Time: "+ String.format("%02d:%02d:%02d", seconds / 360, seconds / 60, seconds % 60));
            }
        };

        gameTimer = new Timer(1000, timerAction);
        gameTimer.stop();

        gameInfoPanel.add(Box.createVerticalGlue());

        JButton backButton = new JButton("Menu");
        gameInfoPanel.add(backButton);
        backButton.addActionListener(e -> openMenu());

        this.setupRestartButton(gameInfoPanel);
    }

    protected void setupRestartButton(JPanel parent)
    {
        JButton restartButton = new JButton("Restart Game");
        parent.add(restartButton);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    protected void setupGrid(int width, int height, int bombCount)
    {
        this.grid = new Grid(new Dimension(width,height), bombCount);
        this.grid.addEventListener(this::gridEventHandler);
        gridScrollPane = new JScrollPane(grid);

        gridScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(gridScrollPane, BorderLayout.CENTER);
    }

    public void openMenu()
    {
        this.minesweeper.openMenu();
    }

    public void updateFlagNb()
    {
        this.flagFoundLabel.setText("Flags: " + grid.getFlagNumber()+"/"+ grid.getBombCount());
    }

    protected void gridEventHandler(GridEvent event)
    {
        switch (event.command)
        {
            case "flag" : updateFlagNb(); break;
            case "reveal" : gameTimer.start(); break;
            case "restart" : onRestart(); break;
            case "over" : gameTimer.stop();

            default : System.out.println("[WARNING] Grid event not handled by view : " + event.command);
        }
    }

    protected void onRestart()
    {
        gameTimer.restart();
        gameTimer.stop();
        updateFlagNb();
    }

}
