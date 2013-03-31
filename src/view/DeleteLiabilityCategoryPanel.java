package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import data.Category;

import net.miginfocom.swing.MigLayout;

import logic.LiabilityCatMgr;
import logic.EntryMgr;
import logic.HistoryMgr;
import java.awt.event.ActionListener;

/**
 * GUI class that contains a JPanel to handle category deletion
 * @author JP
 *
 */
public class DeleteLiabilityCategoryPanel {
	
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
		
	private JPanel deleteLiabilityCategory_PNL = new JPanel(new MigLayout("", "[150]5[100]5[100]", "[50]5[30]5[50]5[50]"));
	
	private JLabel deleteLiabilityCategoryHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
	private JLabel deleteLiabilityCategorySelect_LBL = new JLabel("Please select a category to delete:");
	private JComboBox<String> deleteLiabilityCategorySelect_CB = new JComboBox<String>();
	private JButton deleteLiabilityCategorySelect_BTN = new JButton("Select");
	private JOptionPane deleteLiabilityCategory_JOP = new JOptionPane();
	
	private LinkedList<Category> categoryList;

	public DeleteLiabilityCategoryPanel(final JFrame hostFrame,
			final LiabilityCatMgr liabilityCatMgr, final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		hostFrame.setSize(500,300);
		hostFrame.getContentPane().add(deleteLiabilityCategory_PNL);
		
		deleteLiabilityCategory_PNL.setBackground(new Color(255, 255, 255));
		
		deleteLiabilityCategoryHeading_LBL.setText("Delete Liability Category");
		deleteLiabilityCategoryHeading_LBL.setFont(heading_font);
		deleteLiabilityCategory_PNL.add(deleteLiabilityCategoryHeading_LBL, "cell 0 0 3 1");
		
		deleteLiabilityCategory_PNL.add(deleteLiabilityCategorySelect_LBL, "cell 0 1 ");
		
		this.categoryList = liabilityCatMgr.getCategoryList();
		for(Category category : categoryList){
			deleteLiabilityCategorySelect_CB.addItem(category.category);
		}
		deleteLiabilityCategory_PNL.add(deleteLiabilityCategorySelect_CB, "cell 1 1");
		deleteLiabilityCategorySelect_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String selectedCategory = (String) deleteLiabilityCategorySelect_CB.getSelectedItem();
				for(Category category : categoryList){
					
					//check if the selected category's balance is 0, and delete if it is 0
					if(category.category.equals(selectedCategory) && category.amount == 0.0){
						
						liabilityCatMgr.deleteCategory(selectedCategory);
						
						//pop up to inform user
						deleteLiabilityCategory_JOP.setMessage("Deleted Successfully!");
						deleteLiabilityCategory_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
						JDialog dialog = deleteLiabilityCategory_JOP.createDialog(null);
						dialog.setVisible(true);
						hostFrame.dispose();
						return;
					}
				}
				//otherwise prompt user to transfer transactions to another category
				deleteLiabilityCategory_JOP.setMessage("This Liability category has a non-zero balance.\n" +
						"You can delete those transactions, or make a transfer to another Liability category.");
				deleteLiabilityCategory_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
				JDialog dialog = deleteLiabilityCategory_JOP.createDialog("Unable to delete");
				dialog.setVisible(true);
				hostFrame.dispose();
				return;
			}
		});
		
		deleteLiabilityCategory_PNL.add(deleteLiabilityCategorySelect_BTN, "cell 2 1");
		
	}

	
}
