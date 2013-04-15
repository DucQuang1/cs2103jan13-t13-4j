package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;

import logic.EntryMgr;
import logic.HistoryMgr;
import logic.LiabilityCatMgr;

/**
 * Specific TransferPanel for handling transfer between Liability Categories
 * @author A0086581W, Wong Jing Ping
 *
 */
public class TransferPanelLiability extends TransferPanel{
	
	private final LiabilityCatMgr liabilityCatMgr;

	public TransferPanelLiability(final JFrame hostFrame, final LiabilityCatMgr liabilityCatMgr, 
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		super(hostFrame, entryMgr, historyMgr);
		this.liabilityCatMgr = liabilityCatMgr;
		resetFields();
		
		JButton btnSubmitEntry = new JButton("Submit Entry");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				//variables for storing user's inputs. Initialized just to avoid compilation error
				int DD = 01, MM = 0, YYYY = 2013;
				double amount = 0;
				String description;
				
				//check if amount is valid
				try{
					amount = Double.parseDouble(amountField.getText());
					if (amount < 0)
						throw new Exception();
				} catch (Exception exAmount){
					errorMsg += amount + " is negative. Amount cannot be a negative number!<br>";
				}
				
				//check if day of month is valid
				try{
					DD = Integer.parseInt(dd.getText());
					if (DD <= 0 || DD > 31)
						throw new Exception();
				} catch (Exception exDD){
					errorMsg += "Day entered was not a valid number.<br>";
				}
				
				//check if month is valid
				try{
					MM = Integer.parseInt(mm.getText());
					if (MM <= 0 || MM > 12)
						throw new Exception();
				} catch (Exception exMM){
					errorMsg += "Month entered was not a valid number.<br>";
				}
				
				//check if year is valid
				try{
					YYYY  = Integer.parseInt(yyyy.getText());
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
				
				description = descriptionField.getText();
				
				//check if balance in relevant categories sufficient
				String category1 = String.valueOf(fromCB.getSelectedItem());
				String category2 = String.valueOf(toCB.getSelectedItem());
			
				if(amount > liabilityCatMgr.getAmount(category1)){
					errorMsg += "Sorry, you do not have enough money in " + category1 + " for transferring.<br>";
				}

				//check for pipe characters in description field
				description = descriptionField.getText();
				if(description.indexOf("|") >= 0)
					errorMsg += "Pipe characters are not supported.<br>" +
								"So sorry about that!<br>";
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					ErrorDisplay.setText(errorMsg);
					errorMsg = "";
				}
				else {
					
					//update entryMgr
					int currentId = entryMgr.addEntry(6, amount, date, category1, category2, description);
					
					//update historyMgr
					historyMgr.addLog(0, currentId, 6, 
							amount, date, category1, category2, description);
					
					//update LiabilityCatMgr with the new amount
					liabilityCatMgr.addAmountToCategory(category1, -amount);
					liabilityCatMgr.addAmountToCategory(category2, amount);
					
					hostFrame.dispose();
				}
			}
		});
		transferPanel.add(btnSubmitEntry, "cell 4 4");
		
	}
	
	/**
	 * Resets all input fields
	 */
	public void resetFields(){
		 
		resetDefault();
		
		//populate fromCB with updated categories
		LinkedList<String> fromList = liabilityCatMgr.getCategoryList();
		for(String category : fromList)
			fromCB.addItem(category);
		
		//populate toCB with updated categories
		LinkedList<String> toList = liabilityCatMgr.getCategoryList();
		for(String category : toList)
			toCB.addItem(category);
		
		descriptionField.setText("");
	}

}
