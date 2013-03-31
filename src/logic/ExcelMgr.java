package logic;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import data.Entry;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Class that takes in a JTable, a file path, and writes to it
 * For exporting to excel
 * @author JP
 *
 */
public class ExcelMgr {
	
	private static final DecimalFormat amount_format = new DecimalFormat("##.00");
	protected final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	
	public ExcelMgr(){}
	
	public boolean exportTable(JTable table, File excelFile){
		try {

			TableModel model = table.getModel();
			WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);


		  	for(int i=0; i < model.getColumnCount();i++) {
		  		sheet.addCell(new Label(i, 0, model.getColumnName(i)));
		  	}

		  	for(int i=0; i < model.getRowCount();i++){
		  		
		  		sheet.addCell(new Number(0, i+1, Double.parseDouble(model.getValueAt(i, 0).toString())));
		  		sheet.addCell(new Label(1, i+1, model.getValueAt(i,1).toString()));
		  		sheet.addCell(new Number(2, i+1, Double.parseDouble(model.getValueAt(i,2).toString())));	
		  		
		  		for(int j=3; j< model.getColumnCount(); j++){
		  		
		  			sheet.addCell(new Label(j, i+1, model.getValueAt(i,j).toString()));	
				}
		  	}
		  	workbook.write();
		  	workbook.close();

		  	return true;
		  	
		} catch (WriteException | IOException e) {
			e.printStackTrace();
			return false;
		}
		}
	
	public boolean exportEntryList(LinkedList<Entry> entryList, File excelFile){

		try {
			
			WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);

			sheet.addCell(new Label(0,0,"ID"));
			sheet.addCell(new Label(1,0,"Transaction Type"));
			sheet.addCell(new Label(2,0,"Amount"));
			sheet.addCell(new Label(3,0,"Date"));
			sheet.addCell(new Label(4,0,"Category 1"));
			sheet.addCell(new Label(5,0,"Category 2"));
			sheet.addCell(new Label(6,0,"Description"));
			
			int size = entryList.size();
		  	for(int i=0; i < size;i++){
		  		
		  		sheet.addCell(new Number(0, i+1, entryList.get(i).getId()));
		  		
		  		switch(entryList.get(i).getTransactionType()){
		  		case 0:
		  			sheet.addCell(new Label(1, i+1, "Income Received"));
		  			break;
		  		case 1:
		  			sheet.addCell(new Label(1, i+1, "Expense Using Assets"));
		  			break;
		  		case 2:
		  			sheet.addCell(new Label(1, i+1, "Expense Using Liabilities"));
		  			break;
		  		case 3:
		  			sheet.addCell(new Label(1, i+1, "Repay Loan"));
		  			break;
		  		case 4:
		  			sheet.addCell(new Label(1, i+1, "Take Loan"));
		  			break;
		  		case 5:
		  			sheet.addCell(new Label(1, i+1, "Intra-Asset Transfer"));
		  			break;
		  		case 6:
		  			sheet.addCell(new Label(1, i+1, "Intra-Liability Transfer"));
		  			break;
		  		}
		  		
				sheet.addCell(new Number(2, i+1, entryList.get(i).getAmount()));
				sheet.addCell(new Label(3, i+1, date_format.format(entryList.get(i).getDate())));
				sheet.addCell(new Label(4, i+1, entryList.get(i).getCategory1()));
				sheet.addCell(new Label(5, i+1, entryList.get(i).getCategory2()));
				sheet.addCell(new Label(6, i+1, entryList.get(i).getDescription()));
				
		  	}
		  	workbook.write();
		  	workbook.close();

		  	return true;
		  	
		} catch (WriteException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public LinkedList<Entry> importExcel(final String file_path) throws Exception {
        
		int i=1;
		LinkedList<Entry> importList = new LinkedList<Entry>();
		
		try {
            Workbook wb = Workbook.getWorkbook(new File(file_path));    // get the excel work book
            Sheet sheet = wb.getSheet(0);
            int columns = sheet.getColumns();
            boolean hasDescription = (columns == 7 ? true : false);		//check if user input has description column
             
            // travel all cells in the selected sheet. ignore row 0
            for(i=1; i < sheet.getRows(); i++) {
            	
            	Entry entry = null;
            	int id = Integer.parseInt(sheet.getCell(0,i).getContents());
            	String type = sheet.getCell(1,i).getContents();
            	
            	//parse for transaction type
            	int transactionType;
            	if(type.equals("Income Received")){
            		transactionType = 0;
            	} else if(type.equals("Expense Using Assets")){
            		transactionType = 1;
            	} else if(type.equals("Expense Using Liabilities")){
            		transactionType = 2;
            	} else if(type.equals("Repay Loan")){
            		transactionType = 3;
            	} else if(type.equals("Take Loan")){
            		transactionType = 4;
            	} else if(type.equals("Intra-Asset Transfer")){
            		transactionType = 5;
            	} else if(type.equals("Intra-Liability Transfer")){
            		transactionType = 6;
            	} else throw new Exception("Invalid transaction type!");
            	
            	double amount = Double.parseDouble(sheet.getCell(2,i).getContents());
            	Date date = date_format.parse(sheet.getCell(3,i).getContents());
            	String category1 = sheet.getCell(4,i).getContents();
            	String category2 = sheet.getCell(5,i).getContents();
            	String description = "";
            	if(hasDescription)
            		description = sheet.getCell(6,i).getContents();
            	entry = new Entry(id,transactionType,amount,date,category1,category2,description);
            	
            	importList.add(entry);
            	
            }
        } catch (BiffException e) {
            throw new Exception("Error with " + i + "th entry\n" + e.getMessage());
        } catch (NumberFormatException e) {
        	throw new Exception("Did you use numbers in your file when they were required?");
        } catch (IOException e) {
        	throw new Exception("Does your file exist?");
        }
		return importList;
    }
}
