package PAC;

import Buttons.MenuButton;

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
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);

        this.minesweeper.add(menuPanel);

        JButton presetButton1 = new JButton("8x8 : 10 bombs");
        presetButton1.addActionListener( e -> { menuPanel.setVisible(false); this.minesweeper.startGame(8,8,10);});
        JButton presetButton2 = new JButton("16x16 : 40 bombs");
        presetButton2.addActionListener(e -> { menuPanel.setVisible(false); this.minesweeper.startGame(16,16,40);});
        JButton presetButton3 = new JButton("30x16 : 99 bombs");
        presetButton3.addActionListener(e -> { menuPanel.setVisible(false); this.minesweeper.startGame(30,16,99); });

        MenuButton presetButtonCustom = new MenuButton("? : Custom");
        presetButtonCustom.addActionListener(e -> { menuPanel.setVisible(false); createCustomPresetMenu(); });

        menuPanel.add(presetButton1, constraints);
        constraints.gridx = 1;
        menuPanel.add(presetButton2, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        menuPanel.add(presetButton3, constraints);
        constraints.gridx = 1;
        menuPanel.add(presetButtonCustom, constraints);
    }

    private void createCustomPresetMenu()
    {
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));
        selectionPanel.setMaximumSize(new Dimension(300, 200));
        selectionPanel.setPreferredSize(new Dimension(300, 200));
        minesweeper.add(selectionPanel);

        selectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);

        Dimension fieldSize = new Dimension(40,20);

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));

        JLabel sizeLabel = new JLabel("Size: ");
        JTextField widthField = new JTextField();
        widthField.setPreferredSize(fieldSize);
        JLabel xLabel = new JLabel(" x ");
        JTextField heightField = new JTextField();
        heightField.setPreferredSize(fieldSize);
        sizePanel.add(sizeLabel, constraints);
        sizePanel.add(widthField, constraints);
        sizePanel.add(xLabel, constraints);
        sizePanel.add(heightField, constraints);
        selectionPanel.add(sizePanel, constraints);

        JPanel bombPanel = new JPanel();
        bombPanel.setLayout(new BoxLayout(bombPanel, BoxLayout.X_AXIS));
        JLabel bombLabel = new JLabel("Bombs: ");
        JTextField bombTextField = new JTextField();
        bombTextField.setPreferredSize(fieldSize);
        constraints.gridy++;
        bombPanel.add(bombLabel);
        bombPanel.add(bombTextField);
        selectionPanel.add(bombPanel, constraints);


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
                if(bombCount < width * height && (width > 3 || height > 3))
                {
                    minesweeper.startGame(width, height, bombCount);
                    selectionPanel.setVisible(false);
                }
            }

            catch (NumberFormatException ignored){} // Do nothing
        });
        constraints.gridy++;
        selectionPanel.add(confirmButton, constraints);

//        revalidate();
//        repaint();
    }
}
