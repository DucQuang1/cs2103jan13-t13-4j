package logic;


/**
 * AssetCatMgr inherits from CatMgr
 * It only has to set the txt_path for the functions from CatMgr to work as usual
 * @author Wong Jing Ping, A0086581W
 *
 */
public class AssetCatMgr extends CatMgr {
	
	private final static String txt_path = "AssetCategory.txt";
	
	public AssetCatMgr(){

		setTxtPath(txt_path);
		
	}
}
