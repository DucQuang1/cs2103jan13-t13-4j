import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entry class that contains essential fields for an entry.
 * @author JP
 *
 */
public class Entry{
	private int transactionType;
	private int id;
	private double amount;
	private Date date;
	private String category1;
	private String category2;
	private String description;
	private final static SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yy");
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
	
	//Getters
	public int getId(){
		return id;
	}
	public int getTransactionType(){
		return transactionType;
	}
	public double getAmount(){
		return amount;
	}
	public Date getDate(){
		return date;
	}
	public String getCategory1(){
		return category1;
	}
	public String getCategory2(){
		return category2;
	}
	public String getDescription(){
		return description;
	}
	
	//Setters (for updating)
	public void setId(int id){
		this.id= id;
	}
	public void setTransactionType(int transactionType){
		this.transactionType= transactionType;
	}
	public void setAmount(double amount){
		this.amount = amount;
	}
	public void setDate(Date date){
		this.date = date;
	}
	public void setCategory1(String category1){
		this.category1 = category1;
	}
	public void setCategory2(String category2){
		this.category2 = category2;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public String toTxt(){
		String output = new String();
		output = "\n"  + Integer.toString(id) + "|" + Integer.toString(transactionType) + "|"
				+ Double.toString(amount) + "|" + date_format.format(date) + "|"
				+ category1 + "|" + category2 + "|" + description ;
		return output;
	}
}