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

import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.LiabilityCatMgr;


/**
 * GUI class that handles an liability entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanelType2 extends InputPanel {

	public InputPanelType2(final JFrame hostFrame, final LiabilityCatMgr liabilityCatMgr, final ExpenseCatMgr expenseCatMgr,
			final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		super(hostFrame, entryMgr, historyMgr);
		setCatMgr(liabilityCatMgr, expenseCatMgr);
		
		inputPanelInput_PNL.setBackground(new Color(255, 200, 0));
		
		JLabel lblType = new JLabel("Liability Type");
		inputPanelInput_PNL.add(lblType, "cell 0 1,alignx left");
		
		//drop down menu for liability categories
		final JComboBox<String> liabilityTypeCB = new JComboBox<String>();
		for(String liabilityCat : liabilityCatMgr.getCategoryNameList())
			liabilityTypeCB.addItem(liabilityCat);
		liabilityTypeCB.addItem("New Category");
		inputPanelInput_PNL.add(liabilityTypeCB, "cell 1 1,growx");
		
		JLabel lblliabilityCategory = new JLabel("Expense Category");
		inputPanelInput_PNL.add(lblliabilityCategory, "cell 2 1,alignx left");
		
		//drop down menu for expense categories
		final JComboBox<String> expenseCatCB = new JComboBox<String>();
		for(String expenseCat : expenseCatMgr.getCategoryNameList())
			expenseCatCB.addItem(expenseCat);
		expenseCatCB.addItem("New Category");
		inputPanelInput_PNL.add(expenseCatCB, "cell 3 1,growx");
		
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
					amount = ((double)((int) (Double.parseDouble(inputPanelAmount_TF.getText()) * 100))) / 100;
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
	
				//get liability category
				if (liabilityTypeCB.getSelectedIndex() == liabilityTypeCB.getItemCount()-1){
					category1 = inputPanelCat1_TF.getText();
					//check if new category is unique
					if (liabilityCatMgr.checkExisting(category1))
						errorMsg += "Liability Type already exists.<br>" +
								"Please try a different name.<br>";
					if(category1.indexOf("|") >= 0)
						pipePresent = true;
				}
				else {
					category1 = (String) liabilityTypeCB.getSelectedItem();
				}
				//get expense category
				if (expenseCatCB.getSelectedIndex() == expenseCatCB.getItemCount()-1){
					category2 = inputPanelCat2_TF.getText();
					//check if new category is unique
					if (expenseCatMgr.checkExisting(category2))
						errorMsg += "Expense Category already exists.<br>" +
								"Please try a different name.<br>";
					if(category2.indexOf("|") >= 0)
						pipePresent = true;
				}
				else {
					category2 = (String) expenseCatCB.getSelectedItem();
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
					entryMgr.addEntry(2, amount, date, category1, category2, description);
					
					//update liabilityCatmgr
					int liabilityType = liabilityTypeCB.getSelectedIndex();
					if (liabilityType == liabilityTypeCB.getItemCount()-1){
						liabilityCatMgr.addCategory(category1, amount);
					}
					else{
						liabilityCatMgr.addAmountToCategory(liabilityTypeCB.getItemAt(liabilityType), amount);
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
					historyMgr.addLog(0, id, 2, amount, date, category1, category2, description);

					resetFields();
					
					hostFrame.dispose();
				}
			}
		});
		inputPanelInput_PNL.add(btnAddEntry, "cell 4 4");

	}
}
