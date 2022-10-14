package PAC.Roguelike.PowerUps;

import javax.swing.*;

public class PowerUpComponent extends JLabel
{
    public final PowerUp powerUp;

    public PowerUpComponent(PowerUp _powerUp)
    {
        super(new ImageIcon(_powerUp.getImage()));
        this.powerUp = _powerUp;
    }
}
