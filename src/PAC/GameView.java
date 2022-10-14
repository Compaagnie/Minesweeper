package PAC;

import Buttons.CellButton;
import GridPAC.Grid;
import SpeechRecognition.Recorder;
import SpeechRecognition.SpeechRecognition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameView extends JPanel
{
    protected Minesweeper minesweeper;

    protected Grid grid;
    protected SpeechRecognition speechRecognition;

    protected Recorder recorder;
    public Timer gameTimer;

    protected JLabel flagFoundLabel;
    protected JPanel gameInfoPanel = new JPanel();
    protected JToggleButton micToggleButton;

    protected JLabel gameStatusLabel;

    public GameView(Minesweeper minesweeper, int width, int height, int bombCount)
    {
        super();
        this.minesweeper = minesweeper;
        this.setLayout(new BorderLayout());

        this.minesweeper.add(this);

        this.setupGrid(width, height, bombCount);


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
            public void actionPerformed(ActionEvent e) {
                seconds++;
                timeSpentLabel.setText("Time: "+ String.format("%02d:%02d:%02d", seconds / 360, seconds / 60, seconds % 60));
            }
        };

        gameTimer = new Timer(1000, timerAction);
        gameTimer.stop();

        gameStatusLabel = new JLabel();
        gameInfoPanel.add(gameStatusLabel);

        gameInfoPanel.add(Box.createVerticalGlue());

        micToggleButton = new JToggleButton("Toggle Mic");
        gameInfoPanel.add(micToggleButton);
        recorder = new Recorder(1);
        speechRecognition = new SpeechRecognition(this, recorder);
        micToggleButton.addActionListener(e -> {
            if (micToggleButton.isSelected()){
                speechRecognition.setSpacePressed(true);
            } else {
                speechRecognition.setSpacePressed(false);
            }
        });
        speechRecognition.start();

        gameInfoPanel.add(Box.createVerticalGlue());

        JButton backButton = new JButton("Menu");
        gameInfoPanel.add(backButton);
        backButton.addActionListener(e -> openMenu());

        this.setupRestartButton(gameInfoPanel);
    }

    protected void setupRestartButton(JPanel parent)
    {
        JButton restartButton = new JButton("Restart Game");
        parent.add(restartButton);

        restartButton.addActionListener(e -> grid.restartGame());
        restartButton.setPreferredSize(new Dimension(120,60));
        restartButton.setMnemonic(KeyEvent.VK_R);
    }

    protected void setupGrid(int width, int height, int bombCount)
    {
        this.grid = new Grid(this, new Dimension(width,height), bombCount);
        JScrollPane scrollPane = new JScrollPane(grid);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void openMenu()
    {
        speechRecognition.clear();
        this.minesweeper.openMenu();
    }

    public void updateFlagNb()
    {
        this.flagFoundLabel.setText("Flags: " + grid.getFlagNumber()+"/"+ grid.getBombCount());
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
