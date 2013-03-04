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
 * GUI class that handles an liability entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class TransactionMgrJPanelExpCredit extends InputPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionMgrJPanelExpCredit(final JFrame hostFrame, final LiabilityCatMgr liabilityCatMgr, final ExpenseCatMgr expenseCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(liabilityCatMgr, expenseCatMgr);
		
		setBackground(new Color(255, 240, 243));
		
		JLabel lblType = new JLabel("Asset Type");
		add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for asset categories
		final JComboBox<String> liabilityTypeCB = new JComboBox<String>();
		for(String liabilityCat : liabilityCatMgr.getCategoryList())
			liabilityTypeCB.addItem(liabilityCat);
		liabilityTypeCB.addItem("New Category");
		add(liabilityTypeCB, "cell 1 1,growx");
		
		JLabel lblliabilityCategory = new JLabel("liability Category");
		add(lblliabilityCategory, "cell 2 1,alignx left");
		
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
				if (liabilityTypeCB.getSelectedIndex() == liabilityTypeCB.getItemCount()-1){
					System.out.println(liabilityTypeCB.getSelectedIndex());
					category1 = category1Field.getText();
					//check if new category is unique
					if (assetCatMgr.checkExisting(category1))
						errorMsg += "Asset Type already exists.<br>" +
								"You may clear the text field or try a different name.<br>";
				}
				else {
					category1 = (String) liabilityTypeCB.getSelectedItem();
					System.out.println(liabilityTypeCB.getSelectedIndex());
				}
				//get liability category
				if (expenseCatCB.getSelectedIndex() == expenseCatCB.getItemCount()-1){
					System.out.println(expenseCatCB.getSelectedIndex());
					category2 = category1Field.getText();
					//check if new category is unique
					if (assetCatMgr.checkExisting(category2))
						errorMsg += "Asset Type already exists.<br>" +
								"You may clear the text field or try a different name.<br>";
				}
				else {
					category2 = (String) expenseCatCB.getSelectedItem();
					System.out.println(expenseCatCB.getSelectedIndex());
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
					int assetType = liabilityTypeCB.getSelectedIndex();
					if (assetType == liabilityTypeCB.getItemCount()-1){
						assetCatMgr.addCategory(category1, amount);
					}
					else{
						assetCatMgr.addAmountToCategory(liabilityTypeCB.getItemAt(assetType), amount);
					}
					
					//update liabilityCatMgr
					int liabilityType = expenseCatCB.getSelectedIndex();
					if (liabilityType == expenseCatCB.getItemCount()-1){
						liabilityCatMgr.addCategory(category1, amount);
					}
					else{
						liabilityCatMgr.addAmountToCategory(expenseCatCB.getItemAt(liabilityType), amount);
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
