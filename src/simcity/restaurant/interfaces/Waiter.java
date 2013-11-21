package simcity.restaurant.interfaces;

import java.util.List;

import simcity.DRestaurant.DCheck;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DOrder;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.DWaiterRole.MyCustomer;
import simcity.DRestaurant.DWaiterRole.WaiterState;

public interface Waiter {

	public abstract void msgSitAtTable(int t, DCustomerRole cust);
	public void msgImReadyToOrder(DCustomerRole cust);
	
	public abstract void msgHereIsMyChoice(DCustomerRole cust, String choice);
	public abstract void msgCheckIsReady();
	public abstract void msgOutOfFood(DOrder o);
	
//	public abstract void msgOrderIsReady(Order o);
	
	public abstract void msgDoneEatingAndLeaving(DCustomerRole cust);

	public abstract void msgCantAffordNotStaying(DCustomerRole cust);

	public abstract void msgAtTable();
	
	public abstract void msgIWantABreak();
	
	public abstract void msgBreakReply(Boolean yn);
	
	public abstract void msgOutOfBreak();
	
	public abstract void msgHereIsACheck(int tnum, double amnt);
	void msgHereIsABill(DCheck bill);
	void msgOrderIsReady(int tablenum);
	
}
