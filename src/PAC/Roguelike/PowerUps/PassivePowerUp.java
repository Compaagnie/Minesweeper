package PAC.Roguelike.PowerUps;

public enum PassivePowerUp implements PowerUp
{
    DOUBLE_COIN,
    DOUBLE_EDGED_SWORD,
    REVIVE,
    FREE_FIRST_SKILL,
    SHOP_AHEAD,
    EASY_GRID,

    COUNT; // placeholder to get the number of item in this enum (using ordinal)

    public final int mask; // if more than 32 values, switch to long
    public final int shopCost;
    PassivePowerUp()
    {
        // mask = 2^ordinal
        shopCost = 1;
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
    public String getName(){ return String.valueOf(this.ordinal()); }

    @Override
    public boolean isActive() { return false; }
}
