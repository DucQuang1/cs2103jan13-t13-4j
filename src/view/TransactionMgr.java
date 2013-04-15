package view;

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
import org.jfree.data.xy.XYDataset;

import data.Entry;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

/**
 * GUI class that handles all requests from the user
 * note: all the managers are static to prevent duplication, and final to prevent mutation of references
 * 
 * @author A0087809M, Pang Kang Wei, Joshua, A0086581W, Wong Jing Ping
 */
public class TransactionMgr {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	
	private Finances finances;	//for calling the refresh function

	private JFrame transactionMgrPopUp_FRM;
	private JPanel transactionMgrInput_PNL= new JPanel();
	private JComboBox<String> transactionMgr_CB = new JComboBox<String>();

	private static final AssetCatMgr assetCatMgr = new AssetCatMgr();
	private static final LiabilityCatMgr liabilityCatMgr = new LiabilityCatMgr();
	private static final IncomeCatMgr incomeCatMgr = new IncomeCatMgr();
	private static final ExpenseCatMgr expenseCatMgr = new ExpenseCatMgr();
	private static final EntryMgr entryMgr = new EntryMgr();
	private static final HistoryMgr historyMgr = new HistoryMgr(
			assetCatMgr, liabilityCatMgr, incomeCatMgr, expenseCatMgr, entryMgr);
	
	private static LinkedList<Entry> transactionList = entryMgr.getTransactionList();
	
	/**
	 * Default Constructor
	 */
	public TransactionMgr(final Finances finances){
		
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
	 * @return entryMgr.getTransactionList()
	 */
	public static LinkedList<Entry> getTransactionList(){
		return entryMgr.getTransactionList();
	}
	
	/**
	 * Gets the XYDataset of transactions over a 1-month period
	 * @return entryMgr.getTransactionByTime()
	 */
	public static XYDataset getLineChartData(){
		return entryMgr.getTransactionByTime();
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
		
		setUptransactionMgrPopUp_FRM(0);
		
		JLabel lblTransactionType = new JLabel("Transaction Type");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		transactionMgrPopUp_FRM.getContentPane().setBackground(new Color(255, 255, 255));
		
		//to remove previous input panel's fields
		if(transactionMgrInput_PNL!= null){
			transactionMgrInput_PNL.removeAll();
			transactionMgrInput_PNL.setBackground(new Color(255, 255, 255));
		}
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		transactionMgr_CB.removeAllItems();
		transactionMgr_CB.setSize(150, 20);
		transactionMgr_CB.addItem("Income Received");
		transactionMgr_CB.addItem("Expense by Assets");
		transactionMgr_CB.addItem("Expense by Credit");
		transactionMgr_CB.addItem("Repaying Loan");
		transactionMgr_CB.addItem("Take Loan");
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "flowx, cell 0 0 1 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				transactionMgrPopUp_FRM.getContentPane().remove(transactionMgrInput_PNL);
				
				switch(transactionMgr_CB.getSelectedIndex()){
				case 0:
					InputPanelType0 transactionMgrJPanelInc = new InputPanelType0(
							transactionMgrPopUp_FRM, assetCatMgr, incomeCatMgr, entryMgr, historyMgr);
					transactionMgrInput_PNL= transactionMgrJPanelInc.getPanel();
					break;
				case 1:
					InputPanelType1 transactionMgrJPanelExpAssets= new InputPanelType1(
							transactionMgrPopUp_FRM, assetCatMgr, expenseCatMgr, entryMgr, historyMgr);
					transactionMgrInput_PNL= transactionMgrJPanelExpAssets.getPanel();
					break;
				case 2:
					InputPanelType2 transactionMgrJPanelExpCredit= new InputPanelType2(
							transactionMgrPopUp_FRM, liabilityCatMgr, expenseCatMgr, entryMgr, historyMgr);
					transactionMgrInput_PNL= transactionMgrJPanelExpCredit.getPanel();
					break;
				case 3:
					InputPanelType3 transactionMgrJPanelRepayLoan = new InputPanelType3(
							transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					transactionMgrInput_PNL= transactionMgrJPanelRepayLoan.getPanel();
					break;
				case 4:
					InputPanelType4 transactionMgrJPanelTakeLoan = new InputPanelType4(
							transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					transactionMgrInput_PNL= transactionMgrJPanelTakeLoan.getPanel();
					break;
				}
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 1 1 1,grow");
				transactionMgrPopUp_FRM.revalidate();
			}
		});
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 1 6 1,grow");
		
		return true;
	}
	
	/**
	 * Edits an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @return true if added successfully
	 */
	public boolean editTransaction(){

		setUptransactionMgrPopUp_FRM(0);
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		transactionMgrPopUp_FRM.getContentPane().setBackground(new Color(255, 255, 255));
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		transactionMgr_CB.removeAllItems();
		transactionMgr_CB.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		for(Entry entry : transactionList)
			transactionMgr_CB.addItem(Integer.toString(entry.getId()));
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "flowx, cell 0 0 1 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int selectedIndex = transactionMgr_CB.getSelectedIndex();
				Entry selectedEntry = transactionList.get(selectedIndex);
				
				EditPanel editPanel = new EditPanel(transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				transactionMgrPopUp_FRM.getContentPane().remove(transactionMgrInput_PNL);
				transactionMgrInput_PNL= editPanel.getPanel();
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 1 6 1,grow");
				transactionMgrPopUp_FRM.revalidate();
				
			}
		});
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 4 0 2 1");
		
		return true;
	}
	
	/**
	 * Deletes an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @return true if deleted successfully
	 */
	public boolean deleteTransaction(){

		setUptransactionMgrPopUp_FRM(0);
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		transactionMgrPopUp_FRM.getContentPane().setBackground(new Color(255, 255, 255));
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		transactionMgr_CB.removeAllItems();
		transactionMgr_CB.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		for(Entry entry : transactionList)
			transactionMgr_CB.addItem(Integer.toString(entry.getId()));
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "flowx, cell 0 0 1 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				int selectedIndex = transactionMgr_CB.getSelectedIndex();
				Entry selectedEntry = transactionList.get(selectedIndex);
				
				DeletePanel deletePanel = new DeletePanel(transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				transactionMgrPopUp_FRM.getContentPane().remove(transactionMgrInput_PNL);
				transactionMgrInput_PNL= deletePanel.getPanel();
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 1 6 1,grow");
				transactionMgrPopUp_FRM.revalidate();
				
			}
		});
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 4 0 2 1");
		
		return true;
	}
	
	/**
	 * Performs an intra-asset transfer
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @return true if updated successfully
	 */
	public boolean transferIntraAsset(){
		
		setUptransactionMgrPopUp_FRM(0);

		TransferPanelAsset transferPanelAsset = new TransferPanelAsset(
				transactionMgrPopUp_FRM, assetCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.getContentPane().add(transferPanelAsset.getPanel());
		transactionMgrPopUp_FRM.validate();
		
		return true;
	}
	
	/**
	 * Performs an intra-liability transfer
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @return true if updated successfully
	 */
	public boolean transferIntraLiability(){
		
		setUptransactionMgrPopUp_FRM(0);

		TransferPanelLiability transferPanelLiability = new TransferPanelLiability (
				transactionMgrPopUp_FRM, liabilityCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.getContentPane().add(transferPanelLiability.getPanel());
		transactionMgrPopUp_FRM.validate();

		return true;
	}
	
	/**
	 * Creates a pop up that allows user to edit asset categories 
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @return true if updated successfully
	 */
	public boolean renameAssetCategories(){

		setUptransactionMgrPopUp_FRM(1);
		
		RenameAssetCatPanel renameAssetCatPanel = new RenameAssetCatPanel(
				transactionMgrPopUp_FRM, assetCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
		
		return true;
	}
	
	/**
	 * Creates a pop up that allows user to edit liability categories 
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and LiabilityCatMgr
	 * @return true if updated successfully
	 */
	public boolean renameLiabilityCategories(){

		setUptransactionMgrPopUp_FRM(1);
		RenameLiabilityCatPanel renameLiabilityCatPanel = new RenameLiabilityCatPanel(
				transactionMgrPopUp_FRM, liabilityCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
		
		return true;
	}
	
	/**
	 * Creates a pop up that allows user to edit income categories 
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and IncomeCatMgr
	 * @return true if updated successfully
	 */
	public boolean renameIncomeCategories(){

		setUptransactionMgrPopUp_FRM(1);
		RenameIncomeCatPanel renameIncomeCatPanel = new RenameIncomeCatPanel(
				transactionMgrPopUp_FRM, incomeCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
		
		return true;
	}
	
	/**
	 * Creates a pop up that allows user to edit expense categories 
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and ExpenseCatMgr
	 * @return true if updated successfully
	 */
	public boolean renameExpenseCategories(){

		setUptransactionMgrPopUp_FRM(1);
		RenameExpenseCatPanel renameExpenseCatPanel = new RenameExpenseCatPanel(
				transactionMgrPopUp_FRM, expenseCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
		
		return true;
	}
	
	/**
	 * Undoes the last transaction.
	 * @return true if undone successfully
	 */
	public boolean undo(){
		historyMgr.undo();
		return true;
	}	
	
	/**
	 * Private method for setting up the pop up frame's formatting
	 * @param size (0 for normal frame, 1 for small frame)
	 */
	private void setUptransactionMgrPopUp_FRM(int size){
		
		transactionMgrPopUp_FRM = new JFrame();
		transactionMgrPopUp_FRM.setVisible(true);
		transactionMgrPopUp_FRM.setResizable(false);
		transactionMgrPopUp_FRM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		transactionMgrPopUp_FRM.getContentPane().setBackground(new Color(255, 255, 255));		
		transactionMgrPopUp_FRM.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
			
	            finances.refresh();
	        }
		});
		switch(size){
			case 0:	transactionMgrPopUp_FRM.getContentPane().setLayout(new MigLayout(
					"", 
					"[720]", 
					"[30,grow]5[350,grow]"));
					transactionMgrPopUp_FRM.setSize(720,400);
					break;
			case 1:	transactionMgrPopUp_FRM.getContentPane().setLayout(new MigLayout(
					"", 
					"[400]", 
					"[30,grow]5[350,grow]"));
					transactionMgrPopUp_FRM.setSize(400,400);
					break;
		}
	}
}