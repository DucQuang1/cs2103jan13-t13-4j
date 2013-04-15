package logic;


/**
 * ExpenseCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author Wong Jing Ping, A0086581W
 *
 */
public class ExpenseCatMgr extends CatMgr {
	
	private final static String txt_path = "ExpenseCategory.txt";
	
	public ExpenseCatMgr(){
		
		setTxtPath(txt_path);
	}
}
