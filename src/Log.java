import java.util.Date;


/**
 * This class is similar to transaction, except it has an extra field to record the operation type
 * @author JP
 *
 */
public class Log extends Entry{
	/**
	 * operationType:
	 * 	0: add
	 * 	1: edit
	 *  2: delete
	 */
	protected int operationType;	
	
	/**
	 * Default Constructor
	 */
	Log(){}
	
	/**
	 * Constructor
	 * @param operationType
	 * @param id
	 * @param transactionType
	 * @param amount
	 * @param date
	 * @param category1
	 * @param category2
	 * @param description
	 */
	Log(int operationType, int id, int transactionType, double amount, Date date, 
			String category1, String category2, String description){
		this.operationType = operationType;
		this.id = id;
		this.transactionType = transactionType;
		this.amount = amount;
		this.date = date;
		this.category1 = category1;
		this.category2 = category2;
		this.description = description;
	}
	
	/**
	 * gets operationType
	 * @return operationType
	 */
	public int getOperationType(){
		return operationType;
	}
	/**
	 * sets operationType
	 * @param operationType
	 */
	public void setOperationType(int operationType){
		this.operationType = operationType;
	}
	
	/**
	 * returns a string in format to be printed to txt file
	 * each field is separated by a pipe character
	 * boolean parameter allows printing with or without newline
	 * @param includeNewLine
	 * @return output
	 */
	public String toTxt(boolean includeNewLine){
		
		String output = new String();
		
		if (includeNewLine)
			output += "\n";
		
		output = Integer.toString(operationType) + "|" + Integer.toString(id) + "|" 
				+ Integer.toString(transactionType) + "|"+ Double.toString(amount) + "|" 
				+ date_format.format(date) + "|" + category1 + "|" + category2 + "|" + description ;
		return output;
	}
}
