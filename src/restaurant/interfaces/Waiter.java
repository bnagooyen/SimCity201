package restaurant.interfaces;

import java.util.List;

import restaurant.Check;
import restaurant.CustomerAgent;
import restaurant.Order;
import restaurant.Order.OrderState;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.WaiterAgent.WaiterState;

public interface Waiter {

	public abstract void msgSitAtTable(int t, CustomerAgent cust);
	public void msgImReadyToOrder(CustomerAgent cust);
	
	public abstract void msgHereIsMyChoice(CustomerAgent cust, String choice);
	public abstract void msgCheckIsReady();
	public abstract void msgOutOfFood(Order o);
	
//	public abstract void msgOrderIsReady(Order o);
	
	public abstract void msgDoneEatingAndLeaving(CustomerAgent cust);

	public abstract void msgCantAffordNotStaying(CustomerAgent cust);

	public abstract void msgAtTable();
	
	public abstract void msgIWantABreak();
	
	public abstract void msgBreakReply(Boolean yn);
	
	public abstract void msgOutOfBreak();
	
	public abstract void msgHereIsACheck(int tnum, double amnt);
	void msgHereIsABill(Check bill);
	void msgOrderIsReady(int tablenum);
	
}
