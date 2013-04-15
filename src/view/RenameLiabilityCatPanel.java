package view;

import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logic.EntryMgr;
import logic.HistoryMgr;
import logic.LiabilityCatMgr;

/**
 * Sub-class of RenameCatPanel for Liabilities
 * @author Wong Jing Ping, A0086581W
 *
 */
public class RenameLiabilityCatPanel extends RenameCatPanel{
	
	/**
	 * Constructor of EditLiabilityCatPanel
	 * @param hostFrame
	 * @param liabilityCatMgr
	 * @param entryMgr
	 * @param historyMgr
	 */
	RenameLiabilityCatPanel(final JFrame hostFrame, final LiabilityCatMgr liabilityCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr){
		
		super(hostFrame);
		heading.setText("Rename Liability Category");
		LinkedList<String> categoryList = liabilityCatMgr.getCategoryNameList();
		
		//this block creates the iOS-styled row.
		for(int i = 0; i < categoryList.size(); ++i){

			final Row newRow = new Row(i);
			newRow.lblOldName.setText(categoryList.get(i));
			newRow.btnRename.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e){
					
					//need these 2 final references to maintain immutable reference to iterated row
					final Row currentRow = newRow;
					final JPanel parentPanel = currentRow.rowPanel;
					parentPanel.remove(currentRow.newNameField);	//remove newNameField if already there
					parentPanel.add(currentRow.newNameField, "cell 0 1,growx");
					parentPanel.add(currentRow.btnConfirm, "cell 1 1");
					currentRow.btnConfirm.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent e){
							
							String newName = currentRow.newNameField.getText();
							String errorMsg = "";
							
							if(!hasLetters(newName)){
								errorMsg += "<html>Please enter a valid string with characters.</html>";
							} else if(hasPipe(newName)){
								errorMsg += "<html>Pipe characters are not supported.<br>" +
										"Please try again.</html>";
							} else if(liabilityCatMgr.checkExisting(newName)){
								errorMsg += "<html>That name's taken already. " +
										"Please try another name!</html>";
							}
							if(!errorMsg.equals("")){
								currentRow.newNameField.setText("");
								parentPanel.validate();
								//pop up to inform user
								JOptionPane error_JOP = new JOptionPane();
								error_JOP.setMessage(errorMsg);
								error_JOP.setFont(error_font);
								error_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
								JDialog dialog = error_JOP.createDialog(null);
								dialog.setVisible(true);
							}
							else{
								//updation
								String oldName = currentRow.lblOldName.getText();
								liabilityCatMgr.editCategoryName(oldName, newName);
								entryMgr.renameCat(1, oldName, newName);
								historyMgr.renameCat(1, oldName, newName);
								hostFrame.dispose();
							}
						}
					});
					parentPanel.revalidate();
				}
			});

			scrollPanel.add(newRow.rowPanel);
			scrollPanel.revalidate();
		}
		
		scrollPanel.setLayout(new MigLayout("flowy", "5[grow,left]5", "5[grow,top]5"));
		scrollPane.setViewportView(scrollPanel);
		hostFrame.add(scrollPane, "cell 0 1,grow");
	}

}
