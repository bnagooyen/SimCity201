package simcity.test;


import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankLoanOfficerRole.accountState;
import simcity.Bank.BankRobberRole;
import simcity.Bank.BankRobberRole.bankRobberState;
import simcity.test.mock.MockBankCustomer;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankTeller;
import junit.framework.TestCase;

public class BankLoanOfficerTest extends TestCase{

	PersonAgent p;
	BankLoanOfficerRole loanOfficer;
	MockBankTeller teller;
	MockBankManager mgr;
	MockBankCustomer customer1;
	MockBankCustomer customer2;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("loanOfficer");
		loanOfficer = new BankLoanOfficerRole(p);
		p.addRole(loanOfficer);
		mgr = new MockBankManager("MockBankManager");
		teller = new MockBankTeller("mockTeller");
		loanOfficer.manager=mgr;
		customer1= new MockBankCustomer("customer1");
		customer2= new MockBankCustomer("customer2");
	}
	
	public void testCreateAccount() {
		assertEquals("mock bank manager should have 0 logs", mgr.log.size(), 0);
		assertEquals("mock cus1 should have 0 logs", customer1.log.size(), 0);
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		
		loanOfficer.msgMakeAccount(customer1);
		
		assertTrue("should be a customer there", loanOfficer.GetCustomer()!=null);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("MyCustomer state should be request", loanOfficer.GetCustomer().state, BankLoanOfficerRole.accountState.requested);
		
		assertEquals("Bankmanager should have received account creation request", mgr.log.size(), 1);
		assertTrue("received correct msg", mgr.log.containsString("Received a message to create a new account"));
		
		//make sure PAEAA returns false;
		assertFalse(loanOfficer.pickAndExecuteAnAction());
	}   
	
}