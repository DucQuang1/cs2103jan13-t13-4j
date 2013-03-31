package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.HistoryMgr;
import logic.LiabilityCatMgr;


/**
 * GUI class that handles an liability entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanelType3 extends InputPanel {

	public InputPanelType3(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(assetCatMgr, liabilityCatMgr);
		
		inputPanelInput_PNL.setBackground(new Color(255, 185, 215));
		
		JLabel lblType = new JLabel("Asset Type");
		inputPanelInput_PNL.add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> assetTypeCB = new JComboBox<String>();
		for(String assetCat : assetCatMgr.getCategoryList())
			assetTypeCB.addItem(assetCat);
		assetTypeCB.addItem("New Category");
		inputPanelInput_PNL.add(assetTypeCB, "cell 1 1,growx");
		
		JLabel lblLiabilityCategory = new JLabel("Liability Category");
		inputPanelInput_PNL.add(lblLiabilityCategory, "cell 2 1,alignx left");
		
		//drop down menu for liability categories
		final JComboBox<String> liabilityCatCB = new JComboBox<String>();
		for(String liabilityCat : liabilityCatMgr.getCategoryList())
			liabilityCatCB.addItem(liabilityCat);
		liabilityCatCB.addItem("New Category");
		inputPanelInput_PNL.add(liabilityCatCB, "cell 3 1,growx");
		
		JButton btnAddEntry = new JButton("Add Entry");
		btnAddEntry.addActionListener(new ActionListener() {
			
			//variables for storing user's inputs
			boolean pipePresent = false;
			int DD, MM, YYYY;
			double amount;
			String category1, category2, description;
			
			public void actionPerformed(ActionEvent e) {
				
				//check if amount is valid
				try{
					amount = Integer.parseInt(inputPanelAmount_TF.getText());
					if (amount < 0)
						throw new Exception();					
				} catch (Exception exAmount){
					errorMsg += "Amount was not a valid number.<br>";
				}
				
				//check if day of month is valid
				try{
					DD = Integer.parseInt(inputPanelDD_TF.getText());
					if (DD <= 0 || DD > 31)
						throw new Exception();
				} catch (Exception exDD){
					errorMsg += "Day entered was not a valid number.<br>";
				}
				
				//check if month is valid
				try{
					MM = Integer.parseInt(inputPanelMM_TF.getText());
					if (MM <= 0 || MM > 12)
						throw new Exception();
				} catch (Exception exMM){
					errorMsg += "Month entered was not a valid number.<br>";
				}
				
				//check if year is valid
				try{
					YYYY  = Integer.parseInt(inputPanelYYYY_TF.getText());
					if (YYYY < 1900 || YYYY > Calendar.getInstance().get(Calendar.YEAR))
						throw new Exception();
				} catch (Exception exYY){
					errorMsg += "Year entered was not a valid number.<br>";
				}
				
				Date date = null;
				String dateString = Integer.toString(DD) + "/" + Integer.toString(MM) + "/" + Integer.toString(YYYY);
				try {
					date = date_format.parse(dateString);
				} catch (ParseException e1) {
					errorMsg += "Unable to process date.<br>";
				}
	
				//get asset category
				if (assetTypeCB.getSelectedIndex() == assetTypeCB.getItemCount()-1){
					//cannot deduct from new category (illegal)
					errorMsg += "Cannot deduct from a new category.<br>" +
								"You may register an asset in the new category first or deduct from an existing category.<br>";
				}
				else {
					category1 = (String) assetTypeCB.getSelectedItem();
					//check if balance in category sufficient
					if(assetCatMgr.getAmount(category1) < amount){
						errorMsg += "Sorry, you do not have enough money in " + category1 + ".<br>" +
									"Please register an income in " + category1 + " or deduct from another category.<br>";
				}
				
				//get liability category
				if (liabilityCatCB.getSelectedIndex() == liabilityCatCB.getItemCount()-1){
					//cannot deduct from new category (illegal)
					errorMsg += "Cannot deduct from a new category.<br>" +
								"You may register a liability in the new category first or deduct from an existing category.<br>";
				}
				else {
					category2 = (String) liabilityCatCB.getSelectedItem();
				}
				
				description = inputPanelDescription_TF.getText();
				if(description.indexOf("|") >= 0)
					pipePresent = true;
				
				//check if pipes present
				if(pipePresent){
					errorMsg += "Pipe characters are not supported.<br>" +
							"So sorry about that!<br>";
				}
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					inputPanelError_LBL.setText(errorMsg);
					errorMsg = "";
					pipePresent = false;
				}
				else {
					
					int id = 1 + entryMgr.getCurrentId();
					
					//update entryMgr
					entryMgr.addEntry(3, amount, date, category1, category2, description);
					
					//update assetCatmgr
					int assetType = assetTypeCB.getSelectedIndex();
					if (assetType == assetTypeCB.getItemCount()-1){
						assetCatMgr.addCategory(category1, amount);
					}
					else{
						assetCatMgr.addAmountToCategory(assetTypeCB.getItemAt(assetType), -amount);
					}
					
					//update liabilityCatMgr
					int liabilityType = liabilityCatCB.getSelectedIndex();
					if (liabilityType == liabilityCatCB.getItemCount()-1){
						liabilityCatMgr.addCategory(category2, amount);
					}
					else{
						liabilityCatMgr.addAmountToCategory(liabilityCatCB.getItemAt(liabilityType), -amount);
					}
					
					//update historyMgr
					historyMgr.addLog(0, id, 3, amount, date, category1, category2, description);

					resetFields();
					
					hostFrame.dispose();
					}
				}
			}
		});
		inputPanelInput_PNL.add(btnAddEntry, "cell 4 4");
	}
}
