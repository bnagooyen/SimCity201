package simcity.LRestaurant.interfaces;

import restaurant.CustomerRole;
import restaurant.WaiterRole.CustomerState;
import restaurant.WaiterRole.MyCustomers;
import restaurant.WaiterRole.WaiterState;

public interface Waiter {
	public abstract void msgBreakReply(boolean reply);

	public abstract void msgSeatCustomer(Customer cust, int table);
	
	public abstract void msgReadyToOrder(Customer cust); 
	
	public abstract void msgHereIsMyChoice(Customer cust, String choice);

	public abstract void msgRanOutofFood(int table, String choice);

	public abstract void msgOrderIsReady(int table, String choice);

	public abstract void msgReadyForCheck(Customer cust);

	public abstract void msgHereIsCheck(int check, Customer cust);

	public abstract void msgDoneEatingAndLeaving(Customer cust);


}