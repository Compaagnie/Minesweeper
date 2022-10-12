package PAC.Roguelike;

import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import PAC.GameView;
import PAC.Minesweeper;
import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.PowerUps.PassivePowerUp;
import Shop.*;

public class RoguelikeView extends GameView
{
    protected JLabel levelLabel;
    protected JLabel currencyLabel;
    protected JLabel debugPassivePowerUps;
    protected Component centerComponent;
    protected int currentLevel = 0;
    protected int currencyCount = 0;
    protected ArrayList<ActivePowerUp> activePowerUps = new ArrayList<>();
    protected int passivePowerUps = 0;
    private Integer energy = Integer.MAX_VALUE; // todo : design check
    private int energy_MAX = Integer.MAX_VALUE; // todo : design check

    RoguelikeModel model;

    public RoguelikeView(Minesweeper minesweeper)
    {
        // Created grid, info panel and buttons
        super();

        this.model = new RoguelikeModel();

        this.minesweeper = minesweeper;
        this.setLayout(new BorderLayout());

        // todo : remove debug power ups :
        activePowerUps.add(ActivePowerUp.RADAR_REVEAL);
//        activePowerUps.add(ActivePowerUp.BOMB_REVEAL);
//        activePowerUps.add(ActivePowerUp.LINE_REVEAL);
//        activePowerUps.add(ActivePowerUp.COLUMN_REVEAL);

        this.minesweeper.add(this);

        this.setupCurrentLevelGrid();

        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.PAGE_AXIS));
        this.add(gameInfoPanel, BorderLayout.EAST);

        flagFoundLabel = new JLabel("Flag: 0/" + grid.getBombCount());
        gameInfoPanel.add(flagFoundLabel);

        JLabel timeSpentLabel = new JLabel("Time: 00:00:00");
        gameInfoPanel.add(timeSpentLabel);

        //todo : remove debug power ups :
        debugPassivePowerUps = new JLabel();
        DEBUGupdatePassivePowerUpLabel();
        gameInfoPanel.add(debugPassivePowerUps);


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

        gameInfoPanel.add(levelLabel);
        currencyLabel = new JLabel("Coins : 0");
        gameInfoPanel.add(currencyLabel);

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
                        if(e.getKeyChar() == '0') executePowerUp(9);
                        else executePowerUp(e.getKeyChar() - '1');
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

    private void executePowerUp(int index)
    {
        if(index < 0 || index >= activePowerUps.size()) System.out.println("Not yet bound to power up : " + index);
        else
        {
            ActivePowerUp activePowerUp = activePowerUps.get(index);
            Point mouse = grid.getMousePosition();
            if(activePowerUp.getEnergyCost() <= energy && mouse != null && grid.contains(mouse))
            {
                float absolute_x = grid.dimensions.width * mouse.x/ (float) grid.getWidth();
                float absolute_y = grid.dimensions.height * mouse.y/ (float) grid.getHeight();

                int currentMouseCell =   ((int)absolute_x + (int)absolute_y * grid.dimensions.width);
                //Debug :
//                System.out.println("Dims :" + grid.dimensions);
//                System.out.println("Radar :" + (int) absolute_x + " " + (int) absolute_y + " cell: " + currentMouseCell);

                if(activePowerUp.use((RoguelikeGrid) this.grid, currentMouseCell))
                {
//                energy -= powerUp.getCost(); // TODO : uncomment when not debugging
                }
            }
        }
    }

    public void setupCurrentLevelGrid()
    {
        removeCenterComponent();
        this.updateLevel();
        this.grid = null;
        this.grid = getGridFromLevel(currentLevel);
        this.gridScrollPane = new JScrollPane(grid);
        this.gridScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.gridScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(gridScrollPane, BorderLayout.CENTER);
        centerComponent = gridScrollPane;
    }

    protected void removeCenterComponent()
    {
        if(centerComponent != null) this.remove(centerComponent);
    }

    protected void refillEnergy()
    {
        this.energy = this.energy_MAX;
    }

    protected void updateLevel()
    {
        ++this.currentLevel;
        if(levelLabel == null) levelLabel = new JLabel();
        this.levelLabel.setText("Level: " + currentLevel);
    }

    public void updateCurrency(int modifier)
    {
        // todo : magic number to constant & design check
        // todo : add animations
        if(modifier > 0)
        {
            if(has(PassivePowerUp.DOUBLE_COIN))
            {
                modifier *= 2;
            }
            if(has(PassivePowerUp.DOUBLE_EDGED_SWORD))
            {
                modifier *= 1.5;
            }
        }

        this.currencyCount += modifier;
        this.currencyLabel.setText("Coins: " + currencyCount);
    }

    protected RoguelikeGrid getGridFromLevel(int level)
    {
        // TODO : non-rectangular grids ?

        // todo : magic number to constant & design check
        int BOMB_PERCENT = 10 + 2*level;
        int size = (level+1) * 4;
        if(has(PassivePowerUp.DOUBLE_EDGED_SWORD)) BOMB_PERCENT *= 1.5f;


        int bomb_count = (int) (size * size * (BOMB_PERCENT / 100f));
        RoguelikeGrid grid = new RoguelikeGrid(this, new Dimension(size, size), bomb_count);
        grid.setOnWinCallback(this::onLevelClear);
        return grid;
    }

    public void onLevelClear()
    {
        updateCurrency(+3); // Todo : currency formula
        refillEnergy();

        removeCenterComponent();
        this.flagFoundLabel.setVisible(false);
        grid = null;

        Shop shop; // free stage store : choose one of 3 power ups
        if(!has(PassivePowerUp.SHOP_AHEAD)) shop = new Shop(this, true, this::nextLevel);
        else
        {
            shop = new Shop(this, true, this::createShop);
            remove(PassivePowerUp.SHOP_AHEAD);
        }
        centerComponent = shop;
        this.add(shop, BorderLayout.CENTER);
    }

    public void createShop()
    {
        removeCenterComponent();
        Shop shop = new Shop(this, false, this::nextLevel);
        centerComponent = shop;
        this.add(shop, BorderLayout.CENTER);
    }

    public void nextLevel()
    {
        this.flagFoundLabel.setVisible(true);
        this.remove(centerComponent);
        this.setupCurrentLevelGrid();
        updateFlagNb();
        //TODO: fix timer or accept general timer behaviour
//        gameTimer.restart();
//        gameTimer.stop();

        repaint();
    }

    public void add(PassivePowerUp powerUp)
    {
        // set masked bit to 1
        this.passivePowerUps = this.passivePowerUps | powerUp.mask;
        DEBUGupdatePassivePowerUpLabel();
    }

    public boolean has(PassivePowerUp powerUp)
    {
        // check if the masked bit is 1
        return (this.passivePowerUps & powerUp.mask) != 0;
    }

    public void remove(PassivePowerUp powerUp)
    {
        // set the masked bit to 0
        this.passivePowerUps = this.passivePowerUps & ~powerUp.mask;
        DEBUGupdatePassivePowerUpLabel();
    }

    public void add(ActivePowerUp powerUp)
    {
        this.activePowerUps.add(powerUp);
    }

    public boolean has(ActivePowerUp powerUp)
    {
        return this.activePowerUps.contains(powerUp);
    }

    public void remove(ActivePowerUp powerUp)
    {
        this.activePowerUps.remove(powerUp);
    }

    protected void DEBUGupdatePassivePowerUpLabel()
    {
        String text = "";
        for(int i = 0; i < PassivePowerUp.COUNT.ordinal(); ++i)
        {
            if(has(PassivePowerUp.values()[i])) text += "1";
            else text += "0";
        }
        debugPassivePowerUps.setText(text);
    }
}
