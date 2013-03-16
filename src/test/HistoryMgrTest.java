package test;
import static org.junit.Assert.*;

import java.util.Date;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test driver for HistoryMgr
 * @author JP
 *
 */
public class HistoryMgrTest {

	@Test
	public void test() {
		
		AssetCatMgr assetCatMgr = new AssetCatMgr();
		LiabilityCatMgr liabilityCatMgr = new LiabilityCatMgr();
		IncomeCatMgr incomeCatMgr = new IncomeCatMgr();
		ExpenseCatMgr expenseCatMgr = new ExpenseCatMgr();
		EntryMgr entryMgr = new EntryMgr();
		
		HistoryMgr historyMgr = new HistoryMgr(assetCatMgr, liabilityCatMgr, incomeCatMgr, expenseCatMgr, entryMgr);

		//historyMgr.clearLog();
		//@SuppressWarnings("deprecation")
		//Date date = new Date(13,02,2013);
		//historyMgr.addLog(0, 1, 2, 2000, date, "DBS", "Salary", "testing");
		//System.out.println(historyMgr.getLastId());
		//System.out.println(historyMgr.undoLast().toTxt(false));
		//historyMgr.renameCat(0, "cash$$", "Cash");
		//historyMgr.renameCat(3, "Transport", "transport");
		
	}

}
