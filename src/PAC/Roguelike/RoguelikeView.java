package PAC.Roguelike;

import GridPAC.GridEvent;
import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import PAC.GameView;
import PAC.Minesweeper;
import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.PowerUps.PowerUp;
import PAC.Roguelike.PowerUps.PowerUpComponent;
import Shop.ShopButton;

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



    public final static int COIN_IMAGE_SIZE = 16;
    public final static int POWERUP_IMAGE_SIZE = 64;

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

        JPanel globalInfoPanel = new JPanel();
        globalInfoPanel.setLayout(new BoxLayout(globalInfoPanel, BoxLayout.LINE_AXIS));
        this.add(globalInfoPanel, BorderLayout.EAST);
        globalInfoPanel.add(bombFoundSlider);
        globalInfoPanel.add(revealedSlider);
        bombFoundSlider.setFillColor(Color.red);
        bombFoundSlider.setMinimum(0);
        revealedSlider.setFillColor(Color.green);
        revealedSlider.setMinimum(0);

        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);        globalInfoPanel.add(gameInfoPanel);

        this.CreateFlagDisplay(constraints);
        constraints.gridy++;
        super.CreateTimerDisplay(constraints);
        constraints.gridy++;

        levelLabel = new JLabel("Level : 0");
        gameInfoPanel.add(levelLabel, constraints);
        constraints.gridy++;

        currencyLabel = new JLabel(" 0");
        gameInfoPanel.add(currencyLabel, constraints);
        constraints.gridy++;

        currencyLabel.setIcon(new ImageIcon(ShopButton.coinImage.getScaledInstance(COIN_IMAGE_SIZE, COIN_IMAGE_SIZE, Image.SCALE_SMOOTH)));
//        currencyLabel.setHorizontalTextPosition(JLabel.EAST);
//        currencyLabel.setVerticalTextPosition(JLabel.CENTER);

        energyLabel = new JLabel("Energy: 0/0");
        gameInfoPanel.add(energyLabel, constraints);
        constraints.gridy++;

        JPanel powerUpPanel = new JPanel();
        powerUpPanel.setLayout(new BoxLayout(powerUpPanel, BoxLayout.PAGE_AXIS));
        gameInfoPanel.add(powerUpPanel, constraints);
        constraints.gridy++;
        powerUpPanel.setOpaque(false);

        activePowerUpPanel = new JPanel();
        activePowerUpPanel.setLayout(new BoxLayout(activePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(activePowerUpPanel, constraints);
        constraints.gridy++;
        activePowerUpPanel.setOpaque(false);
        activePowerUpPanel.add(new JLabel("Active:"));

        passivePowerUpPanel = new JPanel();
        passivePowerUpPanel.setLayout(new BoxLayout(passivePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(passivePowerUpPanel, constraints);
        constraints.gridy++;
        passivePowerUpPanel.setOpaque(false);
        passivePowerUpPanel.add(new JLabel("Passive:"));

        constraints.weighty = 1;
        gameInfoPanel.add(Box.createVerticalGlue(), constraints);
        constraints.weighty = 0;
        constraints.gridy++;

        super.CreateBackButton(constraints);
        constraints.gridy++;

        this.setupRestartButton(constraints);
        constraints.gridy++;

        setPowerUpKeys();

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void setupRestartButton(GridBagConstraints constraints)
    {
        JButton restartButton = new JButton("Restart Game");
        gameInfoPanel.add(restartButton);

        restartButton.addActionListener(e -> { this.onRestart(); controller.onRestart(); } );
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    @Override
    protected void CreateFlagDisplay(GridBagConstraints constraints)
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
                    super.keyTyped(e);
                    if(grid != null && grid.isGenerated())
                    {
                        if(Character.isDigit(e.getKeyChar()))
                        {
                            if(e.getKeyChar() == '0') controller.executePowerUp(9);
                            else controller.executePowerUp(e.getKeyChar() - '1');
                        }
                        else
                        {
                            char[] charAsAZERTY = new char[]{'&', 'é', '\"', '\'', '(', '-', 'è', '_', 'ç', 'à'};
                            for(int i = 0; i < charAsAZERTY.length; ++i)
                            {
                                if(charAsAZERTY[i] == e.getKeyChar()) controller.executePowerUp(i);
                            }
                        }
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
        this.currencyLabel.setText(" " + controller.getCurrencyCount());
        this.levelLabel.setText("Level: " + controller.getCurrentLevel());
        this.energyLabel.setText("Energy: " + controller.getCurrentEnergy() + "/" + controller.getMaxEnergy());
        this.bombFoundSlider.setMaximum(grid.getBombCount());
        this.bombFoundSlider.setValue(grid.getFlagNumber());
        this.revealedSlider.setMaximum(grid.getCellCount() - grid.getBombCount());
        this.revealedSlider.setValue(grid.getRevealedCount());
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
            case "flag" : update(); break;
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
                    update();
                }
            } break;

            case "over" :
            {
                gameTimer.stop();
                flagFoundLabel.setVisible(false);
                bombFoundSlider.setValue(0);
                revealedSlider.setValue(0);
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
            if(!powerUpChange.isActive()) panel.add(new PowerUpComponent(powerUpChange, POWERUP_IMAGE_SIZE));
            else panel.add(new PowerUpComponent(powerUpChange, ((ActivePowerUp) powerUpChange).getShortcut(), POWERUP_IMAGE_SIZE));
        }
        repaint();
        revalidate();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        activePowerUpPanel.removeAll();
        activePowerUpPanel.add(new JLabel("Active:"));
        passivePowerUpPanel.removeAll();
        passivePowerUpPanel.add(new JLabel("Passive:"));
    }
}
