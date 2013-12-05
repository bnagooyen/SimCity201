package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCookRole.Food;
import simcity.KRestaurant.KCookRole.Food;
import simcity.KRestaurant.KCookRole.marketOrderState;
import simcity.Market.MFoodOrder;
import simcity.test.mock.MockKRestaurantWaiter;
import simcity.test.mock.MockMarketManager;
import junit.framework.TestCase;

public class KCookTest extends TestCase {

		PersonAgent p;
		KCookRole cook;
		MockMarketManager manager;
		MockKRestaurantWaiter waiter;
		
		public void setUp() throws Exception {
			super.setUp();
			p = new PersonAgent("person");
			cook = new KCookRole();
			manager = new MockMarketManager("mockmanager");
			waiter = new MockKRestaurantWaiter("mockwaiter");
			cook.myPerson = p;
			cook.markets.add(manager);
		}
		
		public void testOrderingFromMarket() {
			
			// preconditions
			assertTrue("cook's log should be empty but isn't", cook.log.size() == 0);
			assertTrue("manager's log should be empty but isn't", manager.log.size() == 0);	
			assertTrue("waiter's log should be empty but isn't", waiter.log.size() == 0);	

			// make steak inventory low
			Food f =  cook.new Food("Steak", 5000, 2, 3, 1,10);
			cook.foods.put("Steak",f );
			assertEquals("cook's steak inventory should be 2 but isn't",2, cook.foods.get("Steak").amount);

			// give cook an order
			cook.msgHereIsAnOrder(waiter, "Steak", 0);
			assertTrue("cook's log say got an order but doesn't", cook.log.containsString("got order from waiter"));
			assertTrue("manager's log should be empty but isn't", manager.log.size() == 0);	
			assertTrue("waiter's log should be empty but isn't", waiter.log.size() == 0);	
			
			cook.pickAndExecuteAnAction();
			assertTrue("cook's log say got an order but doesn't", cook.log.containsString("need to order food"));
			assertTrue("waiter's log should be empty but isn't", waiter.log.size() == 0);	
			assertTrue("manager's log should say got order from cook but doesn't", manager.log.containsString("got order from cook"));	
			assertTrue("cook's needToOrder should be false but isn't", cook.needToOrder == false);
			assertTrue("cook's marketOrders should have one order but doesn't", cook.marketOrders.size() == 1);

			// give requested order to cook
			List<MFoodOrder> order = new ArrayList<MFoodOrder>();
			order.add(new MFoodOrder("Steak", 2));
			cook.msgHereIsDelivery(order, 20.0, manager, null);
			assertTrue("cook's log say got delivery but doesn't", cook.log.containsString("got delivery from market"));

			// post conditions
			assertTrue("cook's marketOrders should have an order that's state is arrived", cook.marketOrders.get(0).state == marketOrderState.arrived);

		}
}
