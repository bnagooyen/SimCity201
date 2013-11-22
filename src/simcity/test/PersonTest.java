package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.housing.LandlordRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.mockrole.MockRole;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockPerson;
import simcity.test.mock.MockRepairMan;
import simcity.Market.MarketCashierRole.orderState;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusAgent.busState;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.Transportation.CarAgent.carState;
import junit.framework.TestCase;

public class PersonTest extends TestCase{
	PersonAgent person;
	MockRole role;
	MockBus bus;
	MockBusStop busStop;
	
	
	

	
	public void setUp() throws Exception{
		role=new MockRole("mockrole", person);
		person=new PersonAgent("person", role);

	}
	
	

	public void testCarTestOne(){
		
		

	}




}
