package view;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;


public class RenameExpenseCatPanel extends RenameCatPanel{
	
	/**
	 * Constructor of EditExpenseCatPanel
	 * @param hostFrame
	 * @param expenseCatMgr
	 * @param entryMgr
	 * @param historyMgr
	 */
	RenameExpenseCatPanel(final JFrame hostFrame, final ExpenseCatMgr expenseCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr){
		
		super(hostFrame);
		heading.setText("Rename Expense Category");
		LinkedList<String> categoryList = expenseCatMgr.getCategoryNameList();
		
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
							
							if(expenseCatMgr.checkExisting(newName)){
								parentPanel.remove(currentRow.newNameField);
								parentPanel.remove(currentRow.btnConfirm);
								currentRow.errorDisplay .setText("<html>That name's taken already. " +
										"Please try another name!</html>");
								currentRow.errorDisplay.setFont(error_font);
								parentPanel.add(currentRow.errorDisplay, "cell 0 0");
								parentPanel.validate();
							}
							else{
								//updation
								String oldName = currentRow.lblOldName.getText();
								expenseCatMgr.editCategoryName(oldName, newName);
								entryMgr.renameCat(3, oldName, newName);
								historyMgr.renameCat(3, oldName, newName);
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
