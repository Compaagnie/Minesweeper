package PAC.Roguelike;

import GridPAC.GridEvent;
import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import PAC.GameView;
import PAC.Minesweeper;

public class RoguelikeView extends GameView
{
    protected JLabel levelLabel;
    protected JLabel currencyLabel;
    protected JLabel debugPassivePowerUps;
    protected JLabel energyLabel;
    protected Component centerComponent;
    protected RogueLikeController controller;
    protected JScrollPane centerScrollPane = new JScrollPane();

    public RoguelikeView(RogueLikeController _controller, Minesweeper minesweeper)
    {
        // Created grid, info panel and buttons
        super();
        this.minesweeper = minesweeper;
        this.controller = _controller;
    }

    public void init()
    {
        this.setLayout(new BorderLayout());

        this.add(centerScrollPane, BorderLayout.CENTER);
        this.centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.centerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.PAGE_AXIS));
        this.add(gameInfoPanel, BorderLayout.EAST);

        flagFoundLabel = new JLabel("Flag: 0/" + controller.getBombCount());
        gameInfoPanel.add(flagFoundLabel);

        JLabel timeSpentLabel = new JLabel("Time: 00:00:00");
        gameInfoPanel.add(timeSpentLabel);


        ActionListener timerAction = new ActionListener()
        {
            int seconds = 0;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                seconds++;
                timeSpentLabel.setText("Time: "+ String.format("%02d:%02d:%02d", seconds / 360, seconds / 60, seconds % 60));
            }
        };

        gameTimer = new Timer(1000, timerAction);
        gameTimer.stop();

        levelLabel = new JLabel("Level : 0");
        gameInfoPanel.add(levelLabel);

        currencyLabel = new JLabel("Coins : 0");
        gameInfoPanel.add(currencyLabel);

        energyLabel = new JLabel("Energy: 0/0");
        gameInfoPanel.add(energyLabel);

        gameInfoPanel.add(Box.createVerticalGlue());

        JButton backButton = new JButton("Menu");
        gameInfoPanel.add(backButton);
        backButton.addActionListener(e -> openMenu());

        this.setupRestartButton(gameInfoPanel);

        setPowerUpKeys();

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void setPowerUpKeys()
    {
        RoguelikeView thisView = this;
        this.addKeyListener(
            new KeyAdapter()
            {
                @Override
                public void keyTyped(KeyEvent e)
                {
//                    System.out.println("Typed: " + e.getKeyChar());
                    if(grid != null && grid.isGenerated() && Character.isDigit(e.getKeyChar()))
                    {
                        if(e.getKeyChar() == '0') controller.executePowerUp(9);
                        else controller.executePowerUp(e.getKeyChar() - '1');
                    }
                }
            }
        );
        this.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                thisView.requestFocusInWindow();
            }
        });
    }

    public void update()
    {
        this.currencyLabel.setText("Coins: " + controller.getCurrencyCount());
        this.levelLabel.setText("Level: " + controller.getCurrentLevel());
        this.energyLabel.setText("Energy: " + controller.getCurrentEnergy() + "/" + controller.getMaxEnergy());
        this.updateFlagNb();
        repaint();
    }


    protected void removeCenterComponent()
    {
        if(centerComponent != null)
        {
            this.centerComponent.setVisible(false);
            this.centerScrollPane.getViewport().remove(centerComponent);
            centerComponent = null;
        }
    }

    public void setGrid(RoguelikeGrid _grid)
    {
        this.grid = _grid;
        grid.addEventListener(this::gridEventHandler);
    }

    public void setCenterComponent(JComponent newCenterComponent)
    {
        this.removeCenterComponent();
        this.centerComponent = newCenterComponent;
        this.centerScrollPane.getViewport().add(this.centerComponent);
        this.centerComponent.setVisible(true);
        repaint();
    }

    @Override
    protected void gridEventHandler(GridEvent event)
    {
        switch (event.command)
        {
            case "flag" : updateFlagNb(); break;
            case "restart" : onRestart(); break;

            case "reveal" :
            {
                gameTimer.start();
                flagFoundLabel.setVisible(true);
            } break;

            case "over" :
            {
                gameTimer.stop();
                flagFoundLabel.setVisible(false);
            } break;

            default : System.out.println("[WARNING] Grid event not handled by view : " + event.command);
        }
    }


}
