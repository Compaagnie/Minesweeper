package CustomComponents.Buttons;

import java.awt.*;

public class MenuButtonUI {

    public void paint(Graphics pen, MenuButton c){
        pen.setColor(c.getPaintColor());
        pen.fillRoundRect(0,0,c.getWidth()-1, c.getHeight()-1, 20,20);
        String label = c.getText();
        pen.setColor(Color.BLACK);
        pen.drawString(label, (int) (c.getWidth()/2. - pen.getFontMetrics().stringWidth(label)/2.), (int) (c.getHeight()/2. + pen.getFontMetrics().getLineMetrics(label,pen).getHeight()/2.));
    }
}
