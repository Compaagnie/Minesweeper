import PAC.Minesweeper;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args){
        new Minesweeper();
        try {
            FileInputStream fileInputStream = new FileInputStream("Elevator Music - 1 hour.mp3");
            Player player = new Player(fileInputStream);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }
}
