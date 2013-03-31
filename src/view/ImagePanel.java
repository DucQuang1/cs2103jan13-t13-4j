package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * JPanel with a background
 * @author JP
 *
 */
public class ImagePanel extends JPanel {

	private Image image;
	
	public ImagePanel(){}
	
	public ImagePanel(Image image) {
        this.image = image;
    }
	
    public void setImage(Image image) {
        this.image = image;
    }
    
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
