import org.junit.Test;


public class HistoryMgrTest {

	@Test
	public void test() {
		
		HistoryMgr historyMgr = new HistoryMgr();
		//historyMgr.clearLog();
		//Date date = new Date(13,02,2013);
		//historyMgr.addLog(0, 1, 2, 2000, date, "DBS", "Salary", "testing");
		System.out.println(historyMgr.getLastId());
		System.out.println(historyMgr.undoLast().toTxt());
	}

}
