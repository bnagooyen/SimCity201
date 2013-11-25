package simcity.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.housing.LandlordRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.mockrole.MockRoleCook;
import simcity.mockrole.MockRoleMarketCashier;
import simcity.mockrole.MockRoleMarketManager;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockCook;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockMarketManager;
import simcity.test.mock.MockPerson;
import simcity.test.mock.MockRepairMan;
import simcity.Market.MarketCashierRole.orderState;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusAgent.busState;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.Transportation.CarAgent.carState;
import simcity.Transportation.DeliveryTruckAgent.truckState;
import simcity.Transportation.DeliveryTruckAgent;
import junit.framework.TestCase;

public class DeliveryTruckTest extends TestCase{
	
//	MockPerson person; 
	DeliveryTruckAgent truck;
	MockRoleMarketManager manager;
	MockRoleMarketCashier cashier;
	List<MFoodOrder>supply = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	MockRoleCook cook;
	PersonAgent p;
	

	public void setUp() throws Exception{
		
//		person=new MockPerson("person");
		p = new PersonAgent("default");
		truck = new DeliveryTruckAgent(manager);
		manager = new MockRoleMarketManager("mockManager",p);
		cashier = new MockRoleMarketCashier("mockMarketCashier", p);
		cook = new MockRoleCook("mockCook", p);

	}
	
	

	public void testDeliveryTruckJob(){
		truck.manager = manager;
		
		//preconditions
		assertEquals("Truck state is available", truck.state, truckState.available);
		assertEquals("No Destination set", truck.destination, null);
		assertEquals("Supply in truck is nonexistant", truck.supply, null);
		
		//Ordered by manager to go to restaurant destination
		supply.add(new MFoodOrder("Chicken", 2));
		truck.msgGoToDestination(cashier, supply, "r1", 20.0, cook);
		assertEquals("Truck state is receivedLocation", truck.state, truckState.receivedLocation);
		assertEquals("Destination is set", truck.destination, "r1");
		
		//Going to the Location
		truck.pickAndExecuteAnAction();
		assertTrue("Truck Logged: " + truck.log.getLastLoggedEvent().toString(), truck.log.containsString("Going to destination"));

		//Arrived at the location
		truck.msgAtDestination();
		assertEquals("Truck state is arrived", truck.state, truckState.arrived);
		
		//Handing over supply
		truck.pickAndExecuteAnAction();
		assertTrue("MockManager Logged: " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Received msgBackFromDelivery"));
		assertTrue("Truck Logged: " + truck.log.getLastLoggedEvent().toString(), truck.log.containsString("Arrived at destination"));
		
		
	}




}
