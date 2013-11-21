package simcity.LRestaurant.interfaces;

import restaurant.CustomerRole;
import restaurant.MarketAgent;
import restaurant.WaiterRole;
import restaurant.CookAgent.MarketState;
import restaurant.CookAgent.MyMarket;
import restaurant.CookAgent.Order;
import restaurant.CookAgent.OrderState;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook {
	
	public void msgHereIsAnOrder(int table, String choice, WaiterRole w);

	public void msgSupplyUnfulfilled(String choice, int stillNeed, MarketAgent market);

	public void msgHereIsFoodSupply(String choice, int a, Market mark);

	public void msgDone(Order o);
	
	public void msgTask();

}