package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * JPanel with a background
 * @author Wong Jing Ping, A0086581W
 */
public class BackgroundPanel extends JPanel {

	private Image image;
	
	public BackgroundPanel(){}
	
	public BackgroundPanel(Image image) {
        this.image = image;
    }
	
    public void setImage(Image image) {
        this.image = image;
    }
    
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
