package Shop;

import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.PowerUps.PassivePowerUp;
import PAC.Roguelike.PowerUps.PowerUp;
import PAC.Roguelike.RoguelikeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Shop extends JPanel
{
    protected PassivePowerUp selectedPassive = null;
    protected ActivePowerUp selectedActive = null;
    protected ShopButton currentSelectedButton = null;

    protected RoguelikeModel roguelikeModel;

    public Shop(RoguelikeModel roguelikeModel, boolean isFreeShop, Runnable whenDoneCallback)
    {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.roguelikeModel = roguelikeModel;

        SetPowerUps(isFreeShop);

        this.add(Box.createGlue());

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> close(whenDoneCallback));
        this.add(doneButton);

        setFocusable(true);
        requestFocusInWindow();
    }

    protected void close(Runnable callback)
    {
        // Not free shops : only one power up added : the last one selected
        if(selectedActive != null) roguelikeModel.add(selectedActive);
        if(selectedPassive != null) roguelikeModel.add(selectedPassive);
        callback.run();
    }

    private void SetPowerUps(boolean isFree)
    {
        JPanel powerUpPanel = new JPanel();
        this.add(powerUpPanel);
        powerUpPanel.setLayout(new BoxLayout(powerUpPanel, BoxLayout.LINE_AXIS));

        // todo : random power ups, active and passive, depending on what's missing or not
        //  actual view with highlight on selected thing

        ArrayList<PowerUp> missingPowerUps = new ArrayList<>();

        for(int i = 0; i < PassivePowerUp.COUNT.ordinal(); ++i)
        {
            PassivePowerUp powerUp = PassivePowerUp.values()[i];
            if(!roguelikeModel.has(powerUp)) missingPowerUps.add(powerUp);
        }

        for(int i = 0; i < ActivePowerUp.COUNT.ordinal(); ++i)
        {
            ActivePowerUp powerUp = ActivePowerUp.values()[i];
            if(!roguelikeModel.has(powerUp)) missingPowerUps.add(powerUp);
        }

        //todo : move to constants
        while(missingPowerUps.size() > (isFree? 3 : 5))
        {
            int toRemove = (new Random().nextInt()) % missingPowerUps.size();
            if(toRemove < 0) toRemove += missingPowerUps.size();
            missingPowerUps.remove(toRemove);
        }

        for(PowerUp powerUp : missingPowerUps)
        {
            ShopButton button = new ShopButton(powerUp.getName());
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            Image powerUpImage = powerUp.getImage();
            if(powerUpImage != null) button.setIcon(new ImageIcon(powerUp.getImage()));
            else System.out.println("[WARNING] Could not find image for power up: " + powerUp.getName());
            if(!isFree) button.addActionListener(e -> buy(powerUp, button));
            else button.addActionListener(e -> select(powerUp, button));
            if(!isFree)
            {
                button.setPrice(powerUp.getShopCost());
                if(powerUp.getShopCost() > roguelikeModel.getCurrencyCount()) button.setEnabled(false);
            }
            powerUpPanel.add(button);
        }
        this.repaint();
    }

    private void select(PowerUp powerUp, ShopButton button)
    {
        if(powerUp.isActive())
        {
            selectedActive = (ActivePowerUp) powerUp;
            selectedPassive = null;
        }
        else
        {
            selectedActive = null;
            selectedPassive = (PassivePowerUp) powerUp;
        }
        if(currentSelectedButton != null) currentSelectedButton.unSelect();
        currentSelectedButton = button;
        currentSelectedButton.select();
    }

    private void buy(PowerUp powerUp, ShopButton button)
    {
        if(roguelikeModel.getCurrencyCount() >= powerUp.getShopCost())
        {
            button.setVisible(false);
            this.remove(button);
            if(powerUp.isActive()) roguelikeModel.add((ActivePowerUp) powerUp);
            else roguelikeModel.add((PassivePowerUp) powerUp);
            roguelikeModel.updateCurrency(-powerUp.getShopCost());
        }
    }


}
