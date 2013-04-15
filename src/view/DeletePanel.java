package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.Entry;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;
import net.miginfocom.swing.MigLayout;

/**
 * GUI class that contains a JPanel to edit entries
 * @author A0086581W, Wong Jing Ping
 *
 */
public class DeletePanel {

	//default format for date
	protected static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//hostFrame refers to the pop-up frame that holds this DeletePanel
	protected JFrame hostFrame;
	
	private JPanel deletePanel;

	private JLabel lblTransactionType = new JLabel();
	private JLabel lblAmount = new JLabel("Amount");
	private JLabel amountField = new JLabel();
	private JLabel lblDate = new JLabel("Date");
	private JLabel dd = new JLabel();
	private JLabel mm = new JLabel();
	private JLabel yyyy = new JLabel();
	
	private JLabel lblCategory1 = new JLabel();
	private JLabel lblCategory2 = new JLabel();
	private JLabel lblDescription = new JLabel("Description");
	private JLabel descriptionField = new JLabel();
	private JLabel ErrorDisplay = new JLabel();
	private String errorMsg = "";
	
	private final AssetCatMgr assetCatMgr;
	private final LiabilityCatMgr liabilityCatMgr;
	private final IncomeCatMgr incomeCatMgr;
	private final ExpenseCatMgr expenseCatMgr;
	private final EntryMgr entryMgr;
	private final HistoryMgr historyMgr;
	
	private final Entry entry;
	
	public DeletePanel(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr,
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
		
		deletePanel = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[20]5[]5[30]5[50]10[50,grow,top]"));
		deletePanel.setBackground(new Color(255, 255, 255));
		deletePanel.setSize(700,300);
		
		deletePanel.add(lblTransactionType, "cell 0 0 2 1,growx");
		deletePanel.add(lblCategory1, "cell 0 1,growx");
		deletePanel.add(lblCategory2, "cell 1 1,growx");
		deletePanel.add(lblAmount, "cell 0 2,alignx left");
		deletePanel.add(amountField, "cell 1 2,growx");
		deletePanel.add(lblDate, "cell 2 2,alignx left");
		deletePanel.add(dd, "flowx,cell 3 2,growx");
		deletePanel.add(mm, "cell 3 2,growx");
		deletePanel.add(yyyy, "cell 4 2");
		deletePanel.add(lblDescription, "cell 0 3");
		deletePanel.add(descriptionField, "cell 1 3 4 1,grow");
		
		ErrorDisplay.setFont(error_font);
		ErrorDisplay.setForeground(Color.RED);
		deletePanel.add(ErrorDisplay, "cell 1 4 3 1,grow");
		
		JButton btnSubmitEntry = new JButton("Delete Entry");
		btnSubmitEntry.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				int transactionType = entry.getTransactionType();
				double amount = entry.getAmount();
				
				//check if balance in relevant categories sufficient
				String category1 = entry.getCategory1();
				String category2 = entry.getCategory2();
				boolean category1insufficient = false;	//true if category insufficient
				boolean category2insufficient = false;
				switch(transactionType){
					case 0:	if(amount > assetCatMgr.getAmount(category1)){	//if deduction > current
								category1insufficient = true;
							}	//no need to check income as income can only increase
							break;
					case 1:	//no need to check asset as deleting this type will only increase asset
							//no need to check expense as expense can only increase
							break;
					case 2: if(amount > liabilityCatMgr.getAmount(category1)){	//if deduction > current
								category1insufficient = true;
							}	//no need to check expense as expense can only increase
							break;
					case 3:	//no need to check asset and liability as deleting this type will only increase both types
							break;
					case 4:	if(amount > assetCatMgr.getAmount(category1)){	//if deduction > current
								category1insufficient = true;
							}
							if(amount > liabilityCatMgr.getAmount(category2)){	//if deduction > current
								category2insufficient = true;
							}
							break;
					case 5:	if(amount > assetCatMgr.getAmount(category2)){	//if deduction > cat2's balance
								category2insufficient = true;
							}
							break;
					case 6:	if(amount > liabilityCatMgr.getAmount(category2)){
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
				
				//if any errors present, display errorMsg
				if (errorMsg != ""){
					errorMsg = "<html>" + errorMsg + "Please try again!" + "</html>";	//to wrap text
					ErrorDisplay.setText(errorMsg);
					errorMsg = "";
				}
				else {
					
					//delete entry from entryMgr using entry's id
					entryMgr.deleteEntry(entry.getId());
					
					//update historyMgr with the old entry's information
					historyMgr.addLog(2, entry.getId(), transactionType, entry.getAmount(),
							entry.getDate(), entry.getCategory1(), entry.getCategory2(), entry.getDescription());
					
					//update 2 relevant out of 4 CatMgrs with the new difference
					switch(transactionType){
						case 0:	assetCatMgr.addAmountToCategory(entry.getCategory1(), - amount);
								incomeCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
						case 1:	assetCatMgr.addAmountToCategory(entry.getCategory1(), amount);
								expenseCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
						case 2:	liabilityCatMgr.addAmountToCategory(entry.getCategory1(), - amount);
								expenseCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
						case 3:	assetCatMgr.addAmountToCategory(entry.getCategory1(), amount);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), amount);
								break;
						case 4:	assetCatMgr.addAmountToCategory(entry.getCategory1(), - amount);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
						case 5:	assetCatMgr.addAmountToCategory(entry.getCategory1(), + amount);
								assetCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
						case 6:	liabilityCatMgr.addAmountToCategory(entry.getCategory1(), + amount);
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), - amount);
								break;
					}
					
					hostFrame.dispose();
				}
				
			}
		});
		deletePanel.add(btnSubmitEntry, "cell 4 4");
		populateFields(entry);
		
	}

	/**
	 * Gets the panel
	 * @return deletePanel
	 */
	public JPanel getPanel(){
		return deletePanel;
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
		deletePanel.validate();
	}
}
