package CustomComponents.Buttons;

import GridPAC.CellContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ButtonTextures
{
    static Image[] bottomTextures = new Image[11];
    static Image[] topTextures = new Image[3];

    static String path = "textures/cells/";

    public ButtonTextures()
    {
        try {
            loadTextures();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTextures() throws IOException {
        topTextures[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path + "top0.png")));
        topTextures[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path + "flag.png")));
        topTextures[2] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path + "wrongFlag.png")));
        String fileName;
        for(int i = 0; i < bottomTextures.length - 2; i++)
        {
            fileName = path + i + ".png";
            bottomTextures[i] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(fileName)));

        }
        bottomTextures[9] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path + "bomb.png")));
        bottomTextures[10] = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path + "losingBomb.png")));
    }

    public static Image getBottomTexture(int textureNb)
    {
        return bottomTextures[textureNb == CellContent.BOMB ? 9 : textureNb];
    }

    public static Image getTopTexture(int textureNb) {
        return topTextures[textureNb];
    }


}
