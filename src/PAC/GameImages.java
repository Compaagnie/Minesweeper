package PAC;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

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
        try {
            image = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path))).getScaledInstance(SIZE,SIZE,Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
