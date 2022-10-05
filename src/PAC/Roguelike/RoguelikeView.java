package PAC.Roguelike;

import GridPAC.Roguelike.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import PAC.GameView;
import PAC.Minesweeper;
import PAC.Roguelike.Powerups.*;
import Shop.*;

public class RoguelikeView extends GameView
{
    protected JLabel levelLabel;
    protected JLabel currencyLabel;
    protected int currentLevel = 0;
    protected int currencyCount = 0;
    protected Component centerComponent;
    protected ArrayList<PowerUp> powerUps = new ArrayList<>();
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

        // todo : remove debug powerups :
        powerUps.add(new Radar());
        powerUps.add(new BombReveal());
        powerUps.add(new LineReveal());
        powerUps.add(new ColumnReveal());

        this.minesweeper.add(this);

        this.setupCurrentLevelGrid();

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
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                thisView.requestFocusInWindow();
            }
        });
    }

    private void executePowerUp(int index)
    {
        if(index < 0 || index >= powerUps.size()) System.out.println("Not yet bound to power up : " + index);
        else
        {
            PowerUp powerUp = powerUps.get(index);
            Point mouse = grid.getMousePosition();
            if(powerUp.getCost() <= energy && mouse != null && grid.contains(mouse))
            {
                float absolute_x = grid.dimensions.width * mouse.x/ (float) grid.getWidth();
                float absolute_y = grid.dimensions.height * mouse.y/ (float) grid.getHeight();

                int currentMouseCell =   ((int)absolute_x + (int)absolute_y * grid.dimensions.width);
                //Debug :
//                System.out.println("Dims :" + grid.dimensions);
//                System.out.println("Radar :" + (int) absolute_x + " " + (int) absolute_y + " cell: " + currentMouseCell);

                if(powerUp.use((RoguelikeGrid) this.grid, currentMouseCell))
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

    protected void updateCurrency(int modifier)
    {
        this.currencyCount += modifier;
        this.currencyLabel.setText("Coins: " + currencyCount);
    }

    protected RoguelikeGrid getGridFromLevel(int level)
    {
        // arbitrary difficulty curve :
        // TODO : change this placeholder to an actual difficulty curve
        // TODO : non-rectangular grids and constructor depending only on cell count + bomb count or bomb%
        int BOOM_PERCENT = 10 + 2*level;
        int size = (level+1) * 4;
        int bomb_count = (int) (size*size * (BOOM_PERCENT / 100f));

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
        //TODO : display shop

        Shop shop = new Shop(true, this::nextLevel);
        centerComponent = shop;
        this.add(shop, BorderLayout.CENTER);
    }

    public void nextLevel()
    {
        this.flagFoundLabel.setVisible(true);
        this.remove(centerComponent);
        setupCurrentLevelGrid();
        updateFlagNb();
        //TODO: fix timer or accept general timer behaviour
//        gameTimer.restart();
//        gameTimer.stop();

        repaint();
    }
}
