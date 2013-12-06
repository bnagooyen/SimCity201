package simcity.interfaces;

import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole.CustomerState;
import simcity.LRestaurant.LWaiterRole.MyCustomers;
import simcity.LRestaurant.LWaiterRole.WaiterState;

public interface LWaiter {
	public abstract void msgBreakReply(boolean reply);

	public abstract void msgSeatCustomer(LCustomer cust, int table);
	
	public abstract void msgReadyToOrder(LCustomer cust); 
	
	public abstract void msgHereIsMyChoice(LCustomer cust, String choice);

	public abstract void msgRanOutofFood(int table, String choice);

	public abstract void msgOrderIsReady(int table, String choice);

	public abstract void msgReadyForCheck(LCustomer cust);

	public abstract void msgHereIsCheck(int check, LCustomer cust);

	public abstract void msgDoneEatingAndLeaving(LCustomer cust);


}