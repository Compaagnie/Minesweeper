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

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);

        minesweeper.add(menuPanel);

        MenuButton presetButton1 = new MenuButton("8x8 : 10 bombs");
        presetButton1.addActionListener( e -> { menuPanel.setVisible(false); minesweeper.startGame(8,8,10);});
        MenuButton presetButton2 = new MenuButton("16x16 : 40 bombs");
        presetButton2.addActionListener(e -> { menuPanel.setVisible(false); minesweeper.startGame(16,16,40);});
        MenuButton presetButton3 = new MenuButton("30x16 : 99 bombs");
        presetButton3.addActionListener(e -> { menuPanel.setVisible(false); minesweeper.startGame(30,16,99); });

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
                if(bombCount < width * height)
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
