package CustomComponents.Buttons;

import java.awt.*;

public class MenuButtonUI {
    static Color disabledColor = new Color(66, 63, 63);

    public void paint(Graphics pen, MenuButton c){
        if (c.isEnabled())
            pen.setColor(c.getPaintColor());
        else
            pen.setColor(disabledColor);
        pen.fillRoundRect(0,0,c.getWidth()-1, c.getHeight()-1, 20,20);
        String label = c.getText();
        pen.setColor(Color.BLACK);
        pen.drawString(label, (int) (c.getWidth()/2. - pen.getFontMetrics().stringWidth(label)/2.), (int) (c.getHeight()/2. + pen.getFontMetrics().getLineMetrics(label,pen).getHeight()/2.));
        pen.setColor(new Color(0,0,0,0));
    }
}
