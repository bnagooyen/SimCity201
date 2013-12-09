package simcity.interfaces;

import simcity.TRestaurant.OrderStand;
import simcity.TRestaurant.TCashierRole;
import simcity.TRestaurant.TCookRole;
import simcity.TRestaurant.THostRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TWaiter {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	public abstract void msgSeatAtTable(TCustomer cust, int t);

	public abstract void msgReadyToOrder(TCustomer cust);

	public abstract void msgHeresMyChoice(TCustomer cust, String order);

	public abstract void msgOutOfFood(int t);

	public abstract void msgOrderIsReady(int t);
	
	public abstract void msgReadyForCheck(TCustomer cust);
	
	public abstract void msgHereIsCheck(TCustomer cust, double c);
	
	public abstract void msgLeavingTable(TCustomer cust);

	public abstract void msgNoBreak();

	public abstract void msgOnBreak();
	
	public abstract void msgGoHome(double moneys);

	public abstract void setHost(THostRole tHostRole);

	public abstract void setHomePosition(int i);

	public abstract void setCashier(TCashierRole cashier);

	public abstract void setCook(TCookRole cook);

	public abstract void setMonitor(OrderStand myStand); 


}