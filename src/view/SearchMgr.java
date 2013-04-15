package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ButtonGroup;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.table.*;
import javax.swing.*;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import data.Entry;

import logic.EntryMgr;
import logic.ExcelMgr;
import java.awt.event.KeyEvent;

/**
 * This GUI class is the Search Manager to help the user with searching for particular entries 
 * @author: A0087809M, Pang Kang Wei, Joshua
 */
public class SearchMgr {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");	//private SimpleDateFormat variable to format the date while processing Entry information
	private static final DecimalFormat double_format = new DecimalFormat("##.00");			//private DecimalFormat variable to format double amounts of money
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);		//font for heading

	private JFrame frame;																	//Frame to hold the GUI elements
	private JTextField searchMgrDay_TF;														//Text field to specify day
	private JTextField searchMgrMonth_TF;													//Text field to specify month
	private JTextField searchMgrYear_TF;													//Text field to specify year
	private JLabel searchMgrHeading_LBL = new JLabel("Search");								//Label for heading
	private JLabel searchMgrDisplay_LBL = new JLabel("Please do a search selection");		//Label to display number of search results
	private JTable searchMgrResults_TABLE;													//Table to hold searched results
	private JButton searchMgrExport_BTN = new JButton("Export");							//Button used to export search results
	
	private boolean searched = false;
	private int DD, MM, YYYY;																//private day, month and year for processing of Entry information
	
	private EntryMgr entryMgr = new EntryMgr();												//Create an instance of EntryMgr
	private JTextField searchMgrDesc_TF;

	/**
	 * description: This function is to sort the search results by ascending ID
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortIDAscending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getId() > entryLL.get(j).getId()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by descending ID
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortIDDecending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getId() < entryLL.get(j).getId()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by ascending amount
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortAmtAscending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getAmount() > entryLL.get(j).getAmount()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by descending amount
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortAmtDecending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getAmount() < entryLL.get(j).getAmount()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by ascending date
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortDateAscending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getDate().after(entryLL.get(j).getDate())){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by descending date
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortDateDecending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getDate().before(entryLL.get(j).getDate())){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by ascending transaction type
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	static LinkedList<Entry> bubbleSortTransAscending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getTransactionType() > entryLL.get(j).getTransactionType()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}

	/**
	 * description: This function is to sort the search results by descending transaction type
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: entryLL
	 */
	public static LinkedList<Entry> bubbleSortTransDecending(LinkedList<Entry> entryLL) {
		for(int i=0; i<entryLL.size(); i++){
			for(int j=1; j<entryLL.size(); j++){
				if(entryLL.get(j-1).getTransactionType() < entryLL.get(j).getTransactionType()){
					Entry temp = entryLL.get(j);
					entryLL.set(j, entryLL.get(j-1));
					entryLL.set(j-1, temp);
				}
			}
		}
		
		return entryLL;
	}
	
	/**
	 * description: This function is to call the appropriate sort function depending on the search type
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: searchType
	 * @param: searched
	 */
	public static LinkedList<Entry> sortSearch(int searchType, LinkedList<Entry> searched){
		
		switch (searchType) {
		case 0:
			searched = bubbleSortIDAscending(searched);
			break;
			
		case 1:
			searched = bubbleSortIDDecending(searched);
			break;

		case 2:
			searched = bubbleSortAmtAscending(searched);
			break;

		case 3:
			searched = bubbleSortAmtDecending(searched);
			break;
			
		case 4:
			searched = bubbleSortDateAscending(searched);
			break;
			
		case 5:
			searched = bubbleSortDateDecending(searched);
			break;

		case 6:
			searched = bubbleSortTransAscending(searched);
			break;
			
		case 7:
			searched = bubbleSortTransDecending(searched);
			break;
		}
		return searched;
	}

	/**
	 * description: This function is to return a linked list of Entry results after searching by transaction type
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: transactionType
	 */
	public LinkedList<Entry> searchByTransactionType(int transactionType){
		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		allEntries = entryMgr.getTransactionList();

		for (int i = 0; i < allEntries.size(); i++) {
			if(allEntries.get(i).getTransactionType() == transactionType){
				resultEntries.add(allEntries.get(i));
			}
		}
		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		return resultEntries;
	}
	
	/**
	 * description: This function is to return a linked list of Entry results after searching by date
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: sign
	 * @param: inputDD
	 * @param: inputMM
	 * @param: inputYYYY
	 */
	public LinkedList<Entry> searchByDate(int sign, String inputDD, String inputMM, String inputYYYY){

		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		String formatString = "dd/MM/yyyy";
		String dateString = inputDD + "/" + inputMM + "/" + inputYYYY;

		try {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			format.setLenient(false);
			format.parse(dateString);
		
		Date date = null;
		try {
			date = date_format.parse(dateString);
		} catch (Exception e1) {
			System.out.println(e1);
		}
		
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

		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(frame, "Invalid Date");
		} catch (IllegalArgumentException e2) {
			JOptionPane.showMessageDialog(frame, "Invalid Date");
		}
		return resultEntries;
	}
	
	/**
	 * description: This function is to return a linked list of Entry results after searching by amount
	 * @author: A0087809M, Pang Kang Wei, Joshua
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

		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");

		return resultEntries;
		}
	
	/**
	 * description: This function is to return a linked list of Entry results after searching by description
	 * @author: A0087809M, Pang Kang Wei, Joshua
	 * @param: description
	 */
	public LinkedList<Entry> searchByDescription(String description){
		LinkedList<Entry> allEntries = new LinkedList<Entry>();
		LinkedList<Entry> resultEntries = new LinkedList<Entry>();
		allEntries = entryMgr.getTransactionList();

		StringTokenizer st = new StringTokenizer(description, " ");
		String keyword;
		String concat = "";

		if (description != "") {

			for (int i = 0; i < allEntries.size(); i++) {
				st = new StringTokenizer(description, " ");
				concat = "";
				concat += allEntries.get(i).getAmount() + " ";
				concat += allEntries.get(i).getId() + " ";
				concat += allEntries.get(i).getCategory1() + " ";
				concat += allEntries.get(i).getCategory2() + " ";
				concat += allEntries.get(i).getDescription() + " ";
				concat += allEntries.get(i).getDate() + " ";
				concat += allEntries.get(i).getTransactionType();

				System.out.println(concat);
				concat.toUpperCase();
				
				while (st.hasMoreTokens()) {
					keyword = st.nextToken().toUpperCase();

					System.out.println("keyword is " + keyword);
					if(concat.contains(keyword)){
						resultEntries.add(allEntries.get(i));
						break;
					}
				}
			}
		}

		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");

		return resultEntries;
		}

	/**
	 * Default Constructor
	 * @param finances_FRM
	 */
	public SearchMgr(final Finances finances){
		
		//disable main frame to prevent generation of more than 1 pop up frame
		finances.disableFrame();
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JScrollPane searchMgr_SCP = new JScrollPane();				//ScrollPane to enable scrolling in window
		frame.getContentPane().add(searchMgr_SCP);
		
		final JPanel searchMgr_PNL = new JPanel(new MigLayout("", "[50px:n:50px][50:n:100][100px:n:100px][100px:n:100px,grow][100px:n:100px][grow][grow]", "[][][15px:n:15px][15px:n:15px][][15px:n:15px][15px:n:15px][][15px:n:15px][15px:n:15px][][][][][grow][grow]"));
		searchMgr_PNL.setBackground(Color.white);
		searchMgr_SCP.setViewportView(searchMgr_PNL);
		
		JRadioButton searchMgrSelectionDesc_RDBTN = new JRadioButton("");
		searchMgr_PNL.add(searchMgrSelectionDesc_RDBTN, "cell 0 1");
		searchMgrSelectionDesc_RDBTN.setMnemonic(3);
		
		searchMgrDesc_TF = new JTextField();
		searchMgr_PNL.add(searchMgrDesc_TF, "cell 3 1 2 1,growx");
		searchMgrDesc_TF.setColumns(10);
		
		JLabel searchMgrDesc_LBL = new JLabel("Description");
		searchMgr_PNL.add(searchMgrDesc_LBL, "cell 3 2,alignx center");
		
		
		final JScrollPane searchMgrScPane_SCP = new JScrollPane();					//ScrollPane to put the results Panel into it and to display results
		searchMgr_PNL.add(searchMgrScPane_SCP, "cell 0 14 7 2,grow");

		searchMgrResults_TABLE = new JTable();										//Table to display search results
		searchMgrScPane_SCP.setViewportView(searchMgrResults_TABLE);
		
		
		searchMgr_PNL.add(searchMgrDisplay_LBL, "cell 2 13 3 1");
		
		searchMgrHeading_LBL.setText("Search");
		searchMgrHeading_LBL.setFont(heading_font);
		searchMgrHeading_LBL.setIcon(new ImageIcon(Finances.class.getResource("/img/Search.png")));
		searchMgr_PNL.add(searchMgrHeading_LBL, "cell 0 0, alignx center");
		
		JRadioButton searchMgrSelectionAmount_RDBTN = new JRadioButton("");			//Radio button to specify search by amount
		searchMgr_PNL.add(searchMgrSelectionAmount_RDBTN, "cell 0 4");
		searchMgrSelectionAmount_RDBTN.setMnemonic(0);
		
		final JComboBox<String> searchMgrAmount_CB = new JComboBox<String>();		//Combo box to specify search by amount sign
		searchMgr_PNL.add(searchMgrAmount_CB, "cell 1 4 2 1");
		searchMgrAmount_CB.addItem("Greater than");
		searchMgrAmount_CB.addItem("Lesser than");
		searchMgrAmount_CB.addItem("Equal");
		searchMgrAmount_CB.addItem("Greater-Equal");
		searchMgrAmount_CB.addItem("Lesser-Equal");
		
		final JTextField searchMgrAmout_TF = new JTextField();						//Text field to specify amount
		searchMgr_PNL.add(searchMgrAmout_TF, "cell 3 4,growx");
		searchMgrAmout_TF.setColumns(10);
		
		JLabel searchMgrAmount_LBL = new JLabel("Amount");							//Label to specify amount
		searchMgr_PNL.add(searchMgrAmount_LBL, "cell 3 5,alignx center");
		
		JRadioButton searchMgrSelectionDate_RDBTN = new JRadioButton("");			//Radio button to specify search by date
		searchMgr_PNL.add(searchMgrSelectionDate_RDBTN, "cell 0 7");
		searchMgrSelectionDate_RDBTN.setMnemonic(1);
		
		final JComboBox<String> searchMgrDate_CB = new JComboBox<String>();			//Combo box to select the type of sign for date
		searchMgr_PNL.add(searchMgrDate_CB, "cell 1 7 2 1");
		searchMgrDate_CB.addItem("Before");
		searchMgrDate_CB.addItem("After");
		searchMgrDate_CB.addItem("Specific");
		searchMgrDate_CB.addItem("Bef-Include");
		searchMgrDate_CB.addItem("Aft-Include");
		
		
		searchMgrDay_TF = new JTextField();											//Text field to specify the day
		searchMgr_PNL.add(searchMgrDay_TF, "flowx,cell 3 7,alignx center");
		searchMgrDay_TF.setColumns(10);
		
		searchMgrMonth_TF = new JTextField();										//Text field to specify the month
		searchMgr_PNL.add(searchMgrMonth_TF, "cell 4 7,alignx center");
		searchMgrMonth_TF.setColumns(10);
		
		searchMgrYear_TF = new JTextField();										//Text field to specify the year
		searchMgr_PNL.add(searchMgrYear_TF, "cell 5 7,alignx center");
		searchMgrYear_TF.setColumns(10);
		
		JLabel searchMgrDay_LBL = new JLabel("DD");									//Label to specify the day
		searchMgr_PNL.add(searchMgrDay_LBL, "cell 3 8,alignx center");
		
		JLabel searchMgrMonth_LBL = new JLabel("MM");								//Label to specify the month
		searchMgr_PNL.add(searchMgrMonth_LBL, "cell 4 8,alignx center");
		
		JLabel searchMgrYear_LBL = new JLabel("YYYY");								//Label to specify the year
		searchMgr_PNL.add(searchMgrYear_LBL, "cell 5 8,alignx center");
		
		JRadioButton searchMgrSelectionType_RDBTN = new JRadioButton("");			//Radio button for selection type
		searchMgr_PNL.add(searchMgrSelectionType_RDBTN, "cell 0 10");
		searchMgrSelectionType_RDBTN.setMnemonic(2);
		
		final ButtonGroup searchMgrBtnGroup = new ButtonGroup();					//Radio button to specify the selection of search function
		searchMgrBtnGroup.add(searchMgrSelectionAmount_RDBTN);
		searchMgrBtnGroup.add(searchMgrSelectionDate_RDBTN);
		searchMgrBtnGroup.add(searchMgrSelectionType_RDBTN);
		searchMgrBtnGroup.add(searchMgrSelectionDesc_RDBTN);
		searchMgrSelectionAmount_RDBTN.setSelected(true);
		
		
		JLabel searchMgrType_LBL = new JLabel("Transaction Type");					//Label to specify the transaction type
		searchMgr_PNL.add(searchMgrType_LBL, "cell 1 10 2 1,alignx left");
		
		final JComboBox<String> searchMgrType_CB = new JComboBox<String>();			//Combo box for selecting the type of transaction type
		searchMgr_PNL.add(searchMgrType_CB, "cell 3 10 2 1,growx");
		searchMgrType_CB.addItem("Income Received");
		searchMgrType_CB.addItem("Expense by Assets");
		searchMgrType_CB.addItem("Expense by Credit");
		searchMgrType_CB.addItem("Repaying Loan");
		searchMgrType_CB.addItem("Take Loan");
		searchMgrType_CB.addItem("Transfer of Assets");
		searchMgrType_CB.addItem("Transfer of Liabilities");
		
		JButton searchMgrSearchNow_BTN = new JButton("Search Now");					//Button to search for Entries
		searchMgr_PNL.add(searchMgrSearchNow_BTN, "cell 5 13,alignx center");
		
		final JComboBox searchMgrSort_CB = new JComboBox();
		searchMgr_PNL.add(searchMgrSort_CB, "cell 4 13");
		searchMgrSort_CB.addItem("ID Asc");
		searchMgrSort_CB.addItem("ID Desc");
		searchMgrSort_CB.addItem("Amt Asc");
		searchMgrSort_CB.addItem("Amt Desc");
		searchMgrSort_CB.addItem("Date Asc");
		searchMgrSort_CB.addItem("Date Desc");
		searchMgrSort_CB.addItem("Type Asc");
		searchMgrSort_CB.addItem("Type Desc");
		/**
		 * description: Function is to listen for the Search Now button to be clicked and do the appropriate search
		 * @author: A0087809M, Pang Kang Wei, Joshua
		 */
		searchMgrSearchNow_BTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				LinkedList<Entry> searchedResults = new LinkedList<Entry>();

				switch (searchMgrBtnGroup.getSelection().getMnemonic()) {
				case 0:
					searchedResults = new LinkedList<Entry>();
					try{
					searchedResults = searchByAmount(searchMgrAmount_CB.getSelectedIndex(), Double.parseDouble(searchMgrAmout_TF.getText()));
					
					searchedResults = sortSearch(searchMgrSort_CB.getSelectedIndex(), searchedResults);
					}
					catch(Exception e){
						System.out.println("Invalid amount");

						JOptionPane.showMessageDialog(frame, "Invalid amount");
					}
					break;
				case 1:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByDate(searchMgrDate_CB.getSelectedIndex(), searchMgrDay_TF.getText(), searchMgrMonth_TF.getText(), searchMgrYear_TF.getText());

					searchedResults = sortSearch(searchMgrSort_CB.getSelectedIndex(), searchedResults);
					break;
				case 2:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByTransactionType(searchMgrType_CB.getSelectedIndex());

					searchedResults = sortSearch(searchMgrSort_CB.getSelectedIndex(), searchedResults);
					break;
				case 3:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByDescription(searchMgrDesc_TF.getText());

					searchedResults = sortSearch(searchMgrSort_CB.getSelectedIndex(), searchedResults);
					break;
				}
				
				DefaultTableModel model;

				String data[][] = new String[searchedResults.size()][7];
				
				//Setup the search results into data[][]
				for(int i=0; i<searchedResults.size(); i++){
					
					data[i][0] = searchedResults.get(i).getId() +"";
					
					switch(searchedResults.get(i).getTransactionType()){
					
					case 0:	data[i][1] = "Income Received";
							break;
					case 1:	data[i][1] = "Expense Using Assets";
							break;
					case 2:	data[i][1] = "Expense Using Liabilities";
							break;
					case 3:	data[i][1] = "Repay Loan";
							break;
					case 4:	data[i][1] = "Take Loan";
							break;
					case 5:	data[i][1] = "Intra-Asset Transfer";
							break;
					case 6:	data[i][1] = "Intra-Liability Transfer";
							break;
					}
					
					data[i][2] = double_format.format(searchedResults.get(i).getAmount());
					data[i][3] = date_format.format(searchedResults.get(i).getDate());
					data[i][4] = searchedResults.get(i).getCategory1();
					data[i][5] = searchedResults.get(i).getCategory2();
					data[i][6] = searchedResults.get(i).getDescription();
				}

				//Setup the column names for table
				String col[] = { "ID", "Transaction Type", "Amount", "Date", "Category 1", "Category 2", "Description" };

				model = new DefaultTableModel(data, col);
				searchMgrResults_TABLE = new JTable(model);
				searchMgrResults_TABLE.enable(false);
				searchMgrResults_TABLE.setLayout(new MigLayout("","[20][20][160][100][100][100][grow]","[]"));
				
				searchMgrResults_TABLE.revalidate();
				searchMgrScPane_SCP.setViewportView(searchMgrResults_TABLE);
				
				searched = true;
				searchMgrExport_BTN.setVisible(true);
				searchMgr_PNL.add(searchMgrExport_BTN, "cell 6 10");
			}
		});
		
		searchMgrExport_BTN.setIcon(new ImageIcon(Finances.class.getResource("/img/Export.png")));
		if(searched){
			searchMgrExport_BTN.setVisible(true);
			searchMgr_PNL.add(searchMgrExport_BTN, "cell 6 10");
		}
		
		searchMgrExport_BTN.addActionListener(new ActionListener(){
			/**
			 * description: button outputs search results to a xls file
			 * @author A0086581W, Wong Jing Ping
			 */
			public void actionPerformed(ActionEvent e) {
				
				//disable search frame
				frame.setEnabled(false);
				
				JFrame exportFrame = new JFrame();
				exportFrame.setVisible(true);
				exportFrame.setResizable(false);
				exportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				exportFrame.getContentPane().setBackground(new Color(255, 255, 255));
				exportFrame.getContentPane().setLayout(new MigLayout("", "[400]", "[shrink]"));
				
				exportFrame.addWindowListener(new WindowAdapter(){
					
					public void windowClosed(WindowEvent e) {
					
						frame.setEnabled(true);	
			        }
				});
				
				ExportPanel exportPanel = new ExportPanel(exportFrame, searchMgrResults_TABLE);
				
			}
			
		});
		frame.addWindowListener(new WindowAdapter(){
			
			public void windowClosed(WindowEvent e) {
				
				finances.reactivateFrame();	
				finances.refresh();
			}
		});
	}
	
}
