package simcity.test;

import simcity.PersonAgent;
import simcity.LRestaurant.LWaiterRole.CustomerState;
import simcity.LRestaurant.LWaiterRole.WaiterState;
import simcity.LRestaurant.LWaiterSharedDataRole;
import simcity.LRestaurant.ProducerConsumerMonitor;
import simcity.LRestaurant.gui.LWaiterGui;
import simcity.test.mock.EventLog;
import simcity.test.mock.MockKRestaurantCustomer;
import simcity.test.mock.MockLHost;
//import simcity.LRestaurant.test.mock.MockMarket;
import simcity.interfaces.LCustomer;
import simcity.test.mock.MockLCustomer;
import junit.framework.*;


public class LWaiterSharedDataTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent p;
	LWaiterSharedDataRole sWaiter;
	MockLCustomer customer;
	MockLHost host;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		p = new PersonAgent("person");
		customer = new MockLCustomer("mockcustomer");	
		sWaiter = new LWaiterSharedDataRole();
		host = new MockLHost("mostkhost");
		sWaiter.myPerson = p;
		ProducerConsumerMonitor m = new ProducerConsumerMonitor();
		sWaiter.setMonitor(m);
		sWaiter.setHost(host);
		
	}	
	
	public void testWaiter(){
		
		//preconditions
		assertEquals("sWatier's state should be working but isn't", sWaiter.waiterState, WaiterState.working);
		assertTrue("sWaiter's log should be empty but isn't", sWaiter.log.size() == 0);
		assertTrue("waiter''s customer should be size 0 but isn't", sWaiter.customers.size() == 0);		
		assertTrue("customer's log should be empty but isn't", customer.log.size() == 0);		
		
		//  give waiter customer
		sWaiter.msgSeatCustomer(customer, 0);
		assertTrue("waiter''s customer should be size 1 but isn't", sWaiter.customers.size() == 1);		
		assertTrue("waiter's log should say it's seating a customer but doesnt'", sWaiter.log.containsString("Received msgSeatCustomer"));		
		assertTrue("customer's log should be empty but isn't", customer.log.size() == 0);		

		sWaiter.pickAndExecuteAnAction();
		assertTrue("waiter's log should say it's seating customer but doesn't", sWaiter.log.containsString("Seating customer"));		
		assertTrue("customer's log should say being seated but doesn't", customer.log.containsString("Received msgFollowMe"));		

		// customer gives waiter order
		sWaiter.msgHereIsMyChoice(customer, "Steak");
		assertTrue("waiter's log should say it's customer is ready to order but doesn't", sWaiter.log.containsString("Received msgHereIsMyChoice"));		

		sWaiter.pickAndExecuteAnAction();
		assertTrue("waiter's log should say putting order on stand but doesn't", sWaiter.log.containsString("Giving order cook"));		

		// postconditions
		assertFalse("waiter's scheduler should return false", sWaiter.pickAndExecuteAnAction());
		
		
	}
	
	
}
