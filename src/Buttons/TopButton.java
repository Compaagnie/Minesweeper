package Buttons;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import PAC.*;

import javax.swing.*;

public class TopButton extends MineButton
{
    ImageIcon flag = new ImageIcon("textures/flag.png");
    ImageIcon empty = new ImageIcon("textures/0.png");
    public TopButton(int position, Grid g)
    {
        super(position, g);

        TopButton thisButton = this;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    if (!grid.FlagArray.contains(thisButton.position)) {
                        grid.revealCell(thisButton.position);
                        thisButton.setVisible(false);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    toggleFlag();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void toggleFlag(){
        if (grid.FlagArray.contains(this.position))
        {
            this.setIcon(empty);
            grid.FlagArray.remove((Integer) this.position);
        } else
        {
            this.setIcon(flag);
            grid.FlagArray.add(this.position);
        }

    }



}
