package Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Shop extends JPanel
{
    protected ShopModel model;
    protected ShopView view;

    public Shop(boolean isFreeShop, Runnable whenDoneCallback)
    {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        SetPowerUps(isFreeShop);

        this.add(Box.createGlue());

        JButton doneButton = new JButton("Done");
        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE) close(whenDoneCallback);
            }
        });
        doneButton.addActionListener(e -> close(whenDoneCallback));
        this.add(doneButton);
        setFocusable(true);
        requestFocusInWindow();
    }

    protected void close(Runnable callback)
    {
        this.setVisible(false);
        callback.run();
    }

    private void SetPowerUps(boolean isFree)
    {
        JPanel powerUpPanel = new JPanel();
        this.add(powerUpPanel);
        powerUpPanel.setLayout(new BoxLayout(powerUpPanel, BoxLayout.LINE_AXIS));
        powerUpPanel.add(new JButton("1"));
        powerUpPanel.add(new JButton("2"));
        powerUpPanel.add(new JButton("3"));
    }


}
