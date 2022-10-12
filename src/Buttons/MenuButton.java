package Buttons;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {
    MenuButtonUI ui;
    public MenuButton(String text)
    {
        super(text);
        setPreferredSize(new Dimension(200,30));
        setMinimumSize(new Dimension(200,30));
        this.ui = new MenuButtonUI();
    }

    @Override
    public void paintComponent(Graphics pen){
        super.paintComponent(pen);
        //ui.paint(pen, this);
    }
}
