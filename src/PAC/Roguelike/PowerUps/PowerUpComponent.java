package PAC.Roguelike.PowerUps;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class PowerUpComponent extends JLabel
{
    public final PowerUp powerUp;
    public final Integer shortcutKey;

    public final static int shortcutTooltipSize = 20;

    public PowerUpComponent(PowerUp _powerUp, int _size)
    {
        super(new ImageIcon(_powerUp.getImage().getScaledInstance(_size, _size, Image.SCALE_SMOOTH)));
        this.powerUp = _powerUp;
        shortcutKey = null;
        this.setToolTipText(powerUp.getName());
    }
    public PowerUpComponent(PowerUp _powerUp, int _shortcutKey, int _size)
    {
        super(new ImageIcon(_powerUp.getImage().getScaledInstance(_size, _size, Image.SCALE_SMOOTH)));
        this.powerUp = _powerUp;
        shortcutKey = _shortcutKey;
        this.setToolTipText("Press " + shortcutKey + " for " + powerUp.getName());
    }

    @Override
    public void paint(Graphics _pen)
    {
        super.paint(_pen);
        if(shortcutKey != null)
        {
            Graphics2D pen = (Graphics2D) _pen;
            Color previousColor = pen.getColor();
            pen.setColor(new Color(121, 121, 121));
            pen.fillRoundRect(0, this.getHeight() - shortcutTooltipSize, shortcutTooltipSize, shortcutTooltipSize, 3, 3);

            Rectangle2D string_bounds = pen.getFontMetrics().getStringBounds(String.valueOf(shortcutKey), pen);
            pen.setColor(new Color(255, 255, 255));
            pen.drawString(String.valueOf(shortcutKey), (int) (shortcutTooltipSize/2 - string_bounds.getWidth()/2), (int) (this.getHeight() - shortcutTooltipSize/2 + string_bounds.getHeight()/2));
            pen.setColor(previousColor);
        }
    }
}
