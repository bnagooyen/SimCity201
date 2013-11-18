package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
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
		landlord.addRepairMan(repairman);
		
		
	}
	
	public void testAskForRent() {
		
	}
	
	public void testCallForRepair() {
		
	}
}
