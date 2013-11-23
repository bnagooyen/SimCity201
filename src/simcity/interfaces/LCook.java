package simcity.interfaces;

import simcity.LRestaurant.LCustomerRole;
//import simcity.LRestaurant.LMarketAgent;
import simcity.LRestaurant.LWaiterRole;
//import simcity.LRestaurant.LCookAgent.MarketState;
//import simcity.LRestaurant.LCookAgent.MyMarket;
import simcity.LRestaurant.LCookRole.Order;
import simcity.LRestaurant.LCookRole.OrderState;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface LCook {
	
	public void msgHereIsAnOrder(int table, String choice, LWaiterRole w);

//	public void msgSupplyUnfulfilled(String choice, int stillNeed, LMarketAgent market);

//	public void msgHereIsFoodSupply(String choice, int a, LMarket mark);

	public void msgDone(Order o);
	
	public void msgTask();

}