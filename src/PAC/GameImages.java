package PAC;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public enum GameImages
{
    ENERGY("textures/powerups/energy.png"),

    COUNT;

    public final Image image;
    public static final int SIZE = 32;
    GameImages()
    {
        image = null;
    }

    GameImages(String path)
    {
        image = (new ImageIcon(path)).getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
    }
}
