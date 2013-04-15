package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import logic.EntryMgr;

import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import data.Entry;

/**
 * This tests the functions within the EntryMgr, such as crud operations, returning a list etc
 * @author JP
 *
 */
public class EntryMgrTest {

	@Test
	public void test() {
		EntryMgr entryMgr = new EntryMgr();
		Date d1 = null;
		final SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yy");
		try {
			d1 = date_format.parse("18/02/13");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//int tempId = entryMgr.addEntry(1, 100.00, d1, "DBS", "Restaurant", "trial");
		//System.out.println(tempId);
		//Entry testEntry = new Entry(1, 1, 4000, d1, "DBS", "Mont Blanc", "trial");
		//entryMgr.editEntry(testEntry);
		
		LinkedList<Entry> transactionList = new LinkedList<Entry>(entryMgr.getTransactionList());
		
		for(Entry entry : transactionList){
			System.out.println(entry.toTxt(true));
			System.out.print(entry.getDate().toString());
		}
		System.out.println();
		
		assertTrue(entryMgr.checkCategoryExists(0, "OCBC"));
		assertTrue(entryMgr.checkCategoryExists(0, "Cash"));
		assertFalse(entryMgr.checkCategoryExists(1, "OCBC"));
		assertFalse(entryMgr.checkCategoryExists(1, "Cash"));
		assertFalse(entryMgr.checkCategoryExists(1, "AMEX"));
		assertTrue(entryMgr.checkCategoryExists(1, "HDB Loan"));
		
		//entryMgr.sort(transactionList);	//change sort to public when testing
		
		
		//XYDataset dataset = entryMgr.getTransactionByTime();
		//System.out.println(dataset.toString());

	}

}
