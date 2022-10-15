package PAC.Roguelike;

import GridPAC.GridEvent;
import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import PAC.GameView;
import PAC.Minesweeper;
import PAC.Roguelike.PowerUps.PowerUp;
import PAC.Roguelike.PowerUps.PowerUpComponent;

public class RoguelikeView extends GameView
{
    protected JLabel levelLabel;
    protected JLabel currencyLabel;
    protected JLabel energyLabel;
    protected JPanel passivePowerUpPanel;
    protected JPanel activePowerUpPanel;
    protected JComponent centerComponent;
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

        this.CreateFlagDisplay();
        super.CreateTimerDisplay();

        levelLabel = new JLabel("Level : 0");
        gameInfoPanel.add(levelLabel);

        currencyLabel = new JLabel("Coins : 0");
        gameInfoPanel.add(currencyLabel);

        energyLabel = new JLabel("Energy: 0/0");
        gameInfoPanel.add(energyLabel);

        JPanel powerUpPanel = new JPanel();
        gameInfoPanel.add(powerUpPanel);
        powerUpPanel.setLayout(new GridLayout(1,2));
        powerUpPanel.setOpaque(false);

        activePowerUpPanel = new JPanel();
        activePowerUpPanel.setLayout(new BoxLayout(activePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(activePowerUpPanel);
        activePowerUpPanel.setOpaque(false);
        activePowerUpPanel.add(new JLabel("Active:"));

        passivePowerUpPanel = new JPanel();
        passivePowerUpPanel.setLayout(new BoxLayout(passivePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(passivePowerUpPanel);
        passivePowerUpPanel.setOpaque(false);
        passivePowerUpPanel.add(new JLabel("Passive:"));

        gameInfoPanel.add(Box.createVerticalGlue());

        super.CreateBackButton();

        this.setupRestartButton();

        setPowerUpKeys();

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void setupRestartButton()
    {
        JButton restartButton = new JButton("Restart Game");
        gameInfoPanel.add(restartButton);

        restartButton.addActionListener(e -> controller.onRestart());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    @Override
    protected void CreateFlagDisplay()
    {
        flagFoundLabel = new JLabel("Flag: 0/" + controller.getBombCount());
        gameInfoPanel.add(flagFoundLabel);
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
            this.remove(centerComponent);
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
        this.centerComponent.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        this.add(centerComponent, BorderLayout.CENTER);
        this.centerComponent.setVisible(true);
        repaint();
    }

    @Override
    protected void gridEventHandler(GridEvent event)
    {
//        System.out.println("Grid event : " + event.command);
        switch (event.command)
        {
            case "flag" : updateFlagNb(); break;
            case "restart" :
            {
                this.onRestart();
                controller.onRestart();
            } break;

            case "reveal" :
            {
                if(!grid.isOver())
                {
                    gameTimer.start();
                    flagFoundLabel.setVisible(true);
                }
            } break;

            case "over" :
            {
                gameTimer.stop();
                flagFoundLabel.setVisible(false);
            } break;

            default : System.out.println("[WARNING] Grid event not handled by view : " + event.command);
        }
        repaint();
    }

    public void handlePowerUpUpdate(PowerUp powerUpChange, boolean addedPowerUp)
    {
        JPanel panel = (powerUpChange.isActive() ? activePowerUpPanel : passivePowerUpPanel);
        if(!addedPowerUp)
        {
            for(Component c : panel.getComponents())
            {
                try
                {
                    if(((PowerUpComponent) c).powerUp == powerUpChange)
                    {
                        panel.remove(c);
                        break;
                    }
                }
                catch (ClassCastException ignore) {}
            }
        }
        else
        {
            panel.add(Box.createRigidArea(new Dimension(0,0)));
            panel.add(new PowerUpComponent(powerUpChange));
        }
        repaint();
        revalidate();
    }
}
