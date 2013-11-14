package restaurant.interfaces;

import java.util.List;

import restaurant.Check;
import restaurant.CustomerRole;
import restaurant.Order;
import restaurant.Order.OrderState;
import restaurant.WaiterRole.MyCustomer;
import restaurant.WaiterRole.WaiterState;

public interface Waiter {

	public abstract void msgSitAtTable(int t, CustomerRole cust);
	public void msgImReadyToOrder(CustomerRole cust);
	
	public abstract void msgHereIsMyChoice(CustomerRole cust, String choice);
	public abstract void msgCheckIsReady();
	public abstract void msgOutOfFood(Order o);
	
//	public abstract void msgOrderIsReady(Order o);
	
	public abstract void msgDoneEatingAndLeaving(CustomerRole cust);

	public abstract void msgCantAffordNotStaying(CustomerRole cust);

	public abstract void msgAtTable();
	
	public abstract void msgIWantABreak();
	
	public abstract void msgBreakReply(Boolean yn);
	
	public abstract void msgOutOfBreak();
	
	public abstract void msgHereIsACheck(int tnum, double amnt);
	void msgHereIsABill(Check bill);
	void msgOrderIsReady(int tablenum);
	
}
