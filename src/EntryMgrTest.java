import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.junit.Test;

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
		final SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yy");
		try {
			d1 = date_format.parse("20/02/13");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int tempId = entryMgr.addEntry(2, 100.00, d1, "DBS", "Restaurant", "trial");
		System.out.println(tempId);
		//Entry testEntry = new Entry(5, 1, 4000, d1, "DBS", "Salary", "trial");
		//entryMgr.editEntry(testEntry);
		LinkedList<Entry> transactionList = new LinkedList<Entry>(entryMgr.getTransactionList());
		
		System.out.println(entryMgr.getEntry(3).toTxt());
		for(Entry entry : transactionList){
			System.out.print(entry.toTxt());
		}
		System.out.println();
	}

}
