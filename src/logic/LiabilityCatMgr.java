package logic;
/**
 * LiabilityCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author A0086581W, Wong Jing Ping
 *
 */
public class LiabilityCatMgr extends CatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/LiabilityCategory.txt";
	
	public LiabilityCatMgr(){
	
		setTxtPath(txt_path);
	}
}
