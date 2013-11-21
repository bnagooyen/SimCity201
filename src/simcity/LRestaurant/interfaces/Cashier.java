package simcity.LRestaurant.interfaces;

import restaurant.CustomerRole;
import restaurant.MarketAgent;
import restaurant.WaiterRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	
	public abstract void msgComputeCheck(String choice, Customer c, Waiter w);
	
	public abstract void msgHereIsPayment(Customer c, int money, int check);
	
	public abstract void msgHereIsSupplyCheck(int bill, Market market);

}