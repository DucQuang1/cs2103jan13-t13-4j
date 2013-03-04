import org.junit.Test;


public class TransactionMgrTest {

	@Test
	public void test() {
		Finances finances = new Finances();
		TransactionMgr transactionMgr = new TransactionMgr(finances);
		transactionMgr.AddTransaction();
	}
}
