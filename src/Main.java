import PAC.Minesweeper;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main
{
    static Minesweeper minesweeper;
    static Player player;
    public static void main(String[] args)
    {
        minesweeper = new Minesweeper();
        minesweeper.setFocusable(true);

        try {
            FileInputStream fileInputStream = new FileInputStream("Elevator Music - 1 hour.mp3");
            player = new Player(fileInputStream);
            //player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }
}
