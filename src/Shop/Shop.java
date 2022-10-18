package Shop;

import CustomComponents.BackgroundPanel;
import CustomComponents.Buttons.MenuButton;
import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.PowerUps.PassivePowerUp;
import PAC.Roguelike.PowerUps.PowerUp;
import PAC.Roguelike.RoguelikeModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Shop extends JPanel
{
    protected PassivePowerUp selectedPassive = null;
    protected ActivePowerUp selectedActive = null;
    protected ShopButton currentSelectedButton = null;
    protected RoguelikeModel roguelikeModel;
    public final static int POWER_UP_IMAGE_SIZE = 64;

    ArrayList<ShopButton> shopItems = new ArrayList<>();

    JPanel powerUpPanel;

    public Shop(RoguelikeModel roguelikeModel, boolean isFreeShop, Runnable whenDoneCallback)
    {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);
        this.roguelikeModel = roguelikeModel;

        powerUpPanel = new JPanel();
        powerUpPanel.setOpaque(false);
        this.add(powerUpPanel, constraints);

        MenuButton doneButton = new MenuButton("Done");
        doneButton.addActionListener(e -> close(whenDoneCallback));
        constraints.gridy++;
        this.add(doneButton, constraints);

        SetPowerUps(isFreeShop);
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
        powerUpPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(10,10,10,10);
        // todo : random power ups, active and passive, depending on what's missing or not
        //  actual view with highlight on selected thing

        ArrayList<PowerUp> missingPowerUps = new ArrayList<>();

        for(int i = 0; i < PassivePowerUp.COUNT.ordinal(); ++i)
        {
            PassivePowerUp powerUp = PassivePowerUp.values()[i];
            if(!roguelikeModel.has(powerUp) && /*not shop in shop*/!(!isFree && powerUp == PassivePowerUp.SHOP_AHEAD))
                missingPowerUps.add(powerUp);
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
            ShopButton button = new ShopButton(this, powerUp.getName());
            Image powerUpImage = powerUp.getImage();
            if(powerUpImage != null) button.setIcon(new ImageIcon(powerUpImage.getScaledInstance(POWER_UP_IMAGE_SIZE, POWER_UP_IMAGE_SIZE, Image.SCALE_SMOOTH)));
            else System.out.println("[WARNING] Could not find image for power up: " + powerUp.getName());
            if(isFree) button.addActionListener(e -> select(powerUp, button));
            else
            {
                button.addActionListener(e -> buy(powerUp, button));
                button.setPrice(powerUp.getShopCost());
                button.setEnabled(powerUp.getShopCost() <= roguelikeModel.getCurrencyCount());
            }
            button.setToolTipText(powerUp.getDescription());
            shopItems.add(button);
            powerUpPanel.add(button, constraints);
            constraints.gridx++;
            button.repaint();button.revalidate();
        }

        if(missingPowerUps.size() == 0) powerUpPanel.add(new JLabel("Congratulations ! You have acquired all power ups"));

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
            shopItems.remove(button);
            if(shopItems.size()==0) powerUpPanel.add(new JLabel("No power up available"));
        }
    }

    public int getCurrencyCount()
    {
        return roguelikeModel.getCurrencyCount();
    }
}
