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


public class LiabilityCatMgr {
	
	private static final String key = "Amount";
	
	public ChartPanel readLiabilityCatTxt() {
		double amt;
		String category;
		String path = getClass().getResource(".").getPath();	//see if necessary

		//initialize LiabilityDataSet
		DefaultCategoryDataset LiabilityDataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner LiabilityCatReader = new Scanner(new FileReader(path + "/db/LiabilityCategory.txt"));

			while (LiabilityCatReader.hasNext()) {
				StringTokenizer st = new StringTokenizer(LiabilityCatReader.nextLine(), ",");
				amt = Double.parseDouble(st.nextToken());
				category = st.nextToken();
				LiabilityDataset.setValue(amt,key,category);
			}
			LiabilityCatReader.close();
		
			//Create JFreeChart with LiabilityDataSet
			JFreeChart LiabilityType = ChartFactory.createBarChart(
					"Liabilities", "Categories", "Amount", LiabilityDataset,
					PlotOrientation.VERTICAL, false, true, false);
			
			//Change the chart's visual properties
			CategoryPlot LiabilityPlot = LiabilityType.getCategoryPlot();
			LiabilityPlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
			BarRenderer LiabilityRenderer = (BarRenderer) LiabilityPlot.getRenderer();
			LiabilityRenderer.setSeriesPaint(0, new Color(200, 30, 20));
			LiabilityRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel LiabilityTypeChart = new ChartPanel(LiabilityType,350,200,200,100,800,300,true,true,true,true,true,true);
			LiabilityTypeChart.setSize(270, 200);
			LiabilityTypeChart.setVisible(true);
			
			return LiabilityTypeChart;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
