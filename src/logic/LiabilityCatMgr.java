package logic;


/**
 * LiabilityCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author Wong Jing Ping, A0086581W
 *
 */
public class LiabilityCatMgr extends CatMgr {
	
	private final static String txt_path = "LiabilityCategory.txt";
	
	public LiabilityCatMgr(){

		setTxtPath(txt_path);
	}
}
