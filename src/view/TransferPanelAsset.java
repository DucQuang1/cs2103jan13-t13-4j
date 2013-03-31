package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.HistoryMgr;

/**
 * Specific TransferPanel for handling transfer between Asset Categories
 * @author JP
 *
 */
public class TransferPanelAsset extends TransferPanel{
	
	private final AssetCatMgr assetCatMgr;

	public TransferPanelAsset(final JFrame hostFrame, final AssetCatMgr assetCatMgr, 
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		super(hostFrame, entryMgr, historyMgr);
		this.assetCatMgr = assetCatMgr;
		resetFields();
		
		transferPanelHeading_LBL.setText("Intra Asset Transfer");
		
		JButton btnSubmitEntry = new JButton("Submit Entry");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				//variables for storing user's inputs. Initialized just to avoid compilation error
				int DD = 01, MM = 0, YYYY = 2013;
				double amount = 0;
				String description;
				
				//check if amount is valid
				try{
					amount = Double.parseDouble(transferPanelAmount_TF.getText());
					if (amount < 0)
						throw new Exception();
				} catch (Exception exAmount){
					errorMsg += amount + " is negative. Amount cannot be a negative number!<br>";
				}
				
				//check if day of month is valid
				try{
					DD = Integer.parseInt(transferPanelDD_TF.getText());
					if (DD <= 0 || DD > 31)
						throw new Exception();
				} catch (Exception exDD){
					errorMsg += "Day entered was not a valid number.<br>";
				}
				
				//check if month is valid
				try{
					MM = Integer.parseInt(transferPanelMM_TF.getText());
					if (MM <= 0 || MM > 12)
						throw new Exception();
				} catch (Exception exMM){
					errorMsg += "Month entered was not a valid number.<br>";
				}
				
				//check if year is valid
				try{
					YYYY  = Integer.parseInt(transferPanelYYYY_TF.getText());
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
				
				//check for pipe characters in description field
				description = transferPanelDescription_TF.getText();
				if(description.indexOf("|") >= 0)
					errorMsg += "Pipe characters are not supported.<br>" +
								"So sorry about that!<br>";
				
				//check if balance in relevant categories sufficient
				String category1 = String.valueOf(transferPanelFrom_CB.getSelectedItem());
				String category2 = String.valueOf(transferPanelTo_CB.getSelectedItem());
			
				if(amount > assetCatMgr.getAmount(category1)){
					errorMsg += "Sorry, you do not have enough money in " + category1 + " for transferring.<br>";
				}
				
				//check if both categories are the same
				if(category1.equals(category2)){
					errorMsg += "Both categories are the same.<br>" +
							"You need to select 2 different categories to make a transfer.<br>";
				}
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					transferPanelError_LBL.setText(errorMsg);
					errorMsg = "";
				}
				else {
					
					//update entryMgr
					int currentId = entryMgr.addEntry(5, amount, date, category1, category2, description);
					
					//update historyMgr
					historyMgr.addLog(0, currentId, 5, 
							amount, date, category1, category2, description);
					
					//update assetCatMgr with the new amount
					assetCatMgr.addAmountToCategory(category1, -amount);
					assetCatMgr.addAmountToCategory(category2, amount);
					
					hostFrame.dispose();
				}
			}
		});
		transferPanel_PNL.add(btnSubmitEntry, "cell 4 4");

	}
	
	/**
	 * Resets all input fields
	 */
	public void resetFields(){
		 
		resetDefault();
		
		//populate fromCB with updated categories
		LinkedList<String> fromList = assetCatMgr.getCategoryNameList();
		for(String category : fromList)
			transferPanelFrom_CB.addItem(category);
		
		//populate toCB with updated categories
		LinkedList<String> toList = assetCatMgr.getCategoryNameList();
		for(String category : toList)
			transferPanelTo_CB.addItem(category);
		
		transferPanelDescription_TF.setText("");
	}

}
