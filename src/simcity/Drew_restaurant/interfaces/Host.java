package simcity.Drew_restaurant.interfaces;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Host {

	public abstract void whatIsWait(Customer cust);
	
	public abstract void msgIWantFood(Customer cust);

	public abstract void tableIsFree(int table);
	
	public abstract void iWantToGoOnBreak(Waiter wait);
	
	public abstract void backFromBreak(Waiter wait);
	
	public abstract void leaving(Customer cust);

}