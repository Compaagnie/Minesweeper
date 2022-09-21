package Buttons;

import GridPAC.CellContent;
import GridPAC.Grid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BottomButton extends MineButton
{
    private static Image[] textures = new Image[10];
    public BottomButton(int position, Grid g)
    {
        super(position, g);
        //this.setText("bot");
        try
        {
            String fileName = "";
            for(int i = 0; i < textures.length - 1; i++)
            {
                fileName = "textures/" + i + ".png";
                textures[i] = ImageIO.read(new File(fileName));
            }
            textures[9] = ImageIO.read(new File(fileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addActionListener(e -> grid.propagateReveal(this.position)); //Call to the model : update surroundings
    }

    public void setTextureFromValue(int value)
    {
        if (value == CellContent.BOMB) this.setTexture(textures[9]);
        else this.setTexture(textures[value]);
    }
}
