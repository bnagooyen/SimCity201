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
import simcity.interfaces.MarketCashier;
import simcity.test.mock.MockCashier;
import simcity.test.mock.MockCook;
import simcity.test.mock.MockInventoryBoy;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockMarketManager;
import simcity.Market.MarketCashierRole.orderState;
import junit.framework.TestCase;

public class MarketCashierTest extends TestCase{

        PersonAgent p;
        MarketCashierRole mc;
        MockInventoryBoy ib;
        MockMarketCustomer c;
        MockCook cook;
        MockMarketManager man;
        MockCashier restaurantC;
        
        List<MFoodOrder> foods =Collections.synchronizedList(new ArrayList<MFoodOrder>());
        MFoodOrder f1;
        
        public void setUp() throws Exception{
                super.setUp();
                p = new PersonAgent("MarketCashier", mc);
                mc = new MarketCashierRole(p);
                p.addRole(mc);
                ib = new MockInventoryBoy("mockInventoryBoy");
                c = new MockMarketCustomer("mockCustomer");
                cook = new MockCook("mockCook");
                man = new MockMarketManager("mockManager");
                restaurantC = new MockCashier("mockCashier");
        }
        
        
        public void testMsgOrderCust() {
                mc.ib = ib;
                mc.manager = man;
                ib.mc = mc;
                c.mc = mc;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
        // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        
        //give mc adding an order
        mc.msgOrder(c, foods, "b1");
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        assertTrue("MarketCashier is giving order to ib.", mc.pickAndExecuteAnAction());
        assertEquals("Order state is inquiring.", mc.orders.get(0).state, orderState.inquiring);
        
        //give mc an order to fulfill
        assertTrue("InventoryBoy logged: " + ib.log.getLastLoggedEvent().toString(), ib.log.containsString("Received msgCheckInventory from market cashier."));
        assertFalse("MarketCashier has finished messaging ib.", mc.pickAndExecuteAnAction());
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an event log. The ib's event log reads: "
                + ib.log.toString(), 1, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        
        }
        
        public void testMsgCanGiveCust(){
                mc.ib = ib;
                ib.mc = mc;
                c.mc = mc;
                mc.manager = man;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        
        //give mc adding an order
        mc.msgOrder(c, foods, "b1");
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        mc.msgCanGive(mc.orders.get(0));
        assertEquals("Order state is ready.", mc.orders.get(0).state, orderState.ready);  
        assertTrue("MarketCashier is giving order to ib.", mc.pickAndExecuteAnAction());
        
        //give mc an order to fulfill
        assertTrue("MarketCustomer logged: " + c.log.getLastLoggedEvent().toString(), c.log.containsString("Received msgHereIsOrderAndCheck from market cashier."));
        assertEquals("MarketCustomer event log: " + c.log.toString(), 1, c.log.size());
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        }
        
        public void testMsgHereIsPaymentCust(){
                mc.ib = ib;
                ib.mc = mc;
                c.mc = mc;
                mc.manager = man;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
                
      //give mc adding an order
        mc.msgOrder(c, foods, "b1");
        mc.orders.get(0).c = c.cr; 
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        mc.msgHereIsPayment((Role)mc.orders.get(0).c, 10.0);
        assertEquals("Order state is ready.", mc.orders.get(0).state, orderState.paid);  
        assertTrue("MarketCashier is getting payment.", mc.pickAndExecuteAnAction());
        
       //interaction with customer 
        assertTrue("MarketManager logged: " + man.log.getLastLoggedEvent().toString(), man.log.containsString("Received msgCustomerDone from market cashier."));
        assertEquals("MarketCashier stored money.", mc.marketMoney, 10.0);  
        assertEquals("Order state is done.", mc.orders.get(0).state, orderState.done);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        assertEquals("MarketManager event log: " + man.log.toString(), 1, man.log.size());
        }
        
        public void testMsgOrderCook() {
                mc.ib = ib;
                mc.manager = man;
                ib.mc = mc;
                cook.mc = mc;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        
        //give mc adding an order
        mc.msgOrder(cook, foods, "b1");
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        assertTrue("MarketCashier is giving order to ib.", mc.pickAndExecuteAnAction());
        assertEquals("Order state is inquiring.", mc.orders.get(0).state, orderState.inquiring);
        
        //give mc an order to fulfill
        assertTrue("InventoryBoy logged: " + ib.log.getLastLoggedEvent().toString(), ib.log.containsString("Received msgCheckInventory from market cashier."));
        assertFalse("MarketCashier has finished messaging ib.", mc.pickAndExecuteAnAction());
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an event log. The ib's event log reads: "
                + ib.log.toString(), 1, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        }
        
        public void testMsgCanGiveCook(){
                mc.ib = ib;
                ib.mc = mc;
                c.mc = mc;
                mc.manager = man;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
                
      //give mc adding an order
        mc.msgOrder(cook, foods, "b1");
//        mc.orders.get(0).cashier = restaurantC; 
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        mc.msgCanGive(mc.orders.get(0));
        assertEquals("Order state is ready.", mc.orders.get(0).state, orderState.ready);  
        assertTrue("MarketCashier is giving order.", mc.pickAndExecuteAnAction());
        
        //give mc an order to fulfill
        //assertTrue("Restaurant Cook logged: " + restaurantC.log.getLastLoggedEvent().toString(), restaurantC.log.containsString("Received msgBillFromMarket from market manager"));
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        //assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 1, restaurantC.log.size());
        }
        
        public void testMsgHereIsPaymentCook(){
                mc.ib = ib;
                ib.mc = mc;
                c.mc = mc;
                mc.manager = man;
                f1 = new MFoodOrder("Ch", 2);
                foods.add(f1);
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
                
      //give mc adding an order
        mc.msgOrder(cook, foods, "b1");
        mc.orders.get(0).cook = cook.cr; 
        assertEquals("MarketCashier should have one order", mc.orders.size(), 1);
        mc.msgHereIsPayment((Role)mc.orders.get(0).cook, 10.0);
        assertEquals("Order state is ready.", mc.orders.get(0).state, orderState.paid);  
        assertTrue("MarketCashier is getting payment.", mc.pickAndExecuteAnAction());
        
       //interaction with customer 
        assertTrue("MarketManager logged: " + man.log.getLastLoggedEvent().toString(), man.log.containsString("Received msgCustomerDone from market cashier."));
        assertEquals("MarketCashier stored money.", mc.marketMoney, 10.0);  
        assertEquals("Order state is done.", mc.orders.get(0).state, orderState.done); 
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager event log: " + man.log.toString(), 1, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        }
        
        public void testMsgGoHome(){
                
                // preconditions
        assertEquals("MarketCashier should have zero orders but doesn't", mc.orders.size(), 0);
        assertEquals("MarketCashier should have collected zero money", mc.marketMoney, 0.0);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        
        //message to go home from work
        mc.msgGoHome();
        assertEquals("MarketCashier's state is goHome.", mc.state, myState.goHome);
        assertTrue("MarketCashier going home.", mc.pickAndExecuteAnAction());
        
        //in action
        assertEquals("MarketCashier's state is unavailable.", mc.state, myState.unavailable);
        assertEquals("MarketCashier's not active.", mc.active,false);
        assertEquals("MarketCashier should have an empty event log. The mc's event log read: " + mc.log.toString(), 0, mc.log.size());
        assertEquals("MockInventoryBoy should have an empty event log. The ib's event log reads: "
                + ib.log.toString(), 0, ib.log.size());
        assertEquals("MarketCustomer empty event log: " + c.log.toString(), 0, c.log.size());
        assertEquals("MarketManager empty event log: " + man.log.toString(), 0, man.log.size());
        assertEquals("Restaurant Cook event log: " + restaurantC.log.toString(), 0, restaurantC.log.size());
        
        }
        
}