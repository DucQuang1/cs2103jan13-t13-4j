package logic;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

/**
 * Class that takes in a JTable, a file path, and writes to it
 * For exporting to excel
 * @author JP
 *
 */
public class ExcelExporter {
	
	public ExcelExporter(){}
	
	public void exportTable(JTable table, WritableSheet sheet){
		try {

			TableModel model = table.getModel();

		  	for(int i=0; i < model.getColumnCount();i++) {
		  		sheet.addCell(new Label(i, 0, model.getColumnName(i)));
		  	}

		  	for(int i=0; i < model.getRowCount();i++){
		  		for(int j=0; j< model.getColumnCount(); j++){
		  		//get [i][j] and write to [i+1][j] as first row is for headers
		  			sheet.addCell(new Label(j, i+1, model.getValueAt(i,j).toString()));	
				}
		  	}

		} catch (WriteException e) {
			e.printStackTrace();
		}
		}
}
