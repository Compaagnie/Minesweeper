package PAC;

import GridPAC.RoguelikeGrid;

import javax.swing.*;
import java.awt.*;

public class RoguelikeGameView extends GameView
{
    protected JLabel levelLabel;
    protected JLabel currencyLabel;
    protected int currentLevel = 0;
    protected int currencyCount = 0;

    RoguelikeGameView(Minesweeper minesweeper)
    {
        // Created grid, info panel and buttons
        super(minesweeper, 0, 0, 0); // parameters don't matter here
        gameInfoPanel.add(levelLabel);
        currencyLabel = new JLabel("Coins : 0");
        gameInfoPanel.add(currencyLabel);
    }

    @Override
    public void setupGrid(int width, int height, int bombCount) { setupCurrentLevelGrid(); }

    public void setupCurrentLevelGrid()
    {
        this.updateLevel();
        this.grid = getGridFromLevel(currentLevel);
        this.gridScrollPane = new JScrollPane(grid);
        this.gridScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.gridScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(gridScrollPane, BorderLayout.CENTER);
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
        int BOOM_PERCENT = 20 + 2*level;
        int size = (level+1) * 4;
        int bomb_count = (int) (size*size * (BOOM_PERCENT / 100f));

        RoguelikeGrid grid = new RoguelikeGrid(this, new Dimension(size, size), bomb_count);
        grid.setOnWinCallback(this::nextLevel);
        return grid;
    }

    public void nextLevel()
    {
        this.remove(gridScrollPane);

        setupCurrentLevelGrid();
        updateFlagNb();
        //TODO: fix timer or accept general timer behaviour
//        gameTimer.restart();
//        gameTimer.stop();

        updateCurrency(+3);

        repaint();
    }
}
