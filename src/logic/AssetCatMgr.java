package logic;

/**
 * AssetCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author A0086581W, Wong Jing Ping
 *
 */
public class AssetCatMgr extends CatMgr {
	
	private final String txt_path = getClass().getResource(".").getPath() + "/db/AssetCategory.txt";
	
	public AssetCatMgr(){
	
		setTxtPath(txt_path);
	}
}
