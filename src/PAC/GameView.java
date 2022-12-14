package PAC;

import CustomComponents.BackgroundPanel;
import CustomComponents.Buttons.MenuButton;
import CustomComponents.VSlider;
import GridPAC.Grid;
import GridPAC.GridEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.KeyEvent.VK_ENTER;

public class GameView extends BackgroundPanel
{
    protected Minesweeper minesweeper;

    protected Grid grid;
    public Timer gameTimer;
    protected JLabel flagFoundLabel;
    protected JPanel gameInfoPanel = new JPanel();
    protected JLabel gameStatusLabel;
    protected VSlider bombFoundSlider = new VSlider();
    protected VSlider revealedSlider = new VSlider();
    protected int timerSeconds = 0;
    protected JPanel globalInfoPanel = new JPanel();
    protected JPanel centerPanel = new JPanel();

    protected MenuButton recordButton;

    protected boolean enterPressed = false;

    protected Font labelFont;
    protected Color labelTextColor;

    public GameView(){super();}

    public GameView(Minesweeper minesweeper, int width, int height, int bombCount)
    {
        super();

        this.labelFont = this.getFont().deriveFont(22.0f);
        this.labelTextColor = Color.white;

        this.minesweeper = minesweeper;
        this.setLayout(new BorderLayout());

        this.minesweeper.add(this);
        this.add(centerPanel, BorderLayout.CENTER);
        this.centerPanel.setOpaque(false);
        this.centerPanel.setLayout(new GridBagLayout());

        this.setupGrid(width, height, bombCount);

        CreateGeneralPanelAndSliders();

        setUpInfoPanel(globalInfoPanel);
    }

    private void setUpInfoPanel(JPanel globalInfoPanel)
    {
        gameInfoPanel = new JPanel();
        gameInfoPanel.setOpaque(false);
        gameInfoPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);
        
        globalInfoPanel.add(gameInfoPanel);

        CreateStatusLabel(constraints);
        CreateFlagDisplay(constraints);
        CreateTimerDisplay(constraints);


        constraints.weighty = 1;
        gameInfoPanel.add(Box.createVerticalGlue(), constraints);
        constraints.weighty = 0;
        constraints.gridy++;

        CreateRecordButton(constraints);


        constraints.weighty = 1;
        gameInfoPanel.add(Box.createVerticalGlue(), constraints);
        constraints.weighty = 0;
        constraints.gridy++;


        CreateBackButton(constraints);
        constraints.gridy++;

        this.setupRestartButton(constraints);
    }

    protected void CreateRecordButton(GridBagConstraints constraints) {
        recordButton = new MenuButton("Record");
        recordButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                setEnterPressed(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setEnterPressed(false);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER && getMinesweeper().speechInitiated){
                    recordButton.setSelected(true);
                    setEnterPressed(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER){
                    recordButton.setSelected(false);
                    setEnterPressed(false);
                }
            }
        });

        recordButton.setMnemonic(VK_ENTER);
        if (!getMinesweeper().speechInitiated)
            recordButton.setEnabled(false);
        gameInfoPanel.add(recordButton, constraints);
        constraints.gridy++;
    }

    private void CreateStatusLabel(GridBagConstraints constraints)
    {
        gameStatusLabel = new JLabel();
        setCurrentSettingToLabel(gameStatusLabel);
        gameInfoPanel.add(gameStatusLabel, constraints);
        constraints.gridy++;
    }

    protected void CreateGeneralPanelAndSliders()
    {
        globalInfoPanel.setLayout(new BoxLayout(globalInfoPanel, BoxLayout.LINE_AXIS));
        this.add(globalInfoPanel, BorderLayout.EAST);
        globalInfoPanel.add(bombFoundSlider);
        globalInfoPanel.add(revealedSlider);
        globalInfoPanel.setOpaque(false);
        bombFoundSlider.setFillColor(Color.red);
        bombFoundSlider.setMinimum(0);
        bombFoundSlider.setValue(0);
        revealedSlider.setFillColor(Color.green);
        revealedSlider.setMinimum(0);
        revealedSlider.setValue(0);
    }

    protected void setupGrid(int width, int height, int bombCount)
    {
        this.grid = new Grid(this, new Dimension(width,height), bombCount);
        this.grid.addEventListener(this::gridEventHandler);
        this.grid.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        this.grid.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        this.centerPanel.add(grid,constraints);
        this.bombFoundSlider.setMaximum(grid.getBombCount());
        this.revealedSlider.setMaximum(grid.getCellCount()-grid.getBombCount());
    }

    protected void gridEventHandler(GridEvent event)
    {
        switch (event.command)
        {
            case "flag" : updateFlagNb(); break;
            case "reveal" : if(!grid.isOver())
            {
                gameTimer.start();
                revealedSlider.setValue(grid.getRevealedCount());
            } break;
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
        revealedSlider.setValue(0);
        bombFoundSlider.setValue(0);
        updateFlagNb();
    }

    protected void CreateBackButton(GridBagConstraints constraints)
    {
        MenuButton backButton = new MenuButton("Menu");
        gameInfoPanel.add(backButton, constraints);
        backButton.addActionListener(e -> openMenu());
        constraints.gridy++;
    }

    protected void CreateTimerDisplay(GridBagConstraints constraints)
    {
        JLabel timeSpentLabel = new JLabel("Time: 00:00:00");
        setCurrentSettingToLabel(timeSpentLabel);
        gameInfoPanel.add(timeSpentLabel, constraints);
        constraints.gridy++;

        ActionListener timerAction = e ->
        {
            timerSeconds++;
            timeSpentLabel.setText("Time: "+ String.format("%02d:%02d:%02d", timerSeconds / 360, timerSeconds / 60, timerSeconds % 60));
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
       
        
    protected void CreateFlagDisplay(GridBagConstraints constraints)
    {
        flagFoundLabel = new JLabel("Flag: 0/" + grid.getBombCount());
        setCurrentSettingToLabel(flagFoundLabel);
        gameInfoPanel.add(flagFoundLabel, constraints);
        constraints.gridy++;
    }

    protected void setupRestartButton(GridBagConstraints constraints)
    {
        MenuButton restartButton = new MenuButton("Restart Game");
        gameInfoPanel.add(restartButton, constraints);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    public void openMenu()
    {
        this.minesweeper.openMenu();
    }

    public void updateFlagNb()
    {
        this.flagFoundLabel.setText("Flags: " + grid.getFlagNumber()+"/"+ grid.getBombCount());
        this.bombFoundSlider.setValue(grid.getFlagNumber());
    }

    public void setGameStatus(String text) {
        this.gameStatusLabel.setText(text);
    }

    public Grid getGrid()
    {
        return this.grid;
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }

    protected void setCurrentSettingToLabel(JLabel label)
    {
        label.setFont(labelFont);
        label.setForeground(labelTextColor);
    }

    public boolean isRogueLike()
    {
        return false;
    }

    public void setSpeechInitiated(){
        recordButton.setEnabled(true);
    }


    public void setEnterPressed(Boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public Boolean getEnterPressed() {
        return enterPressed;
    }
}
