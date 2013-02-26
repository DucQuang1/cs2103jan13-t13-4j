import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;


public class ExpenseCatMgr {
	
	private static final String key = "Amount";
	
	public ChartPanel readExpenseCatTxt() {
		double amt;
		String category;
		String path = getClass().getResource(".").getPath();	//see if necessary

		//initialize ExpenseDataSet
		DefaultCategoryDataset ExpenseDataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner ExpenseCatReader = new Scanner(new FileReader(path + "/db/ExpenseCategory.txt"));

			while (ExpenseCatReader.hasNext()) {
				StringTokenizer st = new StringTokenizer(ExpenseCatReader.nextLine(), ",");
				amt = Double.parseDouble(st.nextToken());
				category = st.nextToken();
				ExpenseDataset.setValue(amt,key,category);
			}
			ExpenseCatReader.close();
		
			//Create JFreeChart with ExpenseDataSet
			JFreeChart ExpenseType = ChartFactory.createBarChart(
					"Expenses", "Categories", "Amount", ExpenseDataset,
					PlotOrientation.VERTICAL, false, true, false);
			
			//Change the chart's visual properties
			CategoryPlot ExpensePlot = ExpenseType.getCategoryPlot();
			ExpensePlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
			BarRenderer ExpenseRenderer = (BarRenderer) ExpensePlot.getRenderer();
			ExpenseRenderer.setSeriesPaint(0, new Color(255, 205, 50));
			ExpenseRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel ExpenseTypeChart = new ChartPanel(ExpenseType,350,200,200,100,800,300,true,true,true,true,true,true);
			ExpenseTypeChart.setSize(270, 200);
			ExpenseTypeChart.setVisible(true);
			
			return ExpenseTypeChart;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
