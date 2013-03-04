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
import java.awt.Color;

/**
 * GUI class that handles all requests from the user
 * @author Pang Kang Wei,Joshua	A0087809M
 * 
 */
public class TransactionMgr {
	
	private Finances finances;	//for calling the refresh function

	private JFrame frame;
	private JPanel inputPanel = new JPanel();
	private JComboBox<String> comboBox = new JComboBox<String>();

	private static AssetCatMgr assetCatMgr = new AssetCatMgr();;
	private static LiabilityCatMgr liabilityCatMgr = new LiabilityCatMgr();
	private static IncomeCatMgr incomeCatMgr = new IncomeCatMgr();
	private static ExpenseCatMgr expenseCatMgr = new ExpenseCatMgr();
	private static EntryMgr entryMgr = new EntryMgr();
	private static HistoryMgr historyMgr = new HistoryMgr();
	
	/**
	 * Default Constructor
	 */
	public TransactionMgr(Finances finances){
		inputPanel.setBackground(new Color(255, 255, 255));
		this.finances = finances;
	}
	
	/**
	 * Gets the assetCatMgr
	 * @return assetCatMgr
	 */
	public static AssetCatMgr getAssetCatMgr(){
		return assetCatMgr;
	}
	
	/**
	 * Gets the liabilityCatMgr
	 * @return liabilityCatMgr
	 */
	public static LiabilityCatMgr getLiabilityCatMgr(){
		return liabilityCatMgr;
	}
	
	/**
	 * Gets the incomeCatMgr
	 * @return incomeCatMgr
	 */
	public static IncomeCatMgr getIncomeCatMgr(){
		return incomeCatMgr;
	}
	
	/**
	 * Gets the expenseCatMgr
	 * @return expenseCatMgr
	 */
	public static ExpenseCatMgr getExpenseCatMgr(){
		return expenseCatMgr;
	}
	
	/**
	 * Gets the entryMgr
	 * @return entryMgr
	 */
	public static EntryMgr getEntryMgr(){
		return entryMgr;
	}
	
	/**
	 * Gets the historyMgr
	 * @return historyMgr
	 */
	public static HistoryMgr getHistoryMgr(){
		return historyMgr;
	}
	
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
	public boolean AddTransaction(){
		//sets up the pop-up first
		frame = new JFrame();
		frame.setResizable(false);
		frame.setSize(720,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout(
				"", 
				"[750]", 
				"[30,grow]5[350,grow]"));
		
		JLabel lblTransactionType = new JLabel("Transaction Type");
		frame.getContentPane().add(lblTransactionType, "cell 0 0 1 1");
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		
		//populate the comboBox. Clear before populating to prevent duplicate items
		comboBox.removeAllItems();
		comboBox.setSize(150, 20);
		comboBox.addItem("Income Received");
		comboBox.addItem("Expense by Assets");
		comboBox.addItem("Expense by Credit");
		comboBox.addItem("Repaying Loan");
		comboBox.addItem("Take Loan");
		frame.getContentPane().add(comboBox, "flowx, cell 0 0 1 1");
		
		JButton btnSelect = new JButton("Confirm");	//TODO button keeps jumping right after selecting the transaction type
		frame.getContentPane().add(btnSelect, "cell 0 0 1 1");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(comboBox.getSelectedIndex());
				switch(comboBox.getSelectedIndex()){
				case 0:
					frame.getContentPane().remove(inputPanel);
					inputPanel = new TransactionMgrJPanelInc(frame, assetCatMgr, incomeCatMgr, entryMgr, historyMgr);
					frame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
					frame.revalidate();
					break;
				case 1:
					frame.getContentPane().remove(inputPanel);
					inputPanel = new TransactionMgrJPanelExpAssets(frame, assetCatMgr, expenseCatMgr, entryMgr, historyMgr);
					frame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
					frame.revalidate();
					break;
				case 2:
					frame.getContentPane().remove(inputPanel);
					inputPanel = new TransactionMgrJPanelExpCredit(frame, liabilityCatMgr, expenseCatMgr, entryMgr, historyMgr);
					frame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
					frame.revalidate();
					break;
				case 3:
					frame.getContentPane().remove(inputPanel);
					inputPanel = new TransactionMgrJPanelRepayLoan(frame, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					frame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
					frame.revalidate();
					break;
				case 4:
					frame.getContentPane().remove(inputPanel);
					inputPanel = new TransactionMgrJPanelTakeLoan(frame, assetCatMgr, liabilityCatMgr, entryMgr, historyMgr);
					frame.getContentPane().add(inputPanel, "cell 0 1 1 1,grow");
					frame.revalidate();
					break;
				}
			}
		});
		frame.getContentPane().add(btnSelect, "cell 4 0 2 1");
		frame.getContentPane().add(inputPanel, "cell 0 1 6 1,grow");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
                finances.refresh();
            }
		});
		return true;
	}
}