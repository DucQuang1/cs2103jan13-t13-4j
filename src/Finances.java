import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * GUI class that displays the main window
 * @author JP
 *
 */
public class Finances {

	private final JFrame mainFrame = new JFrame();						//GUI of user interface
	
	private final TransactionMgr transactionMgr = new TransactionMgr(this);	//TransactionMgr handles all operations from user
	
	private final JPanel BalancePanel = new JPanel();					//BalancePanel displays the welcome msg and user's balance
	private final JPanel AssetPanel = new JPanel();						//AssetPanel displays the chart, edit and transfer buttons
	private final JPanel LiabilityPanel = new JPanel();					//LiabilityPanel displays the chart, edit and transfer buttons
	private final JPanel IncomePanel = new JPanel();					//IncomePanel holds the chart and edit income category button
	private final JPanel ExpensePanel = new JPanel();					//ExpensePanel holds the chart and edit expense category button
	private final JPanel RightPanel = new JPanel();						//RightPanel holds scrolling pane and crudPanel
	private final JScrollPane TransactionListPane = new JScrollPane();	//Scrollable pane for viewing history of transactions
	private final JPanel TransactionListPanel = new JPanel();			//TransactionListPanel displays the list of past transactions
	private final JPanel crudPanel = new JPanel();						//crudPanel stores the buttons for CRUD operations, undo and search 

	//labels
	private final JLabel lblWelcomeToExpenzs = new JLabel("Welcome to Expenzs!");
	private final JLabel lblBalance = new JLabel("You have $" + transactionMgr.getBalance());	//Needs to be updated
	private final JLabel lblTransactions = new JLabel("Transactions", SwingConstants.CENTER);
	
	//buttons for editing chart and categories
	private final JButton btnEditAssetCategories = new JButton("Edit Asset Categories");
	private final JButton btnAssetTransfer = new JButton("Transfer");
	private final JButton btnEditLiabilityCategories = new JButton("Edit Liability Categories");
	private final JButton btnLiabilityTransfer = new JButton("Transfer");
	private final JButton btnEditIncomeCategories = new JButton("Edit Income Categories");
	private final JButton btnEditExpenseCategories = new JButton("Edit Expense Categories");
	
	//crud, undo, search buttons
	private final JButton btnAdd = new JButton(new ImageIcon(Finances.class.getResource("/img/Add.png")));
	private final JButton btnEdit = new JButton(new ImageIcon(Finances.class.getResource("/img/Edit.png")));
	private final JButton btnDel = new JButton(new ImageIcon(Finances.class.getResource("/img/Del.png")));
	private final JButton btnUndo = new JButton(new ImageIcon(Finances.class.getResource("/img/Undo.png")));
	private final JButton btnSearch = new JButton(new ImageIcon(Finances.class.getResource("/img/Search.png")));
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try {
					Finances window = new Finances();
					window.mainFrame.setVisible(true);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Finances(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(){
		
		mainFrame.getContentPane().setBackground(new Color(255, 255, 255));
		mainFrame.setResizable(false);
		mainFrame.setSize(1160, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new MigLayout("", "[400]0[400]0[250:300:350]", "[100,grow]0[300,grow]0[300,grow]"));
		
		BalancePanel.setBackground(new Color(255, 255, 255));		
		BalancePanel.setLayout(new MigLayout("", "[1100]", "[50]5[30]"));
		BalancePanel.add(lblWelcomeToExpenzs, "cell 0 0");
		BalancePanel.add(lblBalance, "cell 0 1");
		mainFrame.getContentPane().add(BalancePanel, "cell 0 0 3 1,grow");
		
		AssetPanel.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
		AssetDataset = TransactionMgr.getAssetCatMgr().getChartData();
		AssetPanel.add(renderChart(AssetDataset, 0));		
		AssetPanel.add(btnEditAssetCategories);
		AssetPanel.add(btnAssetTransfer);
		mainFrame.getContentPane().add(AssetPanel, "cell 0 1,grow");

		LiabilityPanel.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityCatMgr().getChartData();
		LiabilityPanel.add(renderChart(LiabilityDataset, 1));
		LiabilityPanel.add(btnEditLiabilityCategories);
		LiabilityPanel.add(btnLiabilityTransfer);
		mainFrame.getContentPane().add(LiabilityPanel, "cell 1 1,grow");
		
		RightPanel.setBackground(new Color(255, 255, 255));
		RightPanel.setLayout(new MigLayout("", "0[300,grow]0", "0[50]0[400,grow]0[50]0"));
		mainFrame.getContentPane().add(RightPanel, "cell 2 1 1 2,grow");
		
		lblTransactions.setFont(new Font("Tahoma", Font.BOLD, 22));
		RightPanel.add(lblTransactions, "cell 0 0,growx,aligny top");

		TransactionListPanel.setBackground(new Color(240, 240, 230));
		
		renderList(TransactionListPanel, TransactionMgr.getEntryMgr().getTransactionList());
		TransactionListPane.setViewportView(TransactionListPanel);
		TransactionListPanel.setLayout(new MigLayout("flowy", "5[grow,left]5", "5[grow,top]5"));
		RightPanel.add(TransactionListPane, "cell 0 1,grow");
		RightPanel.validate();
		
		crudPanel.setBackground(new Color(255, 255, 255));
		crudPanel.setLayout(new MigLayout("", "0[50]3[50]3[50]3[50]3[50]0", "0[50]0"));
		RightPanel.add(crudPanel, "flowx,cell 0 2");
		
		//Add the respective buttons with their icon and a simple tooltip
		btnAdd.setBackground(new Color(255, 255, 255));
		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.AddTransaction();
			}
		});
		btnAdd.setToolTipText("Add an Entry");
		crudPanel.add(btnAdd, "cell 0 0");
		
		btnEdit.setBackground(new Color(255, 255, 255));
		btnEdit.setToolTipText("Edit an Entry");
		crudPanel.add(btnEdit, "cell 1 0");
		
		btnDel.setBackground(new Color(255, 255, 255));
		btnDel.setToolTipText("Delete an Entry");
		crudPanel.add(btnDel, "cell 2 0");		
		
		btnUndo.setBackground(new Color(255, 255, 255));
		btnUndo.setToolTipText("Undo your last transaction");
		crudPanel.add(btnUndo, "cell 3 0");
		
		btnSearch.setBackground(new Color(255, 255, 255));
		btnSearch.setToolTipText("Search");
		crudPanel.add(btnSearch, "cell 4 0");
		
		IncomePanel.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.getIncomeCatMgr().getChartData();
		IncomePanel.add(renderChart(IncomeDataset,2));
		IncomePanel.add(btnEditIncomeCategories);
		mainFrame.getContentPane().add(IncomePanel, "cell 0 2,grow");

		
		ExpensePanel.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.getExpenseCatMgr().getChartData();
		ExpensePanel.add(renderChart(ExpenseDataset, 3));
		ExpensePanel.add(btnEditExpenseCategories);
		mainFrame.getContentPane().add(ExpensePanel, "cell 1 2,grow");

	}
	
	/**
	 * refreshes the 4 charts and the transactionList
	 */
	public void refresh(){
		
		//refresh total balance
		lblBalance.setText("You have $" + transactionMgr.getBalance());
		
		//refresh 4 types
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
		AssetDataset = TransactionMgr.getAssetCatMgr().getChartData();
		AssetPanel.removeAll();
		AssetPanel.add(renderChart(AssetDataset, 0));
		AssetPanel.add(btnEditAssetCategories);
		AssetPanel.add(btnAssetTransfer);
		
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityCatMgr().getChartData();
		LiabilityPanel.removeAll();
		LiabilityPanel.add(renderChart(LiabilityDataset, 1));
		LiabilityPanel.add(btnEditLiabilityCategories);
		LiabilityPanel.add(btnLiabilityTransfer);
		
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.getIncomeCatMgr().getChartData();
		IncomePanel.removeAll();
		IncomePanel.add(renderChart(IncomeDataset, 2));
		IncomePanel.add(btnEditIncomeCategories);
		
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.getExpenseCatMgr().getChartData();
		ExpensePanel.removeAll();
		ExpensePanel.add(renderChart(ExpenseDataset, 3));
		ExpensePanel.add(btnEditExpenseCategories);
		
		//refresh transactionList
		renderList(TransactionListPanel, TransactionMgr.getEntryMgr().getTransactionList());
		mainFrame.validate();
	}

	/**
	 * This method takes in a scrollable pane, and populates it with entries from a linked list
	 * Each entry fills up a JTextArea
	 * @param transactionListPane
	 * @param transactionList
	 */
	private void renderList(JPanel ListPane, LinkedList<Entry> transactionList){
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
						break;
			}
			entryText += Double.toString(transactionList.get(i).getAmount()) + "\n";
			entryText += "From:\t" + transactionList.get(i).getCategory1() + "\n";
			entryText += "To:\t" + transactionList.get(i).getCategory2() + "\n";
			entry.setText(entryText);			
			tempPanel.add(entry);
			JLabel descriptionLabel = new JLabel("<html>" + transactionList.get(i).getDescription() + "</html>");
			descriptionLabel.setFont(new Font("SanSerif",Font.ITALIC,12));
			tempPanel.add(descriptionLabel);
			
			ListPane.add(tempPanel, "alignx left, gapx 2px 5px, gapy 2px 2px, top");
			ListPane.validate();
		}
	}

	/**
	 * This method renders the chart given a dataset on the respective panel
	 * @param dataset
	 * @param type (0: Assets, 1: Liabilities, 2: Income, 3: Expense)
	 */
	private ChartPanel renderChart(DefaultCategoryDataset dataset, int type){

		//Customize the chart's title
		String title = "";
		switch(type){
			case 0:	title = "Assets";
					break;
			case 1:	title = "Liabilities";
					break;
			case 2:	title = "Income";
					break;
			case 3:	title = "Expenses";
					break;
		}
		
		//Create JFreeChart with dataSet
		JFreeChart newChart = ChartFactory.createBarChart(
				title, "Categories", "Amount", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		
		//Change the chart's visual properties
		CategoryPlot chartPlot = newChart.getCategoryPlot();
		chartPlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
		BarRenderer chartRenderer = (BarRenderer) chartPlot.getRenderer();
		
		//Customize the chart's color
		switch (type){
			case 0:	chartRenderer.setSeriesPaint(0, new Color(50, 170, 20));
					break;
			case 1:	chartRenderer.setSeriesPaint(0, new Color(200, 30, 20));
					break;
			case 2: chartRenderer.setSeriesPaint(0, new Color(13, 92, 166));
					break;
			case 3:	chartRenderer.setSeriesPaint(0, new Color(255, 205, 50));
					break;
		}
		chartRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
		
		//Create the chart panel
		ChartPanel newChartPanel = new ChartPanel(newChart,350,200,200,100,800,300,true,true,true,true,true,true);
		newChartPanel.setSize(270, 200);
		newChartPanel.setVisible(true);
		
		return newChartPanel;
	}
}
