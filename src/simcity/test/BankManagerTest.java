package simcity.test;


import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankManagerRole.MyLoanOfficer;
import simcity.Bank.BankManagerRole.MyTeller;
import simcity.Bank.BankManagerRole.MyTeller.MyTellerState;
import simcity.Bank.BankManagerRole.MyTeller.MyTellerState;
import simcity.test.mock.MockBankCustomer;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankLoanOfficer;
import simcity.test.mock.MockBankTeller;
import junit.framework.TestCase;

public class BankManagerTest extends TestCase{

	PersonAgent p;
	BankManagerRole manager;
	MockBankTeller teller;
	MockBankLoanOfficer loanOfficer;
	MockBankManager mgr;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("BankManager");
		manager = new BankManagerRole(p);
		p.addRole(manager);
		teller = new MockBankTeller("mockTeller");
		loanOfficer = new MockBankLoanOfficer("mockOfficer");
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
	}
	
	public void testCreateAccount() {
		
		 // preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
		assertEquals("Manager should have 0 accounts", manager.getAccounts().size(), 0);
		assertEquals("BankManager should have 0 logs", manager.log.size(), 0);
		
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("teller has 1 log", manager.log.size(), 1);
		assertTrue("Teller in position", manager.log.containsString("Loan officer added"));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("teller has 2 logs", manager.log.size(), 2);
		assertTrue("Teller in position", manager.log.containsString("Teller added"));
		
		
		
		//teller has a request to create account
		manager.msgCreateAccount("BankTeller");
		
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("should have created an account", manager.getAccounts().size(),1);
		assertEquals("mockteller should have received a message", teller.log.size(),1);
		assertTrue("account created", teller.log.containsString("Account created"));
		
		assertEquals("should have 0 balance", manager.getAccounts().get(1).getBalance(), 0.0);
		assertEquals("should have 9 loan", manager.getAccounts().get(1).getLoan(), 0.0);
		
	}
	
	public void test
	
//	public void testUnsuccessfulRobbery() {
//		
//		//preconditions
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
//        assertEquals("Teller should start as null",robber.teller,null);
//        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + mgr.log.toString(), 0, mgr.log.size());
//        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + teller.log.toString(), 0, teller.log.size());
//        
//        //Start Schedules
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
//        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        
//        //Go To Teller
//        robber.msgGoToTeller(teller);
//        assertTrue("Robber should now have Teller",robber.teller!=null);
//        
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
//        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
//        
//        
//        robber.msgIRefuseToPay();
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.unsucessful);
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertTrue("MockBankTeller should have logged an event for getting shot, but instead it's: "
//        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Got Shot"));
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertFalse("Should deactivate Role", robber.isActive);
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
//        
//	}
//	
//	public void testShootingRobbery() {
//		
//		//preconditions
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
//        assertEquals("Teller should start as null",robber.teller,null);
//        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + mgr.log.toString(), 0, mgr.log.size());
//        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + mgr.log.toString(), 0, mgr.log.size());
//        
//        //Start Schedules
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
//        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        
//        //Go To Teller
//        robber.msgGoToTeller(teller);
//        assertTrue("Robber should now have Teller",robber.teller!=null);
//        
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
//        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
//        
//        
//        robber.msgIShotYou();
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertFalse("Should deactivate Role", robber.isActive);
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
//        
//	}
//	
//	public void testSuccessfulRobbery() {
//		
//		//preconditions
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.arrived);
//        assertEquals("Teller should start as null",robber.teller,null);
//        assertEquals("MockBankManager should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + mgr.log.toString(), 0, mgr.log.size());
//        assertEquals("MockBankTeller should have an empty event log. Instead, the MockBankManager's event log reads: "
//                + mgr.log.toString(), 0, mgr.log.size());
//        
//        //Start Schedules
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertEquals("AccountState should start at ", robber.state, bankRobberState.waiting);
//        assertFalse("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        
//        //Go To Teller
//        robber.msgGoToTeller(teller);
//        assertTrue("Robber should now have Teller",robber.teller!=null);
//        
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertTrue("MockBankTeller should have logged an event for getting robbed, but instead it's: "
//        + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Being Robbed"));
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.inProgress);
//        
//        
//        robber.msgHereIsMoney(10000);
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.done);
//        
//        assertTrue("Scheduler should have returned True", robber.pickAndExecuteAnAction());
//        assertFalse("Should deactivate Role", robber.isActive);
//        assertEquals("AccountState should be at ", robber.state, bankRobberState.arrived);
//        
//	}
//        
}