package logic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Logic class that manages all the  categories
 * @author JP
 * @author Zhiyuan
 * 
 */
public class CatMgr {
	
	private String txt_path = null;
	private final String key = "Amount";

	
	/**
	 * Default Constructor
	 */
	public CatMgr(){}
	
	public void setTxtPath(String txt_path){
		this.txt_path = txt_path;
		try {
			FileReader fileCheck = new FileReader(txt_path);
		} catch (FileNotFoundException e) {
			File newFile = new File(txt_path);
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Checks if a category exists already
	 * @param category
	 * @return true if the category exists already
	 */
	public boolean checkExisting(String category){
		
		boolean exists = false;
		String line = "";
		
		try {
			Scanner CatReader = new Scanner(new FileReader(txt_path));

			while (CatReader.hasNext()){
				line = CatReader.nextLine();
				if (!line.equals("")){
					StringTokenizer st = new StringTokenizer(line, "|");
					if (st.nextToken().equals(category)){
						exists = true;
					}
				}
			}
			CatReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return exists;
	}
	
	/**
	 * Adds a new  category with the specified amount to Category.txt
	 * Pre: Requires an input String from the user to create the new Category name 
	 * Post: Category.txt is updated with a new entry
	 * @param inputCategory
	 * @param amount
	 * @return true if added successfully
	 */
	public boolean addCategory(String inputCategory, Double amount){
		
		BufferedWriter entryWriter;
		
		try {
			Scanner CatReader = new Scanner(new FileReader(txt_path));
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));

			//check if file is empty to avoid adding an empty line for the first entry
			if (!CatReader.hasNext()){
				entryWriter.append(inputCategory + "|" + amount);
			}
			else{
				entryWriter.append("\n" + inputCategory + "|" + amount);
			}
			CatReader.close();
			entryWriter.close();
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Adds an amount to an  category
	 * @param category
	 * @param amount
	 * @return true if operation was successful
	 */
	public boolean addAmountToCategory(String category, double amount){

		boolean edited = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of Category.txt and adds amount if category matches
		    String line = fileReader.readLine(), inputCategory = "";
		    if(!line.equals("")){		    		
	        	StringTokenizer st = new StringTokenizer(line, "|");
	        	inputCategory = st.nextToken();
				if (category.equals(inputCategory)){
					double amountRead = Double.parseDouble(st.nextToken());
					amount += amountRead;
					fileWriter.print(category + "|" + amount);
					edited = true;
				}
				else{
					fileWriter.print(line);
				}
		    }
		    //this block parses through the rest of Category.txt and adds amount if category matches
		    while ((line = fileReader.readLine()) != null) {
		    	if(!line.equals("")){		    		
			    	
		        	StringTokenizer st = new StringTokenizer(line, "|");
		        	inputCategory = st.nextToken();
					if (category.equals(inputCategory)){
						double amountRead = Double.parseDouble(st.nextToken());
						amount += amountRead;
						fileWriter.println();
						fileWriter.print(category + "|" + amount);
						edited = true;
					}
					else{
						fileWriter.println();
						fileWriter.print(line);
					}
		    	}
		    }
		    fileReader.close();
		    fileWriter.close();
		    
		    //warning if not deleted
		    if(!edited){
		    	System.out.println("not deleted!");
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
		
		return edited;
	}
	
	/**
	 * Edits the name of an existing category
	 * @param oldName
	 * @param newName
	 * @return true if edited successfully
	 */
	public boolean editCategoryName(String oldName, String newName){

		boolean edited = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of Category.txt and edits name if oldName found
		    String line = fileReader.readLine(), category = "";
		    if(!line.equals("")){		    		
	        	StringTokenizer st = new StringTokenizer(line, "|");
	        	category = st.nextToken();
				if (category.equals(oldName)){
					double amount = Double.parseDouble(st.nextToken());
					fileWriter.print(newName + "|" + amount);
					edited = true;
				}
				else{
					fileWriter.print(line);
				}
		    }
		    //this block parses through the rest of Category.txt and edits name if oldName found
		    while ((line = fileReader.readLine()) != null) {
		    	if(!line.equals("")){		    		
			    	
		        	StringTokenizer st = new StringTokenizer(line, "|");
		        	category = st.nextToken();
					if (category.equals(oldName)){
						double amount = Double.parseDouble(st.nextToken());
						fileWriter.println();
						fileWriter.print(newName + "|" + amount);
						edited = true;
					}
					else{
						fileWriter.println();
						fileWriter.print(line);
					}
		    	}
		    }
		    fileReader.close();
		    fileWriter.close();
		    
		    //warning if not deleted
		    if(!edited){
		    	System.out.println("not deleted!");
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
		
		return edited;
	}

	
	/**
	 * Searches Category.txt to find the particular category which is to be deleted
	 * Only deletes a category with 0 balance
	 * @param inputCategory (String of which Category to be deleted)
	 * @return true if deleted
	 */
	public boolean deleteCategory(String inputCategory){
		
		boolean deleted = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of Category.txt and adds to temp file if not id to be deleted
		    String line = fileReader.readLine(), category = "";
		    if(!line.equals("")){		    		
		    	
	        	StringTokenizer st = new StringTokenizer(line, "|");
	        	category = st.nextToken();
				if (category.equals(inputCategory)){
					double amount = Double.parseDouble(st.nextToken());
					if (amount == 0){
						deleted = true;
					}
					else{
						fileWriter.print(line);
						deleted = false;
					}
				}
				else{
					fileWriter.print(line);
				}
	    	}
		    //this block parses through remainder of Category.txt and adds to temp file if not id to be deleted
		    while ((line = fileReader.readLine()) != null) {
		    	if(!line.equals("")){		    		
			    	
		        	StringTokenizer st = new StringTokenizer(line, "|");
		        	category = st.nextToken();
					if (category.equals(inputCategory)){
						double amount = Double.parseDouble(st.nextToken());
						if (amount == 0){
							deleted = true;
						}
						else{
							fileWriter.println();
							fileWriter.print(line);
							deleted = false;
						}
					}
					else{
						fileWriter.println();
						fileWriter.print(line);
					}
		    	}
		    }
		    fileReader.close();
		    fileWriter.close();
		    
		    //warning if not deleted
		    if(!deleted){
		    	System.out.println("not deleted!");
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
		return deleted;
	}
	
	/**
	 * Gets amount in a category
	 * Only Used for checking if a transaction is legal (amount to be deducted <= current balance)
	 * @param category
	 * @return amount
	 */
	public double getAmount(String category){
		
		String line = null, readCategory = null;
		double amount = 0;
		
		try {
			Scanner CatReader = new Scanner(new FileReader(txt_path));

			while (CatReader.hasNext()){
				line = CatReader.nextLine();
				if (!line.equals("")){
					StringTokenizer st = new StringTokenizer(line, "|");
					readCategory = st.nextToken();
					if (readCategory.equals(category)){						
						amount = Double.parseDouble(st.nextToken());
						CatReader.close();
						return amount;
					}
				}
			}
			CatReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return amount;
	}

	/**
	 * Gets subtotal from all s
	 * @return subtotal
	 */
	public double getSubtotal(){
		double subtotal = 0;
		String line = "";
		//read in data from the txt file
		try {
			Scanner CatReader = new Scanner(new FileReader(txt_path));

			while (CatReader.hasNext()){
				line = CatReader.nextLine();
				if (!line.equals("")){
					StringTokenizer st = new StringTokenizer(line, "|");
					st.nextToken();		//skip the category
					subtotal += Double.parseDouble(st.nextToken());
				}
			}
			CatReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return subtotal;
	}

	/**
	 * Reads the Category.txt file and return a linked list of category names
	 * Pre: -
	 * @return categoryList
	 */
	public LinkedList<String> getCategoryList(){
		LinkedList<String> categoryList = new LinkedList<String>();
		StringTokenizer st;
		String category, line;
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(txt_path));

			while ((line = br.readLine())!= null){
				st = new StringTokenizer(line, "|");
				category = st.nextToken();
				categoryList.add(category);
			}
			br.close();
		} catch (Exception e){
			System.out.println(e);
		}

		return categoryList;
	}
	
	/**
	 * Parses through the txt file and returns a dataset for chart rendering
	 * @return Dataset
	 */
	public DefaultCategoryDataset getChartData(){
		double amt;
		String category;

		//initialize DataSet
		DefaultCategoryDataset Dataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner CatReader = new Scanner(new FileReader(txt_path));

			while (CatReader.hasNext()){
				StringTokenizer st = new StringTokenizer(CatReader.nextLine(), "|");
				category = st.nextToken();
				amt = Double.parseDouble(st.nextToken());
				if(amt > 0)
					Dataset.setValue(amt,key,category);
			}
			CatReader.close();
			
			return Dataset;
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}
