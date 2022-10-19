import PAC.Minesweeper;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Main
{
    static Minesweeper minesweeper;
    static Player player;
    public static void main(String[] args)
    {
        System.out.println(Main.class.getClassLoader().getName());
        //minesweeper = new Minesweeper(true);
        minesweeper = new Minesweeper(false);
        minesweeper.setFocusable(true);


        try {
            minesweeper.setIconImage(ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("textures/powerups/radar.png"))));
            //FileInputStream fileInputStream = (FileInputStream) ;
            player = new Player(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("textures/music.mp3")));
            player.play();
        } catch (JavaLayerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
