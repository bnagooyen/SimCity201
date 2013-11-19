package simcity.test;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketCustomerRole.customerState;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketManager;
import junit.framework.TestCase;

public class MarketCustomerTest extends TestCase{
	
	PersonAgent p;
	MarketCustomerRole c;
	MockMarketCashier mc;
	MockMarketManager manager;
	
	public void setUp() throws Exception {
		super.setUp();
		p = new PersonAgent("customer");
		c = new MarketCustomerRole(p);
		mc = new MockMarketCashier("mockMarketCashier");
		manager = new MockMarketManager("mockManager");
	}
	
	public void testOrderFood() {
		manager.mc = mc;
		mc.m = manager;
		c.setMarketManager(manager);
		manager.open = true;
		
		// preconditions
        assertEquals("MarketCustomer should have an empty event log. Instead, the customer's event log reads: " + c.log.toString(), c.log.size(), 0);
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketManager should have an empty event log. Instead, the MockMarketManager's event log reads: " + manager.log.toString(), manager.log.size(), 0);
        assertEquals("MarketCustomer's myCheck should be zero but isn't", c.getMyCheck(), 0.0);
        
        // populate customer's order list
        c.order.add(new MFoodOrder("Steak", 3));
        c.order.add(new MFoodOrder("Pizza", 2));
        c.order.add(new MFoodOrder("Chicken", 4));
        assertEquals("MarketCustomer's order list should be size 3 but isn't", c.order.size(), 3);

		// have customer go talk to manager
		c.pickAndExecuteAnAction();
		assertTrue("MarketCustomer should tell manager he's here but didn't", c.log.containsString("telling manager I'm here"));
		assertTrue("MockManager should get customer's msg but didn't", manager.log.containsString("got customer's message"));
		assertTrue("MarketCustomer should get msg to go to cashier but didn't", c.log.containsString("told to go to cashier"));

		// have customer talk to cashier
		c.pickAndExecuteAnAction();
		assertTrue("MarketCustomer should tell cashier his order but didn't", c.log.containsString("telling cashier my order"));
		assertTrue("MockMarketCashier should have gotten order but didn't", mc.log.containsString("got customer's order"));
		assertTrue("MarketCustomer should get order but didn't", c.log.containsString("got food and check"));

		// have customer pay check
		c.pickAndExecuteAnAction();
		assertTrue("MarketCustomer should pay cashier but didn't", c.log.containsString("paying check"));
		assertTrue("MockMarketCashier should have gotten payment but didn't", mc.log.containsString("received payment"));

		// postconditions
		assertTrue("Customer shouldn't be active anymore but is", c.isActive == false);
		assertTrue("Customer's state should be done",c.state == customerState.done);
        assertEquals("customer's scheduler should return false but doesn't", c.pickAndExecuteAnAction(), false);

	}
	
	public void testMarketClosed() {
		c.setMarketManager(manager);
		assertEquals("MarketCustomer should have an empty event log. Instead, the customer's event log reads: " + c.log.toString(), c.log.size(), 0);
        assertEquals("MockMarketManager should have an empty event log. Instead, the MockMarketManager's event log reads: " + manager.log.toString(), manager.log.size(), 0);
		
        // have customer go talk to manager
        c.pickAndExecuteAnAction();
     	assertTrue("MarketCustomer should tell manager he's here but didn't", c.log.containsString("telling manager I'm here"));
     	assertTrue("MockManager should get customer's msg but didn't", manager.log.containsString("got customer's message"));
     	assertTrue("MarketCustomer should get msg market is closed but didn't", c.log.containsString("told market is closed"));
     	
     	// make sure customer leaves
        c.pickAndExecuteAnAction();
     	assertTrue("MarketCustomer should leave but didn't", c.log.containsString("leaving market"));

     	// check postconditions
     	assertTrue("Customer shouldn't be active anymore but is", c.isActive == false);
		assertTrue("Customer's state should be done",c.state == customerState.done);
        assertEquals("customer's scheduler should return false but doesn't", c.pickAndExecuteAnAction(), false);
	}
}
