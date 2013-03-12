/**
 * LiabilityCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author JP
 *
 */
public class LiabilityCatMgr extends CatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/LiabilityCategory.txt";
	
	LiabilityCatMgr(){
	
		setTxtPath(txt_path);
	}
}
