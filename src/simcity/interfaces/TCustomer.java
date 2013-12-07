package simcity.interfaces;

import java.util.List;

import simcity.TRestaurant.TCashierRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TCustomer {
	
	public abstract void msgPleaseWait();

	public abstract void msgSitAtTable(int tableNum, List<String> m);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgHeresYourOrder();
	
	public abstract void msgHereIsYourCheck(double cost);
	
	public abstract void msgChange (double c);
	
	public abstract void setWaiter (TWaiter w);

	public abstract void msgRestaurantClosed();

	public abstract void setCashier(TCashierRole cashier); 


}