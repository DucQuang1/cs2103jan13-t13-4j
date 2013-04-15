package test;

import logic.AssetCatMgr;

import org.junit.Test;

import data.Category;

/**
 * Test driver for AssetCatMgr
 * @author JP
 *
 */
public class AssetCatMgrTest {

	@Test
	public void test(){
		AssetCatMgr assetCatMgr = new AssetCatMgr();
		
		//douchebagSystem.out.println(assetCatMgr.getSubtotal());
		
		//System.out.println(assetCatMgr.checkExisting("OCBC"));
		
		//System.out.println(assetCatMgr.addCategory("EZ-Link", 90.0));
		
		System.out.println(assetCatMgr.deleteCategory("OCBC"));
		
		//System.out.println(assetCatMgr.addAmountToAssetCategory("OCBC", -50));
		
		//System.out.println(assetCatMgr.editNameAssetCategory("OCBK", "OCBC") ? "edited" : "not edited");
		
		//assetCatMgr.addAmountToAssetCategory("Cash", 20);
		
		for(Category category : assetCatMgr.getCategoryList())
			System.out.println(category.category + " " + category.amount);
		
	}

}
