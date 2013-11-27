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
import junit.framework.TestCase;

public class BusStopTest extends TestCase{
	PersonAgent person; 
	PersonAgent person2;
	MockBus bus;
	MockBus bus2;
	BusStopAgent busStop;
	MockRole mockrole;
	MockRole mockrole2;

	List<PersonAgent> waitingPassengers=new ArrayList<PersonAgent>();




	public void setUp() throws Exception{
		person=new PersonAgent("person");
		person2=new PersonAgent("person2");
		mockrole=new MockRole("mockrole");
		mockrole2=new MockRole("mockrole");
		
		bus=new MockBus("mockBus");
		bus2=new MockBus("mockBus2");
		
		busStop=new BusStopAgent();





	}

	public void testOneBusStopTest(){

		//preconditions
		assertEquals("Bus Stop should have no buses in it's list if bus hasnt landed at stop", busStop.buses.size(),0);
		assertEquals("Bus Stop should have no waiting customers if bus has not received message, and it does",busStop.waitingPassengers.size(),0);
		assertEquals("Bus should have no message from Bus Stop with passengers before any messages are sent", bus.log.size(),0);
		//receives messages from passenger that the passenger is waiting
		busStop.msgWaitingForBus(person);
		assertEquals("Bus Stop should now have one person added to its list of waiting Passengers, but it does not",busStop.waitingPassengers.size(),1 );
		
		//receives message from bus that it has arrived at this particular spot
		busStop.msgAnyPassengers(bus);
		assertEquals("Bus Stop should now have one perosn added to its list of buses, but it does not", busStop.buses.size(),1);
		assertTrue("Bus Stop's pickAndExecuteAnAction should be active because there is a bus that it can give the passengers to", busStop.pickAndExecuteAnAction());
		assertEquals("Bus should have received message from Bus Stop with passengers, but it did not", bus.log.size(),1);
		
		assertEquals("Bus Stop should clear all of its passengers once it has already given them to the bus and should have an empty list", busStop.waitingPassengers.size(),0);
		assertEquals("Bus Stop should not have bus in a list because bus has left the stop, but it does", busStop.buses.size(),0);


	}
	
	public void testTwoBusStop(){
		
		//preconditions
				assertEquals("Bus Stop should have no buses in it's list if bus hasnt landed at stop", busStop.buses.size(),0);
				assertEquals("Bus Stop should have no waiting customers if bus has not received message, and it does",busStop.waitingPassengers.size(),0);
				assertEquals("Bus should have no message from Bus Stop with passengers before any messages are sent", bus.log.size(),0);
				//receives messages from passenger that the passenger is waiting
				busStop.msgWaitingForBus(person);
				busStop.msgWaitingForBus(person2);
				
				assertEquals("Bus Stop should now have two people added to its list of waiting Passengers, but it does not",busStop.waitingPassengers.size(),2 );
				
				//receives message from bus that it has arrived at this particular spot
				busStop.msgAnyPassengers(bus);
				busStop.msgAnyPassengers(bus2);
				
				assertEquals("Bus Stop should now have two buses added to its list of buses, but it does not", busStop.buses.size(),2);
				assertTrue("Bus Stop's pickAndExecuteAnAction should be active because there is a bus that it can give the passengers to", busStop.pickAndExecuteAnAction());
				assertEquals("Bus should have received message from Bus Stop with passengers, but it did not", bus.log.size(),1);
				
				assertEquals("Bus Stop should clear all of its passengers once it has already given them to the bus and should have an empty list", busStop.waitingPassengers.size(),0);
				assertEquals("Bus Stop should have 1 bus left at the stop because one has left, but it does", busStop.buses.size(),1);
		
		
	}




}
