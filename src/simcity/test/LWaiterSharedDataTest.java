package simcity.test;

import simcity.PersonAgent;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCashierRole.OrderState;
import simcity.LRestaurant.LWaiterRole.WaiterState;
import simcity.LRestaurant.LWaiterSharedDataRole;
import simcity.Transportation.DeliveryTruckAgent.truckState;
import simcity.test.mock.EventLog;
//import simcity.LRestaurant.test.mock.MockMarket;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;
import simcity.test.mock.MockLCustomer;
import simcity.test.mock.MockLWaiter;
import junit.framework.*;


public class LWaiterSharedDataTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent p;
	LCashierRole cashier;
	LWaiterSharedDataRole sWaiter;
	MockLCustomer customer;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		p = new PersonAgent("person");
		cashier = new LCashierRole(p);		
		customer = new MockLCustomer("mockcustomer");	
		sWaiter = new LWaiterSharedDataRole(p);
		
	}	
	
	public void testWaiter(){
		
		//preconditions
		assertEquals("Truck state is available", sWaiter.waiterState, WaiterState.working);
		assertEquals("");
		
		
		
		
	}
	
	
}
