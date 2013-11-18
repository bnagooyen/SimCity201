package simcity.test;


import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankCustomerRole.bankCustomerState;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankTeller;
import simcity.test.mock.MockBankLoanOfficer;
import junit.framework.TestCase;

public class BankCustomerTest extends TestCase{

	PersonAgent p;
	BankCustomerRole customer;
	MockBankTeller teller;
	MockBankManager mgr;
	MockBankLoanOfficer loanOfficer;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("BankCustomer");
		customer = new BankCustomerRole(p);
		p.addRole(customer);
		mgr = new MockBankManager("MockBankManager");
		teller = new MockBankTeller("mockTeller");
		loanOfficer = new MockBankLoanOfficer("mockLoanOfficer");
		customer.manager=mgr;
	}
	
	public void testWithdrawal() {
		
		p.money=0.00;
		
		//preconditions
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.arrived);
        assertEquals("Teller should start as null",customer.teller,null);
        assertEquals("Loan Officer should start as null",customer.loanOfficer,null);
        assertEquals("Account number should be null ", customer.accountNum, null);
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + teller.log.toString(), 0, teller.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + loanOfficer.log.toString(), 0, loanOfficer.log.size());
        
        
        //Start Schedules
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.waiting);
        assertFalse("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        
        //Go To Teller
        customer.msgGoToTeller(teller);
        assertTrue("Robber should now have Teller",customer.teller!=null);
        
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.inProgress);
        assertTrue("MockBankTeller should have logged an event for getting maker account, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Make Account"));
        
        //Account Made
        customer.msgAccountMade(3);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.waiting);
        assertTrue("Account number should be changed ", customer.accountNum==3);
        
        //Transaction
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.inProgress);
        assertFalse("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for withdrawal, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Withdrawal20.0--3"));
        
        //Transaction Complete
        customer.msgTransactionComplete(-20.0);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.done);
        assertEquals("Money needs to be increased ", customer.myPerson.money, 20.0);
        
        
        //Leave
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertFalse("Should deactivate Role", customer.isActive);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.arrived);
	}
	
	public void testDeposit() {
		
		p.money=120.00;
		
		//preconditions
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.arrived);
        assertEquals("Teller should start as null",customer.teller,null);
        assertEquals("Loan Officer should start as null",customer.loanOfficer,null);
        assertEquals("Account number should be null ", customer.accountNum, null);
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + teller.log.toString(), 0, teller.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + loanOfficer.log.toString(), 0, loanOfficer.log.size());
        
        
        //Start Schedules
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.waiting);
        assertFalse("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        
        //Go To Teller
        customer.msgGoToTeller(teller);
        assertTrue("Robber should now have Teller",customer.teller!=null);
        
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.inProgress);
        assertTrue("MockBankTeller should have logged an event for getting maker account, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Make Account"));
        
        //Account Made
        customer.msgAccountMade(3);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.waiting);
        assertTrue("Account number should be changed ", customer.accountNum==3);
        
        //Transaction
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", customer.state, bankCustomerState.inProgress);
        assertFalse("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for withdrawal, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Deposit20.0--3"));
        
        //Transaction Complete
        customer.msgTransactionComplete(20.0);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.done);
        assertEquals("Money needs to be increased ", customer.myPerson.money, 100.0);
        
        
        //Leave
        assertTrue("Scheduler should have returned True", customer.pickAndExecuteAnAction());
        assertFalse("Should deactivate Role", customer.isActive);
        assertEquals("AccountState should be at ", customer.state, bankCustomerState.arrived);
	}
        
}