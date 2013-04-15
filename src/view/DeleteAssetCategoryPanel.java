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

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.HistoryMgr;
import java.awt.event.ActionListener;

/**
 * GUI class that contains a JPanel to handle category deletion
 * @author A0086581W, Wong Jing Ping
 *
 */
public class DeleteAssetCategoryPanel {
	
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
	
	private JPanel deleteAssetCategory_PNL = new JPanel(new MigLayout("", "[150]5[100]5[100]", "[50]5[30]5[50]5[50]"));
	
	private JLabel deleteAssetCategoryHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
	private JLabel deleteAssetCategorySelect_LBL = new JLabel("Please select a category to delete:");
	private JComboBox<String> deleteAssetCategorySelect_CB = new JComboBox<String>();
	private JButton deleteAssetCategorySelect_BTN = new JButton("Select");
	private JOptionPane deleteAssetCategory_JOP = new JOptionPane();
	
	private LinkedList<Category> categoryList;

	public DeleteAssetCategoryPanel(final JFrame hostFrame,
			final AssetCatMgr assetCatMgr, final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		hostFrame.setSize(500,300);
		hostFrame.getContentPane().add(deleteAssetCategory_PNL);
		
		deleteAssetCategory_PNL.setBackground(new Color(255, 255, 255));
		
		deleteAssetCategoryHeading_LBL.setText("Delete Asset Category");
		deleteAssetCategoryHeading_LBL.setFont(heading_font);
		deleteAssetCategory_PNL.add(deleteAssetCategoryHeading_LBL, "cell 0 0 3 1");
		
		deleteAssetCategory_PNL.add(deleteAssetCategorySelect_LBL, "cell 0 1 ");
		
		this.categoryList = assetCatMgr.getCategoryList();
		for(Category category : categoryList){
			deleteAssetCategorySelect_CB.addItem(category.category);
		}
		deleteAssetCategory_PNL.add(deleteAssetCategorySelect_CB, "cell 1 1");
		deleteAssetCategorySelect_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String selectedCategory = (String) deleteAssetCategorySelect_CB.getSelectedItem();
				for(Category category : categoryList){
					
					//check if the selected category's balance is 0, and delete if it is 0
					if(category.category.equals(selectedCategory) && category.amount == 0.0){
						
						assetCatMgr.deleteCategory(selectedCategory);
						
						//pop up to inform user
						deleteAssetCategory_JOP.setMessage("Deleted Successfully!");
						deleteAssetCategory_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
						JDialog dialog = deleteAssetCategory_JOP.createDialog(null);
						dialog.setVisible(true);
						hostFrame.dispose();
						return;
					}
				}
				//otherwise prompt user to transfer transactions to another category
				deleteAssetCategory_JOP.setMessage("This asset category has a non-zero balance.\n" +
						"You can delete those transactions, or make a transfer to another asset category.");
				deleteAssetCategory_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
				JDialog dialog = deleteAssetCategory_JOP.createDialog("Unable to delete");
				dialog.setVisible(true);
				hostFrame.dispose();
				return;
			}
		});
		
		deleteAssetCategory_PNL.add(deleteAssetCategorySelect_BTN, "cell 2 1");
		
	}

	
}
