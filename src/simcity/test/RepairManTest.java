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
		person = new PersonAgent("RepairMan");
		repairman = new RepairManRole(person);
		person.addRole(repairman);
		
		landlord = new MockLandlord("mockLandlord"); 
	}
	
	public void testMakeRepair() {
		//checking preconditions
		assertEquals("Repairman should have no jobs right now. It doesn't.", repairman.jobs.size(), 0);
		
		//messaging repairman to perform a job
		repairman.NeedRepair("B2", landlord);
		
		//checking postconditions
		assertEquals("Repairman should have one job right now. It doesn't.", repairman.jobs.size(), 1);
		assertEquals("MockLandlord doesn't have an empty log. Instead, it has " + landlord.log.toString(), 0, landlord.log.size());

		//calling the scheduler
		assertTrue("Repairman's scheduler should have returned true, but didn't.", repairman.pickAndExecuteAnAction());
		assertTrue("MockLandlord should have logged an event for receiving jobDone but instead it's: " + landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received a bill from the repairman. Bill = 30.0"));
		assertFalse("Repairman's scheduler should have returned false now, since it has nothing to do. It didn't.", repairman.pickAndExecuteAnAction());
		assertEquals("Repairman should have no jobs again. It doesn't.", repairman.jobs.size(), 0); 

	}
	

}
