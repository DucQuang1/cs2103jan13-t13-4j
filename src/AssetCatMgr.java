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


public class AssetCatMgr {
	
	private static final String key = "Amount";
	
	public ChartPanel readAssetCatTxt() {
		double amt;
		String category;
		String path = getClass().getResource(".").getPath();	//see if necessary

		//initialize AssetDataSet
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner assetCatReader = new Scanner(new FileReader(path + "/db/AssetCategory.txt"));

			while (assetCatReader.hasNext()) {
				StringTokenizer st = new StringTokenizer(assetCatReader.nextLine(), ",");
				amt = Double.parseDouble(st.nextToken());
				category = st.nextToken();
				AssetDataset.setValue(amt,key,category);
			}
			assetCatReader.close();
		
			//Create JFreeChart with AssetDataSet
			JFreeChart AssetType = ChartFactory.createBarChart(
					"Assets", "Categories", "Amount", AssetDataset,
					PlotOrientation.VERTICAL, false, true, false);
			
			//Change the chart's visual properties
			CategoryPlot AssetPlot = AssetType.getCategoryPlot();
			AssetPlot.setBackgroundPaint(Color.WHITE);							//to set the background color of the chart as white
			BarRenderer AssetRenderer = (BarRenderer) AssetPlot.getRenderer();
			AssetRenderer.setSeriesPaint(0, new Color(50, 170, 20));
			AssetRenderer.setBarPainter(new StandardBarPainter());				//to disable the default 'shiny look'
			
			//creating the chart panel
			ChartPanel AssetTypeChart = new ChartPanel(AssetType,350,200,200,100,800,300,true,true,true,true,true,true);
			AssetTypeChart.setSize(270, 200);
			AssetTypeChart.setVisible(true);
			
			return AssetTypeChart;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
