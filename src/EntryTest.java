import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * This tests if Entry is able to store and display its respective fields properly
 * @author JP
 *
 */
public class EntryTest {

	@Test
	public void test() {
		Date d1 = null;
		final SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yy");
		try {
			d1 = date_format.parse("28/02/13");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Entry e1 = new Entry(1,1,5000,d1,"Cash","Salary","first expense");
		System.out.println(d1.toString());
		System.out.println(e1.toString());	//toString is a pre-defined method by the system.
		System.out.println(e1.toTxt(false));		//have to use toTxt to print out to txt.
		System.out.println(e1.toTxt(true));			//prints with an extra newline in front
		
	}

}
