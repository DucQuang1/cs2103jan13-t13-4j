import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;

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
					ErrorDisplay.setText(errorMsg);
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
		transferPanel.add(btnSubmitEntry, "cell 4 4");

	}
	
	/**
	 * Resets all input fields
	 */
	public void resetFields(){
		 
		resetDefault();
		
		//populate fromCB with updated categories
		LinkedList<String> fromList = assetCatMgr.getCategoryList();
		for(String category : fromList)
			fromCB.addItem(category);
		
		//populate toCB with updated categories
		LinkedList<String> toList = assetCatMgr.getCategoryList();
		for(String category : toList)
			toCB.addItem(category);
		
		descriptionField.setText("");
	}

}
