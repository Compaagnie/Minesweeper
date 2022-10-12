package Buttons;

import java.awt.*;

public class MenuButtonUI {

    public void paint(Graphics pen, MenuButton c){
        pen.drawRoundRect(0,0,c.getWidth(), c.getHeight(), 5,5);
    }
}
