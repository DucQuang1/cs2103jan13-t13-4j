import java.awt.EventQueue;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.*;

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
		mainFrame.setSize(1000, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new MigLayout("", "[400,grow]0[400,grow]0[200,grow]", "[100,grow]0[250,grow]0[250,grow]"));
		
		/**
		 * BalancePanel holds the welcome message and the user's balance
		 */
		JPanel BalancePanel = new JPanel();
		BalancePanel.setBackground(new Color(255, 255, 255));		
		mainFrame.getContentPane().add(BalancePanel, "cell 0 0 3 1,grow");
		
		/**
		 * AssetPanel holds the chart, edit and intra-asset transfer buttons
		 */
		JPanel AssetPanel = new JPanel();
		AssetPanel.setBackground(new Color(255, 255, 255));
		
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
			AssetPlot.setBackgroundPaint(Color.WHITE);							//to set the background colour of the chart as white
			BarRenderer AssetRenderer = (BarRenderer) AssetPlot.getRenderer();
			AssetRenderer.setSeriesPaint(0, new Color(50, 170, 20));
			AssetRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			BarRenderer.setDefaultShadowsVisible(false);						//to turn off the 'shadows'. Static property of BarRenderer, so only needs to done once :)
			
			//creating the chart panel
			ChartPanel AssetTypeChart = new ChartPanel(AssetType,350,150,200,100,500,200,true,true,true,true,true,true);
			AssetTypeChart.setSize(270, 200);
			AssetTypeChart.setVisible(true);
			//end of test data.
			
		AssetPanel.add(AssetTypeChart);
		
		mainFrame.getContentPane().add(AssetPanel, "cell 0 1,grow");
		
		/**
		 * LiabilityPanel holds the chart, edit and intra-liability transfer buttons
		 */
		JPanel LiabilityPanel = new JPanel();
		LiabilityPanel.setBackground(new Color(255, 255, 255));
		
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
			ChartPanel LiabilityTypeChart = new ChartPanel(LiabilityType,350,150,200,100,500,200,true,true,true,true,true,true);
			LiabilityTypeChart.setSize(270, 200);
			LiabilityTypeChart.setVisible(true);
			//end of test data.
		
		LiabilityPanel.add(LiabilityTypeChart);
			
		mainFrame.getContentPane().add(LiabilityPanel, "cell 1 1,grow");
		
		/**
		 * HistoryPanel holds the list of transactions and has a menu bar below for CRUD, undo and search
		 */
		JPanel HistoryPanel = new JPanel();
		HistoryPanel.setBackground(new Color(255, 255, 255));
		mainFrame.getContentPane().add(HistoryPanel, "cell 2 1 1 2,grow");
		
		/**
		 * IncomePanel holds the chart and edit income category button
		 */
		JPanel IncomePanel = new JPanel();
		IncomePanel.setBackground(new Color(255, 255, 255));
		mainFrame.getContentPane().add(IncomePanel, "cell 0 2,grow");
		
		/**
		 * ExpensePanel holds the chart and edit expense category button
		 */
		JPanel ExpensePanel = new JPanel();
		ExpensePanel.setBackground(new Color(255, 255, 255));
		mainFrame.getContentPane().add(ExpensePanel, "cell 1 2,grow");
	}

}
