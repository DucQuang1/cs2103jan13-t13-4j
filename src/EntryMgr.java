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
	private static int current_id;
	
	/**
	 * Default Constructor
	 * Initializes current_id based on latest transaction id in EntryList.txt
	 */
	EntryMgr(){
		current_id = initId();	//initialize current_id at start-up.
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
	public int addEntry(int transactionType, double amount, Date date, String category1, String category2, String description){
		int id = current_id + 1;			//update current_id at each entry
		Entry newEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
		BufferedWriter entryWriter;
		try {
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));
			entryWriter.append(newEntry.toTxt());
			entryWriter.close();
			return ++current_id;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return current_id;
	}
	
	/**
	 * Searches EntryList.txt for and entry with the following id and returns an Entry object
	 * @param id
	 * @return entry
	 */
	public Entry getEntry(int id){
		Entry entry = null;
		try{
			String line = null;
			boolean idFound = false;
			
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    
		    //this block parses id of each entry from EntryList.txt and searches for entry with id
		    while ((line = fileReader.readLine()) != null) {
		    	if(line != ""){
			    	int currentId = Character.getNumericValue(line.charAt(0));
			        if (currentId == id) {
			        	idFound = true;
			        	StringTokenizer st = new StringTokenizer(line, "|");
						st.nextToken();	//skip the first token, which is the id
						int transactionType = Integer.parseInt(st.nextToken());
						double amount = Double.parseDouble(st.nextToken());
						Date date = date_format.parse(st.nextToken());
						String category1 = st.nextToken();
						String category2 = st.nextToken();
						String description = st.nextToken();
						entry = new Entry(id, transactionType, amount, date, category1, category2, description);
			        }
		    	}
		    }
		    fileReader.close();
		    
		    //warning if id not found
		    if(!idFound){
		    	System.out.println("id not found!");
		    }
		    
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return entry;
	}
	
	/**
	 * Edits a given entry in EntryList.txt given an updated entry
	 * @param entry
	 * @return true if edited successfully
	 */
	public boolean editEntry(Entry entry){
		int id = entry.getId();
		boolean edited = false;
		try {
			File inFile = new File(txt_path);
			File tempFile;
			tempFile = File.createTempFile("tempFile", ".txt");
			BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses id of each entry from EntryList.txt and adds to temp file if not id to be edited
		    String line = fileReader.readLine();
		    fileWriter.print(line);
		    while ((line = fileReader.readLine()) != null) {
		    	int currentId = Character.getNumericValue(line.charAt(0));
		        if (currentId != id) {
		        	fileWriter.println();			//newline should be added before the line, like Entry.toTxt()
		        	fileWriter.print(line);
		        	fileWriter.flush();
		        }
		        else{
		        	fileWriter.print(entry.toTxt());	//no println before as entry.toTxt() inserts a newline before
		        	fileWriter.flush();
		        	edited = true;
		        }
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	    return edited;
	}
	
	/**
	 * Deletes an entry with a specific id. Returns true if deleted successfully
	 * @param id
	 */
	public Entry deleteEntry(int id){
		Entry deletedEntry = null;
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
		    	if(line != ""){		    		
			    	int currentId = Character.getNumericValue(line.charAt(0));
			        if (currentId != id) {
			        	fileWriter.println();			//newline should be added before the line, like Entry.toTxt()
			        	fileWriter.print(line);
			        	fileWriter.flush();
			        }
			        else{
			        	//collect info about deleted Entry
			        	StringTokenizer st = new StringTokenizer(line, "|");
			        	st.nextToken();	//skip id
						int transactionType = Integer.parseInt(st.nextToken());
						double amount = Double.parseDouble(st.nextToken());
						Date date = date_format.parse(st.nextToken());
						String category1 = st.nextToken();
						String category2 = st.nextToken();
						String description = st.nextToken();
						deletedEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
			        	idFound = true;
			        }
		    	}
		    }
		    fileReader.close();
		    fileWriter.close();
		    
		    //warning if id not found
		    if(!idFound){
		    	System.out.println("id not found!");
		    	return null;
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
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return deletedEntry;
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
		String category1 = "", category2 = "", description = "";
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
				if (st.hasMoreTokens())				//check, as description is an optional entry
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
	
	/**
	 * Gets the latest id used for a transaction.
	 * @return current_id
	 */
	public int getCurrentId(){
		return current_id;
	}
	
	/**
	 * Gets the current id according to the transactions in the file. Only used for initialization!
	 * After initialization the next id will be tracked by current_id
	 * @return next id to be inserted
	 */
	private int initId(){
		int id = 0;
		try {
			Scanner fileReader = new Scanner(new FileReader(txt_path));

			while (fileReader.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(fileReader.nextLine(), "|");
				if(st.hasMoreTokens()){							//if clause to avoid problems with empty lines in txt file
					id = Integer.parseInt(st.nextToken());					
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
}