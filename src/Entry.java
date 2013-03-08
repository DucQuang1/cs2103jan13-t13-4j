import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entry class that contains essential fields for an entry.
 * @author JP
 *
 */
public class Entry{
	protected int transactionType;
	protected int id;
	protected double amount;
	protected Date date;
	protected String category1;
	protected String category2;
	protected String description;
	protected final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 * Default Constructor
	 */
	Entry(){}
	/**
	 * Constructor that creates an entry with the following fields
	 * @param id
	 * @param transactionType
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 */
	Entry(int id, int transactionType, double amount, Date date, String category1, String category2, String description){
		this.id = id;
		this.transactionType = transactionType;
		this.amount = amount;
		this.date = date;
		this.category1 = category1;
		this.category2 = category2;
		this.description = description;
	}
	
	/**
	 * gets id
	 * @return id
	 */
	public int getId(){
		return id;
	}
	/**
	 * gets transactionType
	 * @return transactionType
	 */
	public int getTransactionType(){
		return transactionType;
	}
	/**
	 * gets amount
	 * @return amount
	 */
	public double getAmount(){
		return amount;
	}
	/**
	 * get date
	 * @return date
	 */
	public Date getDate(){
		return date;
	}
	/**
	 * get category1
	 * @return category1
	 */
	public String getCategory1(){
		return category1;
	}
	/**
	 * get category2
	 * @return category2
	 */
	public String getCategory2(){
		return category2;
	}
	/**
	 * get description
	 * @return description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * sets id
	 * @param id
	 */
	public void setId(int id){
		this.id= id;
	}
	/**
	 * sets transactionType
	 * @param transactionType
	 */
	public void setTransactionType(int transactionType){
		this.transactionType= transactionType;
	}
	/**
	 * sets amount
	 * @param amount
	 */
	public void setAmount(double amount){
		this.amount = amount;
	}
	/**
	 * sets date
	 * @param date
	 */
	public void setDate(Date date){
		this.date = date;
	}
	/**
	 * sets category1
	 * @param category1
	 */
	public void setCategory1(String category1){
		this.category1 = category1;
	}
	/**
	 * sets category2
	 * @param category2
	 */
	public void setCategory2(String category2){
		this.category2 = category2;
	}
	/**
	 * sets description
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * returns a string in format to be printed to txt file
	 * each field is separated by a pipe character
	 * includes a newline in front by default
	 * supports option for not printing newline in front, 
	 * which is used for first entry to file, to prevent a blank line and subsequent errors
	 * @param includeNewLine
	 * @return output
	 */
	public String toTxt(boolean includeNewLine){
		
		String output = new String();
		
		if (includeNewLine){
			output += "\n";
		}
		
		output += Integer.toString(id) + "|" + Integer.toString(transactionType) + "|"
				+ Double.toString(amount) + "|" + date_format.format(date) + "|"
				+ category1 + "|" + category2 + "|" + description ;
		
		return output;
	}
}