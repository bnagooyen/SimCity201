package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
import simcity.housing.RepairManRole;
import simcity.interfaces.RepairMan;
import simcity.test.mock.MockLandlord;
import simcity.test.mock.MockRepairMan;
import junit.framework.TestCase;

public class RepairManTest extends TestCase{
	PersonAgent person; 
	RepairManRole repairman;
	MockLandlord landlord;
	
	public void setUp() throws Exception{
		super.setUp();
		person = new PersonAgent("RepairMan", repairman);
		repairman = new RepairManRole(person);
		person.addRole(repairman);
		
		landlord = new MockLandlord("mockLandlord"); 
	}
	
	public void testMakeRepair() {
		//checking preconditions
		assertEquals("Repairman should have no jobs right now. It doesn't.", repairman.jobs.size(), 0);
		assertEquals("MockLandlord should have no logs right now. It doesn't", landlord.log.size(), 0); 
		
		//messaging repairman to perform a job
		repairman.NeedRepair("B2", landlord);
		
		//checking postconditions
		assertEquals("Repairman should have one job right now. It doesn't.", repairman.jobs.size(), 1);
		assertEquals("MockLandlord doesn't have an empty log. Instead, it has " + landlord.log.toString(), 0, landlord.log.size());
		
		//calling the scheduler
		assertTrue("Repairman's scheduler should have returned true, but didn't.", repairman.pickAndExecuteAnAction());
		assertTrue("MockLandlord should have logged an event for receiving jobDone but instead it's: " + landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received a bill from the repairman. Bill = 30.0"));
		assertEquals("MockLandlord should have one log right now. It doesn't", landlord.log.size(), 1); 
		assertFalse("Repairman's scheduler should have returned false now, since it has nothing to do. It didn't.", repairman.pickAndExecuteAnAction());
		assertEquals("Repairman should have no jobs again. It doesn't.", repairman.jobs.size(), 0); 

	}
	
	public void testTwoRepairs() {
		//checking preconditions
		assertEquals("Repairman should have no jobs right now. It doesn't.", repairman.jobs.size(), 0);
		assertEquals("MockLandlord should have no logs right now. It doesn't", landlord.log.size(), 0); 
		
		//messaging repairman to perform a job
		repairman.NeedRepair("B2", landlord);
		repairman.NeedRepair("C1", landlord);
		
		//checking postconditions
		assertEquals("Repairman should have one job right now. It doesn't.", repairman.jobs.size(), 2);
		assertEquals("MockLandlord doesn't have an empty log. Instead, it has " + landlord.log.toString(), 0, landlord.log.size());

		//calling the scheduler
		assertTrue("Repairman's scheduler should have returned true, but didn't.", repairman.pickAndExecuteAnAction());
		
		//checking preconditions and post conditions
		assertTrue("MockLandlord should have logged an event for receiving jobDone but instead it's: " + landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received a bill from the repairman. Bill = 30.0"));
		assertEquals("MockLandlord should have one log right now. It doesn't", landlord.log.size(), 1); 
		assertEquals("Repairman should still have one job. It doesn't.", repairman.jobs.size(), 1);
		
		//calling the scheduler
		assertTrue("Repairman's scheduler should have returned true, but didn't.", repairman.pickAndExecuteAnAction());
		
		//checking postconditions
		assertTrue("MockLandlord should have logged an event for receiving jobDone but instead it's: " + landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received a bill from the repairman. Bill = 30.0"));
		assertEquals("MockLandlord should have one log right now. It doesn't", landlord.log.size(), 2); 
		assertFalse("Repairman's scheduler should have returned false now, since it has nothing to do. It didn't.", repairman.pickAndExecuteAnAction());
		assertEquals("Repairman should have no jobs again. It doesn't.", repairman.jobs.size(), 0);
		
		
	}
	

}
