package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
import simcity.Market.MarketCashierRole;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockPerson;
import simcity.test.mock.MockRepairMan;
import junit.framework.TestCase;

public class LandlordTest extends TestCase{
	PersonAgent person;
	PersonAgent resident; 
	PersonAgent resident2;
	MockPerson resident3;
	LandlordRole landlord;
	MockRepairMan repairman;
	MockRepairMan repairman2;
	MockBankManager bankmanager;
	MarketCashierRole mc; 
	
	public void setUp() throws Exception{
		super.setUp();
		person = new PersonAgent("Landlord");
		landlord = new LandlordRole();
		landlord.myPerson = person; 
		person.addRole(landlord);
		
		
		resident = new PersonAgent("Resident"); 
		resident2 = new PersonAgent("Resident2"); 
		resident3= new MockPerson("Resident3");
		repairman = new MockRepairMan("MockRepairman");
		repairman2 = new MockRepairMan("MockRepairman2");
		
		bankmanager = new MockBankManager("MockBankManager");
		
	}
	
	public void testAskForRent() {
		assertEquals("Landlord s8hould have no tenants right now. It doesn't.", landlord.myTenants.size(), 0);
		assertEquals("MockBankManager should have no logs right now. It doesn't", bankmanager.log.size(), 0); 
		
		landlord.addUnit("B1"); 
		landlord.addTenant(resident, 12);
		landlord.addBankManager(bankmanager); 
				
		assertEquals("Landlord should have one tenants right now. It doesn't.", landlord.myTenants.size(), 1);
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		assertEquals("MockBankManager should have no logs right now. It doesn't", bankmanager.log.size(), 0); 

		landlord.msgTimeUpdate(0);
		
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertTrue("MockBankManager should have logged an event for receiving a request but instead it's: " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("Received from landlord for account 12"));
		assertEquals("MockBankManager should have one log right now. It doesn't", bankmanager.log.size(), 1); 
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
	
		landlord.msgHereIsARentPayment(12, 25);
		landlord.msgTimeUpdate(20);
		
		assertEquals("MockBankManager should still have one log right now. It doesn't", bankmanager.log.size(), 1); 
		assertEquals("Landlord should have more money now, but it doesn't.", landlord.revenue, 1025.0);
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
	
		assertEquals("Landlord should have more money now, but it doesn't.", landlord.revenue, 717.5);

	}
	
	public void testAskRentFromTenant(){
		assertEquals("Landlord should have no tenants right now. It doesn't.", landlord.myTenants.size(), 0);
		assertEquals("MockPerson should have no logs right now. It doesn't", resident3.log.size(), 0); 
		
		landlord.addUnit("B1"); 
		landlord.addTenant(resident3, 0);
		
		assertEquals("Landlord should have one tenants right now. It doesn't.", landlord.myTenants.size(), 1);
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		assertEquals("MockBankManager should have no logs right now. It doesn't", resident3.log.size(), 0); 
		
		landlord.msgTimeUpdate(0);
		
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertTrue("MockPerson should have logged an event for receiving a request but instead it's: " + resident3.log.getLastLoggedEvent().toString(), resident3.log.containsString("Received message from Landlord"));
		assertEquals("MockPerson should have one log right now. It doesn't", resident3.log.size(), 1); 
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		
		landlord.msgHereIsARentPayment(0, 25);
		landlord.msgTimeUpdate(20);
		
		assertEquals("MockPerson should still have one log right now. It doesn't", resident3.log.size(), 1); 
		assertEquals("Landlord should have more money now, but it doesn't.", landlord.revenue, 1025.0);
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		
		assertEquals("Landlord should have more money now, but it doesn't.", landlord.revenue, 717.5);
	}
	
	public void testCallForRepair() {
		//checking preconditions
		assertEquals("Landlord should have no repairmen right now. It doesn't.", landlord.repairmen.size(), 0);
		assertEquals("Landlord should have no workers right now. It doesn't.", landlord.myWorkers.size(), 0);
		assertEquals("Landlord should have no tenants right now. It doesn't.", landlord.myTenants.size(), 0);
		assertEquals("MockRepairMan should have no logs right now. It doesn't", repairman.log.size(), 0); 
		
		landlord.addRepairMan(repairman);
		landlord.addUnit("B2"); 
		landlord.addTenant(resident, 12); 
		
		//checking postconditions
		assertEquals("Landlord should have one repairmen right now. It doesn't.", landlord.repairmen.size(), 1);
		assertEquals("Landlord should have one tenants right now. It doesn't.", landlord.myTenants.size(), 1);
		assertEquals("MockRepairMan should have no logs right now. It doesn't", repairman.log.size(), 0); 
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		
		//changing hour to 10 so landlord calls repairman 
		landlord.msgTimeUpdate(10);
		
		//calling the scheduler and checking postconditions
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertTrue("MockRepairMan should have logged an event for receiving a job but instead it's: " + repairman.log.getLastLoggedEvent().toString(), repairman.log.containsString("Received a job for building B2"));
		assertEquals("MockRepairMan should have one log right now. It doesn't", repairman.log.size(), 1); 
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		assertEquals("Landlord should have one worker right now. It doesn't.", landlord.myWorkers.size(), 1);

		
		//giving landlord money and sending message to pay repairman
		landlord.revenue = 100;
		landlord.msgJobDone("B2", 30);
		
		//calling the scheduler and checking postconditions
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		assertTrue("MockRepairMan should have logged an event for receiving payment but instead it's: " + repairman.log.getLastLoggedEvent().toString(), repairman.log.containsString("Received a payment for job for 30.0"));
		assertEquals("MockRepairMan should have one log right now. It doesn't", repairman.log.size(), 2); 

		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());

		assertEquals("Landlord should have less money now, but it doesn't.", landlord.revenue, 70.0);
		assertEquals("Landlord should have no workers right now. It doesn't.", landlord.myWorkers.size(), 0);


	}
	
	public void testCallForTwoRepairs() {
		//checking preconditions
		assertEquals("Landlord should have no repairmen right now. It doesn't.", landlord.repairmen.size(), 0);
		assertEquals("Landlord should have no workers right now. It doesn't.", landlord.myWorkers.size(), 0);
		assertEquals("Landlord should have no tenants right now. It doesn't.", landlord.myTenants.size(), 0);
		assertEquals("MockRepairMan should have no logs right now. It doesn't", repairman.log.size(), 0); 
				
		landlord.addRepairMan(repairman);
		landlord.addRepairMan(repairman2); 
		landlord.addUnit("B2"); 
		landlord.addUnit("C3"); 
		landlord.addTenant(resident, 12);
		landlord.addTenant(resident2, 3); 
				
		//checking postconditions
		assertEquals("Landlord should have two repairmen right now. It doesn't.", landlord.repairmen.size(), 2);
		assertEquals("Landlord should have two tenants right now. It doesn't.", landlord.myTenants.size(), 2);
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		assertEquals("MockRepairMan should have no logs right now. It doesn't", repairman.log.size(), 0); 
		
		//changing hour to 10 so landlord calls repairman 
		landlord.msgTimeUpdate(10);
		
		//calling the scheduler and checking postconditions
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		if (landlord.myWorkers.get(0).myWorker == repairman) {
			assertTrue("MockRepairMan should have logged an event for receiving a job but instead it's: " + repairman.log.getLastLoggedEvent().toString(), repairman.log.containsString("Received a job for building B2"));
			assertEquals("MockRepairMan should have two logs right now. It doesn't", repairman.log.size(), 2); 
		}
		else {
			assertTrue("MockRepairMan2 should have logged an event for receiving a job but instead it's: " + repairman2.log.getLastLoggedEvent().toString(), repairman2.log.containsString("Received a job for building C3"));
			assertEquals("MockRepairMan2 should have two logs right now. It doesn't", repairman2.log.size(), 2); 
		}
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		assertEquals("Landlord should have 2 workers right now. It doesn't.", landlord.myWorkers.size(), 2);
		assertEquals("Landlord should have called the same worker for both residents, but he didn't.", landlord.myWorkers.get(0).myWorker, landlord.myWorkers.get(1).myWorker);

		
		//giving landlord money and sending message to pay repairman
		landlord.revenue = 100;
		landlord.msgJobDone("B2", 30);

		//calling the scheduler and checking postconditions
		assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
		if (landlord.myWorkers.get(0).myWorker == repairman) {
			assertTrue("MockRepairMan should have logged an event for receiving payment but instead it's: " + repairman.log.getLastLoggedEvent().toString(), repairman.log.containsString("Received a payment for job for 30.0"));
			assertEquals("MockRepairMan should have three logs right now. It doesn't", repairman.log.size(), 3); 

		}
		else {
			assertTrue("MockRepairMan should have logged an event for receiving payment but instead it's: " + repairman2.log.getLastLoggedEvent().toString(), repairman2.log.containsString("Received a payment for job for 30.0"));
			assertEquals("MockRepairMan2 should have three logs right now. It doesn't", repairman2.log.size(), 3); 
		}
	
		assertEquals("Landlord should have less money now, but it doesn't.", landlord.revenue, 70.0);
		assertEquals("Landlord should have one worker right now. It doesn't.", landlord.myWorkers.size(), 1);
		landlord.msgJobDone("C3", 30);
		
		//calling scheduler for second job

		if (landlord.myWorkers.get(0).myWorker == repairman) {
			assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
			assertTrue("MockRepairMan should have logged an event for receiving payment but instead it's: " + repairman.log.getLastLoggedEvent().toString(), repairman.log.containsString("Received a payment for job for 30.0"));
			assertEquals("MockRepairMan should have four logs right now. It doesn't", repairman.log.size(), 4); 
		}
		else {
			assertTrue("Landlord's scheduler should have returned true now, since it has to do something. It didn't.", landlord.pickAndExecuteAnAction());
			assertTrue("MockRepairMan should have logged an event for receiving payment but instead it's: " + repairman2.log.getLastLoggedEvent().toString(), repairman2.log.containsString("Received a payment for job for 30.0"));
			assertEquals("MockRepairMan2 should have four logs right now. It doesn't", repairman2.log.size(), 4); 
		}
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
	
		assertEquals("Landlord should have less money now, but it doesn't.", landlord.revenue, 40.0);
		assertEquals("Landlord should have no workers right now. It doesn't.", landlord.myWorkers.size(), 0);
		assertFalse("Landlord's scheduler should have returned false now, since it has nothing to do. It didn't.", landlord.pickAndExecuteAnAction());
		
	}
	
	

}
