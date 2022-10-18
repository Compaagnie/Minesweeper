package PAC;

import CustomComponents.Buttons.MenuButton;
import CustomComponents.VSlider;
import CustomComponents.BackgroundPanel;
import GridPAC.Grid;
import SpeechRecognition.Recorder;
import SpeechRecognition.SpeechRecognition;
import GridPAC.GridEvent;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static java.awt.Transparency.TRANSLUCENT;
import static java.awt.event.KeyEvent.VK_ENTER;

public class GameView extends BackgroundPanel
{
    protected Minesweeper minesweeper;

    protected Grid grid;
    protected SpeechRecognition speechRecognition;

    protected Recorder recorder;
    public Timer gameTimer;

    protected JLabel flagFoundLabel;
    protected JPanel gameInfoPanel = new JPanel();
    protected JLabel gameStatusLabel;
    protected VSlider bombFoundSlider = new VSlider();
    protected VSlider revealedSlider = new VSlider();

    protected int timerSeconds = 0;

    public GameView(){super(new ImageIcon("textures/background.jpeg").getImage(), 0);}

    public GameView(Minesweeper minesweeper, int width, int height, int bombCount)
    {
        super(new ImageIcon("textures/background.jpeg").getImage(), 0);
        this.minesweeper = minesweeper;
        this.setLayout(new BorderLayout());

        this.minesweeper.add(this);
        
        recorder = new Recorder(1);
        speechRecognition = new SpeechRecognition(this, recorder);
       
        speechRecognition.start();

        this.setupGrid(width, height, bombCount);

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
        gameInfoPanel.setOpaque(false);
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.PAGE_AXIS));
        globalInfoPanel.add(gameInfoPanel);
        
        gameStatusLabel = new JLabel();
        gameInfoPanel.add(gameStatusLabel);

        CreateFlagDisplay();

        CreateTimerDisplay();

        gameInfoPanel.add(Box.createVerticalGlue());

        MenuButton recordButton = new MenuButton("Record");
        recordButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER){
                    minesweeper.setEnterPressed(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER){
                    minesweeper.setEnterPressed(false);
                }
            }
        });
        recordButton.setMnemonic(VK_ENTER);
        gameInfoPanel.add(recordButton);

        gameInfoPanel.add(Box.createVerticalGlue());


        CreateBackButton();

        this.setupRestartButton();
    }

    protected void setupGrid(int width, int height, int bombCount)
    {
        this.grid = new Grid(this, new Dimension(width,height), bombCount);
        this.grid.addEventListener(this::gridEventHandler);
        this.grid.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        this.add(grid, BorderLayout.CENTER);
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
       
        
    protected void CreateFlagDisplay()
    {
        flagFoundLabel = new JLabel("Flag: 0/" + grid.getBombCount());
        gameInfoPanel.add(flagFoundLabel);
    }

    protected void setupRestartButton()
    {
        JButton restartButton = new JButton("Restart Game");
        gameInfoPanel.add(restartButton);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    public void openMenu()
    {
        if(this.speechRecognition != null) this.speechRecognition.clear();
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
}
