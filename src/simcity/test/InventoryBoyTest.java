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
import simcity.Market.gui.IBGui;
import junit.framework.TestCase;

public class InventoryBoyTest extends TestCase{

	PersonAgent p;
	InventoryBoyRole ib;
	IBGui ibGui;
	MockMarketCashier mc;
	MockMarketCustomer c;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("InventoryBoy");
		ib = new InventoryBoyRole(p);
		mc = new MockMarketCashier("mockMarketCashier");
		c = new MockMarketCustomer("mockCustomer");
		ibGui = new IBGui(ib);
		ib.setGui(ibGui);
	}
	
	public void testCheckInventoryFood() {
		mc.ib = ib;
		ib.setMarketCashier(mc);
		ib.purpose = "food";
		
		// preconditions
        assertEquals("inventoryboy should have zero orders but doesn't", ib.orders.size(), 0);
        assertEquals("inventoryboy should have an empty event log before his msgBill is called. Instead, the ib's event log read: " + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);
        assertEquals("inventory should be size 4", ib.inventory.size(), 4);
        assertTrue("inventory should have 20 steaks but doesn't", ib.inventory.get("Steak") == 20);
        assertTrue("inventory should have 20 chickens but doesn't", ib.inventory.get("Chicken") == 20);
        assertTrue("inventory should have 20 salads but doesn't", ib.inventory.get("Salad") == 20);
        assertTrue("inventory should have 20 pizzas but doesn't", ib.inventory.get("Pizza") == 20);
        
        // give ib an oder to fulfill
        List<MFoodOrder> foods = new ArrayList<MFoodOrder>();
        foods.add(new MFoodOrder("Steak", 3));
        assertEquals("foods should have one foodorder but doesn't", foods.size(), 1);
        foods.add(new MFoodOrder("Chicken", 2));
        assertEquals("foods should have two foodorders but doesn't", foods.size(), 2);
        
        MOrder o = new MOrder(foods, "", c, orderState.inquiring);
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgCheckInventory(o);
        assertEquals("ib's orders should have one order but doesn't", ib.orders.size(), 1);
        assertEquals("order should have two foods but doesn't", ib.orders.get(0).foodsNeeded.size(), 2);
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("got an order to fulfill"));
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // try to fulfill order
        ib.pickAndExecuteAnAction();
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("fulfilling an order"));
        assertTrue("mockmarketcashier's log should have this but doesn't", mc.log.containsString("got order back of what we can give customer"));
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // check post conditions
        assertTrue("inventory should have 17 steaks but doesn't", ib.inventory.get("Steak") == 17);
        assertTrue("inventory should have 18 chickens but doesn't", ib.inventory.get("Chicken") == 18);
        assertTrue("inventory should have 20 salads but doesn't", ib.inventory.get("Salad") == 20);
        assertTrue("inventory should have 20 pizzas but doesn't", ib.inventory.get("Pizza") == 20);
        assertEquals("ib's scheduler should return false but doesn't", ib.pickAndExecuteAnAction(), false);
        assertEquals("MockMarketCashier shouldn't have gotten any extra loggedevents. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 1);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

	}
	
	public void testCantCompletelyFulfillOrder(){
		mc.ib = ib;
		ib.setMarketCashier(mc);
		ib.purpose = "food";
		
		// preconditions
        assertEquals("inventoryboy should have zero orders but doesn't", ib.orders.size(), 0);
        assertEquals("inventoryboy should have an empty event log before his msgBill is called. Instead, the ib's event log read: " + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);
        assertEquals("inventory should be size 4", ib.inventory.size(), 4);
        assertTrue("inventory should have 20 steaks but doesn't", ib.inventory.get("Steak") == 20);
        assertTrue("inventory should have 20 chickens but doesn't", ib.inventory.get("Chicken") == 20);
        assertTrue("inventory should have 20 salads but doesn't", ib.inventory.get("Salad") == 20);
        assertTrue("inventory should have 20 pizzas but doesn't", ib.inventory.get("Pizza") == 20);
        
        // give ib an order to fulfill
        List<MFoodOrder> foods = new ArrayList<MFoodOrder>();
        foods.add(new MFoodOrder("Steak", 21));
        assertEquals("foods should have one foodorder but doesn't", foods.size(), 1);
        foods.add(new MFoodOrder("Chicken", 2));
        assertEquals("foods should have two foodorders but doesn't", foods.size(), 2);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);
        
        MOrder o = new MOrder(foods, "", c, orderState.inquiring);
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgGotFood();
        ib.msgCheckInventory(o);
        assertEquals("ib's orders should have one order but doesn't", ib.orders.size(), 1);
        assertEquals("order should have two foods but doesn't", ib.orders.get(0).foodsNeeded.size(), 2);
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("got an order to fulfill"));
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // try to fulfill order
        ib.pickAndExecuteAnAction();
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("fulfilling an order"));
        assertTrue("mockmarketcashier's log should have this but doesn't", mc.log.containsString("got order back of what we can give customer"));
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // check post conditions
        assertTrue("inventory should have 0 steaks but doesn't", ib.inventory.get("Steak") == 0);
        assertTrue("inventory should have 18 chickens but doesn't", ib.inventory.get("Chicken") == 18);
        assertTrue("inventory should have 20 salads but doesn't", ib.inventory.get("Salad") == 20);
        assertTrue("inventory should have 20 pizzas but doesn't", ib.inventory.get("Pizza") == 20);
        assertEquals("ib's scheduler should return false but doesn't", ib.pickAndExecuteAnAction(), false);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

	}
	
	public void testGoHome() {
		// preconditions
        assertEquals("inventoryboy should have an empty event log before his msgBill is called. Instead, the ib's event log read: " + ib.log.toString(), 0, ib.log.size());
        assertEquals("ib's scheduler should return false but doesn't", ib.pickAndExecuteAnAction(), false);
        assertEquals("inventoryboy should have zero orders but doesn't", ib.orders.size(), 0);
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // tell ib to go home
        ib.msgGoHome(50);
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("told to go home"));
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        ib.pickAndExecuteAnAction();
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("going home"));
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // check postconditions
        assertTrue("ib shouldn't be active but it is", ib.isActive == false);
        assertEquals("ib's scheduler should return false but doesn't", ib.pickAndExecuteAnAction(), false);
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

	}
	
	public void testCheckInventoryCar() {
		mc.ib = ib;
		ib.setMarketCashier(mc);
		ib.purpose = "car";
		
		// preconditions
        assertEquals("inventoryboy should have zero orders but doesn't", ib.orders.size(), 0);
        assertEquals("inventoryboy should have an empty event log before his msgBill is called. Instead, the ib's event log read: " + ib.log.toString(), 0, ib.log.size());
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);
        assertEquals("inventory of cars should be size 20", ib.cars.size(), 20);
        
        MOrder o = new MOrder("b1", c,orderState.inquiring);
//        assertEquals("ib should know that customer ordered car", ib.orders.get(0).foodsNeeded, null);
        ib.msgCheckInventory(o);
        assertEquals("ib's orders should have one order but doesn't", ib.orders.size(), 1);
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("got an order to fulfill"));
        assertEquals("MockMarketCashier should have an empty event log. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 0);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // try to fulfill order
        ib.msgGotFood();
        ib.msgGotFood();
        ib.pickAndExecuteAnAction();
        assertTrue("ib's log should have this but doesn't", ib.log.containsString("fulfilling an order"));
//        assertTrue("mockmarketcashier's log should have this but doesn't", mc.log.containsString("got order back of what we can give customer"));
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

        // check post conditions
        assertEquals("inventory of cars should have gone down 1", ib.cars.size(), 19);
//        assertEquals("ib's scheduler should return false but doesn't", ib.pickAndExecuteAnAction(), false);
//        assertEquals("MockMarketCashier shouldn't have gotten any extra loggedevents. Instead, the MockMarketCashier's event log reads: " + mc.log.toString(), mc.log.size(), 1);
        assertEquals("MockMarketCustomer should have an empty event log. Instead, the MockMarketCustomer's event log reads: " + c.log.toString(), c.log.size(), 0);

	}
}
