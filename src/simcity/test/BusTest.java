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

public class BusTest extends TestCase{
	PersonAgent person; 
	BusAgent bus;
	MockBusStop busStop;
	
	List<PersonAgent> waitingPassengers=new ArrayList<PersonAgent>();
	
	
	
	
	public void setUp() throws Exception{
		person=new PersonAgent("person");
		bus=new BusAgent();
		busStop=new MockBusStop("destination");
		
		
		 
		
		
	}
	
	public void testBusOneTest(){
		
		waitingPassengers.add(person);
		
		//Pre-conditions need more.  
		assertEquals("Bus should have no passengers in the bus, but it does.", bus.passengers.size(),0);
		assertEquals("Bus should initially have no bus stops but it does", bus.busStops.size(), 0 );
		
		//Bus receives message of passengers
		bus.msgHereArePassengers(waitingPassengers);
		assertEquals("Bus should have one passenger in his passenger list, but it does not", bus.passengers.size(),1);
		
		
		bus.msgGettingOn(person, "destination");
		assertTrue("Bus's passenger onBus should be true, but it is not", bus.passengers.get(0).onBus);
		assertTrue("Bus's state should be ready to leave because everyone is on Bus", bus.state==busState.readyToGo);
		assertTrue("Bus's pickAndExecuteAnAction should be active because it is ready to go", bus.pickAndExecuteAnAction());
		assertTrue("Bus's state should now be travelling now that it is travelling",bus.state==busState.travelling);
		
		bus.msgAtStop("destination");
		bus.busStops.put("destination", busStop);
		assertTrue("Bus's state should now be at arrived now that it is no longer travelling",bus.state==busState.arrived);
		assertTrue("Bus's pickAndExecuteAnAction should be active because it is at a stop", bus.pickAndExecuteAnAction());
		assertTrue("Bus's state should now be atStop because it is checking for passengers at the stop",bus.state==busState.atStop );
	
		assertEquals("Bus Stop should have received a message from bus and have a log size of 1", busStop.log.size(),1);
		assertEquals("Because passenger has gotten off the bus, there should be no passengers in the waiting list", bus.passengers.size(),0);
		
		
		
		}
	
	
		
	
	
	
}
