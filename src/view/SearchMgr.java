package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ButtonGroup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;

import data.Entry;

import logic.EntryMgr;
import logic.ExcelExporter;

/**
 * This GUI class is the Search Manager to help the user with searching for particular entries 
 * @author: Pang Kang Wei, Joshua A0087809M
 */
public class SearchMgr {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");	//private SimpleDateFormat variable to format the date while processing Entry information
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

	/**
	 * description: This function is to return a linked list of Entry results after searching by transaction type
	 * @author: Pang Kang Wei, Joshua A0087809M
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
			JOptionPane.showMessageDialog(frame, "Invalid Day");
		}
		
		//check if month is valid
		try{
			MM = Integer.parseInt(inputMM);
			if (MM <= 0 || MM > 12)
				throw new Exception();
		} catch (Exception exMM){
			System.out.println(exMM);
			JOptionPane.showMessageDialog(frame, "Invalid Month");
		}
		
		//check if year is valid
		try{
			YYYY  = Integer.parseInt(inputYYYY);
			if (YYYY < 1900 || YYYY > Calendar.getInstance().get(Calendar.YEAR))
				throw new Exception();
		} catch (Exception exYY){
			System.out.println(exYY);
			JOptionPane.showMessageDialog(frame, "Invalid year");
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

		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");
		return resultEntries;
	}
	
	/**
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

		searchMgrDisplay_LBL.setText(resultEntries.size() + " result(s) has been found");

		return resultEntries;
		}

	/**
	 * Default Constructor
	 */
	public SearchMgr(){
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JScrollPane searchMgr_SCP = new JScrollPane();				//ScrollPane to enable scrolling in window
		frame.getContentPane().add(searchMgr_SCP);
		
		final JPanel searchMgr_PNL = new JPanel(new MigLayout(				//Panel that contains all the GUI elements in window
				"", 
				"[50px:n:50px][50:n:100][100px:n:100px][100px:n:100px][100px:n:100px][grow][grow]", 
				"[][][][][][][][][][][][grow][grow]"));
		searchMgr_PNL.setBackground(Color.white);
		searchMgr_SCP.setViewportView(searchMgr_PNL);
		
		
		final JScrollPane searchMgrScPane_SCP = new JScrollPane();					//ScrollPane to put the results Panel into it and to display results
		searchMgr_PNL.add(searchMgrScPane_SCP, "cell 0 11 7 2,grow");
		
		final JPanel searchMgrResultsPane_PNL = new JPanel();						//Panel for the results
		searchMgrScPane_SCP.setViewportView(searchMgrResultsPane_PNL);
		searchMgrResultsPane_PNL.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		searchMgrResults_TABLE = new JTable();										//Table to display search results
		searchMgrResultsPane_PNL.add(searchMgrResults_TABLE, "cell 0 0,grow");
		
		searchMgr_PNL.add(searchMgrDisplay_LBL, "cell 2 10 3 1");
		
		searchMgrHeading_LBL.setText("Search");
		searchMgrHeading_LBL.setFont(heading_font);
		searchMgrHeading_LBL.setIcon(new ImageIcon(Finances.class.getResource("/img/Search.png")));
		searchMgr_PNL.add(searchMgrHeading_LBL, "cell 0 0, grow");
		
		JRadioButton searchMgrSelectionAmount_RDBTN = new JRadioButton("");			//Radio button to specify search by amount
		searchMgr_PNL.add(searchMgrSelectionAmount_RDBTN, "cell 0 2");
		searchMgrSelectionAmount_RDBTN.setMnemonic(0);
		
		final JComboBox<String> searchMgrAmount_CB = new JComboBox<String>();		//Combo box to specify search by amount sign
		searchMgr_PNL.add(searchMgrAmount_CB, "cell 1 2 2 1");
		searchMgrAmount_CB.addItem("Greater than");
		searchMgrAmount_CB.addItem("Lesser than");
		searchMgrAmount_CB.addItem("Equal");
		searchMgrAmount_CB.addItem("Greater-Equal");
		searchMgrAmount_CB.addItem("Lesser-Equal");
		
		final JTextField searchMgrAmout_TF = new JTextField();						//Text field to specify amount
		searchMgr_PNL.add(searchMgrAmout_TF, "cell 3 2,growx");
		searchMgrAmout_TF.setColumns(10);
		
		JLabel searchMgrAmount_LBL = new JLabel("Amount");							//Label to specify amount
		searchMgr_PNL.add(searchMgrAmount_LBL, "cell 3 3,alignx center");
		
		JRadioButton searchMgrSelectionDate_RDBTN = new JRadioButton("");			//Radio button to specify search by date
		searchMgr_PNL.add(searchMgrSelectionDate_RDBTN, "cell 0 5");
		searchMgrSelectionDate_RDBTN.setMnemonic(1);
		
		final JComboBox<String> searchMgrDate_CB = new JComboBox<String>();			//Combo box to select the type of sign for date
		searchMgr_PNL.add(searchMgrDate_CB, "cell 1 5 2 1");
		searchMgrDate_CB.addItem("Before");
		searchMgrDate_CB.addItem("After");
		searchMgrDate_CB.addItem("Specific");
		searchMgrDate_CB.addItem("Bef-Include");
		searchMgrDate_CB.addItem("Aft-Include");
		
		
		searchMgrDay_TF = new JTextField();											//Text field to specify the day
		searchMgr_PNL.add(searchMgrDay_TF, "flowx,cell 3 5,alignx center");
		searchMgrDay_TF.setColumns(10);
		
		searchMgrMonth_TF = new JTextField();										//Text field to specify the month
		searchMgr_PNL.add(searchMgrMonth_TF, "cell 4 5,alignx center");
		searchMgrMonth_TF.setColumns(10);
		
		searchMgrYear_TF = new JTextField();										//Text field to specify the year
		searchMgr_PNL.add(searchMgrYear_TF, "cell 5 5,alignx center");
		searchMgrYear_TF.setColumns(10);
		
		JLabel searchMgrDay_LBL = new JLabel("DD");									//Label to specify the day
		searchMgr_PNL.add(searchMgrDay_LBL, "cell 3 6,alignx center");
		
		JLabel searchMgrMonth_LBL = new JLabel("MM");								//Label to specify the month
		searchMgr_PNL.add(searchMgrMonth_LBL, "cell 4 6,alignx center");
		
		JLabel searchMgrYear_LBL = new JLabel("YYYY");								//Label to specify the year
		searchMgr_PNL.add(searchMgrYear_LBL, "cell 5 6,alignx center");
		
		JRadioButton searchMgrSelectionType_RDBTN = new JRadioButton("");			//Radio button for selection type
		searchMgr_PNL.add(searchMgrSelectionType_RDBTN, "cell 0 8");
		searchMgrSelectionType_RDBTN.setMnemonic(2);
		
		final ButtonGroup searchMgrBtnGroup = new ButtonGroup();					//Radio button to specify the selection of search function
		searchMgrBtnGroup.add(searchMgrSelectionAmount_RDBTN);
		searchMgrBtnGroup.add(searchMgrSelectionDate_RDBTN);
		searchMgrBtnGroup.add(searchMgrSelectionType_RDBTN);
		searchMgrSelectionAmount_RDBTN.setSelected(true);
		
		
		JLabel searchMgrType_LBL = new JLabel("Transaction Type");					//Label to specify the transaction type
		searchMgr_PNL.add(searchMgrType_LBL, "cell 1 8 2 1,alignx left");
		
		final JComboBox<String> searchMgrType_CB = new JComboBox<String>();			//Combo box for selecting the type of transaction type
		searchMgr_PNL.add(searchMgrType_CB, "cell 3 8 2 1,growx");
		searchMgrType_CB.addItem("Income Received");
		searchMgrType_CB.addItem("Expense by Assets");
		searchMgrType_CB.addItem("Expense by Credit");
		searchMgrType_CB.addItem("Repaying Loan");
		searchMgrType_CB.addItem("Take Loan");
		searchMgrType_CB.addItem("Transfer of Assets");
		searchMgrType_CB.addItem("Transfer of Liabilities");
		
		JButton searchMgrSearchNow_BTN = new JButton("Search Now");					//Button to search for Entries
		searchMgr_PNL.add(searchMgrSearchNow_BTN, "cell 5 10,alignx center");
		/**
		 * description: Function is to listen for the Search Now button to be clicked and do the appropriate search
		 * @author: Pang Kang Wei, Joshua A0087809M
		 */
		searchMgrSearchNow_BTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				LinkedList<Entry> searchedResults = new LinkedList<Entry>();

				switch (searchMgrBtnGroup.getSelection().getMnemonic()) {
				case 0:
					searchedResults = new LinkedList<Entry>();
					try{
					searchedResults = searchByAmount(searchMgrAmount_CB.getSelectedIndex(), Double.parseDouble(searchMgrAmout_TF.getText()));
					}
					catch(Exception e){
						System.out.println("Invalid amount");

						JOptionPane.showMessageDialog(frame, "Invalid amount");
					}
					break;
				case 1:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByDate(searchMgrDate_CB.getSelectedIndex(), searchMgrDay_TF.getText(), searchMgrMonth_TF.getText(), searchMgrYear_TF.getText());
					break;
				case 2:
					searchedResults = new LinkedList<Entry>();
					searchedResults = searchByTransactionType(searchMgrType_CB.getSelectedIndex());
					break;
				}
				
				DefaultTableModel model;

				String data[][] = new String[searchedResults.size()][7];
				
				//Setup the search results into data[][]
				for(int i=0; i<searchedResults.size(); i++){
					data[i][0] = searchedResults.get(i).getId() +"";
					data[i][1] = searchedResults.get(i).getTransactionType() +"";
					data[i][2] = searchedResults.get(i).getAmount() +"";
					data[i][3] = searchedResults.get(i).getDate() +"";
					data[i][4] = searchedResults.get(i).getCategory1() +"";
					data[i][5] = searchedResults.get(i).getCategory2() +"";
					data[i][6] = searchedResults.get(i).getDescription() +"";
				}

				//Setup the column names for table
				String col[] = { "ID", "Transaction Type", "Amount", "Date", "Cat1", "Cat2", "Description" };

				model = new DefaultTableModel(data, col);
				searchMgrResults_TABLE = new JTable(model);
				searchMgrResults_TABLE.enable(false);
				searchMgrResults_TABLE.setLayout(new MigLayout("","[20][20][160][100][100][100][grow]","[]"));
				
				//Renew the results panel
				searchMgrResultsPane_PNL.removeAll();
				searchMgrScPane_SCP.setViewportView(searchMgrResultsPane_PNL);
				searchMgrResultsPane_PNL.setLayout(new MigLayout("", "[grow]", "[grow]"));
				searchMgrResultsPane_PNL.add(searchMgrResults_TABLE, "cell 0 0,grow");
				searchMgrResults_TABLE.revalidate();
				searchMgrResultsPane_PNL.revalidate();
				searchMgrResultsPane_PNL.setVisible(true);
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
			 * @author JP
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO allow user to input file path
				File result = new File("results.xls");
				
				ExcelExporter exporter = new ExcelExporter();
				exporter.exportTable(searchMgrResults_TABLE, result);

			}
			
		});
	}
}
