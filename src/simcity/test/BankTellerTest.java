package simcity.test;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.Bank.BankTellerRole;
import simcity.Bank.BankTellerRole.accountState;
import simcity.Bank.BankTellerRole.bankTellerState;
import simcity.test.mock.MockBankCustomer;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankRobber;
import junit.framework.TestCase;

public class BankTellerTest extends TestCase{

	PersonAgent p;
	BankTellerRole t;
	MockBankCustomer customer;
	MockBankManager mgr;
	MockBankRobber robber;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("BankManager");
		t = new BankTellerRole(p);
		p.addRole(t);
		mgr = new MockBankManager("MockBankManager");
		customer = new MockBankCustomer("mockCustomer");
		robber = new MockBankRobber("mockRobber");
		t.manager=mgr;
	}
	
	public void testNormativeWithdrawal() {
		
		//preconditions
        assertEquals("Requested should equal 0", t.requested, 0.0);
        assertEquals("Requested should equal 0", t.transacted, 0.0);
        assertEquals("AccountState should start at ", t.state, bankTellerState.working);
        assertEquals("teller should have an empty event log before his msgBill is called. Instead, the teller's event log read: " + t.log.toString(), 0, t.log.size());
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        
        
    	//Create Account
        t.msgMakeAccount(customer);
        assertFalse("Teller should have a Person", t.myPerson==null);
        assertFalse("Teller should have a MyCustomer", t.customer==null);
        assertFalse("Teller should have a MyCustomer", t.customer.BC==null);
        assertEquals("CustomerState should start at ", t.customer.state, accountState.none);
        
        
        //Scheduler
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());
        assertEquals("CustomerState should start at ", t.customer.state, accountState.requested);    
		assertTrue("MockBankManager should have logged an event for receiving createAccount but instead it's: " + mgr.log.getLastLoggedEvent().toString(), mgr.log.containsString("Received a message to create a new account"));
	
		//Account Created
		t.msgAccountCreated(3);
        assertEquals("CustomerState should be ", t.customer.state, accountState.justMade);  
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());  
        assertEquals("CustomerState should be  ", t.customer.state, accountState.existing);  
        
        //Message Withdrawal
        t.msgWithdrawal(customer, 3, 50);
        assertEquals("Requested should equal -50", t.requested, -50.0);
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());  
        assertTrue("MockBankManager should have logged an event for receiving createAccount but instead it's: " + mgr.log.getLastLoggedEvent().toString(), mgr.log.containsString("Received a message to withdraw -50.0"));
        
        
        //Message withdrawal has been processed
        t.msgTransactionProcessed(-30.0);
        assertEquals("Requested should equal 0", t.transacted, -30.0);
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());
        assertEquals("Requested should equal 0: equals "+t.requested, t.requested, 0.0);
        assertEquals("Requested should equal 0", t.transacted, 0.0);
        assertTrue("Teller should have a MyCustomer", t.customer==null);
	}
	
	public void testNormativeDeposit() {
		
		//preconditions
        assertEquals("Requested should equal 0", t.requested, 0.0);
        assertEquals("Requested should equal 0", t.transacted, 0.0);
        assertEquals("AccountState should start at ", t.state, bankTellerState.working);
        assertEquals("teller should have an empty event log before his msgBill is called. Instead, the teller's event log read: " + t.log.toString(), 0, t.log.size());
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        
        
    	//Create Account
        t.msgMakeAccount(customer);
        assertFalse("Teller should have a Person", t.myPerson==null);
        assertFalse("Teller should have a MyCustomer", t.customer==null);
        assertFalse("Teller should have a MyCustomer", t.customer.BC==null);
        assertEquals("CustomerState should start at ", t.customer.state, accountState.none);
        
        
        //Scheduler
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());
        assertEquals("CustomerState should start at ", t.customer.state, accountState.requested);    
		assertTrue("MockBankManager should have logged an event for receiving createAccount but instead it's: " + mgr.log.getLastLoggedEvent().toString(), mgr.log.containsString("Received a message to create a new account"));
	
		//Account Created
		t.msgAccountCreated(3);
        assertEquals("CustomerState should be ", t.customer.state, accountState.justMade);  
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());  
        assertEquals("CustomerState should be  ", t.customer.state, accountState.existing);  
        
        //Message Withdrawal
        t.msgDeposit(customer, 3, 50);
        assertEquals("Requested should equal 50", t.requested, 50.0);
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());  
        assertTrue("MockBankManager should have logged an event for receiving createAccount but instead it's: " + mgr.log.getLastLoggedEvent().toString(), mgr.log.containsString("Received a message to withdraw 50.0"));
        
        
        //Message withdrawal has been processed
        t.msgTransactionProcessed(30.0);
        assertEquals("Requested should equal 0", t.transacted, 30.0);
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());
        assertEquals("Requested should equal 0: equals "+t.requested, t.requested, 0.0);
        assertEquals("Requested should equal 0", t.transacted, 0.0);
        assertTrue("Teller should have a MyCustomer", t.customer==null);
	}
	
	public void testGettingRobbed() {
		
		//preconditions
        assertEquals("Requested should equal 0", t.requested, 0.0);
        assertEquals("Requested should equal 0", t.transacted, 0.0);
        assertEquals("AccountState should start at ", t.state, bankTellerState.working);
        assertEquals("teller should have an empty event log before his msgBill is called. Instead, the teller's event log read: " + t.log.toString(), 0, t.log.size());
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        
        
    	//Create Account
        t.msgIAmRobbingYou(robber);
        assertFalse("Teller should have a Person", t.myPerson==null);
        assertTrue("Teller should not have a MyCustomer", t.customer==null);
        assertFalse("Teller should have a Bankrobber", t.robber==null);
        
        //Run Scheduler
        assertTrue("Scheduler should have returned True", t.pickAndExecuteAnAction());
        
        
	}
}
