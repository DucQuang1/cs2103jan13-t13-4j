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

//TODO month textfield not showing up properly (too squeezed)

/**
 * GUI class that handles an income entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class TransactionMgrJPanelInc extends InputPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionMgrJPanelInc(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final IncomeCatMgr incomeCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(assetCatMgr, incomeCatMgr);
		
		setBackground(new Color(230, 240, 255));
		
		JLabel lblType = new JLabel("Asset Type");
		add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> assetTypeCB = new JComboBox<String>();
		for(String assetCat : assetCatMgr.getCategoryList())
			assetTypeCB.addItem(assetCat);
		assetTypeCB.addItem("New Category");
		add(assetTypeCB, "cell 1 1,growx");
		
		JLabel lblExpenseCategory = new JLabel("Income Category");
		add(lblExpenseCategory, "cell 2 1,alignx left");
		
		//drop down menu for income categories
		final JComboBox<String> incomeCatCB = new JComboBox<String>();
		for(String incomeCat : incomeCatMgr.getCategoryList())
			incomeCatCB.addItem(incomeCat);
		incomeCatCB.addItem("New Category");
		add(incomeCatCB, "cell 3 1,growx");
		
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
					System.out.println(assetTypeCB.getSelectedIndex());
					category1 = category1Field.getText();
					//check if new category is unique
					if (assetCatMgr.checkExisting(category1))
						errorMsg += "Asset Type already exists.<br>" +
								"You may clear the text field or try a different name.<br>";
				}
				else {
					category1 = (String) assetTypeCB.getSelectedItem();
					System.out.println(assetTypeCB.getSelectedIndex());
				}
				//get income category
				if (incomeCatCB.getSelectedIndex() == incomeCatCB.getItemCount()-1){
					System.out.println(incomeCatCB.getSelectedIndex());
					category2 = category1Field.getText();
					//check if new category is unique
					if (assetCatMgr.checkExisting(category2))
						errorMsg += "Asset Type already exists.<br>" +
								"You may clear the text field or try a different name.<br>";
				}
				else {
					category2 = (String) incomeCatCB.getSelectedItem();
					System.out.println(incomeCatCB.getSelectedIndex());
				}
				
				description = descriptionField.getText();
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					ErrorDisplay.setText(errorMsg);
					errorMsg = "";
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
						incomeCatMgr.addCategory(category1, amount);
					}
					else{
						incomeCatMgr.addAmountToCategory(incomeCatCB.getItemAt(incomeType), amount);
					}
					
					//update historyMgr
					historyMgr.addLog(0, id, 0, amount, date, category1, category2, description);

					hostFrame.dispose();
				}
			}
		});
		add(btnSubmitEntry, "cell 4 4");

	}
}
