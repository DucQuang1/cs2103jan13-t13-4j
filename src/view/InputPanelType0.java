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
import logic.IncomeCatMgr;


/**
 * GUI class that handles an income entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanelType0 extends InputPanel {

	public InputPanelType0(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final IncomeCatMgr incomeCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(assetCatMgr, incomeCatMgr);
		
		inputPanelInput_PNL.setBackground(new Color(160, 190, 220));
		
		JLabel lblType = new JLabel("Asset Type");
		inputPanelInput_PNL.add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> assetTypeCB = new JComboBox<String>();
		for(String assetCat : assetCatMgr.getCategoryNameList())
			assetTypeCB.addItem(assetCat);
		assetTypeCB.addItem("New Category");
		inputPanelInput_PNL.add(assetTypeCB, "cell 1 1,growx");
		
		JLabel lblExpenseCategory = new JLabel("Income Category");
		inputPanelInput_PNL.add(lblExpenseCategory, "cell 2 1,alignx left");
		
		//drop down menu for income categories
		final JComboBox<String> incomeCatCB = new JComboBox<String>();
		for(String incomeCat : incomeCatMgr.getCategoryNameList())
			incomeCatCB.addItem(incomeCat);
		incomeCatCB.addItem("New Category");
		inputPanelInput_PNL.add(incomeCatCB, "cell 3 1,growx");
		
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
				
				//process dd mm yyyy into date
				Date date = null;
				String dateString = Integer.toString(DD) + "/" + Integer.toString(MM) + "/" + Integer.toString(YYYY);
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
				
				//get income category
				if (incomeCatCB.getSelectedIndex() == incomeCatCB.getItemCount()-1){
					category2 = inputPanelCat2_TF.getText();
					//check if new category is unique
					if (incomeCatMgr.checkExisting(category2))
						errorMsg += "Income category already exists.<br>" +
								"Please try a different name.<br>";
					if(category2.indexOf("|") >= 0)
						pipePresent = true;
				}
				else {
					category2 = (String) incomeCatCB.getSelectedItem();
				}
				
				description = inputPanelDescription_TF.getText();
				if(description.indexOf("|") >= 0)
					pipePresent = true;
				
				//check if pipes present
				if(pipePresent){
					errorMsg += "Pipe characters are not supported.<br>" +
							"So sorry about that!<br>";
				}
				
				//if any errors present, display errorMsg, reset errorMsg and boolean flags
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					inputPanelError_LBL.setText(errorMsg);
					errorMsg = "";
					pipePresent = false;
				}
				else {
					
					int id = 1 + entryMgr.getCurrentId();
					
					//update entryMgr
					entryMgr.addEntry(0, amount, date, category1, category2, description);
					
					//update assetCatmgr
					int assetType = assetTypeCB.getSelectedIndex();
					if (assetType == assetTypeCB.getItemCount()-1){
						assetCatMgr.addCategory(category1, amount);
					}
					else{
						assetCatMgr.addAmountToCategory(assetTypeCB.getItemAt(assetType), amount);
					}
					
					//update incomeCatMgr
					int incomeType = incomeCatCB.getSelectedIndex();
					if (incomeType == incomeCatCB.getItemCount()-1){
						incomeCatMgr.addCategory(category2, amount);
					}
					else{
						incomeCatMgr.addAmountToCategory(incomeCatCB.getItemAt(incomeType), amount);
					}
					
					//update historyMgr
					historyMgr.addLog(0, id, 0, amount, date, category1, category2, description);

					resetFields();
					
					hostFrame.dispose();
				}
			}
		});
		inputPanelInput_PNL.add(btnAddEntry, "cell 4 4");

	}
}
