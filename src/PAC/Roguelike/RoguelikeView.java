package PAC.Roguelike;

import CustomComponents.Buttons.MenuButton;
import GridPAC.GridEvent;
import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import PAC.GameImages;
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

    public final static int COIN_IMAGE_SIZE = 32;
    public final static int POWERUP_IMAGE_SIZE = 64;

    public RoguelikeView(RogueLikeController _controller, Minesweeper _minesweeper)
    {
        // Created grid, info panel and buttons
        super();
        this.minesweeper = _minesweeper;
        this.controller = _controller;
    }

    public void init()
    {
        this.setLayout(new BorderLayout());

        labelFont = this.getFont().deriveFont(22.f);
        labelTextColor = Color.white;

        this.add(centerScrollPane, BorderLayout.CENTER);
        this.centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.centerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        globalInfoPanel.setLayout(new BoxLayout(globalInfoPanel, BoxLayout.LINE_AXIS));
        this.add(globalInfoPanel, BorderLayout.EAST);

        super.CreateGeneralPanelAndSliders();

        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        gameInfoPanel.setOpaque(false);

        globalInfoPanel.add(gameInfoPanel);

        this.CreateFlagDisplay(constraints);
        super.CreateTimerDisplay(constraints);

        this.CreateLevelLabel(constraints);
        this.CreateCurrencyLabel(constraints);
        this.CreateEnergyLabel(constraints);
        this.CreatePowerUpPane(constraints);
        constraints.weighty = 1;
        gameInfoPanel.add(Box.createVerticalGlue());
        constraints.weighty = 0;
        constraints.gridy++;

        super.CreateRecordButton(constraints);

        constraints.weighty = 1;
        gameInfoPanel.add(Box.createVerticalGlue(), constraints);
        constraints.weighty = 0;
        constraints.gridy++;

        super.CreateBackButton(constraints);
        this.setupRestartButton(constraints);
        this.setPowerUpKeys();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void CreateLevelLabel(GridBagConstraints constraints)
    {
        levelLabel = new JLabel("Level : 0");
        setCurrentSettingToLabel(levelLabel);
        gameInfoPanel.add(levelLabel, constraints);
        constraints.gridy++;
    }
    private void CreateCurrencyLabel(GridBagConstraints constraints)
    {
        currencyLabel = new JLabel(" 0");
        setCurrentSettingToLabel(currencyLabel);
        gameInfoPanel.add(currencyLabel, constraints);
        constraints.gridy++;

        currencyLabel.setIcon(new ImageIcon(ShopButton.coinImage.getScaledInstance(COIN_IMAGE_SIZE, COIN_IMAGE_SIZE, Image.SCALE_SMOOTH)));
    }

    private void CreateEnergyLabel(GridBagConstraints constraints)
    {
        energyLabel = new JLabel("Energy: 0/0");
        setCurrentSettingToLabel(energyLabel);
        energyLabel.setIcon(new ImageIcon(GameImages.ENERGY.image));
        energyLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        gameInfoPanel.add(energyLabel, constraints);
        constraints.gridy++;
    }

    private void CreatePowerUpPane(GridBagConstraints constraints)
    {
        JPanel powerUpPanel = new JPanel();
        powerUpPanel.setLayout(new BoxLayout(powerUpPanel, BoxLayout.LINE_AXIS));
        powerUpPanel.setOpaque(false);
        constraints.weighty = 1;
        gameInfoPanel.add(powerUpPanel, constraints);
        constraints.weighty = 0;
        constraints.gridy++;

        activePowerUpPanel = new JPanel();
        activePowerUpPanel.setOpaque(false);
        activePowerUpPanel.setLayout(new BoxLayout(activePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(activePowerUpPanel);
        activePowerUpPanel.setOpaque(false);
        JLabel activeLabel = new JLabel("Active:");
        setCurrentSettingToLabel(activeLabel);
        activePowerUpPanel.add(activeLabel);

        passivePowerUpPanel = new JPanel();
        passivePowerUpPanel.setOpaque(false);
        passivePowerUpPanel.setLayout(new BoxLayout(passivePowerUpPanel, BoxLayout.PAGE_AXIS));
        powerUpPanel.add(passivePowerUpPanel);
        passivePowerUpPanel.setOpaque(false);
        powerUpPanel.add(passivePowerUpPanel);
        JLabel passiveLabel = new JLabel("Passive:");
        setCurrentSettingToLabel(passiveLabel);
        passivePowerUpPanel.add(passiveLabel);
    }

    @Override
    protected void setupRestartButton(GridBagConstraints constraints)
    {
        MenuButton restartButton = new MenuButton("Restart Game");
        gameInfoPanel.add(restartButton, constraints);
        constraints.gridy++;

        restartButton.addActionListener(e -> { this.onRestart(); controller.onRestart(); } );
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    @Override
    protected void CreateFlagDisplay(GridBagConstraints constraints)
    {
        flagFoundLabel = new JLabel("Flag: 0/" + controller.getBombCount());
        setCurrentSettingToLabel(flagFoundLabel);
        gameInfoPanel.add(flagFoundLabel, constraints);
        constraints.gridy++;
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
            PowerUpComponent component;
            if(!powerUpChange.isActive()) component = new PowerUpComponent(powerUpChange, POWERUP_IMAGE_SIZE);
            else component = new PowerUpComponent(powerUpChange, ((ActivePowerUp) powerUpChange).getShortcut(), POWERUP_IMAGE_SIZE);
            component.setOpaque(false);
            panel.add(Box.createRigidArea(new Dimension(0,0)));
            panel.add(component);
        }
        repaint();
        revalidate();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        activePowerUpPanel.removeAll();
        JLabel activeLabel = new JLabel("Active:");
        activePowerUpPanel.add(activeLabel);
        passivePowerUpPanel.removeAll();
        JLabel passiveLabel = new JLabel("Passive:");
        passiveLabel.setFont(labelFont);
        passivePowerUpPanel.add(passiveLabel);
    }
}
