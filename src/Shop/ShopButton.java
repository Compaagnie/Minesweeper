package Shop;

import PAC.Roguelike.PowerUps.PowerUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ShopButton extends JButton
{
    Color paintColor;
    static Color backgroundColor = new Color(0,0,0,0);
    static Color fillColor = new Color(117, 139, 190);
    static Color hoverColor = new Color(66, 108, 204);
    private boolean isSelected = false;
    private int price = 0;
    public final static int COIN_IMAGE_SIZE = 32;
    public static Image coinImage = new ImageIcon("textures/powerups/coin.png").getImage().getScaledInstance(COIN_IMAGE_SIZE, COIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
    public static final int small_margin = 20; // margin between border and other elements (image - margin - text - margin - border)
    static Dimension size = new Dimension( 160, 160);

    public ShopButton(String text)
    {
        super(text);
        coinImage = new ImageIcon("textures/powerups/coin.png").getImage().getScaledInstance(COIN_IMAGE_SIZE, COIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
        setPreferredSize(size);
        setSize(size);
        setBorder(BorderFactory.createEmptyBorder());
        paintColor = fillColor;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                paintColor = hoverColor;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected)
                    paintColor = fillColor;
            }
        });
    }

    @Override
    public Dimension getPreferredSize()
    {
        return size;
    }

    @Override
    public Dimension getMinimumSize()
    {
        return this.getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics pen)
    {
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics bufferedPen = image.getGraphics();


        bufferedPen.setColor(backgroundColor);
        bufferedPen.fillRect(0,0, (int) size.getWidth(), (int) size.getHeight());
        bufferedPen.setColor(paintColor);
        bufferedPen.fillRoundRect(0,0, (int) size.getWidth(), (int) size.getHeight(), 20,20);
        bufferedPen.setColor(Color.black);

        bufferedPen.drawImage(((ImageIcon) getIcon()).getImage(), (int) (getWidth()/2. - PowerUp.IMAGE_SIZE/2), small_margin, null);

        bufferedPen.drawString(getText(), (int) (size.getWidth()/2. - pen.getFontMetrics().getStringBounds(getText(), bufferedPen).getWidth()/2.), (int) (small_margin + PowerUp.IMAGE_SIZE + pen.getFontMetrics().getStringBounds(getText(),pen).getHeight()));

        if(this.price != 0)
        {
            String costString = String.valueOf(price);
            Rectangle2D costBound = pen.getFontMetrics().getStringBounds(costString, pen);
            int costWidth = (int) costBound.getWidth();
            int costHeight = (int) costBound.getHeight();

            int coinImageX = (int) (size.getWidth()/2. - COIN_IMAGE_SIZE/2.);
            int coinImageY = (int) (small_margin + PowerUp.IMAGE_SIZE + pen.getFontMetrics().getStringBounds(getText(),pen).getHeight() + small_margin);

            bufferedPen.drawImage(coinImage, coinImageX, coinImageY, null);
            bufferedPen.drawString(costString, (int) (size.getWidth()/2 - costWidth/2), coinImageY + COIN_IMAGE_SIZE/2 + costHeight/2 -1);
        }

        if(isEnabled())
            pen.drawImage(image, 0, 0, image.getWidth(), image.getWidth(), null);
        else
            pen.drawImage(applyGreyFilter(image), 0, 0, image.getWidth(), image.getWidth(), null);
    }

    /*
    @Override
    public void paintComponent(Graphics _pen)
    {
        this.setEnabled(shop.getCurrencyCount() >= this.price);

        Graphics2D pen = (Graphics2D) _pen;
        super.paintComponent(pen);
        Color previousColor = pen.getColor();

        if(isEnabled()) pen.setColor(Color.white);
        else pen.setColor(Color.lightGray);

        pen.fillRect(0,0, this.getWidth(), this.getHeight());

        pen.setColor(Color.black);

        Dimension iconDimensions;
        if(this.getIcon() != null) iconDimensions = new Dimension(this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
        else iconDimensions = new Dimension(PowerUp.IMAGE_SIZE, PowerUp.IMAGE_SIZE);

        Image iconImage = ((ImageIcon) this.getIcon()).getImage();
        if(!isEnabled() && iconImage != null) iconImage = applyGreyFilter(iconImage);
        pen.drawImage(iconImage, small_margin, small_margin, null);

        pen.drawString(getText(), (int) (small_margin + iconDimensions.width/2. - pen.getFontMetrics().stringWidth(getText())/2.), (int) (iconDimensions.height + pen.getFontMetrics().getStringBounds(getText(),pen).getHeight()));

        if(isSelected)
        {
            int stroke = small_margin - 1;
            pen.setColor(new Color(0x76B5FF));
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

            if(!isEnabled() && coinImage != null && coinImage.getWidth(null) > 0 && coinImage.getHeight(null) > 0)
            {
                pen.drawImage(applyGreyFilter(coinImage), getWidth() - COIN_IMAGE_SIZE - small_margin, small_margin, null);
            }
            else
            {
                pen.drawImage(coinImage, getWidth() - COIN_IMAGE_SIZE - small_margin, small_margin, null);
            }
            pen.drawString(costString, getWidth() - COIN_IMAGE_SIZE/2 - costWidth/2 - small_margin, COIN_IMAGE_SIZE/2 + costHeight/2 + small_margin/2);
        }
        pen.setColor(previousColor);
    }
    */

    private Image applyGreyFilter(Image sourceImage)
    {
        // https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
        // -> image to buffered image

        BufferedImage copy = new BufferedImage(sourceImage.getWidth(null), sourceImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        copy.getGraphics().drawImage(sourceImage, 0, 0 , null);

        // https://stackoverflow.com/questions/39667665/rgb-image-filter-in-java
        // -> finding how to manipulate colors in a buffered image

        for(int i = 0; i < copy.getWidth(); ++i)
        {
            for(int j = 0; j < copy.getHeight(); ++j)
            {
                // 0x AA RR GG BB
                int color = copy.getRGB(i,j);
                int alpha = (color >> 24) & 0xff;
                int red = (color >> 16) & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = color & 0xff;
                int greyValue = (red + green + blue)/3;
                int newColor = alpha << 24 | greyValue << 16 | greyValue << 8 | greyValue;
                copy.setRGB(i, j, newColor);
            }
        }

        return copy;
    }

    public void select()
    {
        paintColor = hoverColor;
        isSelected = true;
        repaint();
    }
    public void unSelect()
    {
        paintColor = fillColor;
        isSelected = false;
        repaint();
    }

    public void setSelected(Boolean bool){
        if (bool)
            select();
        else
            unSelect();
    }

    public void setPrice(int _price){ this.price = _price; }
}
