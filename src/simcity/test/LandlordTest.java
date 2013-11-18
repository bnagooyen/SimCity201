package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.housing.LandlordRole;
import simcity.interfaces.MarketCashier;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockRepairMan;
import simcity.Market.MarketCashierRole.orderState;
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
