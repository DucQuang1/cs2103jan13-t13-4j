import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.*;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class MainWindow {

	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
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
		
		JLabel lblYouHave = new JLabel("You have $");
		BalancePanel.add(lblYouHave, "cell 0 1");
		
		/**
		 * AssetPanel holds the chart, edit and intra-asset transfer buttons
		 */
		JPanel AssetPanel = new JPanel();
		AssetPanel.setBackground(new Color(255, 255, 255));
		
		/*
			//test chart. Actual case will use a getChartPanel() method from the AssetManager object
			DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
			AssetDataset.setValue(1000, "Amount", "OCBC");
			AssetDataset.setValue(800, "Amount", "DBS");
			AssetDataset.setValue(200, "Amount", "Cash");
			AssetDataset.setValue(55, "Amount", "EZ-Link");
	
			//creating the JFreeChart
			JFreeChart AssetType = ChartFactory.createBarChart(
					"Assets", "Categories", "Amount", AssetDataset,
					PlotOrientation.VERTICAL, false, true, false);
			
			//to change the chart's visual properties
			CategoryPlot AssetPlot = AssetType.getCategoryPlot();
			AssetPlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
			BarRenderer AssetRenderer = (BarRenderer) AssetPlot.getRenderer();
			AssetRenderer.setSeriesPaint(0, new Color(50, 170, 20));
			AssetRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			BarRenderer.setDefaultShadowsVisible(false);						//to turn off the 'shadows'. Static property of BarRenderer, so only needs to done once :)
			
			//creating the chart panel
			ChartPanel AssetTypeChart = new ChartPanel(AssetType,350,200,200,100,500,200,true,true,true,true,true,true);
			AssetTypeChart.setSize(270, 200);
			AssetTypeChart.setVisible(true);
			//end of test data.
		*/
		AssetCatMgr AssetCatMgrTemp = new AssetCatMgr();
		AssetPanel.add(AssetCatMgrTemp.readAssetCatTxt());
		
		mainFrame.getContentPane().add(AssetPanel, "cell 0 1,grow");
		
		/**
		 * LiabilityPanel holds the chart, edit and intra-liability transfer buttons
		 */
		JPanel LiabilityPanel = new JPanel();
		LiabilityPanel.setBackground(new Color(255, 255, 255));
		
		/*
			//test chart. Actual case will use a getChartPanel() method from the AssetManager object
			DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
			LiabilityDataset.setValue(1000, "Amount", "AMEX Card");
			LiabilityDataset.setValue(2000, "Amount", "OCBC Platinum");
			
			//creating the JFreeChart
			JFreeChart LiabilityType = ChartFactory.createBarChart(
					"Liabilities", "Categories", "Amount", LiabilityDataset,
					PlotOrientation.VERTICAL, false, true, false);
	
			//to change the bar colours
			CategoryPlot LiabilityPlot = LiabilityType.getCategoryPlot();
			LiabilityPlot.setBackgroundPaint(Color.WHITE);							//to set the background colour of the chart as black
			BarRenderer LiabilityRenderer = (BarRenderer) LiabilityPlot.getRenderer();
			LiabilityRenderer.setSeriesPaint(0, new Color(200, 30, 20));
			LiabilityRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel LiabilityTypeChart = new ChartPanel(LiabilityType,350,200,200,100,500,200,true,true,true,true,true,true);
			LiabilityTypeChart.setSize(270, 200);
			LiabilityTypeChart.setVisible(true);
			//end of test data.
		*/
		LiabilityCatMgr LiabilityCatMgrTemp = new LiabilityCatMgr();
		LiabilityPanel.add(LiabilityCatMgrTemp.readLiabilityCatTxt());
			
		mainFrame.getContentPane().add(LiabilityPanel, "cell 1 1,grow");
		
		/**
		 * HistoryPanel holds the list of transactions and has a menu bar below for CRUD, undo and search
		 */
		JPanel HistoryPanel = new JPanel();
		HistoryPanel.setBackground(new Color(255, 255, 255));
		mainFrame.getContentPane().add(HistoryPanel, "cell 2 1 1 2,grow");
		HistoryPanel.setLayout(new MigLayout("", "0[300,grow]0", "0[50]0[400,grow]0[50]0"));
		
		/**
		 * Header above the transactions list
		 */
		JLabel lblTransactions = new JLabel("Transactions",SwingConstants.CENTER);
		lblTransactions.setFont(new Font("labelFont", Font.PLAIN, 20));
		HistoryPanel.add(lblTransactions, "cell 0 0,grow");
		
		/**
		 * Scrollable pane for viewing history of transactions
		 */
		JScrollPane TransactionListPane = new JScrollPane();
		HistoryPanel.add(TransactionListPane, "cell 0 1,grow");
		
		/**
		 * crudPanel stores the buttons for CRUD operations, undo and search
		 */
		JPanel crudPanel = new JPanel();
		crudPanel.setBackground(new Color(255, 255, 255));
		HistoryPanel.add(crudPanel, "flowx,cell 0 2");
		crudPanel.setLayout(new MigLayout("", "0[50]0[50]0[50]0[50]0[50]0", "0[50]0"));
		
		JButton btnAdd = new JButton("");
		btnAdd.setBackground(new Color(255, 255, 255));
		btnAdd.setIcon(new ImageIcon(MainWindow.class.getResource("/img/Add.png")));
		crudPanel.add(btnAdd, "cell 0 0");
		
		JButton btnEdit = new JButton(new ImageIcon(MainWindow.class.getResource("/img/Edit.png")));
		crudPanel.add(btnEdit, "cell 1 0");
		
		JButton btnDel = new JButton("");
		btnDel.setIcon(new ImageIcon(MainWindow.class.getResource("/img/Del.png")));
		crudPanel.add(btnDel, "cell 2 0");
		
		JButton btnUndo = new JButton("");
		btnUndo.setIcon(new ImageIcon(MainWindow.class.getResource("/img/Undo.png")));
		crudPanel.add(btnUndo, "cell 3 0");
		
		JButton btnSearch = new JButton("");
		btnSearch.setIcon(new ImageIcon(MainWindow.class.getResource("/img/Search.png")));
		crudPanel.add(btnSearch, "cell 4 0");
		
		/**
		 * IncomePanel holds the chart and edit income category button
		 */
		JPanel IncomePanel = new JPanel();
		IncomePanel.setBackground(new Color(255, 255, 255));
		
		/*
			//test chart. Actual case will use a getChartPanel() method from the AssetManager object
			DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
			IncomeDataset.setValue(3000, "Amount", "Salary");
			IncomeDataset.setValue(500, "Amount", "Dividends");
			IncomeDataset.setValue(300, "Amount", "GST rebate");
			
			//creating the JFreeChart
			JFreeChart IncomeType = ChartFactory.createBarChart(
					"Income", "Categories", "Amount", IncomeDataset,
					PlotOrientation.VERTICAL, false, true, false);
	
			//to change the bar colours
			CategoryPlot IncomePlot = IncomeType.getCategoryPlot();
			IncomePlot.setBackgroundPaint(Color.WHITE);							//to set the background colour of the chart as black
			BarRenderer IncomeRenderer = (BarRenderer) IncomePlot.getRenderer();
			IncomeRenderer.setSeriesPaint(0, new Color(50, 96, 255));
			IncomeRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel IncomeTypeChart = new ChartPanel(IncomeType,350,200,200,100,500,200,true,true,true,true,true,true);
			IncomeTypeChart.setSize(270, 200);
			IncomeTypeChart.setVisible(true);
			//end of test data.
		*/
		IncomeCatMgr IncomeCatMgrTemp = new IncomeCatMgr();
		IncomePanel.add(IncomeCatMgrTemp.readIncomeCatTxt());
			
		mainFrame.getContentPane().add(IncomePanel, "cell 0 2,grow");
		
		/**
		 * ExpensePanel holds the chart and edit expense category button
		 */
		JPanel ExpensePanel = new JPanel();
		ExpensePanel.setBackground(new Color(255, 255, 255));
	
		/*
			//test chart. Actual case will use a getChartPanel() method from the AssetManager object
			DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
			ExpenseDataset.setValue(1000, "Amount", "Food");
			ExpenseDataset.setValue(800, "Amount", "Rent");
			ExpenseDataset.setValue(500, "Amount", "Transport");
			ExpenseDataset.setValue(200, "Amount", "Utilities");
			ExpenseDataset.setValue(200, "Amount", "Gifts");
			
			//creating the JFreeChart
			JFreeChart ExpenseType = ChartFactory.createBarChart(
					"Expense", "Categories", "Amount", ExpenseDataset,
					PlotOrientation.VERTICAL, false, true, false);
	
			//to change the bar colours
			CategoryPlot ExpensePlot = ExpenseType.getCategoryPlot();
			ExpensePlot.setBackgroundPaint(Color.WHITE);							//to set the background colour of the chart as black
			BarRenderer ExpenseRenderer = (BarRenderer) ExpensePlot.getRenderer();
			ExpenseRenderer.setSeriesPaint(0, new Color(255, 205, 50));
			ExpenseRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel ExpenseTypeChart = new ChartPanel(ExpenseType,350,200,200,100,500,200,true,true,true,true,true,true);
			ExpenseTypeChart.setSize(270, 200);
			ExpenseTypeChart.setVisible(true);
			//end of test data.
		*/
		ExpenseCatMgr ExpenseCatMgrTemp = new ExpenseCatMgr();
		ExpensePanel.add(ExpenseCatMgrTemp.readExpenseCatTxt());
		
	mainFrame.getContentPane().add(ExpensePanel, "cell 1 2,grow");
	}

}
