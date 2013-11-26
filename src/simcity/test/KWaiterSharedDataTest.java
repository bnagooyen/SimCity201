package simcity.test;

import simcity.PersonAgent;
import simcity.KRestaurant.KWaiterRole.WaiterState;
import simcity.KRestaurant.KWaiterRole.customerstate;
import simcity.KRestaurant.KWaiterSharedDataRole;
import simcity.KRestaurant.ProducerConsumerMonitor;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.test.mock.EventLog;
import simcity.test.mock.MockKRestaurantCustomer;
//import simcity.LRestaurant.test.mock.MockMarket;
import simcity.interfaces.KCustomer;
import simcity.test.mock.MockLCustomer;
import junit.framework.*;


public class KWaiterSharedDataTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent p;
	KWaiterSharedDataRole sWaiter;
	MockKRestaurantCustomer customer;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		p = new PersonAgent("person");
		customer = new MockKRestaurantCustomer("mockcustomer");	
		sWaiter = new KWaiterSharedDataRole();
		sWaiter.myPerson = p;
		ProducerConsumerMonitor m = new ProducerConsumerMonitor();
		sWaiter.setMonitor(m);
	}	
	
	public void testWaiter(){
		
		//preconditions
		assertEquals("sWatier's state should be working but isn't", sWaiter.mystate, WaiterState.working);
		assertTrue("sWaiter's log should be empty but isn't", sWaiter.log.size() == 0);
		assertTrue("waiter''s customer should be size 0 but isn't", sWaiter.customers.size() == 0);		
		assertTrue("customer's log should be empty but isn't", customer.log.size() == 0);		
		
		//  give waiter customer
		sWaiter.msgSeatCustomer(customer, 0);
		assertTrue("waiter''s customer should be size 1 but isn't", sWaiter.customers.size() == 1);		
		assertTrue("waiter's log should say it's seating a customer but doesnt'", sWaiter.log.containsString("got msg to seat customer"));		
		assertTrue("customer's log should be empty but isn't", customer.log.size() == 0);		

		sWaiter.pickAndExecuteAnAction();
		assertTrue("waiter's log should say it's seating customer but doesn't", sWaiter.log.containsString("seating customer"));		
		assertTrue("customer's log should say being seated but doesn't", customer.log.containsString("told to sit at table"));		

		// customer gives waiter order
		sWaiter.msgHereIsChoice(customer, "Steak");
		assertTrue("waiter's log should say it's customer is ready to order but doesn't", sWaiter.log.containsString("customer is ready to order"));		

		sWaiter.pickAndExecuteAnAction();
		assertTrue("waiter's log should say putting order on stand but doesn't", sWaiter.log.containsString("putting order on rotating stand"));		

		// postconditions
		assertFalse("waiter's scheduler should return false", sWaiter.pickAndExecuteAnAction());
		assertTrue("waiter's customer list should contain a customer who's status is waitingForFood", sWaiter.customers.get(0).s== customerstate.waitingForFood);		

		
	}
	
	
}
