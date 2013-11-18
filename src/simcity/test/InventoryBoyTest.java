package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.interfaces.MarketCashier;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.Market.MarketCashierRole.orderState;
import junit.framework.TestCase;

public class InventoryBoyTest extends TestCase{

	PersonAgent p;
	InventoryBoyRole ib;
	MockMarketCashier mc;
	MockMarketCustomer c;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("InventoryBoy");
		ib = new InventoryBoyRole(p);
		mc = new MockMarketCashier("mockMarketCashier");
		c = new MockMarketCustomer("mockCustomer");
	}
	
	public void testCheckInventory() {
		mc.ib = ib;
		
		// preconditions
        assertEquals("inventoryboy should have zero orders but doesn't", ib.orders.size(), 0);
        assertEquals("inventoryboy should have an empty event log before his msgBill is called. Instead, the ib's event log read: " + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        
        // populate ib's inventory
        ib.inventory.put("Steak", 5);
        assertEquals("inventory should have one thing", ib.inventory.size(), 1);

        
        // give ib an oder to fulfill
        List<MFoodOrder> foods = new ArrayList<MFoodOrder>();
        
        MOrder o = new MOrder(foods, "", c, orderState.inquiring);
        ib.msgCheckInventory(o);
        
        
	}
	
}
