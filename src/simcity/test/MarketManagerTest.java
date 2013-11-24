package simcity.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCashierRole.myState;
import simcity.Market.MarketManagerRole;
import simcity.Market.MarketManagerRole.workerState;
import simcity.interfaces.MarketCashier;
import simcity.mockrole.MockRoleMarketCashier;
import simcity.mockrole.MockRoleMarketCustomer;
import simcity.test.mock.MockCook;
import simcity.test.mock.MockInventoryBoy;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockMarketManager;
import simcity.Market.MarketCashierRole.orderState;
import junit.framework.TestCase;

public class MarketManagerTest extends TestCase{

	PersonAgent p;
	MockRoleMarketCashier mc;
	MockInventoryBoy ib;
	MockRoleMarketCustomer c;
	MockCook cook;
	MarketManagerRole m;
	
	//List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	List<MFoodOrder> foods =Collections.synchronizedList(new ArrayList<MFoodOrder>());
	MFoodOrder f1;
	//MOrder a;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("MarketCashier");
		mc = new MockRoleMarketCashier("mockMarketCashier", p);
		p.addRole(m);
		ib = new MockInventoryBoy("mockInventoryBoy");
		c = new MockRoleMarketCustomer("mockCustomer", p);
		cook = new MockCook("mockCook");
		m = new MarketManagerRole(p);
	}
	
	
	public void testMsgIAmHere() {
		mc.ib = ib;
		mc.m = m;
		ib.mc = mc;
		c.mc = mc;
		f1 = new MFoodOrder("Ch", 2);
		foods.add(f1);

		
		// preconditions
        assertEquals("MarketManager should have zero cashiers", m.cashiers.size(), 0);
        assertEquals("MarketManager should have zero inventoryBoys", m.inventoryBoys.size(), 0);
        assertEquals("MarketManager should have zero customers", m.customers.size(), 0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + m.log.toString(), 0, m.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCustomer should have an empty event log. The c's event log reads: "
                + c.log.toString(), 0, c.log.size());
        assertEquals("MockCook should have an empty event log. The cook's event log reads: "
                + cook.log.toString(), 0, cook.log.size());
        assertEquals("MockCashier should have an empty event log. The mc's event log reads: "
                + mc.log.toString(), 0, mc.log.size());
        
        
        //give mc adding an cashier
        m.msgIAmHere(mc,"cashier");
        assertEquals("MarketManager should have one cashier", m.cashiers.size(), 1);
        assertEquals("Market Cashier can work", m.cashiers.get(0).state, workerState.available);
        //assertTrue("MarketManager is checking to open.", m.pickAndExecuteAnAction());
        assertEquals("isClosed.", m.isClosed, true);
        assertTrue("MarketManager logged: " + m.log.getLastLoggedEvent().toString(), m.log.containsString("Received msgIAmHere."));
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCustomer should have an empty event log. The c's event log reads: "
                + c.log.toString(), 0, c.log.size());
        assertEquals("MockCook should have an empty event log. The cook's event log reads: "
                + cook.log.toString(), 0, cook.log.size());
        assertEquals("MockCashier should have an empty event log. The mc's event log reads: "
                + mc.log.toString(), 0, mc.log.size());
        
        //adding inventory boy
        m.msgIAmHere(ib.ib,"inventory boy");
        assertEquals("MarketManager should have one inventory boy", m.inventoryBoys.size(), 1);
        assertEquals("isClosed.", m.isClosed, false);
        assertTrue("MarketManager logged: " + m.log.getLastLoggedEvent().toString(), m.log.containsString("Received msgIAmHere."));
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCustomer should have an empty event log. The c's event log reads: "
                + c.log.toString(), 0, c.log.size());
        assertEquals("MockCook should have an empty event log. The cook's event log reads: "
                + cook.log.toString(), 0, cook.log.size());
        assertEquals("MockCashier should have an empty event log. The mc's event log reads: "
                + mc.log.toString(), 0, mc.log.size());
        
        //adding customer
        m.msgIAmHere(c,"customer");
        assertEquals("MarketManager should have one customer", m.customers.size(), 1);
        assertTrue("MarketManager logged: " + m.log.getLastLoggedEvent().toString(), m.log.containsString("Received msgIAmHere."));
        assertTrue("MarketManager is checking to open.", m.pickAndExecuteAnAction());
        assertEquals("Market Cashier can work", m.cashiers.get(0).state, workerState.occupied);
        assertTrue("MarketCustomer logged: " + c.log.getLastLoggedEvent().toString(), c.log.containsString("Received msgGoToCashier from market manager."));
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockCook should have an empty event log. The cook's event log reads: "
                + cook.log.toString(), 0, cook.log.size());
        assertEquals("MockCashier should have an empty event log. The mc's event log reads: "
                + mc.log.toString(), 0, mc.log.size());
      
        //time to close market
        m.msgTimeUpdate(20);
        assertEquals("isClosed.", m.isClosed, false);     
        m.pickAndExecuteAnAction();
        assertEquals("isClosed.", m.isClosed, true);
        assertTrue("MarketManager is closing.", m.pickAndExecuteAnAction());
        assertEquals("MarketManager should have zero cashiers", m.cashiers.size(), 0);
        assertEquals("MarketManager should have zero inventoryBoys", m.inventoryBoys.size(), 0);
        m.pickAndExecuteAnAction(); 
        assertEquals("MarketManager should have zero customers", m.customers.size(), 0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + m.log.toString(), 3, m.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCustomer should have an event log. The c's event log reads: "
                + c.log.toString(), 1, c.log.size());
        assertEquals("MockCook should have an empty event log. The cook's event log reads: "
                + cook.log.toString(), 0, cook.log.size());
        assertEquals("MockCashier should have an event log. The mc's event log reads: "
                + mc.log.toString(), 1, mc.log.size());
	}
	
}
