import PAC.Minesweeper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main
{
    public static void main(String[] args)
    {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.setFocusable(true);
        minesweeper.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                minesweeper.requestFocusInWindow();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    minesweeper.setEnterPressed(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    minesweeper.setEnterPressed(false);
                }
            }
        });
    }
}
