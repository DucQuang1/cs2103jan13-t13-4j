package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Class that takes in a JTable, a file path, and writes to it
 * For exporting to excel
 * @author JP
 *
 */
public class ExcelExporter {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	private String date = date_format.format(new Date());
	
	public ExcelExporter(){}
	
	public void exportTable(JTable table, File file){
		try {

			TableModel model = table.getModel();
			FileWriter out = new FileWriter(file);

		  	for(int i=0; i < model.getColumnCount();i++) {
		  		out.write(model.getColumnName(i)+"\t");
		  	}
		  	out.write("\n");

		  	for(int i=0; i < model.getRowCount();i++){
		  		for(int j=0; j< model.getColumnCount(); j++){
	  				out.write(model.getValueAt(i,j).toString() + "\t");
				}
		  		out.write("\n");
		  	}
		  	out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		}
}
