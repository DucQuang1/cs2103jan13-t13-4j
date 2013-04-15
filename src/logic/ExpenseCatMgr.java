package logic;
/**
 * ExpenseCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author A0086581W, Wong Jing Ping
 *
 */
public class ExpenseCatMgr extends CatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/ExpenseCategory.txt";
	
	public ExpenseCatMgr(){
	
		setTxtPath(txt_path);
	}
}
