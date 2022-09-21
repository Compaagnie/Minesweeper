package Buttons;

import GridPAC.CellContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonTextures
{
    static Image[] textures = new Image[10];

    static Image flagTexture;

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
            flagTexture = ImageIO.read(new File(path + "flag.png"));
            String fileName = "";
            for(int i = 0; i < textures.length - 1; i++)
            {
                fileName = path + i + ".png";
                textures[i] = ImageIO.read(new File(fileName));
            }
            textures[9] = ImageIO.read(new File(fileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getTexture(int textureNb)
    {
        return textures[textureNb == CellContent.BOMB ? 9 : textureNb];
    }

    public static Image getFlagTexture() {
        return flagTexture;
    }


}
