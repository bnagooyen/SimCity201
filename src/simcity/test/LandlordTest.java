package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockRepairMan;
import junit.framework.TestCase;

public class LandlordTest extends TestCase{
	PersonAgent person; 
	LandlordRole landlord;
	MockRepairMan repairman;
	MockBankManager bankmanager;
	
	public void setUp() throws Exception{
		super.setUp();
		person = new PersonAgent("Landlord");
		landlord = new LandlordRole(person);
		person.addRole(landlord);		
	}
	
	public void testAskForRent() {
		
	}
	
	public void testCallForRepair() {
		//checking preconditions
		assertEquals("Landlord should have no repairmen right now. It doesn't.", landlord.repairmen.size(), 0);
		assertEquals("Landlord should have no workers right now. It doesn't.", landlord.myWorkers.size(), 0);
		assertEquals("Landlord should have no tenants right now. It doesn't.", landlord.myTenants.size(), 0);
		
		landlord.addRepairMan(repairman);
		landlord.addTenant(person, 12); 
		
		//checking postconditions
		assertEquals("Landlord should have one repairmen right now. It doesn't.", landlord.repairmen.size(), 1);
		assertEquals("Landlord should have one tenants right now. It doesn't.", landlord.myTenants.size(), 1);


		
		
	}
}
