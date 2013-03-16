package test;

import org.junit.Test;

import view.Finances;
import view.TransactionMgr;

/**
 * Test driver for TransactionMgr
 * @author JP
 *
 */
public class TransactionMgrTest {

	@Test
	public void test() {
		Finances finances = new Finances();
		TransactionMgr transactionMgr = new TransactionMgr(finances);
		transactionMgr.addTransaction();
	}
}
