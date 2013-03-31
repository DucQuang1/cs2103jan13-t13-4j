package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import data.Entry;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
 * @author JP
 *
 */
public class EditPanel {

	//default format for date and errorMsg
	protected static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected static final DecimalFormat double_format = new DecimalFormat("##.00");
	protected static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//hostFrame refers to the pop-up frame that holds this EditPanel
	protected JFrame hostFrame;
	
	//the panel that contains all the GUI fields and buttons
	private JPanel editPanel;
	
	private JTextField editPanelAmount_TF;
	private JTextField editPanelDD_TF;
	private JTextField editPanelMM_TF;
	private JTextField editPanelYYYY_TF;
	private JTextField editPanelDescription_TF;
	
	private JLabel editPanelTransactionType_LBL = new JLabel();
	private JLabel editPanelAmount_LBL = new JLabel("Amount");
	private JLabel editPanelDate_LBL = new JLabel("Date");
	private JLabel editPanelCategory1_LBL = new JLabel();
	private JLabel editPanelCategory2_LBL = new JLabel();
	private JLabel editPanelDescription_LBL = new JLabel("Description");
	private JLabel editPanelError_LBL = new JLabel();
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
		
		//set colour for panel depending on transaction type
		switch(entry.getTransactionType()){
			case 0:	editPanel.setBackground(new Color(160, 190, 220));
					break;
			case 1:
			case 2:	editPanel.setBackground(new Color(255, 200, 0));
					break;
			case 3:
			case 4:	editPanel.setBackground(new Color(255, 185, 215));
					break;
		}
		
		editPanel.add(editPanelTransactionType_LBL, "cell 0 0 2 1,growx");
		editPanel.add(editPanelCategory1_LBL, "cell 0 1,growx");
		editPanel.add(editPanelCategory2_LBL, "cell 1 1,growx");
		
		editPanel.add(editPanelAmount_LBL, "cell 0 2,alignx left");

		editPanelAmount_TF = new JTextField();
		editPanelAmount_TF.setColumns(10);
		editPanel.add(editPanelAmount_TF, "cell 1 2,growx");
		
		editPanel.add(editPanelDate_LBL, "cell 2 2,alignx left");

		editPanelDD_TF = new JTextField();
		editPanelDD_TF.setColumns(5);
		editPanel.add(editPanelDD_TF, "flowx,cell 3 2,growx");
		
		editPanelMM_TF = new JTextField();
		editPanelMM_TF.setColumns(5);
		editPanel.add(editPanelMM_TF, "cell 3 2,growx");
		
		editPanelYYYY_TF = new JTextField();
		editPanel.add(editPanelYYYY_TF, "cell 4 2");
		editPanelYYYY_TF.setColumns(8);
		
		editPanel.add(editPanelDescription_LBL, "cell 0 3");
		
		editPanelDescription_TF = new JTextField();
		editPanel.add(editPanelDescription_TF, "cell 1 3 4 1,grow");
		
		editPanelError_LBL.setFont(error_font);
		editPanelError_LBL.setForeground(Color.RED);
		editPanel.add(editPanelError_LBL, "cell 1 4 3 1,grow");
		
				
		
		
		JButton btnSubmitEntry = new JButton(new ImageIcon(Finances.class.getResource("/img/Tick.png")));
		btnSubmitEntry.setToolTipText("Click to confirm!");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				//variables for storing user's inputs. Initialized just to avoid compilation error
				int DD = 01, MM = 0, YYYY = 2013, transactionType = entry.getTransactionType();
				double amount = 0;
				String description;
				
				//check if amount is valid
				try{
					amount = Double.parseDouble(editPanelAmount_TF.getText());
					if (amount < 0)
						throw new Exception();
				} catch (Exception exAmount){
					errorMsg += amount + " is negative. Amount cannot be a negative number!<br>";
				}
				
				//check if day of month is valid
				try{
					DD = Integer.parseInt(editPanelDD_TF.getText());
					if (DD <= 0 || DD > 31)
						throw new Exception();
				} catch (Exception exDD){
					errorMsg += "Day entered was not a valid number.<br>";
				}
				
				//check if month is valid
				try{
					MM = Integer.parseInt(editPanelMM_TF.getText());
					if (MM <= 0 || MM > 12)
						throw new Exception();
				} catch (Exception exMM){
					errorMsg += "Month entered was not a valid number.<br>";
				}
				
				//check if year is valid
				try{
					YYYY  = Integer.parseInt(editPanelYYYY_TF.getText());
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
				description = editPanelDescription_TF.getText();
				if(description.indexOf("|") >= 0)
					errorMsg += "Pipe characters are not supported.<br>" +
								"So sorry about that!<br>";
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					editPanelError_LBL.setText(errorMsg);
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
			case 0:	editPanelTransactionType_LBL.setText("Income Entry:");
					break;
			case 1: editPanelTransactionType_LBL.setText("Expense using Assets:");
					break;
			case 2: editPanelTransactionType_LBL.setText("Expense using Liabilities:");
					break;
			case 3: editPanelTransactionType_LBL.setText("Repay Loan:");
					break;
			case 4: editPanelTransactionType_LBL.setText("Take Loan");
					break;
			case 5:	editPanelTransactionType_LBL.setText("Asset Transfer");
					break;
			case 6:	editPanelTransactionType_LBL.setText("Liability Transfer");
					break;
		}
		editPanelAmount_TF.setText(double_format.format(entry.getAmount()));
		String date = date_format.format(entry.getDate());
		StringTokenizer stDate = new StringTokenizer(date,"/");
		editPanelDD_TF.setText(stDate.nextToken());
		editPanelMM_TF.setText(stDate.nextToken());
		editPanelYYYY_TF.setText(stDate.nextToken());
		editPanelCategory1_LBL.setText(entry.getCategory1());
		editPanelCategory2_LBL.setText(entry.getCategory2());
		editPanelDescription_TF.setText(entry.getDescription());
		editPanel.validate();
	}
}
