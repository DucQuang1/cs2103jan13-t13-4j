import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Class that manages all the logs via History.txt
 * @author JP
 *
 */
public class HistoryMgr {

	//default format for date
	public final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yy");
	public final String txt_path = getClass().getResource(".").getPath() + "/db/History.txt";
	
	/**
	 * Default Constructor
	 */
	HistoryMgr(){}
	
	/**
	 * Adds a log entry. No limit at the moment.
	 * @param operationType
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 */
	public void addLog(int operationType, int id, int transactionType, double amount, 
			Date date, String category1, String category2, String description){
		
		Log newLog = new Log(operationType, id, transactionType, amount, date, category1, category2, description);
		
		BufferedWriter logWriter;
		
		try {
			Scanner logReader = new Scanner(new FileReader(txt_path));
			logWriter = new BufferedWriter(new FileWriter(txt_path, true));
			
			//check if first log. do not add a newline for the first log.
			if (!logReader.hasNext()){
				logWriter.append(newLog.toTxt(false));
			}
			else{
				logWriter.append(newLog.toTxt(true));				
			}
			logReader.close();
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * gets that transactionId of the last operation (add/edit/delete)
	 * @return transactionId
	 */
	public int getLastId(){
		int id = 0;
		try {
			Scanner assetCatReader = new Scanner(new FileReader(txt_path));

			//reads the log for last transactionId
			while (assetCatReader.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(assetCatReader.nextLine(), "|");
				if(st.hasMoreTokens()){
					st.nextToken();	//skip the operation type
					id = Integer.parseInt(st.nextToken());
				}
			}
			assetCatReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * deletes the last operation and returns the log
	 * @return Log
	 */
	public Log undoLast(){
		Log lastOperation = null;
		
		try {
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
			BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    String line = new String();
		    String prevLine = null;
			StringTokenizer st = null;
			//while loop to get last line
			prevLine = new String(fileReader.readLine());
			while ((line = fileReader.readLine()) != null) {
		    	if(prevLine != ""){
			        	fileWriter.println();			//newline should be added before the line, like Entry.toTxt()
			        	fileWriter.print(prevLine);
			        	fileWriter.flush();
		        }
		    	prevLine = new String(line);
			}
			st = new StringTokenizer(prevLine, "|");
			if(st.hasMoreTokens()){							//if clause to avoid problems with empty lines in txt file
				int operationType = Integer.parseInt(st.nextToken());
				int id = Integer.parseInt(st.nextToken());
				int transactionType = Integer.parseInt(st.nextToken());
				double amount = Double.parseDouble(st.nextToken());
				Date date = date_format.parse(st.nextToken());
				String category1 = st.nextToken();
				String category2 = st.nextToken();
				String description = "";
				if (st.hasMoreTokens())				//check, as description is an optional entry
					description = st.nextToken();
				lastOperation = new Log(operationType, id, transactionType, amount,
						date, category1, category2, description);
			}
			fileReader.close();
			fileWriter.close();
			
		    //warning if could not delete file
		    try{ 
		    	inFile.delete();
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		    
		    //warning if could not rename file
		    try{
		    	tempFile.renameTo(inFile);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return lastOperation;
	}
	
	/**
	 * Clears the log. Automatically executed when closing the application.
	 */
	public boolean clearLog(){
		try {
			File inFile = new File(txt_path);
		    try{ 
		    	inFile.delete();
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
			File newFile = new File(txt_path);
			newFile.createNewFile();
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}
}
