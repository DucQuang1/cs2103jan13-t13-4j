import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Logic class that manages all the asset categories
 * @author Zhiyuan, JP
 *
 */
public class AssetCatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/AssetCategory.txt";
	private final String key = "Amount";

	
	/**
	 * Default Constructor
	 */
	public AssetCatMgr(){}

	/**
	 * Adds a new asset category with the specified amount to AssetCategory.txt
	 * Pre: Requires an input String from the user to create the new AssetCategory name 
	 * Post: AssetCategory.txt is updated with a new entry
	 * @param inputCategory
	 * TODO inherit
	 */
	public void addAssetCategory(String inputCategory, Double amount){
		
		BufferedWriter entryWriter;
		try {
			entryWriter = new BufferedWriter(new FileWriter(txt_path, true));
			entryWriter.append("\n" + inputCategory + "|" + amount);
			entryWriter.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a category exists already
	 * @param inputCategory
	 * @return true if the category exists already
	 * TODO inherit
	 */
	public boolean checkExisting(String inputCategory){
		
		boolean exists = false;
		String line = "";
		
		try {
			Scanner assetCatReader = new Scanner(new FileReader(txt_path));

			while (assetCatReader.hasNext()){
				line = assetCatReader.nextLine();
				if (!line.equals("")){
					StringTokenizer st = new StringTokenizer(line, "|");
					if (st.nextToken().equals(inputCategory)){
						exists = true;
					}
				}
			}
			assetCatReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return exists;
	}
	

	/**
	 * Adds an amount to an asset category
	 * @param inputCategory
	 * @param amount
	 * @return true if operation was successful
	 * TODO inherit
	 */
	public boolean addAmountToAssetCategory(String inputCategory, double amount){

		boolean edited = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of AssetCategory.txt and adds amount if category matches
		    String line = fileReader.readLine(), category = "";
		    if(!line.equals("")){		    		
	        	StringTokenizer st = new StringTokenizer(line, "|");
	        	category = st.nextToken();
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
		    //this block parses through the rest of AssetCategory.txt and adds amount if category matches
		    while ((line = fileReader.readLine()) != null) {
		    	if(!line.equals("")){		    		
			    	
		        	StringTokenizer st = new StringTokenizer(line, "|");
		        	category = st.nextToken();
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
	 * Edits the name of an existing asset category
	 * @param oldName
	 * @param newName
	 * @return
	 * TODO inherit
	 */
	public boolean editNameAssetCategory(String oldName, String newName){

		boolean edited = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of AssetCategory.txt and edits name if oldName found
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
		    //this block parses through the rest of AssetCategory.txt and edits name if oldName found
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
	 * Searches AssetCategory.txt to find the particular category which is to be deleted
	 * Only deletes a category with 0 balance
	 * @param delAssetCategory (String of which AssetCategory to be deleted)
	 * @return true if deleted
	 * TODO inherit
	 */
	public boolean deleteAssetCategory(String inputCategory){
		
		boolean deleted = false;
		
		try{
			File inFile = new File(txt_path);
			File tempFile = File.createTempFile("tempFile", ".txt");
		    BufferedReader fileReader = new BufferedReader(new FileReader(txt_path));	//reads from EntryList.txt
		    PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));			//writes to a temp file
		    
		    //this block parses through first line of AssetCategory.txt and adds to temp file if not id to be deleted
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
		    //this block parses through remainder of AssetCategory.txt and adds to temp file if not id to be deleted
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
	 * Parses through the txt file and returns a dataset for chart rendering
	 * @return AssetDataset
	 * TODO inherit
	 */
	public DefaultCategoryDataset getChartData(){
		double amt;
		String category;

		//initialize AssetDataSet
		DefaultCategoryDataset AssetDataset = new DefaultCategoryDataset();

		//read in data from the txt file
		try {
			Scanner assetCatReader = new Scanner(new FileReader(txt_path));

			while (assetCatReader.hasNext()){
				StringTokenizer st = new StringTokenizer(assetCatReader.nextLine(), "|");
				category = st.nextToken();
				amt = Double.parseDouble(st.nextToken());
				AssetDataset.setValue(amt,key,category);
			}
			assetCatReader.close();
			
			return AssetDataset;
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Gets subtotal from all assets
	 * @return subtotal
	 * TODO inherit
	 */
	public double getSubtotal(){
		double subtotal = 0;
		String line = "";
		//read in data from the txt file
		try {
			Scanner assetCatReader = new Scanner(new FileReader(txt_path));

			while (assetCatReader.hasNext()){
				line = assetCatReader.nextLine();
				if (!line.equals("")){
					StringTokenizer st = new StringTokenizer(line, "|");
					st.nextToken();		//skip the category
					subtotal += Double.parseDouble(st.nextToken());
				}
			}
			assetCatReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return subtotal;
	}

	/**
	 * Reads the AssetCategory.txt file and return a linked list of category names
	 * Pre: -
	 * @return categoryList
	 * TODO inherit
	 */
	public LinkedList<String> getAssetCategory(){
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
}
