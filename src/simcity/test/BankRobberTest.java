package simcity.test;


import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.mockrole.MockRole;
import simcity.Bank.BankRobberRole;
import simcity.Bank.BankRobberRole.bankRobberState;
import simcity.test.mock.MockBankCustomer;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankTeller;
import junit.framework.TestCase;

public class BankRobberTest extends TestCase{

	PersonAgent p;
	BankRobberRole robber;
	MockBankTeller teller;
	MockBankManager mgr;
	MockRole mockrole;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("BankRobber");
		mockrole=new MockRole("mockrole");
		robber = new BankRobberRole();
		robber.myPerson = p;
		p.addRole(robber);
		mgr = new MockBankManager("MockBankManager");
		teller = new MockBankTeller("mockTeller");
		robber.manager=mgr;
	}
	
	public void testUnsuccessfulRobbery() {
		
		//preconditions
        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
        assertEquals("Teller should start as null",robber.teller,null);
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + teller.log.toString(), 0, teller.log.size());
        
        //Start Schedules
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        
        //Go To Teller
        robber.msgGoToTeller(teller);
        assertTrue("Robber should now have Teller",robber.teller!=null);
        
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
        
        
        robber.msgIRefuseToPay();
        assertEquals("AccountState should be at ", robber.state, bankRobberState.unsucessful);
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for getting shot, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Got Shot"));
        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertFalse("Should deactivate Role", robber.isActive);
        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
        
	}
	
	
	public void testShootingRobbery() {
		
		//preconditions
        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
        assertEquals("Teller should start as null",robber.teller,null);
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        
        //Start Schedules
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        
        //Go To Teller
        robber.msgGoToTeller(teller);
        assertTrue("Robber should now have Teller",robber.teller!=null);
        
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
        
        
        robber.msgIShotYou();
        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertFalse("Should deactivate Role", robber.isActive);
        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
        
	}
	
	public void testSuccessfulRobbery() {
		
		//preconditions
        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
        assertEquals("Teller should start as null",robber.teller,null);
        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
                + mgr.log.toString(), 0, mgr.log.size());
        
        //Start Schedules
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        
        //Go To Teller
        robber.msgGoToTeller(teller);
        assertTrue("Robber should now have Teller",robber.teller!=null);
        
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
        
        
        robber.msgHereIsMoney(10000);
        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
        
        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
        assertFalse("Should deactivate Role", robber.isActive);
        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
        
	}
	
        
}