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

    protected int timerSeconds = 0;

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

        CreateFlagDisplay();

        CreateTimerDisplay();

        gameInfoPanel.add(Box.createVerticalGlue());

        CreateBackButton();

        this.setupRestartButton(gameInfoPanel);
    }

    protected void setupGrid(int width, int height, int bombCount)
    {
        this.grid = new Grid(new Dimension(width,height), bombCount);
        this.grid.addEventListener(this::gridEventHandler);
        this.grid.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        this.add(grid, BorderLayout.CENTER);
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
            case "reveal" : if(!grid.isOver()) gameTimer.start(); break;
            case "restart" : onRestart(); break;
            case "over" : gameTimer.stop(); break;

            default : System.out.println("[WARNING] Grid event not handled by view : " + event.command);
        }
        repaint();
    }

    protected void onRestart()
    {
        gameTimer.restart();
        gameTimer.stop();
        updateFlagNb();
    }

    protected void CreateBackButton()
    {
        JButton backButton = new JButton("Menu");
        gameInfoPanel.add(backButton);
        backButton.addActionListener(e -> openMenu());
    }

    protected void CreateTimerDisplay()
    {
        JLabel timeSpentLabel = new JLabel("Time: 00:00:00");
        gameInfoPanel.add(timeSpentLabel);

        ActionListener timerAction = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timerSeconds++;
                timeSpentLabel.setText("Time: "+ String.format("%02d:%02d:%02d", timerSeconds / 360, timerSeconds / 60, timerSeconds % 60));
            }
        };

        gameTimer = new Timer(1000, timerAction)
        {
            @Override
            public void restart()
            {
                super.restart();
                timerSeconds = 0;
                timeSpentLabel.setText("Time: 00:00:00");
                timeSpentLabel.repaint();
            }
        };
        gameTimer.stop();
    }

    protected void CreateFlagDisplay()
    {
        flagFoundLabel = new JLabel("Flag: 0/" + grid.getBombCount());
        gameInfoPanel.add(flagFoundLabel);
    }

    protected void setupRestartButton(JPanel parent)
    {
        JButton restartButton = new JButton("Restart Game");
        parent.add(restartButton);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }
}
