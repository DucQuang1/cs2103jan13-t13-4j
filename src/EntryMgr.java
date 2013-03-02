import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Manages all entries via EntryList.txt
 * @author JP
 *
 */
public class EntryMgr {
	
	//default format for date
	public final static SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yy");
	public final String txt_path = getClass().getResource(".").getPath() + "/db/EntryList.txt";
	private static int next_id;
	
	/**
	 * Default Constructor
	 * Initializes next_id based on latest transaction id in EntryList.txt
	 */
	EntryMgr(){
		next_id = getNextId();	//initialize next_id at start-up.
	}
	
	/**
	 * Adds an entry to EntryList.txt
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 */
	public void addEntry(int transactionType, double amount, Date date,
			String category1, String category2, String description){
		int id = next_id++;			//update next_id at each entry
		Entry newEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
		BufferedWriter entryWriter;
		try {
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));
			entryWriter.append(newEntry.toTxt());
			entryWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void editEntry(){
		
	}
	
	/**
	 * Deletes an entry with a specific id. Returns true if deleted successfully
	 * @param id
	 */
	public boolean deleteEntry(int id){
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
			boolean idFound = false;
			
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses id of each entry from EntryList.txt and adds to temp file if not id to be deleted
		    String line = fileReader.readLine();
		    fileWriter.print(line);
		    while ((line = fileReader.readLine()) != null) {
		    	int currentId = Character.getNumericValue(line.charAt(0));	//empty lines will cause problems here
		        if (currentId != id) {
		        	fileWriter.println();			//newline should be added before the line, like Entry.toTxt()
		        	fileWriter.print(line);
		        	fileWriter.flush();
		        }
		        else idFound = true;
		    }
		    fileReader.close();
		    fileWriter.close();
		    
		    //warning if id not found
		    if(!idFound){
		    	System.out.println("id not found!");
		    	return false;
		    }
		    
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Reads the entries from EntryList.txt and returns a linked list of all entries
	 * @return transactionList
	 */
	public LinkedList<Entry> getTransactionList(){
		LinkedList<Entry> transactionList = new LinkedList<Entry>();
		int id, transactionType;
		double amount;
		Date date = new Date();
		String category1, category2, description;
		Entry tempEntry;
		try {
			Scanner fileReader = new Scanner(new FileReader(txt_path));

			while (fileReader.hasNext()) {
				StringTokenizer st = new StringTokenizer(fileReader.nextLine(), "|");
				id = Integer.parseInt(st.nextToken());
				transactionType = Integer.parseInt(st.nextToken());
				amount = Double.parseDouble(st.nextToken());
				date = date_format.parse(st.nextToken());
				category1 = st.nextToken();
				category2 = st.nextToken();
				description = st.nextToken();
				tempEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
				transactionList.add(tempEntry);
			}
			fileReader.close();
			
		} catch (FileNotFoundException | ParseException e) {
			e.printStackTrace();
		}
		
		return transactionList;
	}
	
	
	
	private int getNextId(){
		int id = 0;
		try {
			Scanner assetCatReader = new Scanner(new FileReader(txt_path));

			while (assetCatReader.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(assetCatReader.nextLine(), "|");
				id = Integer.parseInt(st.nextToken());
			}
			assetCatReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ++id;
	}
}