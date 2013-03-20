package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import logic.ExcelExporter;

import org.junit.Test;

public class ExcelExporterTest {

	@Test
	public void test() {
		String [][] data = {
			    {"Summerall","0785214925"},
			    {"The Secret Message of Jesus","084990000X"},
			    {"Buck Wild","159555064X"},
			    {"25 Ways to Win with People","0785260943"},
			    {"Aesop and the CEO ","0785260102"},
			    {"ALL Business is Show Business ","0785206086"},
			    {"Becoming A Person of Influence","0785271007"},
			    {"Checklist for Life for Leaders","0785260013"},
			    {"Duct Tape Marketing ","078522100X"},
			    {"38 Values to Live By ","0849916631"},
			    {"Blue Moon","0785260641"},
			    {"Blue Like Jazz ","9780785263708"},
			    {"Wild at Heart ","0785262989"},
			    {"Wild Men, Wild Alaska ","078521772X "},
			    {"The Duct Tape Bible, NCV","0718018249"}
			  };
		String [] headers = {"Title","ISBN"};
		DefaultTableModel model = new DefaultTableModel(data,headers);
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(new File("result.xls"));
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		JTable table = new JTable(model);
		ExcelExporter exporter = new ExcelExporter();
		exporter.exportTable(table, sheet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
