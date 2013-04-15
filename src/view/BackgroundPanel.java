package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * JPanel with a background
 * @author A0086581W, Wong Jing Ping 
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
