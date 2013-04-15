package logic;


/**
 * IncomeCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author Wong Jing Ping, A0086581W
 *
 */
public class IncomeCatMgr extends CatMgr {
	
	private final static String txt_path = "IncomeCategory.txt";
	
	public IncomeCatMgr(){

		setTxtPath(txt_path);
	}
}
