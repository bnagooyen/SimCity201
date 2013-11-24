package simcity.interfaces;

import simcity.LRestaurant.LCustomerRole;
//import simcity.LRestaurant.LMarketAgent;
import simcity.LRestaurant.LWaiterRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface LCashier {
	
	public abstract void msgComputeCheck(String choice, LCustomer c, LWaiter w);
	
	public abstract void msgHereIsPayment(LCustomer c, int money, int check);
	
	public abstract void msgHereIsSupplyCheck(double check, MarketCashier mc);
	
//	public abstract void msgHereIsSupplyCheck(int bill, LMarket market);

}