package view;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * This is the parent class (GUI) used for getting user's input when renaming categories
 * @author JP
 *
 */
public class RenameCatPanel{
	
	/**
	 * This private class is meant for creating the GUI for popUpFrame,
	 * which is organized into rows.
	 * Other than index which is immutable, remaining fields can be edited, removed.
	 * @author JP
	 *
	 */
	protected class Row{
		
		private final int index;
		public JPanel rowPanel = new JPanel();
		public JLabel lblOldName = new JLabel();
		public JButton btnRename = new JButton("Rename");
		public JTextField newNameField = new JTextField();
		public JButton btnConfirm = new JButton("Submit");
		public JLabel errorDisplay = new JLabel("Sorry, that name has already been taken");
		
		/**
		 * Constructor that initializes row object with appropriate index
		 * @param index
		 */
		Row(int index){
			this.index = index;
			rowPanel.setBackground(new Color(255, 255, 255));
			rowPanel.setLayout(new MigLayout("", "5[200,grow,left]5[grow,right]5", "5[grow,top]5[grow]5"));
			rowPanel.add(lblOldName, "cell 0 0,flowx");
			rowPanel.add(btnRename, "cell 1 0");
			errorDisplay.setFont(error_font);
		}
		
		/**
		 * Gets the index of the row
		 * Row index is immutable for lifetime of pop-up
		 * @return index
		 */
		public int getIndex(){
			return index;
		}
	}
	
	protected final static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	protected JLabel heading = new JLabel(new ImageIcon(Finances.class.getResource("/img/Tag.png")));
	protected JPanel scrollPanel = new JPanel();
	protected JScrollPane scrollPane = new JScrollPane();
	
	protected final LinkedList<Row> rowList = new LinkedList<Row>();	//immutable reference due to action listeners
	
	RenameCatPanel(JFrame hostFrame){
		
		heading.setBackground(new Color(255, 255, 255));
		heading.setFont(new Font("Tahoma", Font.BOLD, 22));
		hostFrame.getContentPane().add(heading, "cell 0 0");
	}
}
