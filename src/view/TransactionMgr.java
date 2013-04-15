package view;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;

import org.jfree.data.category.DefaultCategoryDataset;
import data.Category;
import data.Entry;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

/**
 * GUI class that handles all requests from the user that may potential change user's data
 * This class generates a popup frame, which is passed to another class (depending on the function called)
 * that will continue to generate the rest of the fields and pass user's input to the relevant managers
 * note: all the managers are static to prevent duplication, and final to prevent mutation of references
 * 
 * @author Pang Kang Wei,Joshua	A0087809M
 * @author Wong Jing Ping, A0086581W
 */
public class TransactionMgr {
	
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
	
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
	 * @author Wong Jing Ping, A0086581W
	 * @return assetCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getAssetChartData(){
		return assetCatMgr.getChartData();
	}
	
	/**
	 * Gets the liabilityCatMgr's chart data
	 * @author Wong Jing Ping, A0086581W
	 * @return liabilityCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getLiabilityChartData(){
		return liabilityCatMgr.getChartData();
	}
	
	/**
	 * Gets the incomeCatMgr's chart data
	 * @author Wong Jing Ping, A0086581W
	 * @return incomeCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getIncomeChartData(){
		return incomeCatMgr.getChartData();
	}
	
	/**
	 * Gets the expenseCatMgr's chart data
	 * @author Wong Jing Ping, A0086581W
	 * @return expenseCatMgr.getChartData()
	 */
	public static DefaultCategoryDataset getExpenseChartData(){
		return expenseCatMgr.getChartData();
	}
	
	/**
	 * Gets transaction list from entryMgr
	 * @author Wong Jing Ping, A0086581W
	 * @return entryMgr.getTransactionList()
	 */
	public static LinkedList<Entry> getTransactionList(){
		return entryMgr.getTransactionList();
	}
	
	/**
	 * Clears the history log
	 * @author Wong Jing Ping, A0086581W
	 * @return true if cleared
	 */
	public static boolean clearLog(){
		return historyMgr.clearLog();
	}
	
	/**
	 * Gets the balance
	 * @author Wong Jing Ping, A0086581W
	 * @return balance
	 */
	public double getBalance(){
		
		double balance = 0;
		balance = assetCatMgr.getSubtotal() - liabilityCatMgr.getSubtotal();
		if(balance < 0){
			//pop up to inform user
			JOptionPane searchMgrConfirm_JOP = new JOptionPane();
			searchMgrConfirm_JOP.setMessage("You have a negative balance!\nTime to save!");
			searchMgrConfirm_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
			JDialog dialog = searchMgrConfirm_JOP.createDialog(null);
			dialog.setVisible(true);
		}
		return balance;
	}

	/**
	 * Records a new transaction by displaying a pop-up that receives user input
	 * Stores the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @author Pang Kang Wei, Joshua A0087809M
	 * @return true if added successfully
	 */
	public boolean addTransaction(){
		
		setUptransactionMgrPopUp_FRM(0);
		
		JLabel transactionMgrHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Add.png")));
		transactionMgrHeading_LBL.setText("Add Transaction");
		transactionMgrHeading_LBL.setFont(heading_font);
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgrHeading_LBL, "cell 0 0");
		
		JLabel lblTransactionType = new JLabel("Transaction Type");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 1");
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
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "cell 0 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 1");
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
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 2");
				transactionMgrPopUp_FRM.revalidate();
			}
		});
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 1 6 1,grow");
		
		return true;
	}
	
	/**
	 * Edits an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @author Wong Jing Ping, A0086581W
	 * @return true if added successfully
	 */
	public boolean editTransaction(){

		setUptransactionMgrPopUp_FRM(0);
		
		JLabel transactionMgrHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Edit.png")));
		transactionMgrHeading_LBL.setText("Edit Transaction");
		transactionMgrHeading_LBL.setFont(heading_font);
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgrHeading_LBL, "cell 0 0");
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 1");
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		transactionMgr_CB.removeAllItems();
		transactionMgr_CB.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		LinkedList<Category> assetCategoryList = assetCatMgr.getCategoryList();
		LinkedList<Category> liabilityCategoryList = liabilityCatMgr.getCategoryList();
		for(Entry entry : transactionList){
			
			boolean category1exists = false;
			boolean category2exists = false;
			int type = entry.getTransactionType();
			
			switch(type){
			case 0:
			case 1:
			case 2:	category2exists = true;
					break;
			}
			
			//check if it is an existing asset category
			for(Category category : assetCategoryList){
				if(category.category.equals(entry.getCategory1()))
					switch(type){
					case 0:
					case 1:
					case 3:
					case 4:
					case 5:	category1exists = true;
							break;
					}
				if(category.category.equals(entry.getCategory2()) && type == 5)
					category2exists = true;
			}
			
			//check if it is an existing liability category
			for(Category category : liabilityCategoryList){
				if(category.category.equals(entry.getCategory1()))
					switch(type){
					case 2:
					case 6:	category1exists = true;
							break;
					}
				if(category.category.equals(entry.getCategory2()))
					switch(type){
					case 3:
					case 4:
					case 6:	category2exists = true;
							break;
					}
			}
			if(category1exists && category2exists){
				String id = Integer.toString(entry.getId());
				transactionMgr_CB.addItem(id);
			}
		}
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "cell 0 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int selectedID = Integer.parseInt((String) transactionMgr_CB.getSelectedItem());
				Entry selectedEntry = null;
				for(Entry entry : transactionList){
					if(entry.getId() == selectedID)
						selectedEntry = entry;
				}
				
				EditPanel editPanel = new EditPanel(transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				transactionMgrPopUp_FRM.getContentPane().remove(transactionMgrInput_PNL);
				transactionMgrInput_PNL= editPanel.getPanel();
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 2");
				transactionMgrPopUp_FRM.revalidate();
				
			}
		});
		
		return true;
	}
	
	/**
	 * Deletes an existing transaction by displaying a pop-up that receives user input
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and 2 out of the 4 CatMgrs
	 * @author Wong Jing Ping, A0086581W
	 * @return true if deleted successfully
	 */
	public boolean deleteTransaction(){

		setUptransactionMgrPopUp_FRM(0);
		
		JLabel transactionMgrHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Del.png")));
		transactionMgrHeading_LBL.setText("Delete Transaction");
		transactionMgrHeading_LBL.setFont(heading_font);
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgrHeading_LBL, "cell 0 0");
		
		JLabel lblTransactionType = new JLabel("Transaction Number");
		transactionMgrPopUp_FRM.getContentPane().add(lblTransactionType, "cell 0 1");
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		transactionMgr_CB.removeAllItems();
		transactionMgr_CB.setSize(150, 20);
		transactionList = entryMgr.getTransactionList();
		LinkedList<Category> assetCategoryList = assetCatMgr.getCategoryList();
		LinkedList<Category> liabilityCategoryList = liabilityCatMgr.getCategoryList();
		for(Entry entry : transactionList){
			
			boolean category1exists = false;
			boolean category2exists = false;
			int type = entry.getTransactionType();
			
			switch(type){
			case 0:
			case 1:
			case 2:	category2exists = true;
					break;
			}
			
			//check if it is an existing asset category
			for(Category category : assetCategoryList){
				if(category.category.equals(entry.getCategory1()))
					switch(type){
					case 0:
					case 1:
					case 3:
					case 4:
					case 5:	category1exists = true;
							break;
					}
				if(category.category.equals(entry.getCategory2()) && type == 5)
					category2exists = true;
			}
			
			//check if it is an existing liability category
			for(Category category : liabilityCategoryList){
				if(category.category.equals(entry.getCategory1()))
					switch(type){
					case 2:
					case 6:	category1exists = true;
							break;
					}
				if(category.category.equals(entry.getCategory2()))
					switch(type){
					case 3:
					case 4:
					case 6:	category2exists = true;
							break;
					}
			}
			if(category1exists && category2exists){
				String id = Integer.toString(entry.getId());
				transactionMgr_CB.addItem(id);
			}
		}
		transactionMgrPopUp_FRM.getContentPane().add(transactionMgr_CB, "flowx, cell 0 1");

		JButton btnSelect = new JButton("Select");
		transactionMgrPopUp_FRM.getContentPane().add(btnSelect, "cell 0 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				int selectedID = Integer.parseInt((String) transactionMgr_CB.getSelectedItem());
				Entry selectedEntry = null;
				for(Entry entry : transactionList){
					if(entry.getId() == selectedID)
						selectedEntry = entry;
				}
				
				DeletePanel deletePanel = new DeletePanel(transactionMgrPopUp_FRM, assetCatMgr, liabilityCatMgr, 
						incomeCatMgr, expenseCatMgr, entryMgr, historyMgr, selectedEntry);
				
				transactionMgrPopUp_FRM.getContentPane().remove(transactionMgrInput_PNL);
				transactionMgrInput_PNL= deletePanel.getPanel();
				transactionMgrPopUp_FRM.getContentPane().add(transactionMgrInput_PNL, "cell 0 2,grow");
				transactionMgrPopUp_FRM.revalidate();
				
			}
		});
		
		return true;
	}
	
	/**
	 * Performs an intra-asset transfer
	 * Updates the new transaction information in the EntryMgr, HistoryMgr, and AssetCatMgr
	 * @author Wong Jing Ping, A0086581W
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
	 * @author Wong Jing Ping, A0086581W
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
	 * @author Wong Jing Ping, A0086581W
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
	 * @author Wong Jing Ping, A0086581W
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
	 * @author Wong Jing Ping, A0086581W
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
	 * @author Wong Jing Ping, A0086581W
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
	 * Creates a pop up that allows user to delete an asset category
	 * @author Wong Jing Ping, A0086581W
	 * @return void
	 */
	public void deleteAssetCategory() {
		
		setUptransactionMgrPopUp_FRM(2);
		DeleteAssetCategoryPanel deleteAssetCategoryPanel = new DeleteAssetCategoryPanel(
				transactionMgrPopUp_FRM, assetCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
	}
	
	/**
	 * Creates a pop up that allows user to delete a liability category
	 * @author Wong Jing Ping, A0086581W
	 * @return void
	 */
	public void deleteLiabilityCategory() {
		
		setUptransactionMgrPopUp_FRM(2);
		DeleteLiabilityCategoryPanel deleteLiabilityCategoryPanel = new DeleteLiabilityCategoryPanel(
				transactionMgrPopUp_FRM, liabilityCatMgr, entryMgr, historyMgr);
		transactionMgrPopUp_FRM.validate();
	}
	
	/**
	 * Undoes the last transaction.
	 * @author Wong Jing Ping, A0086581W
	 * @return true if undone successfully
	 */
	public boolean undo(){
		
		historyMgr.undo();
		finances.reactivateFrame();
		return true;
	}	
	
	/**
	 * Prompts user for file path and imports from there
	 * @author Wong Jing Ping, A0086581W
	 * @param importList
	 * @return true (if imported successfully)
	 */
	public boolean importTransactionList() {
		
		setUptransactionMgrPopUp_FRM(2);
		ImportPanel importPanel = new ImportPanel(transactionMgrPopUp_FRM, 
				assetCatMgr, liabilityCatMgr, incomeCatMgr, expenseCatMgr, entryMgr, historyMgr);
		return true;
	}
	
	/**
	 * Prompts user for file path and exports there
	 * @author Wong Jing Ping, A0086581W
	 * @return true (if exported successfully)
	 */
	public boolean exportTransactionList() {
		
		setUptransactionMgrPopUp_FRM(2);
		ExportPanel exportPanel = new ExportPanel(transactionMgrPopUp_FRM, getTransactionList());
		return true;
	}
	
	/**
	 * Private method for setting up the pop up frame's formatting
	 * @author Wong Jing Ping, A0086581W
	 * @param size (0 for normal frame, 1 for small frame)
	 */
	private void setUptransactionMgrPopUp_FRM(int size){
		
		//disable main frame to prevent generation of more than 1 pop up frame
		finances.disableFrame();
		
		transactionMgrPopUp_FRM = new JFrame();
		transactionMgrPopUp_FRM.setVisible(true);
		transactionMgrPopUp_FRM.setResizable(false);
		transactionMgrPopUp_FRM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		transactionMgrPopUp_FRM.getContentPane().setBackground(new Color(255, 255, 255));		
		transactionMgrPopUp_FRM.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
			
				finances.reactivateFrame();	
	            finances.refresh();
	        }
		});
		switch(size){
			case 0:	transactionMgrPopUp_FRM.getContentPane().setLayout(new MigLayout(
					"", 
					"[720]", 
					"[50,grow]5[30,grow]5[300,grow]"));
					transactionMgrPopUp_FRM.setSize(720,400);
					break;
			case 1:	transactionMgrPopUp_FRM.getContentPane().setLayout(new MigLayout(
					"", 
					"[400]", 
					"[30,grow]5[350,shrink]"));
					transactionMgrPopUp_FRM.setSize(400,400);
					break;
			case 2: transactionMgrPopUp_FRM.getContentPane().setLayout(new MigLayout(
					"", 
					"[400]", 
					"[200,grow]"));
					break;
		}
	}

	
}