package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import data.Entry;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

/**
 * GUI class that contains a JPanel to edit entries
 * @author A0086581W, Wong Jing Ping
 *
 */
public class EditPanel {

	//default format for date and errorMsg
	protected static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//hostFrame refers to the pop-up frame that holds this EditPanel
	protected JFrame hostFrame;
	
	private JPanel editPanel;
	private JTextField amountField;
	private JTextField dd;
	private JTextField mm;
	private JTextField yyyy;
	private JTextField descriptionField;
	
	private JLabel lblTransactionType = new JLabel();
	private JLabel lblAmount = new JLabel("Amount");
	private JLabel lblDate = new JLabel("Date");
	private JLabel lblCategory1 = new JLabel();
	private JLabel lblCategory2 = new JLabel();
	private JLabel lblDescription = new JLabel("Description");
	private JLabel ErrorDisplay = new JLabel();
	private String errorMsg = "";
	
	private final AssetCatMgr assetCatMgr;
	private final LiabilityCatMgr liabilityCatMgr;
	private final IncomeCatMgr incomeCatMgr;
	private final ExpenseCatMgr expenseCatMgr;
	private final EntryMgr entryMgr;
	private final HistoryMgr historyMgr;
	
	private final Entry entry;
	
	/**
	 * Default Constructor for creating the panel.
	 */
	public EditPanel(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr,
			final IncomeCatMgr incomeCatMgr, final ExpenseCatMgr expenseCatMgr, final EntryMgr entryMgr, 
			final HistoryMgr historyMgr, final Entry entry){
		
		//pass reference to the panel for checking and adding of transactions
		this.hostFrame = hostFrame;
		this.assetCatMgr = assetCatMgr;
		this.liabilityCatMgr = liabilityCatMgr;
		this.incomeCatMgr = incomeCatMgr;
		this.expenseCatMgr = expenseCatMgr;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;
		this.entry = entry;
		
		
		editPanel = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[20]5[]5[30]5[50]10[50,grow,top]"));
		editPanel.setBackground(new Color(255, 255, 255));
		editPanel.setSize(700,300);
		
		editPanel.add(lblTransactionType, "cell 0 0 2 1,growx");
		editPanel.add(lblCategory1, "cell 0 1,growx");
		editPanel.add(lblCategory2, "cell 1 1,growx");
		
		editPanel.add(lblAmount, "cell 0 2,alignx left");

		amountField = new JTextField();
		amountField.setColumns(10);
		editPanel.add(amountField, "cell 1 2,growx");
		
		editPanel.add(lblDate, "cell 2 2,alignx left");

		dd = new JTextField();
		dd.setColumns(5);
		editPanel.add(dd, "flowx,cell 3 2,growx");
		
		mm = new JTextField();
		mm.setColumns(5);
		editPanel.add(mm, "cell 3 2,growx");
		
		yyyy = new JTextField();
		editPanel.add(yyyy, "cell 4 2");
		yyyy.setColumns(8);
		
		editPanel.add(lblDescription, "cell 0 3");
		
		descriptionField = new JTextField();
		editPanel.add(descriptionField, "cell 1 3 4 1,grow");
		
		ErrorDisplay.setFont(error_font);
		ErrorDisplay.setForeground(Color.RED);
		editPanel.add(ErrorDisplay, "cell 1 4 3 1,grow");
		
				
		
		
		JButton btnSubmitEntry = new JButton("Edit Entry");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				//variables for storing user's inputs. Initialized just to avoid compilation error
				int DD = 01, MM = 0, YYYY = 2013, transactionType = entry.getTransactionType();
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
				
				
				double difference = amount - entry.getAmount();	//new - old amount
				String category1 = entry.getCategory1();
				String category2 = entry.getCategory2();
				boolean category1insufficient = false;	//true if category insufficient
				boolean category2insufficient = false;
				
				//check if balance in relevant categories sufficient
				switch(transactionType){
				
					case 0:	if(-difference > assetCatMgr.getAmount(category1)){
								category1insufficient = true;
							}	//no need to check income as income can only increase
							break;
					case 1:	if(difference > assetCatMgr.getAmount(category1)){
								category1insufficient = true;
							}	//no need to check expense as expense can only increase
							break;
					case 2: if(-difference > liabilityCatMgr.getAmount(category1)){
								category1insufficient = true;
							}	//no need to check expense as expense can only increase
							break;
					case 3:	if(difference > assetCatMgr.getAmount(category1)){
								category1insufficient = true;
							}
							if(difference > liabilityCatMgr.getAmount(category2)){
								category2insufficient = true;
							}
							break;
					case 4:	if(-difference > assetCatMgr.getAmount(category1)){
								category1insufficient = true;
							}
							if(-difference > liabilityCatMgr.getAmount(category2)){
								category2insufficient = true;
							}
							break;
					case 5:	if(difference > assetCatMgr.getAmount(category1)){
								category1insufficient = true;
							}
							if(-difference > assetCatMgr.getAmount(category2)){
								category2insufficient = true;
							}
							break;
					case 6:	if(difference > liabilityCatMgr.getAmount(category1)){
								category1insufficient = true;
							}
							if(-difference > liabilityCatMgr.getAmount(category2)){
								category2insufficient = true;
							}
							break;
				}
				if(category1insufficient){
					errorMsg += "Sorry, you do not have enough money in " + category1 + " for deduction.<br>";
				}
				if(category2insufficient){
					errorMsg += "Sorry, you do not have enough money in " + category2 + " for deduction.<br>";
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
					
					Entry editedEntry = new Entry(entry.getId(), entry.getTransactionType(), amount,
							date, category1, category2, description);
					
					//update entryMgr
					entryMgr.editEntry(editedEntry);
					
					//update historyMgr
					historyMgr.addLog(1, entry.getId(), entry.getTransactionType(), 
							difference, date, category1, category2, description);
					
					//update 2 relevant out of 4 CatMgrs with the new difference
					switch(transactionType){
						case 0:	assetCatMgr.addAmountToCategory(entry.getCategory1(), difference);
								incomeCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
						case 1:	assetCatMgr.addAmountToCategory(entry.getCategory1(), -difference);
								expenseCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
						case 2:	liabilityCatMgr.addAmountToCategory(entry.getCategory1(), difference);
								expenseCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
						case 3:	assetCatMgr.addAmountToCategory(entry.getCategory1(), -difference);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), -difference);
								break;
						case 4:	assetCatMgr.addAmountToCategory(entry.getCategory1(), difference);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
						case 5:	assetCatMgr.addAmountToCategory(entry.getCategory1(), -difference);
								assetCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
						case 6:	liabilityCatMgr.addAmountToCategory(entry.getCategory1(), -difference);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), difference);
								break;
					}

					hostFrame.dispose();
				}
			}
		});
		editPanel.add(btnSubmitEntry, "cell 4 4");
		populateFields(entry);
		
	}

	
	/**
	 * Gets panel 
	 * @return ediPanel
	 */
	public JPanel getPanel(){
		return editPanel;
	}
	
	/**
	 * Populates fields with the entry provided
	 * @param entry
	 */
	public void populateFields(Entry entry){
		
		int transactionType = entry.getTransactionType();
		
		switch (transactionType){
			case 0:	lblTransactionType.setText("Income Entry:");
					break;
			case 1: lblTransactionType.setText("Expense using Assets:");
					break;
			case 2: lblTransactionType.setText("Expense using Liabilities:");
					break;
			case 3: lblTransactionType.setText("Repay Loan:");
					break;
			case 4: lblTransactionType.setText("Take Loan");
					break;
			case 5:	lblTransactionType.setText("Asset Transfer");
					break;
			case 6:	lblTransactionType.setText("Liability Transfer");
					break;
		}
		amountField.setText(Double.toString(entry.getAmount()));
		String date = date_format.format(entry.getDate());
		StringTokenizer stDate = new StringTokenizer(date,"/");
		dd.setText(stDate.nextToken());
		mm.setText(stDate.nextToken());
		yyyy.setText(stDate.nextToken());
		lblCategory1.setText(entry.getCategory1());
		lblCategory2.setText(entry.getCategory2());
		descriptionField.setText(entry.getDescription());
		editPanel.validate();
	}
}
