package CustomComponents;

import javax.swing.*;
import java.awt.*;

public class VSlider extends JComponent
{
    private Integer value;
    private Integer min;
    private Integer max;

    private Color fillColor = Color.darkGray;
    private Color backColor;
    private Color outlineColor;

    public void setMinimum(int _min) {this.min = _min;}
    public void setMaximum(int _max) {this.max = _max;}
    public void setValue(int _value) {this.value = _value;}
    public void setFillColor(Color _color) {this.fillColor = _color;}
    public void setBackColor(Color _color) {this.backColor = _color;}
    public void setOutlineColor(Color _color) {this.outlineColor = _color;}

    @Override
    public Dimension getPreferredSize()
    {
        if(min == null || max == null || value == null) return super.getPreferredSize();
        else return new Dimension(10, this.getParent().getHeight());
    }

    @Override
    public void paintComponent(Graphics _p)
    {
        super.paintComponent(_p);
        if(min != null && max != null && value != null)
        {
            Graphics2D pen = (Graphics2D) _p;
            Color previousColor = pen.getColor();
            if(outlineColor != null)
            {
                pen.setColor(outlineColor);
                pen.drawRect(0,0,this.getWidth(),this.getHeight());
            }
            if(backColor != null)
            {
                pen.setColor(backColor);
                pen.fillRect(1,1,this.getWidth()-1, this.getHeight()-1);
            }
            if(fillColor != null)
            {
                pen.setColor(fillColor);
                float t = (value-min)/(float)(max-min);
                int fillHeight = this.getHeight() - (int)(t * this.getHeight());
                pen.fillRect(1,fillHeight,this.getWidth()-1, this.getHeight()-1);
            }
            pen.setColor(previousColor);
        }
    }
}
