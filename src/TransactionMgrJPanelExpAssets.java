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


/**
 * GUI class that handles an expense entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class TransactionMgrJPanelExpAssets extends InputPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionMgrJPanelExpAssets(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final ExpenseCatMgr expenseCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(assetCatMgr, expenseCatMgr);
		
		setBackground(new Color(255, 240, 245));
		
		JLabel lblType = new JLabel("Asset Type");
		add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> assetTypeCB = new JComboBox<String>();
		for(String assetCat : assetCatMgr.getCategoryList())
			assetTypeCB.addItem(assetCat);
		assetTypeCB.addItem("New Category");
		add(assetTypeCB, "cell 1 1,growx");
		
		JLabel lblExpenseCategory = new JLabel("Expense Category");
		add(lblExpenseCategory, "cell 2 1,alignx left");
		
		//drop down menu for expense categories
		final JComboBox<String> expenseCatCB = new JComboBox<String>();
		for(String expenseCat : expenseCatMgr.getCategoryList())
			expenseCatCB.addItem(expenseCat);
		expenseCatCB.addItem("New Category");
		add(expenseCatCB, "cell 3 1,growx");
		
		JButton btnSubmitEntry = new JButton("Submit Entry");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			//variables for storing user's inputs
			int DD, MM, YYYY;
			double amount;
			String category1, category2, description;
			
			public void actionPerformed(ActionEvent e) {
				
				//check if amount is valid
				try{
					amount = Integer.parseInt(amountField.getText());
					if (amount < 0)
						throw new Exception();
				} catch (Exception exAmount){
					errorMsg += "Amount was not a valid number.<br>";
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
				
				Date date = null;
				String dateString = Integer.toString(DD) + "/" + Integer.toString(MM) + "/" + Integer.toString(YYYY%100);
				try {
					date = date_format.parse(dateString);
				} catch (ParseException e1) {
					errorMsg += "Unable to process date.<br>";
				}
	
				//get asset category
				if (assetTypeCB.getSelectedIndex() == assetTypeCB.getItemCount()-1){
					//cannot deduct from new category (illegal)
					errorMsg += "Cannot deduct from a new category.<br>" +
								"You may register an income in the new category first or deduct from an existing category.<br>";
				}
				else {
					category1 = (String) assetTypeCB.getSelectedItem();
					//check if balance in category sufficient
					if(assetCatMgr.getAmount(category1) < amount){
						errorMsg += "Sorry, you do not have enough money in " + category1 + ".<br>" +
									"Please register an income in " + category1 + " or deduct from another category.<br>";
					}
				}
				//get expense category
				if (expenseCatCB.getSelectedIndex() == expenseCatCB.getItemCount()-1){
					category2 = category2Field.getText();
					//check if new category is unique
					if (expenseCatMgr.checkExisting(category2))
						errorMsg += "Expense Category already exists.<br>" +
								"Please try a different name.<br>";
				}
				else {
					category2 = (String) expenseCatCB.getSelectedItem();
				}
				
				description = descriptionField.getText();
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					ErrorDisplay.setText(errorMsg);
					errorMsg = "";
					descriptionField.setText("");
				}
				else {

					int id = 1 + entryMgr.getCurrentId();
					
					//update entryMgr
					entryMgr.addEntry(1, amount, date, category1, category2, description);
					
					//update assetCatmgr
					int assetType = assetTypeCB.getSelectedIndex();
					if (assetType == assetTypeCB.getItemCount()-1){
						assetCatMgr.addCategory(category1, amount);
					}
					else{						//note assets are reduced for this transaction type
						assetCatMgr.addAmountToCategory(assetTypeCB.getItemAt(assetType), -amount);
					}
					
					//update expenseCatMgr
					int expenseType = expenseCatCB.getSelectedIndex();
					if (expenseType == expenseCatCB.getItemCount()-1){
						expenseCatMgr.addCategory(category2, amount);
					}
					else{
						expenseCatMgr.addAmountToCategory(expenseCatCB.getItemAt(expenseType), amount);
					}
					
					//update historyMgr
					historyMgr.addLog(0, id, 1, amount, date, category1, category2, description);
					
					resetFields();
					
					hostFrame.dispose();
				}
			}
		});
		add(btnSubmitEntry, "cell 4 4");

	}
}
