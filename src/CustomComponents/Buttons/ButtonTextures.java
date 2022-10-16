package CustomComponents.Buttons;

import GridPAC.CellContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonTextures
{
    static Image[] bottomTextures = new Image[10];
    static Image[] topTextures = new Image[3];

    static String path = "textures/";

    public ButtonTextures()
    {
        loadTextures();
    }

    public static void setPath(String _path)
    {
        path = _path;
    }

    public static void loadTextures()
    {
        try
        {
            topTextures[0] = ImageIO.read(new File(path + "top0.png"));
            topTextures[1] = ImageIO.read(new File(path + "flag.png"));
            String fileName = "";
            for(int i = 0; i < bottomTextures.length - 1; i++)
            {
                fileName = path + i + ".png";
                bottomTextures[i] = ImageIO.read(new File(fileName));
            }
            bottomTextures[9] = ImageIO.read(new File(fileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getBottomTexture(int textureNb)
    {
        return bottomTextures[textureNb == CellContent.BOMB ? 9 : textureNb];
    }

    public static Image getTopTexture(int textureNb) {
        return topTextures[textureNb];
    }


}
