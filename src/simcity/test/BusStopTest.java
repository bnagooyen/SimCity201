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
import junit.framework.TestCase;

public class BusStopTest extends TestCase{
	PersonAgent person; 
	MockBus bus;
	BusStopAgent busStop;
	
	List<PersonAgent> waitingPassengers=new ArrayList<PersonAgent>();
	
	
	
	
	public void setUp() throws Exception{
		person=new PersonAgent("person");
		bus=new MockBus("mockBus");
		busStop=new BusStopAgent();
		
		
		 
		
		
	}
	
	public void testOneBusStopTest(){
		
		
		
		
		
		
		
		
		
	}
		
	
	
	
}
