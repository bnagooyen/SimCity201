package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
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
import simcity.Transportation.CarAgent;
import simcity.Transportation.CarAgent.carState;
import junit.framework.TestCase;

public class CarTest extends TestCase{
	MockPerson person; 
	CarAgent car;
	

	




	public void setUp() throws Exception{
		person=new MockPerson("person");
		car=new CarAgent();

	}
	
	

	public void testCarTestOne(){
		
		assertTrue("Car should not have any driver in it before it receives message, but it does", car.driver==null);
		assertTrue("Car should not have any destination to it before it receives message, but it does", car.destination==null);
		
		//car receives message from driver to go to a destination
		car.msgGoToDestination("destination", person);
		assertTrue("Car should have a destination now that it has received message", car.destination!=null);
		assertTrue("Car should have a destination now that it has received message", car.driver!=null);
		assertTrue("Car's state should be receivedLocation, but it is not", car.state==carState.receivedLocation);
		assertTrue("Car's pickAndExecuteAnAction should be active because of the scheduler condition", car.pickAndExecuteAnAction());
		
		//Car would receive message from Gui that it has arrived at the destination 
		car.msgAtDestination();
		assertTrue("Car's scheduler should be activated as the state arrived leads to scheduler activity", car.pickAndExecuteAnAction());
		assertTrue("State of the car should be parked, but it is not", car.state==carState.parked);
		assertEquals("Driver should have received message from car after arrival, but it did not", person.log.size(),1);
		assertTrue("Driver Logged: " + person.log.getLastLoggedEvent().toString(), person.log.containsString("Received message from car that we have arrived."));

	}




}
