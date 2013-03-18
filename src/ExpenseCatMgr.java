/**
 * ExpenseCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author JP
 *
 */
public class ExpenseCatMgr extends CatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/ExpenseCategory.txt";
	
	ExpenseCatMgr(){
	
		setTxtPath(txt_path);
	}
}
