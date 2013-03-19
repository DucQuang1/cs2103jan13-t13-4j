package view;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
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
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;

import data.Entry;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * GUI class that displays the main window
 * @author JP
 *
 */
public class Finances {

	private final TransactionMgr transactionMgr = new TransactionMgr(this);		//TransactionMgr handles all operations from user
	
	private final JFrame financesMain_FRM = new JFrame();						//Main Window of user interface
	private final JPanel financesMain_PNL = new JPanel();						//Container for all display elements
	private final JScrollPane financesMain_SCP = new JScrollPane(financesMain_PNL);	//Scroll pane class to enable scrolling
	
	private final JPanel financesBalance_PNL = new JPanel();					//financesBalance_PNL displays the welcome msg and user's balance
	private final JPanel financesAsset_PNL = new JPanel();						//financesAsset_PNL displays the chart, edit and transfer buttons
	private final JPanel financesLiability_PNL = new JPanel();					//financesLiability_PNL displays the chart, edit and transfer buttons
	private final JPanel financesIncome_PNL = new JPanel();						//financesIncome_PNL holds the chart and edit income category button
	private final JPanel financesExpense_PNL = new JPanel();					//financesExpense_PNL holds the chart and edit expense category button
	private final JPanel financesRight_PNL = new JPanel();						//financesRight_PNL holds scrolling pane and financesCrud_PNL
	private final JScrollPane financesTransactionList_SCP = new JScrollPane();	//Scrollable pane for viewing history of transactions
	private final JPanel financesTransactionList_PNL = new JPanel();			//financesTransactionList_PNL displays the list of past transactions
	private final JPanel financesCrud_PNL = new JPanel();						//financesCrud_PNL stores the buttons for CRUD operations, undo and search 
	private final JPanel financesLineChart_PNL = new JPanel();					//financesLineChart_PNL displays the line chart of income/expense over time
	
	//labels
	private final JLabel financesWelcome_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Header.png")));
	private final JLabel financesBalance_LBL = new JLabel("You have $" + transactionMgr.getBalance());	//Needs to be updated
	private final JLabel financesTransactions_LBL = new JLabel("Transactions", SwingConstants.CENTER);
	
	//buttons for editing chart and categories
	private final JButton financesRenameAssetCat_BTN = new JButton("Rename Asset Categories");
	private final JButton financesAssetTransfer_BTN = new JButton("Transfer");
	private final JButton financesRenameLiabilityCat_BTN = new JButton("Rename Liability Categories");
	private final JButton financesLiabilityTransfer_BTN = new JButton("Transfer");
	private final JButton financesRenameIncomeCat_BTN = new JButton("Rename Income Categories");
	private final JButton financesRenameExpenseCat_BTN = new JButton("Rename Expense Categories");
	
	//crud, undo, search buttons
	private final JButton financesAdd_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Add.png")));
	private final JButton financesEdit_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Edit.png")));
	private final JButton financesDel_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Del.png")));
	private final JButton financesUndo_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Undo.png")));
	private final JButton financesSearch_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Search.png")));
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try {
					Finances window = new Finances();
					window.financesMain_FRM.setVisible(true);
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
		
		//setting up the main window
		financesMain_FRM.getContentPane().setBackground(new Color(255, 255, 255));
		financesMain_FRM.setSize(1200, 750);
		financesMain_FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		financesMain_FRM.add(financesMain_SCP);
		financesMain_PNL.setLayout(new MigLayout("", "[400]0[400]0[250:300:350]", "0[100,grow]0[300,grow]0[300,grow]0"));
		financesMain_PNL.setBackground(new Color(255, 255, 255));
		
		//setting up the top panel
		financesBalance_PNL.setBackground(new Color(255, 255, 255));		
		financesBalance_PNL.setLayout(new MigLayout("", "[1100]", "[50]5[30]"));
		financesBalance_PNL.add(financesWelcome_LBL, "cell 0 0, align center");
		if(transactionMgr.getBalance() < 0)
			financesBalance_LBL.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
		financesBalance_PNL.add(financesBalance_LBL, "cell 0 1, align center");
		financesMain_PNL.add(financesBalance_PNL, "cell 0 0 3 1,grow");
		
		//setting up the asset panel
		financesAsset_PNL.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
		AssetDataset = TransactionMgr.getAssetChartData();
		financesAsset_PNL.add(renderBarChart(AssetDataset, 0));
		financesRenameAssetCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				transactionMgr.renameAssetCategories();
			}
		});
		financesAsset_PNL.add(financesRenameAssetCat_BTN);
		financesAssetTransfer_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				transactionMgr.transferIntraAsset();
			}
		});
		financesAsset_PNL.add(financesAssetTransfer_BTN);
		financesMain_PNL.add(financesAsset_PNL, "cell 0 1,grow");

		//setting up the liability panel
		financesLiability_PNL.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityChartData();
		financesLiability_PNL.add(renderBarChart(LiabilityDataset, 1));
		financesRenameLiabilityCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				transactionMgr.renameLiabilityCategories();
			}
		});
		financesLiability_PNL.add(financesRenameLiabilityCat_BTN);
		financesLiabilityTransfer_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.transferIntraLiability();
			}
		});
		financesLiability_PNL.add(financesLiabilityTransfer_BTN);
		financesMain_PNL.add(financesLiability_PNL, "cell 1 1,grow");
		
		//setting up the right panel
		financesRight_PNL.setBackground(new Color(255, 255, 255));
		financesRight_PNL.setLayout(new MigLayout("", "0[300,grow]0", "0[40]0[400]0[50]0"));
		financesTransactions_LBL.setFont(new Font("Tahoma", Font.BOLD, 22));
		financesRight_PNL.add(financesTransactions_LBL, "cell 0 0,growx,aligny top");
		financesMain_PNL.add(financesRight_PNL, "cell 2 1 1 2,grow");

		//setting up the transaction list panel and adding it to the right panel
		financesTransactionList_PNL.setBackground(new Color(240, 240, 230));		
		renderList(financesTransactionList_PNL, TransactionMgr.getTransactionList());
		financesTransactionList_SCP.setViewportView(financesTransactionList_PNL);
		financesTransactionList_PNL.setLayout(new MigLayout("flowy", "5[grow,left]5", "5[grow,top]5"));
		financesRight_PNL.add(financesTransactionList_SCP, "cell 0 1,grow");
		financesRight_PNL.validate();
		
		//setting up the crud buttons panel and adding it to the right panel
		financesCrud_PNL.setBackground(new Color(255, 255, 255));
		financesCrud_PNL.setLayout(new MigLayout("", "0[50]3[50]3[50]3[50]3[50]0", "0[50]0"));
		financesRight_PNL.add(financesCrud_PNL, "flowx,cell 0 2");
		
		//Add the respective buttons with their icon and a simple tooltip
		financesAdd_BTN.setToolTipText("Add an Entry");
		financesAdd_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.addTransaction();
			}
		});
		financesCrud_PNL.add(financesAdd_BTN, "cell 0 0");
		
		financesEdit_BTN.setBackground(new Color(255, 255, 255));
		financesEdit_BTN.setToolTipText("Edit an Entry");
		financesEdit_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.editTransaction();
			}
		});
		financesCrud_PNL.add(financesEdit_BTN, "cell 1 0");
		
		financesDel_BTN.setBackground(new Color(255, 255, 255));
		financesDel_BTN.setToolTipText("Delete an Entry");
		financesDel_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.deleteTransaction();
			}
		});
		financesCrud_PNL.add(financesDel_BTN, "cell 2 0");		
		
		financesUndo_BTN.setBackground(new Color(255, 255, 255));
		financesUndo_BTN.setToolTipText("Undo your last transaction");
		financesUndo_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.undo();
				refresh();
			}
		});
		financesCrud_PNL.add(financesUndo_BTN, "cell 3 0");
		
		financesSearch_BTN.setBackground(new Color(255, 255, 255));
		financesSearch_BTN.setToolTipText("Search");
		financesSearch_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SearchMgr searchMgr = new SearchMgr();
			}
		});
		financesCrud_PNL.add(financesSearch_BTN, "cell 4 0");
		
		//setting up the income panel
		financesIncome_PNL.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.getIncomeChartData();
		financesIncome_PNL.add(renderBarChart(IncomeDataset,2));
		financesRenameIncomeCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.renameIncomeCategories();
			}
		});
		financesIncome_PNL.add(financesRenameIncomeCat_BTN);
		financesMain_PNL.add(financesIncome_PNL, "cell 0 2,grow");

		//setting up the expense panel
		financesExpense_PNL.setBackground(new Color(255, 255, 255));
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.getExpenseChartData();
		financesExpense_PNL.add(renderBarChart(ExpenseDataset, 3));
		financesRenameExpenseCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.renameExpenseCategories();
			}
		});
		financesExpense_PNL.add(financesRenameExpenseCat_BTN);
		financesMain_PNL.add(financesExpense_PNL, "cell 1 2,grow");
		
		/*
		 * setting up the line chart panel
		financesLineChart_PNL.setBackground(new Color(255, 255, 255));
		financesLineChart_PNL.add(renderLineChart(TransactionMgr.getLineChartData()));
		financesMain_PNL.add(financesLineChart_PNL, "cell 0 3 3 1,center");
		 */
		
		//to clear log when exiting the application
		financesMain_FRM.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
                TransactionMgr.clearLog();
            }
		});

	}
	
	/**
	 * refreshes the 4 charts and the transactionList
	 */
	public void refresh(){
		
		//refresh total balance
		double balance = transactionMgr.getBalance();
		financesBalance_LBL.setText("You have $" + balance);
		if(balance < 0)
			financesBalance_LBL.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
		else
			financesBalance_LBL.setIcon(null);
		
		//refresh 4 types
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
		AssetDataset = TransactionMgr.getAssetChartData();
		financesAsset_PNL.removeAll();
		financesAsset_PNL.add(renderBarChart(AssetDataset, 0));
		financesAsset_PNL.add(financesRenameAssetCat_BTN);
		financesAsset_PNL.add(financesAssetTransfer_BTN);
		
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityChartData();
		financesLiability_PNL.removeAll();
		financesLiability_PNL.add(renderBarChart(LiabilityDataset, 1));
		financesLiability_PNL.add(financesRenameLiabilityCat_BTN);
		financesLiability_PNL.add(financesLiabilityTransfer_BTN);
		
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.getIncomeChartData();
		financesIncome_PNL.removeAll();
		financesIncome_PNL.add(renderBarChart(IncomeDataset, 2));
		financesIncome_PNL.add(financesRenameIncomeCat_BTN);
		
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.getExpenseChartData();
		financesExpense_PNL.removeAll();
		financesExpense_PNL.add(renderBarChart(ExpenseDataset, 3));
		financesExpense_PNL.add(financesRenameExpenseCat_BTN);
		
		//refresh transactionList
		financesTransactionList_PNL.removeAll();
		renderList(financesTransactionList_PNL, TransactionMgr.getTransactionList());
		
		//refresh line chart
		financesLineChart_PNL.add(renderLineChart(TransactionMgr.getLineChartData()));

		financesMain_FRM.validate();
	}

	/**
	 * This method takes in a scrollable pane, and populates it with entries from a linked list
	 * Each entry fills up a JTextArea
	 * @param financesTransactionList_SCP
	 * @param transactionList
	 */
	private void renderList(JPanel ListPane, LinkedList<Entry> transactionList){
		int size = transactionList.size();
		for(int i = 0; i < size; ++i){
			JPanel tempPanel = new JPanel(new MigLayout("flowy","5[280]5","[]"));
			tempPanel.setBackground(new Color(255, 255, 255));
			JTextArea entry = new JTextArea();
			String entryText = new String();
			Entry tempEntry = transactionList.get(i);
			entryText += "ID:\t" + Integer.toString(tempEntry.getId()) + "\n";
			switch(tempEntry.getTransactionType())
			{
				case 0:	entryText += "Income\t";
						break;
				case 1:
				case 2: entryText += "Expense\t";
						break;
				case 3: entryText += "Repay Loan\t";
						break;
				case 4:	entryText += "Take Loan\t";
						break;
				case 5: entryText += "Asset Transfer\t";
						break;
				case 6:	entryText += "Liability Transfer\t";
						break;
				default:entryText += "Unspecified Type!";
						break;
			}
			entryText += Double.toString(tempEntry.getAmount()) + "\n";
			entryText += "From:\t" + tempEntry.getCategory1() + "\n";
			entryText += "To:\t" + tempEntry.getCategory2() + "\n";
			entry.setText(entryText);			
			tempPanel.add(entry);
			JLabel financesDescription_LBL = new JLabel("<html>" + tempEntry.getDescription() + "</html>");
			financesDescription_LBL.setFont(new Font("SanSerif",Font.ITALIC,12));
			tempPanel.add(financesDescription_LBL);
			
			ListPane.add(tempPanel, "alignx left, gapx 2px 5px, gapy 2px 2px, top");
			ListPane.validate();
		}
	}

	/**
	 * This method renders the chart given a dataset on the respective panel
	 * @param dataset
	 * @param type (0: Assets, 1: Liabilities, 2: Income, 3: Expense)
	 */
	private ChartPanel renderBarChart(DefaultCategoryDataset dataset, int type){

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
		ChartPanel newChartPanel = new ChartPanel(newChart,
				350,200,200,100,800,300,true,true,true,true,true,true);
		newChartPanel.setSize(270, 200);
		
		return newChartPanel;
	}
	
	/**
	 * This method renders a line chart of income and expense entries over 1-month intervals
	 * @param dataset
	 * @return financesLineChart_PNL
	 */
	private ChartPanel renderLineChart(XYDataset dataset){
		
		//Create JFreeChart with dataSet
		JFreeChart lineChart = ChartFactory.createTimeSeriesChart(
				"", "", "Amount", dataset, true, true, false);
		
		
		//Change the x-axis (time interval) format
		XYPlot plot = (XYPlot) lineChart.getPlot();
		DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

		//Change the chart's visual properties
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setDrawSeriesLineAsPath(true);
		}
		
		
		//Create and format the chart panel
		ChartPanel financesLineChart_PNL = new ChartPanel(lineChart,
				600,250,200,100,800,300,true,true,true,true,true,true);
		financesLineChart_PNL.setSize(800,300);
		
		return financesLineChart_PNL;
	}
}
