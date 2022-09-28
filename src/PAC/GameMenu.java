package PAC;

import javax.swing.*;
import java.awt.*;

public class GameMenu extends JPanel
{
    Minesweeper minesweeper;

    public GameMenu(Minesweeper minesweeper)
    {
        this.minesweeper = minesweeper;

        JPanel modeSelectionPanel = new JPanel();
        modeSelectionPanel.setLayout(new GridLayout(1,2));
        this.minesweeper.add(modeSelectionPanel);

        JButton rogueLikeModeButton = new JButton("Roguelike");
        rogueLikeModeButton.addActionListener(e -> {modeSelectionPanel.setVisible(false); minesweeper.startRoguelikeGame();});
        modeSelectionPanel.add(rogueLikeModeButton);

        JButton standardModeButton = new JButton("Standard");
        standardModeButton.addActionListener(e -> {modeSelectionPanel.setVisible(false); createPresetButtons();});
        modeSelectionPanel.add(standardModeButton);
    }

    private void createPresetButtons()
    {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2,2));

        this.minesweeper.add(menuPanel);

        JButton presetButton1 = new JButton("8x8 : 10 bombs");
        presetButton1.addActionListener( e -> { menuPanel.setVisible(false); this.minesweeper.startGame(8,8,10);});
        JButton presetButton2 = new JButton("16x16 : 40 bombs");
        presetButton2.addActionListener(e -> { menuPanel.setVisible(false); this.minesweeper.startGame(16,16,40);});
        JButton presetButton3 = new JButton("30x16 : 99 bombs");
        presetButton3.addActionListener(e -> { menuPanel.setVisible(false); this.minesweeper.startGame(30,16,99); });

        JButton presetButtonCustom = new JButton("? : Custom");
        presetButtonCustom.addActionListener(e -> { menuPanel.setVisible(false); createCustomPresetMenu(); });

        menuPanel.add(presetButton1);
        menuPanel.add(presetButton2);
        menuPanel.add(presetButton3);
        menuPanel.add(presetButtonCustom);
    }

    private void createCustomPresetMenu()
    {
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));
        selectionPanel.setMaximumSize(new Dimension(300, 200));
        selectionPanel.setPreferredSize(new Dimension(300, 200));
        minesweeper.add(selectionPanel);


        JPanel dimensionSubPanel = new JPanel();
        dimensionSubPanel.setPreferredSize(new Dimension(300, 40));
        dimensionSubPanel.setLayout(new BoxLayout(dimensionSubPanel, BoxLayout.LINE_AXIS));
        JLabel sizeLabel = new JLabel("Size:");
        dimensionSubPanel.add(sizeLabel);

        JTextField widthField = new JTextField();
        dimensionSubPanel.add(widthField);

        JLabel xLabel = new JLabel("x");
        dimensionSubPanel.add(xLabel);

        JTextField heightField = new JTextField();
        dimensionSubPanel.add(heightField);

        selectionPanel.add(dimensionSubPanel);

        JPanel bombSubPanel = new JPanel();
        bombSubPanel.setPreferredSize(new Dimension(300, 40));
        bombSubPanel.setLayout(new BoxLayout(bombSubPanel, BoxLayout.LINE_AXIS));
        JLabel bombLabel = new JLabel("Bombs :");
        JTextField bombTextField = new JTextField();
        bombSubPanel.add(bombLabel);
        bombSubPanel.add(bombTextField);

        selectionPanel.add(bombSubPanel);

        JButton confirmButton = new JButton("Start");
        confirmButton.addActionListener(e ->
        {
            try
            {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int bombCount = Integer.parseInt(bombTextField.getText());

                // Preventing most crashes (infinite bomb positioning loop)
                // TODO : true check with exception
                if(bombCount < width * height)
                {
                    minesweeper.startGame(width, height, bombCount);
                    selectionPanel.setVisible(false);
                }
            }
            catch (NumberFormatException exception)
            {
                System.out.println("Grid creation failed : NaN in preset");
            }
        });
        selectionPanel.add(confirmButton);
    }
}
