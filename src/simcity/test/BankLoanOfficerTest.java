package simcity.test;


import com.sun.org.apache.bcel.internal.generic.ASTORE;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.mockrole.MockRole;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankLoanOfficerRole.accountState;
import simcity.Bank.BankLoanOfficerRole.bankLoanState;
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
	MockRole mockrole;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("loanOfficer", mockrole);
		mockrole=new MockRole("mockrole", p);
		loanOfficer = new BankLoanOfficerRole(p);
		p.addRole(loanOfficer);
		mgr = new MockBankManager("MockBankManager");
		teller = new MockBankTeller("mockTeller");
		loanOfficer.manager=mgr;
		customer1= new MockBankCustomer("customer1");
		customer2= new MockBankCustomer("customer2");
	}
	
	public void testCreateAccountAndRequestLoan() {
		assertEquals("mock bank manager should have 0 logs", mgr.log.size(), 0);
		assertEquals("mock cus1 should have 0 logs", customer1.log.size(), 0);
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		
		loanOfficer.msgMakeAccount(customer1);
		
		assertTrue("should be a customer there", loanOfficer.GetCustomer()!=null);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("MyCustomer state should be request", loanOfficer.GetCustomer().state, BankLoanOfficerRole.accountState.requested);
		
		assertEquals("Bankmanager should have received account creation request", mgr.log.size(), 1);
		assertTrue("received correct msg", mgr.log.containsString("Received a message to create a new account"));
		
		assertEquals("mock cus1 should have 0 logs", customer1.log.size(), 0);
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		
		//make sure PAEAA returns false;
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
		loanOfficer.msgAccountCreated(1);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("customer received log that account has been created", customer1.log.size(), 1);
		assertTrue("", customer1.log.containsString("New Account made: 1"));
		assertEquals("loanofficer should be waiting for loan request", loanOfficer.getState(), bankLoanState.waitingForLoanRequest);
		
		
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		assertEquals("mock bank manager should have 1 logs", mgr.log.size(), 1);
		
		//make sure PAEAA returns false;
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
		loanOfficer.msgINeedALoan(customer1, 1, 1000, "waiter");
		
		assertTrue("customer should not be null", loanOfficer.GetCustomer()!=null);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		
		assertEquals("customer logs remain the same", customer1.log.size(), 1);
		assertEquals("mock bank manager should have 2 logs", mgr.log.size(), 2);
		assertTrue("received loan request", mgr.log.containsString("Received loan request"));

		assertEquals("loanofficer should be waiting for loan request", loanOfficer.getState(), bankLoanState.atManager);
		
		//make sure doesn't call same action again
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
		loanOfficer.msgLoanComplete();
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("mock bank manager should have 3 logs", mgr.log.size(), 3);
		assertEquals("mock cus1 should have 2 logs", customer1.log.size(), 2);
		
		assertTrue("mock cust1 should have received loan", customer1.log.containsString("Received loan"));
		
		assertTrue("no more customer", loanOfficer.GetCustomer()==null);
		assertEquals("should be avaliable", loanOfficer.getState(), bankLoanState.working);
		
		assertTrue("manager knows loan officer is available", mgr.log.containsString("Officer now available"));
		
		assertFalse(loanOfficer.pickAndExecuteAnAction());
	}   
	
	public void testCreateAccountAndDenyLoan() {
		assertEquals("mock bank manager should have 0 logs", mgr.log.size(), 0);
		assertEquals("mock cus1 should have 0 logs", customer1.log.size(), 0);
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		
		loanOfficer.msgMakeAccount(customer1);
		
		assertTrue("should be a customer there", loanOfficer.GetCustomer()!=null);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("MyCustomer state should be request", loanOfficer.GetCustomer().state, BankLoanOfficerRole.accountState.requested);
		
		assertEquals("Bankmanager should have received account creation request", mgr.log.size(), 1);
		assertTrue("received correct msg", mgr.log.containsString("Received a message to create a new account"));
		
		assertEquals("mock cus1 should have 0 logs", customer1.log.size(), 0);
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		
		//make sure PAEAA returns false;
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
		loanOfficer.msgAccountCreated(1);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		assertEquals("customer received log that account has been created", customer1.log.size(), 1);
		assertTrue("", customer1.log.containsString("New Account made: 1"));
		assertEquals("loanofficer should be waiting for loan request", loanOfficer.getState(), bankLoanState.waitingForLoanRequest);
		
		
		assertEquals("mock cus2 should have 0 logs", customer2.log.size(), 0);
		assertEquals("mock bank manager should have 1 logs", mgr.log.size(), 1);
		
		//make sure PAEAA returns false;
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
		loanOfficer.msgINeedALoan(customer1, 1, 1000, "marketcashier");
		
		assertTrue("customer should not be null", loanOfficer.GetCustomer()!=null);
		
		assertTrue(loanOfficer.pickAndExecuteAnAction());
		
		assertEquals("customer should have 2 logs", customer1.log.size(), 2);
		assertEquals("mock bank manager should have 2 logs", mgr.log.size(), 2);

		assertEquals("loanofficer should be available", loanOfficer.getState(), bankLoanState.working);
		assertTrue("should be no customer there", loanOfficer.GetCustomer()==null);
		assertTrue("mgr knows officer is available", mgr.log.containsString("Officer now available"));
		//make sure doesn't call same action again
		assertFalse(loanOfficer.pickAndExecuteAnAction());
		
	}
	
	
}