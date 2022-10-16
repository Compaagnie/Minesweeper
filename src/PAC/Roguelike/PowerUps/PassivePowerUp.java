package PAC.Roguelike.PowerUps;

import javax.swing.*;
import java.awt.*;

public enum PassivePowerUp implements PowerUp
{
    DOUBLE_COIN,
    DOUBLE_EDGED_SWORD,
    REVIVE(10),
    FREE_FIRST_SKILL(10),
    SHOP_AHEAD(3),
    EASY_GRID,

    COUNT; // placeholder to get the number of item in this enum (using ordinal)

    public final int mask; // if more than 32 values, switch to long
    public final int shopCost;
    PassivePowerUp()
    {
        // mask = 2^ordinal
        shopCost = 5;
        this.mask = 1 << this.ordinal();
    }

    PassivePowerUp(int cost)
    {
        this.mask = 1 << this.ordinal();
        shopCost = cost;
    }

    @Override
    public int getShopCost()
    {
        return shopCost;
    }

    @Override
    public String getName()
    {
        if(this == PassivePowerUp.DOUBLE_COIN) return "Double Coin";
        else if(this == PassivePowerUp.DOUBLE_EDGED_SWORD) return "Double edged sword";
        else if(this == PassivePowerUp.REVIVE) return "Revive";
        else if(this == PassivePowerUp.FREE_FIRST_SKILL) return "Free first skill";
        else if(this == PassivePowerUp.SHOP_AHEAD) return "Shop ahead";
        else if(this == PassivePowerUp.EASY_GRID) return "Easy grid";
        else return "unassigned name";
    }

    @Override
    public String getDescription()
    {
        if(this == PassivePowerUp.DOUBLE_COIN) return "Doubles the coin gained from grid clears";
        else if(this == PassivePowerUp.DOUBLE_EDGED_SWORD) return "+50% bombs but also +200%coins";
        else if(this == PassivePowerUp.REVIVE) return "Revive once";
        else if(this == PassivePowerUp.FREE_FIRST_SKILL) return "The first skill is free";
        else if(this == PassivePowerUp.SHOP_AHEAD) return "There will be a shop after this power-up";
        else if(this == PassivePowerUp.EASY_GRID) return "The next grid will be easier (-50% bombs)";
        else return "unassigned description";
    }

    @Override
    public Image getImage()
    {
        String iconName;
        Dimension imgDimensions = new Dimension(PowerUp.IMAGE_SIZE, PowerUp.IMAGE_SIZE);
        if(this == PassivePowerUp.DOUBLE_COIN) iconName = "double_coin.png";
        else if(this == PassivePowerUp.DOUBLE_EDGED_SWORD) iconName = "double_edged_sword.png";
        else if(this == PassivePowerUp.REVIVE) iconName = "revive.png";
        else if(this == PassivePowerUp.FREE_FIRST_SKILL) iconName = "free.png";
        else if(this == PassivePowerUp.SHOP_AHEAD) iconName = "shop_ahead.png";
        else if(this == PassivePowerUp.EASY_GRID) iconName = "easymode.png";
        else return null;
        return new ImageIcon("textures/powerups/" + iconName).getImage().getScaledInstance(imgDimensions.width,imgDimensions.height, Image.SCALE_SMOOTH);
    }

    @Override
    public boolean isActive() { return false; }
}
