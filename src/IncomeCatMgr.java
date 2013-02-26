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


public class IncomeCatMgr {
	
	private static final String key = "Amount";
	
	public ChartPanel readIncomeCatTxt() {
		double amt;
		String category;
		String path = getClass().getResource(".").getPath();	//see if necessary

		//initialize IncomeDataSet
		DefaultCategoryDataset IncomeDataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner IncomeCatReader = new Scanner(new FileReader(path + "/db/IncomeCategory.txt"));

			while (IncomeCatReader.hasNext()) {
				StringTokenizer st = new StringTokenizer(IncomeCatReader.nextLine(), ",");
				amt = Double.parseDouble(st.nextToken());
				category = st.nextToken();
				IncomeDataset.setValue(amt,key,category);
			}
			IncomeCatReader.close();
		
			//Create JFreeChart with IncomeDataSet
			JFreeChart IncomeType = ChartFactory.createBarChart(
					"Income", "Categories", "Amount", IncomeDataset,
					PlotOrientation.VERTICAL, false, true, false);
			
			//Change the chart's visual properties
			CategoryPlot IncomePlot = IncomeType.getCategoryPlot();
			IncomePlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
			BarRenderer IncomeRenderer = (BarRenderer) IncomePlot.getRenderer();
			IncomeRenderer.setSeriesPaint(0, new Color(50, 96, 255));
			IncomeRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel IncomeTypeChart = new ChartPanel(IncomeType,350,200,200,100,800,300,true,true,true,true,true,true);
			IncomeTypeChart.setSize(270, 200);
			IncomeTypeChart.setVisible(true);
			
			return IncomeTypeChart;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
