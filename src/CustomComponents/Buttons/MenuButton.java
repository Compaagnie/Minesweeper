package CustomComponents.Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JButton {
    MenuButtonUI ui;

    Color paintColor;

    static Color fillColor = new Color(117, 139, 190);
    static Color hoverColor = new Color(66, 108, 204);
    public MenuButton(String text)
    {
        super(text);
        setPreferredSize(new Dimension(200,30));
        setMinimumSize(new Dimension(200,30));
        setBorder(BorderFactory.createEmptyBorder());
        paintColor = fillColor;
        this.ui = new MenuButtonUI();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                paintColor = hoverColor;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                paintColor = fillColor;
            }
        });
    }
    @Override
    public void paintComponent(Graphics pen){
        //super.paintComponent(pen);
        ui.paint(pen, this);
    }

    public Color getPaintColor() {
        return paintColor;
    }
}
