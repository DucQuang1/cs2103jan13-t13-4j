import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.FlowLayout;

public class Finances {

	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Finances window = new Finances();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Finances() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.getContentPane().setBackground(new Color(255, 255, 255));
		mainFrame.setResizable(false);
		mainFrame.setSize(1160, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new MigLayout("", "[400]0[400]0[250:300:350]", "[100,grow]0[300,grow]0[300,grow]"));
		
		/**
		 * BalancePanel holds the welcome message and the user's balance
		 */
		JPanel BalancePanel = new JPanel();
		BalancePanel.setBackground(new Color(255, 255, 255));		
		mainFrame.getContentPane().add(BalancePanel, "cell 0 0 3 1,grow");
		BalancePanel.setLayout(new MigLayout("", "[1100]", "[50]5[30]"));
		
		JLabel lblWelcomeToExpenzs = new JLabel("Welcome to Expenzs!");
		BalancePanel.add(lblWelcomeToExpenzs, "cell 0 0");
		
		Double balance = 6320.00;	//test data. will use a transactionMgr.getBalance() to get balance
		JLabel lblYouHave = new JLabel("You have $" + balance);
		BalancePanel.add(lblYouHave, "cell 0 1");
		
		/**
		 * AssetPanel holds the chart, edit and intra-asset transfer buttons
		 */
		JPanel AssetPanel = new JPanel();
		AssetPanel.setBackground(new Color(255, 255, 255));
		
		/*	Actual code:
		
			TransactionMgr transactionMgr = new TransactionMgr();
			
			DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
			AssetDataset = TransactionMgr.AssetCatMgr.getChartData();
			renderChart(AssetPanel, AssetDataset);
			
		*/
		
		//start of temp code. direct from AssetCatMgr for now for testing purposes
		AssetCatMgr AssetCatMgrTemp = new AssetCatMgr();
		AssetPanel.add(AssetCatMgrTemp.readAssetCatTxt());
		
		mainFrame.getContentPane().add(AssetPanel, "cell 0 1,grow");
		//end of temp code.
		
		JButton btnEditAssetCategories = new JButton("Edit Asset Categories");
		AssetPanel.add(btnEditAssetCategories);
		
		JButton btnAssetTransfer = new JButton("Transfer");
		AssetPanel.add(btnAssetTransfer);
		
		/**
		 * LiabilityPanel holds the chart, edit and intra-liability transfer buttons
		 */
		JPanel LiabilityPanel = new JPanel();
		LiabilityPanel.setBackground(new Color(255, 255, 255));
		
		/*	Actual code:
		
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.LiabilityCatMgr.getChartData();
		renderChart(LiabilityPanel, LiabilityDataset);
		
		*/
		
		//start of test code. 
		LiabilityCatMgr LiabilityCatMgrTemp = new LiabilityCatMgr();
		LiabilityPanel.add(LiabilityCatMgrTemp.readLiabilityCatTxt());
			
		mainFrame.getContentPane().add(LiabilityPanel, "cell 1 1,grow");
		//end of test code.
		
		JButton btnEditLiabilityCategories = new JButton("Edit Liability Categories");
		LiabilityPanel.add(btnEditLiabilityCategories);
		
		JButton btnLiabilityTransfer = new JButton("Transfer");
		LiabilityPanel.add(btnLiabilityTransfer);
		
		//HistoryPanel holds the list of transactions and has a menu bar below for CRUD, undo and search
		JPanel RightPanel = new JPanel();
		RightPanel.setBackground(new Color(255, 255, 255));
		mainFrame.getContentPane().add(RightPanel, "cell 2 1 1 2,grow");
		RightPanel.setLayout(new MigLayout("", "0[300,grow]0", "0[50]0[400,grow]0[50]0"));
		
		//Header above the transactions list
		JLabel lblTransactions = new JLabel("Transactions",SwingConstants.CENTER);
		lblTransactions.setFont(new Font("Tahoma", Font.BOLD, 22));
		RightPanel.add(lblTransactions, "cell 0 0,growx,aligny top");
		
		//Scrollable pane for viewing history of transactions
		JScrollPane TransactionListPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/*
		 * Actual code: (how to create and access EntryMgr from both transM and searchM?
		 * renderList(TransactionListPane, transactionMgr.EntryMgr.getTransactionList());
		 */
		JPanel TransactionListPanel = new JPanel();
		TransactionListPanel.setBackground(new Color(250, 250, 250));
		
		//start of test code
		EntryMgr entryMgr = new EntryMgr();
		renderList(TransactionListPanel, entryMgr.getTransactionList());
		//end of test code
		TransactionListPane.setViewportView(TransactionListPanel);
		TransactionListPanel.setLayout(new MigLayout("flowy", "5[grow,left]5", "5[grow,top]5"));
		RightPanel.add(TransactionListPane, "cell 0 1,grow");
		RightPanel.validate();
		
		//crudPanel stores the buttons for CRUD operations, undo and search
		JPanel crudPanel = new JPanel();
		crudPanel.setBackground(new Color(255, 255, 255));
		crudPanel.setLayout(new MigLayout("", "0[50]3[50]3[50]3[50]3[50]0", "0[50]0"));
		RightPanel.add(crudPanel, "flowx,cell 0 2");
		
		//Add the respective buttons with their icon and a simple tooltip
		JButton btnAdd = new JButton(new ImageIcon(Finances.class.getResource("/img/Add.png")));
		btnAdd.setToolTipText("Add an Entry");
		crudPanel.add(btnAdd, "cell 0 0");
		
		JButton btnEdit = new JButton(new ImageIcon(Finances.class.getResource("/img/Edit.png")));
		btnEdit.setToolTipText("Edit an Entry");
		crudPanel.add(btnEdit, "cell 1 0");
		
		JButton btnDel = new JButton(new ImageIcon(Finances.class.getResource("/img/Del.png")));
		btnDel.setToolTipText("Delete an Entry");
		crudPanel.add(btnDel, "cell 2 0");
		
		JButton btnUndo = new JButton(new ImageIcon(Finances.class.getResource("/img/Undo.png")));
		btnUndo.setToolTipText("Undo your last transaction");
		crudPanel.add(btnUndo, "cell 3 0");
		
		JButton btnSearch = new JButton(new ImageIcon(Finances.class.getResource("/img/Search.png")));
		btnSearch.setToolTipText("Search");
		crudPanel.add(btnSearch, "cell 4 0");
		
		/**
		 * IncomePanel holds the chart and edit income category button
		 */
		JPanel IncomePanel = new JPanel();
		IncomePanel.setBackground(new Color(255, 255, 255));
		
		/*	Actual code:
		
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.IncomeCatMgr.getChartData();
		renderChart(IncomePanel, IncomeDataset);
		
		*/
		
		//start of test code. 
		IncomeCatMgr IncomeCatMgrTemp = new IncomeCatMgr();
		IncomePanel.add(IncomeCatMgrTemp.readIncomeCatTxt());
			
		mainFrame.getContentPane().add(IncomePanel, "cell 0 2,grow");
		//end of test code.
		
		JButton btnEditIncomeCategories = new JButton("Edit Income Categories");
		IncomePanel.add(btnEditIncomeCategories);
		
		/**
		 * ExpensePanel holds the chart and edit expense category button
		 */
		JPanel ExpensePanel = new JPanel();
		ExpensePanel.setBackground(new Color(255, 255, 255));
	
		/*	Actual code:
		
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.ExpenseCatMgr.getChartData();
		renderChart(ExpensePanel, ExpenseDataset);
		
		*/
		
		//start of test code. 
		ExpenseCatMgr ExpenseCatMgrTemp = new ExpenseCatMgr();
		ExpensePanel.add(ExpenseCatMgrTemp.readExpenseCatTxt());
		
		mainFrame.getContentPane().add(ExpensePanel, "cell 1 2,grow");
		//end of test code.
		
	JButton btnEditExpenseCategories = new JButton("Edit Expense Categories");
	ExpensePanel.add(btnEditExpenseCategories);
	}
	
	/**
	 * This method takes in a scrollable pane, and populates it with entries from a linked list
	 * Each entry fills up a JTextArea
	 * @param transactionListPane
	 * @param transactionList
	 */
	private void renderList(JPanel ListPane, LinkedList<Entry> transactionList) {
		int size = transactionList.size();
		for(int i = 0; i < size; ++i){
			JPanel tempPanel = new JPanel(new MigLayout("flowy","5[280]5","[]"));
			tempPanel.setBackground(new Color(255, 255, 255));
			JTextArea entry = new JTextArea();
			String entryText = new String();
			entryText += "ID:\t" + Integer.toString(transactionList.get(i).getId()) + "\n";
			switch(transactionList.get(i).getTransactionType())
			{
				case 0:	entryText += "Income\t";
						break;
				case 1:
				case 2: entryText += "Expense\t";
						break;
				case 3: entryText += "Take Loan\t";
						break;
				case 4:	entryText += "Repay Loan\t";
						break;
				default:entryText += "Unspecified Type!";
			}
			entryText += Double.toString(transactionList.get(i).getAmount()) + "\n";
			entryText += "From:\t" + transactionList.get(i).getCategory1() + "\n";
			entryText += "To:\t" + transactionList.get(i).getCategory2() + "\n";
			entry.setText(entryText);			
			tempPanel.add(entry);
			JLabel descriptionLabel = new JLabel("<html>" + transactionList.get(i).getDescription() + "</html>");
			descriptionLabel.setFont(new Font("SanSerif",Font.ITALIC,12));
			tempPanel.add(descriptionLabel);
			/*
			 * old style which involves all JLabels
			Border id = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder());
			tempPanel.setBorder(id);
			JLabel idLabel = new JLabel("ID:\t" + Integer.toString(transactionList.get(i).getId()));
			JLabel transactionAmountLabel = new JLabel();
			String amountLine = new String();
			switch(transactionList.get(i).getTransactionType())
			{
				case 0:	amountLine = "Income\t";
						break;
				case 1:
				case 2: amountLine = "Expense\t";
						break;
				case 3: amountLine = "Take Loan\t";
						break;
				case 4:	amountLine = "Repay Loan\t";
						break;
			}
			amountLine += Double.toString(transactionList.get(i).getAmount());
			transactionAmountLabel.setText(amountLine);
			JLabel categoryLabel1 = new JLabel("From:\t" + transactionList.get(i).getCategory1());
			JLabel categoryLabel2 = new JLabel("To:\t" + transactionList.get(i).getCategory2());
			JLabel descriptionLabel = new JLabel("<html>" + transactionList.get(i).getDescription() + "</html>");
			descriptionLabel.setFont(new Font("SanSerif",Font.ITALIC,12));
			tempPanel.add(idLabel);
			tempPanel.add(transactionAmountLabel);
			tempPanel.add(categoryLabel1);
			tempPanel.add(categoryLabel2);
			tempPanel.add(descriptionLabel);
			*/
			
			ListPane.add(tempPanel, "alignx left, gapx 2px 5px, gapy 2px 2px, top");
		}
	}

	/**
	 * This method renders the chart given a dataset on the respective panel
	 * @param panel
	 * @param dataset
	 */
	private void renderChart(JPanel panel, DefaultCategoryDataset dataset){
		
		//Create JFreeChart with dataSet
		JFreeChart newChart = ChartFactory.createBarChart(
				"Assets", "Categories", "Amount", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		
		//Change the chart's visual properties
		CategoryPlot chartPlot = newChart.getCategoryPlot();
		chartPlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
		BarRenderer chartRenderer = (BarRenderer) chartPlot.getRenderer();
		chartRenderer.setSeriesPaint(0, new Color(50, 170, 20));			//TO DO: customize the color
		chartRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
		
		//creating the chart panel
		ChartPanel newChartPanel = new ChartPanel(newChart,350,200,200,100,800,300,true,true,true,true,true,true);
		newChartPanel.setSize(270, 200);
		newChartPanel.setVisible(true);
		
		panel.add(newChartPanel);
	}
}
