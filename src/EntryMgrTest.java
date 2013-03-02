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
			d1 = date_format.parse("28/02/13");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entryMgr.addEntry(2, 600.00, d1, "DBS", "HDB Installment", "trial");
		LinkedList<Entry> transactionList = new LinkedList<Entry>(entryMgr.getTransactionList());
		for(Entry e : transactionList){
			System.out.print(e.toTxt());
		}
		System.out.println(transactionList.size());
	}

}
