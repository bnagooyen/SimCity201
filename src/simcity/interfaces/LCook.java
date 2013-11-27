package simcity.interfaces;

import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterNormalRole;
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

	public abstract void msgHereIsAnOrder(int table, String choice,
			LWaiterNormalRole lWaiterNormalRole);
	
	

}