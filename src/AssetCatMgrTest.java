import org.junit.Test;


public class AssetCatMgrTest {

	@Test
	public void test() {
		AssetCatMgr assetCatMgr = new AssetCatMgr();
		
		System.out.println(assetCatMgr.getSubtotal());
		
		//System.out.println(assetCatMgr.checkExisting("OCBC"));
		
		//System.out.println(assetCatMgr.addCategory("EZ-Link", 90.0));
		
		//System.out.println(assetCatMgr.deleteAssetCategory("Wallet"));
		
		//System.out.println(assetCatMgr.addAmountToAssetCategory("OCBC", -50));
		
		//System.out.println(assetCatMgr.editNameAssetCategory("OCBK", "OCBC") ? "edited" : "not edited");
		
		//assetCatMgr.addAmountToAssetCategory("Cash", 20);
		
		for(String category : assetCatMgr.getCategoryList())
			System.out.println(category);
		
	}

}
