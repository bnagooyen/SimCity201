package simcity.interfaces;

import simcity.Drew_restaurant.Drew_WaiterRole;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Drew_Host {

	public abstract void whatIsWait(Drew_Customer cust);
	
	public abstract void msgIWantFood(Drew_Customer cust);

	public abstract void tableIsFree(int table);
	
	public abstract void iWantToGoOnBreak(Drew_Waiter wait);
	
	public abstract void backFromBreak(Drew_Waiter wait);
	
	public abstract void leaving(Drew_Customer cust);

	public abstract void addWaiter(Drew_Waiter aw);

}