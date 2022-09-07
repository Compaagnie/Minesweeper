package Buttons;

import PAC.*;

public class BottomButton extends MineButton
{
    public BottomButton(int position, Grid g)
    {
        super(position, g);
        //this.setText("bot");
        this.addActionListener(e -> grid.propagateReveal(this.position)); //Call to the model : update surroundings
    }
}
