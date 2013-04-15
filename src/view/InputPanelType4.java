package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.HistoryMgr;
import logic.LiabilityCatMgr;


/**
 * GUI class that handles an liability entry
 * @author A0087809M, Pang Kang Wei, Joshua
 *
 */
public class InputPanelType4 extends InputPanel {

	public InputPanelType4(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(assetCatMgr, liabilityCatMgr);
		
		inputPanelInput_PNL.setBackground(new Color(255, 185, 215));
		
		JLabel lblType = new JLabel("Asset Type");
		inputPanelInput_PNL.add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> assetTypeCB = new JComboBox<String>();
		for(String assetCat : assetCatMgr.getCategoryNameList())
			assetTypeCB.addItem(assetCat);
		assetTypeCB.addItem("New Category");
		inputPanelInput_PNL.add(assetTypeCB, "cell 1 1,growx");
		
		JLabel lblLiabilityCategory = new JLabel("Liability Category");
		inputPanelInput_PNL.add(lblLiabilityCategory, "cell 2 1,alignx left");
		
		//drop down menu for liability categories
		final JComboBox<String> liabilityCatCB = new JComboBox<String>();
		for(String liabilityCat : liabilityCatMgr.getCategoryNameList())
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


				//Check if date is valid
				DD = Integer.parseInt(inputPanelDD_TF.getText());
				MM = Integer.parseInt(inputPanelMM_TF.getText());
				YYYY = Integer.parseInt(inputPanelYYYY_TF.getText());
				String formatString = "dd/MM/yyyy";
				String dateString = Integer.toString(DD) + "/" + Integer.toString(MM) + "/" + Integer.toString(YYYY);

				try {
					SimpleDateFormat format = new SimpleDateFormat(formatString);
					format.setLenient(false);
					format.parse(dateString);
				} catch (ParseException e1) {
					errorMsg += "Invalid date.<br>";
				} catch (IllegalArgumentException e2) {
					errorMsg += "Invalid date.<br>";
				}
					
				Date date = null;
				try {
					date = date_format.parse(dateString);
				} catch (ParseException e1) {
					errorMsg += "Unable to process date.<br>";
				}
				
				//check if date is before today
				Date today = new Date();
				if (today.before(date)){
					errorMsg += "Date given is in the future!.<br>";
				}
	
				//get asset category
				if (assetTypeCB.getSelectedIndex() == assetTypeCB.getItemCount()-1){
					category1 = inputPanelCat1_TF.getText();
					//check if new category is unique
					if (assetCatMgr.checkExisting(category1))
						errorMsg += "Asset Type already exists.<br>" +
								"Please try a different name.<br>";
					if(category1.indexOf("|") >= 0)
						pipePresent = true;
				}
				else {
					category1 = (String) assetTypeCB.getSelectedItem();
				}
				//get liability category
				if (liabilityCatCB.getSelectedIndex() == liabilityCatCB.getItemCount()-1){
					category2 = inputPanelCat2_TF.getText();
					//check if new category is unique
					if (liabilityCatMgr.checkExisting(category2))
						errorMsg += "Asset Type already exists.<br>" +
								"Please try a different name.<br>";
					if(category2.indexOf("|") >= 0)
						pipePresent = true;
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
					entryMgr.addEntry(4, amount, date, category1, category2, description);
					
					//update assetCatmgr
					int assetType = assetTypeCB.getSelectedIndex();
					if (assetType == assetTypeCB.getItemCount()-1){
						assetCatMgr.addCategory(category1, amount);
					}
					else{
						assetCatMgr.addAmountToCategory(assetTypeCB.getItemAt(assetType), amount);
					}
					
					//update liabilityCatMgr
					int liabilityType = liabilityCatCB.getSelectedIndex();
					if (liabilityType == liabilityCatCB.getItemCount()-1){
						liabilityCatMgr.addCategory(category2, amount);
					}
					else{
						liabilityCatMgr.addAmountToCategory(liabilityCatCB.getItemAt(liabilityType), amount);
					}
					
					//update historyMgr
					historyMgr.addLog(0, id, 4, amount, date, category1, category2, description);
					
					resetFields();
					
					hostFrame.dispose();
				}
			}
		});
		inputPanelInput_PNL.add(btnAddEntry, "cell 4 4");

	}
}
