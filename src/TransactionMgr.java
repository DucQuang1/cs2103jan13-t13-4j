import javax.swing.JFrame;
import javax.swing.JComboBox;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;

import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * GUI class that handles all requests from the user
 * note: all the managers are static to prevent duplication, and final to prevent mutation of references
 * 
 * @author Pang Kang Wei,Joshua	A0087809M
 * @author JP
 */
public class TransactionMgr {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yy");
	
	private Finances finances;	//for calling the refresh function

	private JFrame popUpFrame;
	private JPanel inputPanel = new JPanel();
	private JComboBox<String> comboBox = new JComboBox<String>();

	private static final AssetCatMgr assetCatMgr = new AssetCatMgr();
	private static final LiabilityCatMgr liabilityCatMgr = new LiabilityCatMgr();
	private static final IncomeCatMgr incomeCatMgr = new IncomeCatMgr();
	private static final ExpenseCatMgr expenseCatMgr = new ExpenseCatMgr();
	private static final EntryMgr entryMgr = new EntryMgr();
	private static final HistoryMgr historyMgr = new HistoryMgr();
	
	private static LinkedList<Entry> transactionList = entryMgr.getTransactionList();
	
	/**
	 * Default Constructor
	 */
	public TransactionMgr(Finances finances){
		this.finances = finances;
	}
	
	/**
	 * Gets the assetCatMgr's chart data
	 * @return assetCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getAssetChartData(){
		return assetCatMgr.getChartData();
	}
	
	/**
	 * Gets the liabilityCatMgr's chart data
	 * @return liabilityCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getLiabilityChartData(){
		return liabilityCatMgr.getChartData();
	}
	
	/**
	 * Gets the incomeCatMgr's chart data
	 * @return incomeCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getIncomeChartData(){
		return incomeCatMgr.getChartData();
	}
	
	/**
	 * Gets the expenseCatMgr's chart data
	 * @return expenseCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getExpenseChartData(){
		return expenseCatMgr.getChartData();
	}
	
	/**
	 * Gets transaction list from entryMgr
	 * @return transactionList
	 */
	public static LinkedList<Entry> getTransactionList(){
		return entryMgr.getTransactionList();
	}
	
	/**
	 * Clears the history log 
	 * @return true if cleared
	 */
	public static boolean clearLog(){
		return historyMgr.clearLog();
	}
	
	/**
	 * Gets the balance
	 * @return balance
	 */
	public double getBalance(){
		double subtotal = 0;
		subtotal = assetCatMgr.getSubtotal() - liabilityCatMgr.getSubtotal();
		return subtotal;
	}

	/**
	 * Records a new transaction by displaying a pop-up that receives user input
	 * Stores the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @return true if added successfully
	 */
	public boolean addTransaction(){
		//sets up the pop-up first
		popUpFrame = new JFrame();
		popUpFrame.setResizable(false);
		popUpFrame.setSize(720,400);
		popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popUpFrame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));
		
		JLabel lblTransactionType = new JLabel("Transaction Type");
		popUpFrame.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		popUpFrame.getContentPane().setBackground(new Color(255, 255, 255));
		
		//to remove previous input panel's fields
		if(inputPanel != null){
			inputPanel.removeAll();
			inputPanel.setBackground(new Color(255, 255, 255));
		}
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		comboBox.removeAllItems();
		comboBox.setSize(150, 20);
		comboBox.addItem("Income Received");
		comboBox.addItem("Expense by Assets");
		comboBox.addItem("Expense by Credit");
		comboBox.addItem("Repaying Loan");
		comboBox.addItem("Take Loan");
		popUpFrame.getContentPane().add(comboBox, "flowx, cell 0 0 1 1");
		
		//TODO button keeps jumping right after selecting the transaction type
		JButton btnSelect = new JButton("Select");
		popUpFrame.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				popUpFrame.getContentPane().remove(inputPanel);
				
				switch(comboBox.getSelectedIndex()){
				case 0:
					TransactionMgrJPanelInc transactionMgrJPanelInc = new TransactionMgrJPanelInc(
							popUpFrame, assetCatMgr, incomeCatMgr, entryMgr, historyMgr);
					inputPanel = transactionMgrJPanelInc.getPanel();
					break;
				case 1:
					TransactionMgrJPanelExpAssets transactionMgrJPanelExpAssets= new TransactionMgrJPanelExpAssets(
							popUpFrame, assetCatMgr, expenseCatMgr, entryMgr, historyMgr);
					inputPanel = transactionMgrJPanelExpAssets.getPanel();
					break;
				case 2:
					TransactionMgrJPanelExpCredit transactionMgrJPanelExpCredit= new TransactionMgrJPanelExpCredit(
							popUpFrame, liabilityCatMgr, expenseCatMgr, entryMgr, historyMgr);
					inputPanel = transactionMgrJPanelExpCredit.getPanel();
					break;
				case 3:
					TransactionMgrJPanelRepayLoan transactionMgrJPanelRepayLoan = new TransactionMgrJPanelRepayLoan(
							popUpFrame, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					inputPanel = transactionMgrJPanelRepayLoan.getPanel();
					break;
				case 4:
					TransactionMgrJPanelTakeLoan transactionMgrJPanelTakeLoan = new TransactionMgrJPanelTakeLoan(
							popUpFrame, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					inputPanel = transactionMgrJPanelTakeLoan.getPanel();
					break;
				}
				popUpFrame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
				popUpFrame.revalidate();
			}
		});
		popUpFrame.getContentPane().add(btnSelect, "cell 4 0 2 1");
		popUpFrame.getContentPane().add(inputPanel, "cell 0 1 6 1,grow");
		popUpFrame.setVisible(true);
		popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popUpFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
				
                finances.refresh();
            }
		});
		return true;
	}
	
	/**
	 * Edits an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @return true if added successfully
	 */
	public boolean editTransaction(){

		//sets up the pop-up first
		popUpFrame = new JFrame();
		popUpFrame.getContentPane().remove(inputPanel);
		popUpFrame.setResizable(false);
		popUpFrame.setSize(720,400);
		popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popUpFrame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		popUpFrame.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		popUpFrame.getContentPane().setBackground(new Color(255, 255, 255));
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		comboBox.removeAllItems();
		comboBox.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		for(Entry entry : transactionList)
			comboBox.addItem(Integer.toString(entry.getId()));
		popUpFrame.getContentPane().add(comboBox, "flowx, cell 0 0 1 1");

		JButton btnSelect = new JButton("Select");
		popUpFrame.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int selectedIndex = comboBox.getSelectedIndex();
				Entry selectedEntry = transactionList.get(selectedIndex);
				
				EditPanel editPanel = new EditPanel(popUpFrame, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				popUpFrame.getContentPane().remove(inputPanel);
				inputPanel = editPanel.getPanel();
				popUpFrame.getContentPane().add(inputPanel, "cell 0 1 6 1,grow");
				popUpFrame.revalidate();
				
			}
		});
		popUpFrame.getContentPane().add(btnSelect, "cell 4 0 2 1");
		popUpFrame.setVisible(true);
		popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popUpFrame.addWindowListener(new WindowAdapter(){
			
		public void windowClosed(WindowEvent e) {
		
            finances.refresh();
            }
		});
		return true;
	}
	
	/**
	 * Deletes an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @return true if deleted successfully
	 */
	public boolean deleteTransaction(){

		//sets up the pop-up first
		popUpFrame = new JFrame();
		popUpFrame.getContentPane().remove(inputPanel);
		popUpFrame.setResizable(false);
		popUpFrame.setSize(720,400);
		popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popUpFrame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		popUpFrame.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		popUpFrame.getContentPane().setBackground(new Color(255, 255, 255));
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		comboBox.removeAllItems();
		comboBox.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		for(Entry entry : transactionList)
			comboBox.addItem(Integer.toString(entry.getId()));
		popUpFrame.getContentPane().add(comboBox, "flowx, cell 0 0 1 1");

		JButton btnSelect = new JButton("Select");
		popUpFrame.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				int selectedIndex = comboBox.getSelectedIndex();
				Entry selectedEntry = transactionList.get(selectedIndex);
				
				DeletePanel deletePanel = new DeletePanel(popUpFrame, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				popUpFrame.getContentPane().remove(inputPanel);
				inputPanel = deletePanel.getPanel();
				popUpFrame.getContentPane().add(inputPanel, "cell 0 1 6 1,grow");
				popUpFrame.revalidate();
				
			}
		});
		popUpFrame.getContentPane().add(btnSelect, "cell 4 0 2 1");
		popUpFrame.setVisible(true);
		popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popUpFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
			
                finances.refresh();
            }
		});
		return true;
	}
	
	/**
	 * Performs an intra-asset transfer
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @return true if updated successfully
	 */
	public boolean transferIntraAsset(){
		
		//sets up the pop-up first
		popUpFrame = new JFrame();
		popUpFrame.getContentPane().remove(inputPanel);
		popUpFrame.setVisible(true);
		popUpFrame.setSize(720,400);
		popUpFrame.getContentPane().setBackground(new Color(255, 255, 255));
		popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popUpFrame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));

		TransferPanelAsset transferPanelAsset = new TransferPanelAsset(popUpFrame, assetCatMgr, entryMgr, historyMgr);
		popUpFrame.getContentPane().add(transferPanelAsset.getPanel());
		popUpFrame.validate();
		popUpFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
			
                finances.refresh();
            }
		});		
		
		return true;
	}
	
	/**
	 * Performs an intra-liability transfer
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @return true if updated successfully
	 */
	public boolean transferIntraLiability(){
		
		//sets up the pop-up first
		popUpFrame = new JFrame();
		popUpFrame.getContentPane().remove(inputPanel);
		popUpFrame.setVisible(true);
		popUpFrame.setSize(720,400);
		popUpFrame.getContentPane().setBackground(new Color(255, 255, 255));
		popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popUpFrame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));

		TransferPanelLiability transferPanelLiability = new TransferPanelLiability (
				popUpFrame, liabilityCatMgr, entryMgr, historyMgr);
		popUpFrame.getContentPane().add(transferPanelLiability .getPanel());
		popUpFrame.validate();
		popUpFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
			
                finances.refresh();
            }
		});

		return true;
	}
	
	/**
	 * Undoes the last transaction.
	 * @return true if undone successfully
	 */
	public boolean undo(){
		Log lastLog = historyMgr.undoLast();
		StringTokenizer st = new StringTokenizer(lastLog.toTxt(false), "|");
		int operationType = Integer.parseInt(st.nextToken());
		int id = Integer.parseInt(st.nextToken());
		int transactionType = Integer.parseInt(st.nextToken());
		double amount = Double.parseDouble(st.nextToken());
		try {
			Date date = date_format.parse(st.nextToken());
			String category1 = st.nextToken();
			String category2 = st.nextToken();
			String description = "";
			if (st.hasMoreTokens())				//check, as description is an optional entry
				description = st.nextToken();
			
			switch(operationType){
			
				case 0:	return(undoAdd(id, transactionType, amount, category1, category2));
	
				case 1:	return(undoEdit(id, transactionType, amount, date, category1, category2, description));
	
				case 2:	return(undoDelete(id, transactionType, amount, date, category1, category2, description));
	
			}
		
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("No Date!");	//should not happen, but put here just in case for debugging
			return false;
		}
		return true;
	}

	/**
	 * Follows up on the undo operation for 'add' operations
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param category1
	 * @param category2
	 * @return true if updated
	 */
	private boolean undoAdd(int id, int transactionType, double amount, String category1, String category2) {
		
		entryMgr.deleteEntry(id);
		
		//update category 1 based on transactionType
		switch(transactionType){
			case 0:
			case 4:	assetCatMgr.addAmountToCategory(category1, -amount);
					break;
			case 1:
			case 3:
			case 5:	assetCatMgr.addAmountToCategory(category1, amount);
					break;
			case 2:	liabilityCatMgr.addAmountToCategory(category1, -amount);
					break;
			case 6:	liabilityCatMgr.addAmountToCategory(category1, amount);
					break;
		}
		
		//update category2 based on transactionType
		switch(transactionType){
			case 0: incomeCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 1:
			case 2:	expenseCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 3:	liabilityCatMgr.addAmountToCategory(category2, amount);
					break;
			case 4:
			case 6:	liabilityCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 5:	assetCatMgr.addAmountToCategory(category2, -amount);
					break;
		}
		
		return true;
	}
	
	/**
	 * Follows up on the undo operation for 'edit' operations
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 * @return true if updated
	 */
	private boolean undoEdit(int id, int transactionType, double amount, Date date,
			String category1, String category2, String description) {
		
		//amount = entryMgr.getEntry(id).getAmount() - amount;
		//test
		System.out.println(amount);
		entryMgr.editEntry(new Entry(id, transactionType, entryMgr.getEntry(id).getAmount() - amount,
				date, category1, category2, description));
		
		//update category 1 based on transactionType
		switch(transactionType){
			case 0:
			case 4:	assetCatMgr.addAmountToCategory(category1, -amount);
					break;
			case 1:
			case 3:
			case 5:	assetCatMgr.addAmountToCategory(category1, amount);
					break;
			case 2:	liabilityCatMgr.addAmountToCategory(category1, -amount);
					break;
			case 6:	liabilityCatMgr.addAmountToCategory(category1, amount);
					break;
		}
		
		//update category2 based on transactionType
		switch(transactionType){
			case 0: incomeCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 1:
			case 2:	expenseCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 3:	liabilityCatMgr.addAmountToCategory(category2, amount);
					break;
			case 4:
			case 6:	liabilityCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 5:	assetCatMgr.addAmountToCategory(category2, -amount);
					break;
		}
		
		return true;
	}
	
	/**
	 * Follows up on the undo operation for 'delete' operations
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 * @return true if updated 
	 */
	private boolean undoDelete(int id, int transactionType, double amount, Date date,
			String category1, String category2, String description) {

		entryMgr.addEntry(transactionType, amount, date, category1, category2, description);

		//update category 1 based on transactionType
		switch(transactionType){
			case 0:
			case 4:	assetCatMgr.addAmountToCategory(category1, amount);
					break;
			case 1:
			case 3:
			case 5:	assetCatMgr.addAmountToCategory(category1, -amount);
					break;
			case 2:	liabilityCatMgr.addAmountToCategory(category1, amount);
					break;
			case 6:	liabilityCatMgr.addAmountToCategory(category1, -amount);
					break;
		}
		
		//update category2 based on transactionType
		switch(transactionType){
			case 0: incomeCatMgr.addAmountToCategory(category2, amount);
					break;
			case 1:
			case 2:	expenseCatMgr.addAmountToCategory(category2, amount);
					break;
			case 3:	liabilityCatMgr.addAmountToCategory(category2, -amount);
					break;
			case 4:
			case 6:	liabilityCatMgr.addAmountToCategory(category2, amount);
					break;
			case 5:	assetCatMgr.addAmountToCategory(category2, amount);
					break;
		}
				
		return true;
	}
	
}