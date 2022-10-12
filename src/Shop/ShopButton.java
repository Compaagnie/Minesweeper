package Shop;

import PAC.Roguelike.PowerUps.PowerUp;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ShopButton extends JButton
{
    boolean isSelected = false;
    private int price = 0;
    public final static int COIN_IMAGE_SIZE = 32;
    public final static Image coinImage = new ImageIcon("textures/powerups/coin.png").getImage().getScaledInstance(COIN_IMAGE_SIZE, COIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
    public final int small_margin = 5; // margin between border and other elements (image - margin - text - margin - border)

    protected Rectangle2D textBounds = null;

    public ShopButton(String s){ super(s); }

    private Dimension computePreferredSize()
    {
        if(textBounds == null) return new Dimension(1,1);
        else
        {
            return new Dimension(2*small_margin + (int) textBounds.getWidth() + 1 + PowerUp.IMAGE_SIZE,
                    2*small_margin + PowerUp.IMAGE_SIZE);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return computePreferredSize();
    }

    @Override
    public Dimension getMinimumSize()
    {
        return this.getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics _pen)
    {
        Graphics2D pen = (Graphics2D) _pen;
        super.paintComponent(pen);
        Rectangle2D newBounds = pen.getFontMetrics().getStringBounds(this.getText(), pen);
        if(this.textBounds != newBounds)
        {
            this.textBounds = newBounds;
            this.setPreferredSize(computePreferredSize());
            this.revalidate();
        }
        Color previousColor = pen.getColor();
        if(isEnabled()) pen.setColor(Color.white);
        else pen.setColor(Color.lightGray);

        pen.fillRect(0,0, this.getWidth(), this.getHeight());

        pen.setColor(Color.black);

        if(isEnabled()) pen.drawImage(((ImageIcon) this.getIcon()).getImage(), small_margin, small_margin, null);
        else this.drawGreyFilteredImage(pen);

        pen.drawString(getText(), small_margin + PowerUp.IMAGE_SIZE, PowerUp.IMAGE_SIZE);

        if(isSelected)
        {
            int stroke = small_margin - 1;
            pen.setStroke(new BasicStroke(stroke));
            if(isSelected) pen.drawRect(stroke-1, stroke-1, this.getWidth() - 2 * stroke, this.getHeight() - 2 * stroke);
            pen.setStroke(new BasicStroke());
        }

        if(this.price != 0)
        {
            String costString = String.valueOf(price);
            Rectangle2D costBound = pen.getFontMetrics().getStringBounds(costString, pen);
            int costWidth = (int) costBound.getWidth();
            int costHeight = (int) costBound.getHeight();

            pen.drawImage(coinImage, getWidth() - COIN_IMAGE_SIZE - small_margin, small_margin, null);
            pen.drawString(costString, getWidth() - COIN_IMAGE_SIZE/2 - costWidth/2 - small_margin, COIN_IMAGE_SIZE/2 + costHeight/2 + small_margin/2);
        }
        pen.setColor(previousColor);
    }

    private void drawGreyFilteredImage(Graphics2D pen)
    {
        Image image = ((ImageIcon) this.getIcon()).getImage();
        BufferedImage copy = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        copy.getGraphics().drawImage(image, 0, 0 , null);
        for(int i = 0; i < copy.getWidth(); ++i)
        {
            for(int j = 0; j < copy.getHeight(); ++j)
            {
                int color = copy.getRGB(i,j); // AA RR GG BB
                int a = (color >> 24) & 0xff;
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                int greyValue = (r + g + b)/3;
                int newColor = a << 24 | greyValue << 16 | greyValue << 8 | greyValue;
                copy.setRGB(i, j, newColor);
            }
        }
        pen.drawImage(copy, small_margin, small_margin, null);
    }

    public void select()
    {
        isSelected = true;
        repaint();
    }
    public void unSelect()
    {
        isSelected = false;
        repaint();
    }

    public void setPrice(int _price){ this.price = _price; }
}
