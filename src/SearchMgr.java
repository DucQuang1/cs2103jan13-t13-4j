import java.awt.EventQueue;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;


/*
 * This GUI class is the Search Manager to help the user with searching for particular entries 
 * @author: Pang Kang Wei, Joshua A0087809M
 */
public class SearchMgr {
	private JTextField SearchMgrDay_TF;
	private JTextField SearchMgrMonth_TF;
	private JTextField textField_2;
	private JTextField SearchMgrYear_TF;
	private JTable table;
	private EntryMgr entryMgr = new EntryMgr();
	private JTable table_2;
	private JPanel panel = new JPanel();
	private int DD, MM, YYYY;
	private JLabel SearchMgrDisplay_LBL = new JLabel("Please do a search selection");
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	
	
	
	/*
	 * description: This function is to return a linked list of Entry results after searching by transaction type
	 * @author: Pang Kang Wei, Joshua A0087809M
	 * @param: transactionType
	 */
	public LinkedList<Entry> searchByTransactionType(int transactionType){
		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		allEntries = entryMgr.getTransactionList();

		for (int i = 0; i < allEntries.size(); i++) {
			if(allEntries.get(i).transactionType == transactionType){
				resultEntries.add(allEntries.get(i));
			}
		}
		SearchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		return resultEntries;
	}
	
	/*
	 * description: This function is to return a linked list of Entry results after searching by date
	 * @author: Pang Kang Wei, Joshua A0087809M
	 * @param: sign
	 * @param: inputDD
	 * @param: inputMM
	 * @param: inputYYYY
	 */
	public LinkedList<Entry> searchByDate(int sign, String inputDD, String inputMM, String inputYYYY){
		
		//check if day of month is valid
		try{
			DD = Integer.parseInt(inputDD);
			if (DD <= 0 || DD > 31)
				throw new Exception();
		} catch (Exception exDD){
			System.out.println(exDD);
		}
		
		//check if month is valid
		try{
			MM = Integer.parseInt(inputMM);
			if (MM <= 0 || MM > 12)
				throw new Exception();
		} catch (Exception exMM){
			System.out.println(exMM);
		}
		
		//check if year is valid
		try{
			YYYY  = Integer.parseInt(inputYYYY);
			if (YYYY < 1900 || YYYY > Calendar.getInstance().get(Calendar.YEAR))
				throw new Exception();
		} catch (Exception exYY){
			System.out.println(exYY);
		}
		
		Date date = null;
		String dateString = Integer.toString(DD) + "/" + Integer.toString(MM) + "/" + Integer.toString(YYYY);
		try {
			date = date_format.parse(dateString);
		} catch (Exception e1) {
			System.out.println(e1);
		}
		
		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		allEntries = entryMgr.getTransactionList();

		for (int i = 0; i < allEntries.size(); i++) {
			switch(sign){
			case 0:
				if(allEntries.get(i).getDate().before(date)){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 1:
				if(allEntries.get(i).getDate().after(date)){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 2:
				if(allEntries.get(i).getDate().equals(date)){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 3:
				if(allEntries.get(i).getDate().before(date) || allEntries.get(i).getDate().equals(date)){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 4:
				if(allEntries.get(i).getDate().after(date) || allEntries.get(i).getDate().equals(date)){
					resultEntries.add(allEntries.get(i));
				}
				break;
			}
		}

		SearchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		return resultEntries;
	}
	
	/*
	 * description: This function is to return a linked list of Entry results after searching by amount
	 * @author: Pang Kang Wei, Joshua A0087809M
	 * @param: sign
	 * @param: amount
	 */
	public LinkedList<Entry> searchByAmount(int sign, Double amount){
		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		allEntries = entryMgr.getTransactionList();

		for (int i = 0; i < allEntries.size(); i++) {
			switch(sign){
			case 0:
				if(allEntries.get(i).getAmount() > amount){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 1:
				if(allEntries.get(i).getAmount() < amount){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 2:
				if(allEntries.get(i).getAmount() == amount){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 3:
				if(allEntries.get(i).getAmount() >= amount){
					resultEntries.add(allEntries.get(i));
				}
				break;
			case 4:
				if(allEntries.get(i).getAmount() <= amount){
					resultEntries.add(allEntries.get(i));
				}
				break;
			}
		}

		SearchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		return resultEntries;
	}

	private JFrame frame;

	/*
	 * description: Main class to run the Search Manager
	 * @author: Pang Kang Wei, Joshua A0087809M
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchMgr window = new SearchMgr();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * description: Constructor of Search Manager
	 * @author: Pang Kang Wei, Joshua A0087809M
	 */
	public SearchMgr() {
		initialize();
	}

	/*
	 * description: Initialise contents in frame
	 * @author: Pang Kang Wei, Joshua A0087809M
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 680, 433);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow][grow][100px:n:100px][100px:n:100px][100px:n:100px][100px:n:100px][100px:n:100px]", "[][][][][][][][][][][][grow][]"));

		
		JLabel label = new JLabel("");
		frame.getContentPane().add(label, "cell 1 10");
		
		//SearchMgrDisplay_LBL placement
		frame.getContentPane().add(SearchMgrDisplay_LBL, "cell 2 10 3 1");
		
		JRadioButton SearchMgrRadioBtn_0 = new JRadioButton("");
		frame.getContentPane().add(SearchMgrRadioBtn_0, "cell 0 2");
		SearchMgrRadioBtn_0.setMnemonic(0);
		
		final JComboBox SearchMgrAmount_CB = new JComboBox();
		frame.getContentPane().add(SearchMgrAmount_CB, "cell 2 2,growx");
		SearchMgrAmount_CB.addItem("Greater than");
		SearchMgrAmount_CB.addItem("Lesser than");
		SearchMgrAmount_CB.addItem("Equal");
		SearchMgrAmount_CB.addItem("Greater-Equal");
		SearchMgrAmount_CB.addItem("Lesser-Equal");
		
		final JTextField SearchMgrAmout_TF = new JTextField();
		frame.getContentPane().add(SearchMgrAmout_TF, "cell 3 2,growx");
		SearchMgrAmout_TF.setColumns(10);
		
		JLabel lblAmount = new JLabel("Amount");
		frame.getContentPane().add(lblAmount, "cell 3 3,alignx center");
		
		JRadioButton SearchMgrRadioBtn_1 = new JRadioButton("");
		frame.getContentPane().add(SearchMgrRadioBtn_1, "cell 0 5");
		SearchMgrRadioBtn_1.setMnemonic(1);
		
		final JComboBox SearchMgrDate_CB = new JComboBox();
		frame.getContentPane().add(SearchMgrDate_CB, "cell 2 5,growx");
		SearchMgrDate_CB.addItem("Before");
		SearchMgrDate_CB.addItem("After");
		SearchMgrDate_CB.addItem("Specific");
		SearchMgrDate_CB.addItem("Bef-Include");
		SearchMgrDate_CB.addItem("Aft-Include");
		
		
		SearchMgrDay_TF = new JTextField();
		frame.getContentPane().add(SearchMgrDay_TF, "flowx,cell 3 5,alignx center");
		SearchMgrDay_TF.setColumns(10);
		
		SearchMgrMonth_TF = new JTextField();
		frame.getContentPane().add(SearchMgrMonth_TF, "cell 4 5,alignx center");
		SearchMgrMonth_TF.setColumns(10);
		
		SearchMgrYear_TF = new JTextField();
		frame.getContentPane().add(SearchMgrYear_TF, "cell 5 5,alignx center");
		SearchMgrYear_TF.setColumns(10);
		
		JLabel lblDd = new JLabel("DD");
		frame.getContentPane().add(lblDd, "cell 3 6,alignx center");
		
		JLabel lblMm = new JLabel("MM");
		frame.getContentPane().add(lblMm, "cell 4 6,alignx center");
		
		JLabel lblYyyy = new JLabel("YYYY");
		frame.getContentPane().add(lblYyyy, "cell 5 6,alignx center");
		
		JRadioButton SearchMgrRadioBtn_2 = new JRadioButton("");
		frame.getContentPane().add(SearchMgrRadioBtn_2, "cell 0 8");
		SearchMgrRadioBtn_2.setMnemonic(2);
		
		final ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(SearchMgrRadioBtn_0);
		btnGroup.add(SearchMgrRadioBtn_1);
		btnGroup.add(SearchMgrRadioBtn_2);
		SearchMgrRadioBtn_0.setSelected(true);
		
		
		JLabel lblTransactionType = new JLabel("Transaction Type");
		frame.getContentPane().add(lblTransactionType, "cell 2 8,alignx center");
		
		final JComboBox SearchMgrType_CB = new JComboBox();
		frame.getContentPane().add(SearchMgrType_CB, "cell 3 8 2 1,growx");
		SearchMgrType_CB.addItem("Income Received");
		SearchMgrType_CB.addItem("Expense by Assets");
		SearchMgrType_CB.addItem("Expense by Credit");
		SearchMgrType_CB.addItem("Repaying Loan");
		SearchMgrType_CB.addItem("Take Loan");
		
		JButton SearchMgrSearchNow_BTN = new JButton("Search Now");
		SearchMgrSearchNow_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(btnGroup.getSelection().getMnemonic());
				
				LinkedList<Entry> searchedResults = new LinkedList<Entry>();

				switch (btnGroup.getSelection().getMnemonic()) {
				case 0:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByAmount(SearchMgrAmount_CB.getSelectedIndex(), Double.parseDouble(SearchMgrAmout_TF.getText()));
					break;
				case 1:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByDate(SearchMgrDate_CB.getSelectedIndex(), SearchMgrDay_TF.getText(), SearchMgrMonth_TF.getText(), SearchMgrYear_TF.getText());
					break;
				case 2:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByTransactionType(SearchMgrType_CB.getSelectedIndex());
					break;
				}
				

				DefaultTableModel model;

				String data[][] = new String[searchedResults.size()][7];
				
				for(int i=0; i<searchedResults.size(); i++){
					data[i][0] = searchedResults.get(i).getId() +"";
					data[i][1] = searchedResults.get(i).getTransactionType() +"";
					data[i][2] = searchedResults.get(i).getAmount() +"";
					data[i][3] = searchedResults.get(i).getDate() +"";
					data[i][4] = searchedResults.get(i).getCategory1() +"";
					data[i][5] = searchedResults.get(i).getCategory2() +"";
					data[i][6] = searchedResults.get(i).getDescription() +"";
				}

				String col[] = { "ID", "Transaction Type", "Amount", "Date", "Cat1", "Cat2", "Description" };

				model = new DefaultTableModel(data, col);
				table_2 = new JTable(model);
				table_2.enable(false);
				panel.removeAll();
				frame.getContentPane().add(panel, "cell 0 11 7 2,grow");
				panel.setLayout(new MigLayout("", "[1px][grow]", "[1px][grow]"));
				panel.add(table_2, "cell 1 1,grow");
				panel.revalidate();
			}
		});
		frame.getContentPane().add(SearchMgrSearchNow_BTN, "cell 6 10,alignx center");
		
	}

}
