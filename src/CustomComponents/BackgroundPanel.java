package CustomComponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/*
 *  Support custom painting on a panel in the form of
 *
 *  a) images - that can be scaled, tiled or painted at original size
 *  b) non-solid painting - that can be done by using a Paint object
 *
 *  Also, any component added directly to this panel will be made
 *  non-opaque so that the custom painting can show through.
 *
 * This class cas found here: https://tips4java.wordpress.com/2008/10/12/background-panel/
 */
public class BackgroundPanel extends JPanel
{
	private static Image image;
	{
		try {
			image = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("textures/background.jpeg")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BackgroundPanel(){

	}

	/*
	 *	Set the image used as the background
	 */
	public void setImage(Image image)
	{
		BackgroundPanel.image = image;
		repaint();
	}

	/*
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component)
	{
		add(component, null);
	}

	/*
	 *  Override to provide a preferred size equal to the image size
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if (image == null)
			return super.getPreferredSize();
		else
			return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	/*
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component, Object constraints)
	{
		makeComponentTransparent(component);
		super.add(component, constraints);
	}

	/*
	 *	Try to make the component transparent.
	 *  For components that use renderers, like JTable, you will also need to
	 *  change the renderer to be transparent. An easy way to do this it to
	 *  set the background of the table to a Color using an alpha value of 0.
	 */
	private void makeComponentTransparent(JComponent component)
	{
		component.setOpaque( false );

		if (component instanceof JScrollPane scrollPane)
		{
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque( false );
			Component c = viewport.getView();

			if (c instanceof JComponent)
			{
				((JComponent)c).setOpaque( false );
			}
		}
	}

	/*
	 *  Add custom painting
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawScaled(g);
	}

	/*
	 *  Custom painting code for drawing a SCALED image as the background
	 */
	private void drawScaled(Graphics g)
	{
		Dimension d = getSize();
		g.drawImage(image, 0, 0, d.width, d.height, null);
	}
}
