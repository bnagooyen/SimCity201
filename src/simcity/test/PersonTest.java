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
import simcity.PersonAgent.EnergyState;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.TransitState;
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
		person=new PersonAgent("person");
		bus=new MockBus("bus");
		busStop=new MockBusStop("mock busStop");
		person.SetJob(role);
		

	}
	
	

	public void testPersonBusTransit(){
		
		//preconditions
		assertTrue("In order to use bus, person must have no car, but they do. ", person.myCar==null);
		assertEquals("Bus Stop should not have received message from person yet that it is at the station, but it had", busStop.log.size(),0);
		assertEquals("Bus should not have received any messages from Bus, but it has", bus.log.size(),0);
		
		
		person.msgTimeUpdate(7);
		person.msgTimeUpdate(10);
		person.transitState=TransitState.walkingToBus;
		
		
		assertTrue("Person's pickAndExecuteAnAction should be active because he is awake and in transit", person.pickAndExecuteAnAction());
		assertTrue("Person should have no car and thus their myCar should be null", person.myCar==null);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		assertTrue("Person's transitState should be walking to bus", person.transitState==TransitState.walkingToBus);
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		
		person.pickAndExecuteAnAction();
		assertTrue("Person's transit State should be set to tell the bus", person.transitState==TransitState.atBusStop);
		
		person.setBus(bus);
		person.msgBusIsHere(bus);
		
		
		assertTrue("Person's transitState should be get on bus, but it isnt", person.transitState==TransitState.getOnBus);
		person.pickAndExecuteAnAction();
		assertEquals("Bus should have a message in its log after person gets on, but it does not", bus.log.size(), 1);
		
		person.msgAtStop("destination");
		
		assertTrue("Person's transitState should be get off bus but it is not", person.transitState==TransitState.getOffBus);
		person.pickAndExecuteAnAction();
		assertTrue("Person's transitState should be walkingToDestination", person.transitState==TransitState.walkingtoDestination);
		
		
		
		
		
		

	}




}
