package view;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
import data.Entry;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * GUI class that displays the main window
 * @author Wong Jing Ping, A0086581W
 *
 */
public class Finances {

	private final TransactionMgr transactionMgr = new TransactionMgr(this);		//TransactionMgr handles all operations from user
	private final Finances finances = this;
	
	private static final DecimalFormat amount_format = new DecimalFormat("##.00");			//default format for printing amount
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");	//default format for printing dates
	
	private final JFrame finances_FRM = new JFrame();							//Main Window of user interface
	private final BackgroundPanel finances_PNL = new BackgroundPanel();			//Container for all display elements
	
	//panels
	private final JPanel financesBalance_PNL = new JPanel();					//financesBalance_PNL displays the welcome msg and user's balance
	private final JPanel financesAsset_PNL = new JPanel();						//financesAsset_PNL displays the chart, edit and transfer buttons
	private final JPanel financesLiability_PNL = new JPanel();					//financesLiability_PNL displays the chart, edit and transfer buttons
	private final JPanel financesIncome_PNL = new JPanel();						//financesIncome_PNL holds the chart and edit income category button
	private final JPanel financesExpense_PNL = new JPanel();					//financesExpense_PNL holds the chart and edit expense category button
	private final JPanel financesRight_PNL = new JPanel();						//financesRight_PNL holds scrolling pane and financesCrud_PNL
	private final JScrollPane financesTransactionList_SCP = new JScrollPane();	//Scrollable pane for viewing history of transactions
	private final JPanel financesTransactionList_PNL = new JPanel();			//financesTransactionList_PNL displays the list of past transactions
	private final JPanel financesCrud_PNL = new JPanel();						//financesCrud_PNL stores the buttons for CRUD operations, undo and search
	
	//labels
	private final JLabel financesWelcome_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Header.png")));
	private final JLabel financesBalance_LBL = new JLabel();					//Needs to be updated
	private final JLabel financesTransactions_LBL = new JLabel("Transactions", SwingConstants.CENTER);
	
	//menu bar stuff
	private final JMenuBar finances_MNB = new JMenuBar();
	private final JMenu financesFile_MN = new JMenu("File");
	private final JMenuItem financesImport_MNI = new JMenuItem("Import", new ImageIcon(Finances.class.getResource("/img/Import.png")));
	private final JMenuItem financesExport_MNI = new JMenuItem("Export", new ImageIcon(Finances.class.getResource("/img/Export.png")));
	private final JMenuItem financesQuit_MNI = new JMenuItem("Quit", new ImageIcon(Finances.class.getResource("/img/Quit.png")));
	
	//buttons for editing chart and categories
	private final JButton financesRenameAssetCat_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tag.png")));
	private final JButton financesAssetTransfer_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Transfer.png")));
	private final JButton financesDeleteAsset_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
	private final JButton financesRenameLiabilityCat_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tag.png")));
	private final JButton financesLiabilityTransfer_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Transfer.png")));
	private final JButton financesDeleteLiability_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Trash.png")));
	private final JButton financesRenameIncomeCat_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tag.png")));
	private final JButton financesRenameExpenseCat_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tag.png")));
	
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
					window.finances_FRM.setVisible(true);
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
		ImageIcon bg = new ImageIcon(Finances.class.getResource("/img/Wallpaper.jpg"));
		
		finances_FRM.getContentPane().add(finances_PNL);
		finances_FRM.setSize(1200, 800);
		finances_FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finances_PNL.setImage(bg.getImage());
		finances_PNL.setLayout(new MigLayout("", "[400]0[400]0[250:300:350]", "0[150,grow]0[300,grow]0[300,grow]0"));
		finances_PNL.setOpaque(false);
		
		//setting up the top panel
		financesBalance_PNL.setOpaque(false);		
		financesBalance_PNL.setLayout(new MigLayout("", "[grow][grow]", "0[70]10[60]0"));
		financesBalance_PNL.add(financesWelcome_LBL, "cell 0 0 1 2, align center");
		financesBalance_LBL.setText("Your Balance:\t$" + amount_format.format(transactionMgr.getBalance()));
		financesBalance_LBL.setFont(new Font("Tahoma", Font.BOLD, 22));
		if(transactionMgr.getBalance() < 0)
			financesBalance_LBL.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
		financesBalance_PNL.add(financesBalance_LBL, "cell 1 1, align center");
		finances_PNL.add(financesBalance_PNL, "cell 0 0 3 1,grow");
		
		//setting up the menu bar
		financesFile_MN.setIcon(new ImageIcon(Finances.class.getResource("/img/File.png")));
		financesFile_MN.setOpaque(false);
		finances_MNB.setOpaque(false);
		finances_MNB.add(financesFile_MN);
		financesImport_MNI.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
					transactionMgr.importTransactionList();
			}
		});
		financesImport_MNI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		financesFile_MN.add(financesImport_MNI);
		financesExport_MNI.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
					transactionMgr.exportTransactionList();
			}
		});
		financesExport_MNI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		financesFile_MN.add(financesExport_MNI);
		financesQuit_MNI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		financesQuit_MNI.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);									//quit after clicking
			}
			
		});
		financesFile_MN.add(financesQuit_MNI);
		financesBalance_PNL.add(finances_MNB, "cell 1 0, align right");
		
		//setting up the asset panel
		financesAsset_PNL.setOpaque(false);
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();
		AssetDataset = TransactionMgr.getAssetChartData();
		financesAsset_PNL.add(renderBarChart(AssetDataset, 0));
		financesRenameAssetCat_BTN.setToolTipText("Rename Asset Categories");
		financesRenameAssetCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.renameAssetCategories();
			}
		});
		financesAsset_PNL.add(financesRenameAssetCat_BTN);
		financesAssetTransfer_BTN.setToolTipText("Intra-Asset Transfer");
		financesAssetTransfer_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.transferIntraAsset();
			}
		});
		financesAsset_PNL.add(financesAssetTransfer_BTN);
		financesDeleteAsset_BTN.setToolTipText("Delete Asset Category");
		financesDeleteAsset_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.deleteAssetCategory();
			}
		});
		financesAsset_PNL.add(financesDeleteAsset_BTN);
		finances_PNL.add(financesAsset_PNL, "cell 0 1,grow");

		//setting up the liability panel
		financesLiability_PNL.setOpaque(false);
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityChartData();
		financesLiability_PNL.add(renderBarChart(LiabilityDataset, 1));
		financesRenameLiabilityCat_BTN.setToolTipText("Rename Liability Categories");
		financesRenameLiabilityCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				transactionMgr.renameLiabilityCategories();
			}
		});
		financesLiability_PNL.add(financesRenameLiabilityCat_BTN);
		financesLiabilityTransfer_BTN.setToolTipText("Intra-Liability Transfer");
		financesLiabilityTransfer_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.transferIntraLiability();
			}
		});
		financesLiability_PNL.add(financesLiabilityTransfer_BTN);
		financesDeleteLiability_BTN.setToolTipText("Delete Liability Category");
		financesDeleteLiability_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.deleteLiabilityCategory();
			}
		});
		financesLiability_PNL.add(financesDeleteLiability_BTN);
		finances_PNL.add(financesLiability_PNL, "cell 1 1,grow");
		
		//setting up the right panel
		financesRight_PNL.setOpaque(false);
		financesRight_PNL.setLayout(new MigLayout("", "0[300,grow]0", "0[40]0[400]0[50]0"));
		financesTransactions_LBL.setFont(new Font("Tahoma", Font.BOLD, 22));
		financesRight_PNL.add(financesTransactions_LBL, "cell 0 0,growx,aligny top");
		finances_PNL.add(financesRight_PNL, "cell 2 1 1 2,grow");

		//setting up the transaction list panel and adding it to the right panel
		renderList(financesTransactionList_PNL, TransactionMgr.getTransactionList());
		financesTransactionList_PNL.setOpaque(false);
		financesTransactionList_SCP.setViewportView(financesTransactionList_PNL);
		financesTransactionList_SCP.setOpaque(false);
		financesTransactionList_SCP.getViewport().setOpaque(false);
		financesTransactionList_PNL.setLayout(new MigLayout("flowy", "5[grow,left]5", "5[grow,top]5"));
		financesRight_PNL.add(financesTransactionList_SCP, "cell 0 1,grow");
		financesRight_PNL.validate();
		
		//setting up the crud buttons panel and adding it to the right panel
		financesCrud_PNL.setOpaque(false);
		financesCrud_PNL.setLayout(new MigLayout("", "0[50]3[50]3[50]3[50]3[50]0", "0[50]0"));
		financesRight_PNL.add(financesCrud_PNL, "flowx,cell 0 2");
		
		//Add the respective buttons with their icon and a simple tool tip
		financesAdd_BTN.setToolTipText("Add an Entry");
		financesAdd_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.addTransaction();
			}
		});
		financesCrud_PNL.add(financesAdd_BTN, "cell 0 0");
		
		financesEdit_BTN.setToolTipText("Edit an Entry");
		financesEdit_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.editTransaction();
			}
		});
		financesCrud_PNL.add(financesEdit_BTN, "cell 1 0");
		
		financesDel_BTN.setToolTipText("Delete an Entry");
		financesDel_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.deleteTransaction();
			}
		});
		financesCrud_PNL.add(financesDel_BTN, "cell 2 0");		
		
		financesUndo_BTN.setToolTipText("Undo your last transaction");
		financesUndo_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.undo();
				refresh();
			}
		});
		financesCrud_PNL.add(financesUndo_BTN, "cell 3 0");
		
		financesSearch_BTN.setToolTipText("Search");
		financesSearch_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				@SuppressWarnings("unused")
				SearchMgr searchMgr = new SearchMgr(finances);
			}
		});
		financesCrud_PNL.add(financesSearch_BTN, "cell 4 0");
		
		//setting up the income panel
		financesIncome_PNL.setOpaque(false);
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();
		IncomeDataset = TransactionMgr.getIncomeChartData();
		financesIncome_PNL.add(renderBarChart(IncomeDataset,2));
		financesRenameIncomeCat_BTN.setToolTipText("Rename Income Categories");
		financesRenameIncomeCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.renameIncomeCategories();
			}
		});
		financesIncome_PNL.add(financesRenameIncomeCat_BTN);
		finances_PNL.add(financesIncome_PNL, "cell 0 2,grow");

		//setting up the expense panel
		financesExpense_PNL.setOpaque(false);
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();
		ExpenseDataset = TransactionMgr.getExpenseChartData();
		financesExpense_PNL.add(renderBarChart(ExpenseDataset, 3));
		financesRenameExpenseCat_BTN.setToolTipText("Rename Expense Categories");
		financesRenameExpenseCat_BTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				transactionMgr.renameExpenseCategories();
			}
		});
		financesExpense_PNL.add(financesRenameExpenseCat_BTN);
		finances_PNL.add(financesExpense_PNL, "cell 1 2,grow");
		
		//to refresh when the user presses 'R'
		finances_PNL.setFocusable(true);
		finances_PNL.requestFocusInWindow();
		finances_PNL.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
            	
                switch(e.getKeyCode()){
                
                	case KeyEvent.VK_R:	refresh();
            			break;
                	case KeyEvent.VK_A: transactionMgr.addTransaction();
                		break;
                	case KeyEvent.VK_E: transactionMgr.editTransaction();
                		break;
                	case KeyEvent.VK_D: transactionMgr.deleteTransaction();
                		break;
                	case KeyEvent.VK_U: transactionMgr.undo();
                		break;
                	case KeyEvent.VK_S: SearchMgr searchMgr = new SearchMgr(finances);
                		break;
                }
            }
        });
		
		//to clear log when exiting the application
		finances_FRM.addWindowListener(new WindowAdapter(){
			
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
		financesBalance_LBL.setText("You have $" + amount_format.format(balance));
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
		financesAsset_PNL.add(financesDeleteAsset_BTN);
		
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();
		LiabilityDataset = TransactionMgr.getLiabilityChartData();
		financesLiability_PNL.removeAll();
		financesLiability_PNL.add(renderBarChart(LiabilityDataset, 1));
		financesLiability_PNL.add(financesRenameLiabilityCat_BTN);
		financesLiability_PNL.add(financesLiabilityTransfer_BTN);
		financesLiability_PNL.add(financesDeleteLiability_BTN);
		
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
		
		finances_FRM.validate();
	}
	
	/**
	 * Disables main window
	 */
	public void disableFrame(){
		
		finances_FRM.setEnabled(false);
	}

	/**
	 * Reactivates main window
	 */
	public void reactivateFrame(){
		
		finances_FRM.setEnabled(true);
		finances_MNB.requestFocusInWindow();
		finances_PNL.requestFocusInWindow();
	}

	/**
	 * This method takes in a scrollable pane, and populates it with entries from a linked list
	 * Each entry fills up a JTextArea
	 * @param financesTransactionList_SCP
	 * @param transactionList
	 */
	private void renderList(JPanel ListPane, LinkedList<Entry> transactionList){
		
		int size = transactionList.size();
		Date currentDate = new Date();			//current date, for checking
		
		for(int i = 0; i < size; ++i){
			JPanel tempPanel = new JPanel(new MigLayout("flowy","5[280]5","[]"));
			tempPanel.setOpaque(false);
			JEditorPane entry = new JEditorPane();
			entry.setEditable(false);
			entry.setContentType("text/html");
			String entryText = new String("<html><body>");
			Entry tempEntry = transactionList.get(i);
			
			//display ID in left column
			entryText += "<table style=\"font-family: Verdana; font-size: 12px;\"><tr><td align=left>";
			entryText += "<b>ID " + Integer.toString(tempEntry.getId()) + "</b></td>";

			//display date in right column
			if(date_format.format(tempEntry.getDate()).equals(date_format.format(currentDate))){
				entryText += "<td> Today </td>";
			} else if(date_format.format(tempEntry.getDate()).equals(date_format.format(new Date(currentDate.getTime() - 1000*60*60*24)))){
				entryText += "<td> Yesterday </td>";
			} else {
				entryText += "<td> " + date_format.format(tempEntry.getDate()) + " </td></tr>";
			}

			entryText += "<tr><td width=120px>";
			switch(tempEntry.getTransactionType())
			{
				case 0:	entryText += "Income";
						break;
				case 1:
				case 2: entryText += "Expense";
						break;
				case 3: entryText += "Repay Loan";
						break;
				case 4:	entryText += "Take Loan";
						break;
				case 5: entryText += "Asset Transfer";
						break;
				case 6:	entryText += "Liability Transfer";
						break;
				default:entryText += "Unspecified Type!";
						break;
			}
			entryText += "</td><td> " + amount_format.format(tempEntry.getAmount()) + "</td></tr>";
			entryText += "<tr><td> From: </td><td>" + tempEntry.getCategory1() + "</td></tr>";
			entryText += "<tr><td> To:   </td><td>" + tempEntry.getCategory2() + "</td></tr>";
			entryText += "<tr><td colspan=2><i>" + tempEntry.getDescription() + "</i></td></tr>";
			entryText += "</table><br><font face=\"Verdana\" size=12px> </font></body></html>";
			entry.setText(entryText);
			
			//set color of the editor pane
			switch(tempEntry.getTransactionType()){
			case 0:	entry.setBackground(new Color(160,190,220,30));
					break;
			case 1:	entry.setBackground(new Color(255,200,0,30));
					break;
			case 2:	entry.setBackground(new Color(255,200,0,30));
					break;
			case 3:	entry.setBackground(new Color(255,185,215,30));
					break;
			case 4:	entry.setBackground(new Color(255,185,215,30));
					break;
			case 5:	entry.setBackground(new Color(60,160,0,30));
					break;
			case 6:	entry.setBackground(new Color(180,50,10,30));
					break;
			}
			tempPanel.add(entry,"growx");
			ListPane.add(tempPanel, "alignx left, gapx 2px 5px, gapy 2px 2px, top");
		}
		ListPane.validate();
	}

	/**
	 * This method renders the chart given a data set on the respective panel
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
		newChart.setBackgroundPaint(new Color(255,255,255,0));		//to make the chart area transparent
		CategoryPlot chartPlot = newChart.getCategoryPlot();
		chartPlot.setBackgroundPaint(new Color(255,255,255,0));		//to make the plot area transparent
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
		newChartPanel.setOpaque(false);			//to make the chart panel transparent

		return newChartPanel;
	}
	
}
