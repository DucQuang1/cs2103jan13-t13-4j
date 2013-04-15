package logic;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import data.Entry;

/**
 * Manages all entries via EntryList.txt
 * @author Wong Jing Ping, A0086581W
 *
 */
public class EntryMgr {
	
	//default format for date
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	private final String txt_path = "EntryList.txt";
	private static int current_id;
	
	/**
	 * Default Constructor
	 * Initializes current_id based on latest transaction id in EntryList.txt
	 */
	public EntryMgr(){
		
		current_id = initId();	//initialize current_id at start-up.
		
		//checks if the file exists, and creates the txt file if the file doesn't exist
		try {
			FileReader fileCheck = new FileReader(txt_path);
			fileCheck.close();
		} catch (FileNotFoundException e) {
			System.out.println(txt_path + " does not exist!");
			File newFile = new File(txt_path);
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				System.out.println("unable to create new file! at" +
						newFile.getAbsolutePath());
			}
		} catch (IOException e) {
			System.out.println("What's the problem here?");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an entry to EntryList.txt
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 * @return current_id
	 */
	public int addEntry(int transactionType, double amount, Date date, 
			String category1, String category2, String description){
		
		int id = current_id + 1;			//update current_id at each entry
		Entry newEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
		
		BufferedWriter entryWriter;
		
		try {
			Scanner entryReader = new Scanner(new FileReader(txt_path));
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));
			
			if (!entryReader.hasNext()){
				entryWriter.append(newEntry.toTxt(false));
			}
			else{
				entryWriter.append(newEntry.toTxt(true));
			}
			entryReader.close();
			entryWriter.close();
			return ++current_id;	//return 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return current_id;
	}
	
	/**
	 * Adds back an entry with a previous id into EntryList.txt
	 * For undoing a delete operation
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 * @return current_id
	 */
	public int addBackEntry(int id, int transactionType, double amount, Date date, 
			String category1, String category2, String description){
		
		Entry newEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
		
		BufferedWriter entryWriter;
		
		try {
			Scanner entryReader = new Scanner(new FileReader(txt_path));
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));
			
			if (!entryReader.hasNext()){
				entryWriter.append(newEntry.toTxt(false));
			}
			else{
				entryWriter.append(newEntry.toTxt(true));
			}
			entryReader.close();
			entryWriter.close();
			return ++current_id;	//return 
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
		    		StringTokenizer st = new StringTokenizer(line, "|");
			    	int currentId = Integer.parseInt(st.nextToken());
			        if (currentId == id) {
			        	idFound = true;
						int transactionType = Integer.parseInt(st.nextToken());
						double amount = Double.parseDouble(st.nextToken());
						Date date = date_format.parse(st.nextToken());
						String category1 = st.nextToken();
						String category2 = st.nextToken();
						String description = "";
						if (st.hasMoreTokens())				//check, as description is an optional entry
							description = st.nextToken();
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
		    
		    //this block parses id of first entry from EntryList.txt and adds to temp file if not id to be edited
		    //no need to check if first line exists as this method can only be invoked if there are entries in the txt
		    String line = fileReader.readLine();
		    StringTokenizer st = new StringTokenizer(line, "|");
		    if (Integer.parseInt(st.nextToken()) != id){
		    	fileWriter.print(line);		
		    	fileWriter.flush();
		    }
		    else{
		    	fileWriter.print(entry.toTxt(false));
		    	fileWriter.flush();
		    	edited = true;
		    }
		    
		    //this block parses id of each entry from EntryList.txt and adds to temp file if not id to be edited
		    while ((line = fileReader.readLine()) != null) {
		    	
		    	if(line != ""){
			    	st = new StringTokenizer(line, "|");
			        if (Integer.parseInt(st.nextToken())!= id) {
			        	fileWriter.println();			//newline should be added before the line
			        	fileWriter.print(line);
			        	fileWriter.flush();
			        }
			        else{
			        	fileWriter.print(entry.toTxt(true));	//no println before as entry.toTxt() inserts a newline before
			        	fileWriter.flush();
			        	edited = true;
			        }
		    
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
		    
		    //this block parses id of first entry from EntryList.txt and adds to temp file if not id to be deleted
		    //no need to check if first line exists as this method can only be invoked if there are entries in the txt
		    String line = fileReader.readLine();
		    StringTokenizer st = new StringTokenizer(line, "|");
		    if (Integer.parseInt(st.nextToken()) != id){
		    	fileWriter.print(line);		
		    	fileWriter.flush();
		    }
		    else{
		    	idFound = true;
		    }
		    
		    //this block parses id of remaining entries from EntryList.txt and adds to temp file if not id to be deleted
		    while ((line = fileReader.readLine()) != null) {
		    	if(line != ""){
			    	st = new StringTokenizer(line, "|");
			        if (Integer.parseInt(st.nextToken())!= id) {
			        	fileWriter.println();			//newline should be added before the line
			        	fileWriter.print(line);
			        	fileWriter.flush();
			        }
			        else{
			        	//collect info about deleted Entry
			        	st = new StringTokenizer(line, "|");
			        	st.nextToken();	//skip id
						int transactionType = Integer.parseInt(st.nextToken());
						double amount = Double.parseDouble(st.nextToken());
						Date date = date_format.parse(st.nextToken());
						String category1 = st.nextToken();
						String category2 = st.nextToken();
						String description = "";
						if (st.hasMoreTokens())				//check, as description is an optional entry
							description = st.nextToken();
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
	 * Checks if the category still exists in EntryList.txt
	 * @param type (0:Assets, 1:Liabilities, 2:Income, 3:Expense)
	 * @param category
	 * @return true if the category exists
	 */
	public boolean checkCategoryExists(int type, String category){
		
		try{
			String line = null;
			
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    
		    //this block parses id of each entry from EntryList.txt and searches for entry with id
		    while ((line = fileReader.readLine()) != null) {
		    	
		    	//defensive coding in case empty lines exist in EntryList.txt
		    	if(line != ""){
		    		StringTokenizer st = new StringTokenizer(line, "|");
		    		st.nextToken();	//skip id
			    	int currentType = Integer.parseInt(st.nextToken());	//get current transactionType
		        	st.nextToken();	//skip amount
		        	st.nextToken();	//skip date
					String currentCategory1 = st.nextToken();
					String currentCategory2 = st.nextToken();
					
					//check if the relevant category matches
					switch(currentType){
					case 0:	//if asset category matches
							if(type == 0 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if income category matches
							if(type == 2 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
							break;
					case 1:	//if asset category matches
							if(type == 0 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if expense category matches
							if(type == 3 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
							break;
					case 2:	//if liability category matches
							if(type == 1 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if expense category matches
							if(type == 3 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
							break;
					case 3:	//has the same checks as transactionType 4 (A,L)
					case 4:	//if asset category matches
							if(type == 0 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if liability category matches
							if(type == 1 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
							break;
					case 5:	//if asset category matches
							if(type == 0 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if asset category matches
							if(type == 0 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
							break;
					case 6:	//if liability category matches
							if(type == 1 && category.equals(currentCategory1)){
								fileReader.close();
								return true;
							}
							//if liability category matches
							if(type == 1 && category.equals(currentCategory2)){
								fileReader.close();
								return true;
							}
						break;
					}
		    	}
		    }
		    fileReader.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Renames all entries having the old category name with the new category name
	 * @param type (0:Assets, 1:Liabilities, 2:Income, 3:Expenses)
	 * @param oldName
	 * @param newName
	 * @return true if renamed successfully
	 */
	public boolean renameCat(int type, String oldName, String newName){
		
		try {
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
			BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from History.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    boolean notFirstLine = false;	//boolean flag to indicate first line, need new line
		    String line = null;
			StringTokenizer st = null;
			Entry tempEntry = null;
			
			
			while ((line = fileReader.readLine()) != null) {

		    	st = new StringTokenizer(line, "|");
				int id = Integer.parseInt(st.nextToken());
				int transactionType = Integer.parseInt(st.nextToken());
				double amount = Double.parseDouble(st.nextToken());
				Date date = date_format.parse(st.nextToken());
				String category1 = st.nextToken();
				String category2 = st.nextToken();
				String description = "";
				if(st.hasMoreTokens())				//check, as description is an optional entry
					description = st.nextToken();
				
				//nested switch to check type, then transaction type, then the relevant category names
				switch(type){
					case 0:	//check for asset-related transaction types
							switch(transactionType){
							case 5:	if(category2.equals(oldName))
										category2 = newName;
							case 0:
							case 1:
							case 3:
							case 4:	if(category1.equals(oldName))
										category1 = newName;
									break;
							}
							break;
					case 1:	//check for liability-related transaction types
							switch(transactionType){
							case 2:	if(category1.equals(oldName))
										category1 = newName;
									break;
							case 6:	if(category1.equals(oldName))
										category1 = newName;
							case 3:
							case 4:	if(category2.equals(oldName))
										category2 = newName;
									break;
							}
							break;
					case 2:	//check for income-related transaction types
							switch(transactionType){
							case 0:	if(category2.equals(oldName))
										category2 = newName;
									break;
							}
							break;
					case 3:	//check for expense-related transaction types
							switch(transactionType){
							case 1:
							case 2:	if(category2.equals(oldName))
										category2 = newName;
									break;
							}
							break;
				}
				tempEntry = new Entry(id, transactionType, amount,
						date, category1, category2, description);
				fileWriter.print(tempEntry.toTxt(notFirstLine));	//first line no new line, the rest will have new line
				notFirstLine = true;

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
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the latest id used for a transaction.
	 * @return current_id
	 */
	public int getCurrentId(){
		return current_id;
	}
	
	/**
	 * Reads the entries from EntryList.txt 
	 * Returns a sorted linked list of all entries from earliest to latest
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
				else
					description = "";
				tempEntry = new Entry(id, transactionType, amount, date, category1, category2, description);
				transactionList.add(tempEntry);
			}
			fileReader.close();
			
		} catch (FileNotFoundException | ParseException e) {
			e.printStackTrace();
		}
		sort(transactionList);
		return transactionList;
	}
	
	
	/**
	 * Gets the current id according to the transactions in the file. Only used for initialization!
	 * Algorithm looks at all the ids, and takes the largest among them, as the ids might not be sorted in ascending order
	 * After initialization the next id will be tracked by current_id
	 * @return next id to be inserted
	 */
	private int initId(){
		int id = 0, temp = 0;
		try {
			Scanner fileReader = new Scanner(new FileReader(txt_path));

			while (fileReader.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(fileReader.nextLine(), "|");
				if(st.hasMoreTokens()){							//if clause to avoid problems with empty lines in txt file
					temp = Integer.parseInt(st.nextToken());					
					if(temp > id)								//check for max id basically
						id = temp;
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Sorts a list of entries from earliest to latest
	 * @param entryList
	 */
	private void sort(LinkedList<Entry> entryList){
		
		Collections.sort(entryList, new Comparator<Entry>(){
			
	        public int compare(Entry e1, Entry e2) {
	            return later(e1.getDate(),e2.getDate());
	        }
		});
	}
	
	/**
	 * Compares 2 dates
	 * @param d1
	 * @param d2
	 * @return 1 if d1 later d2, 0 if same, -1 if earlier
	 */
	private int later(Date d1, Date d2){

		if (d1.getTime() < d2.getTime())
			return -1;
		if(d1.getTime() == d2.getTime())
			return 0;
		else	//(d1.getTime() > d2.getTime())
			return 1;
	}
}