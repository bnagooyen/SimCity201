package simcity.test.mock;


import java.util.List;

import simcity.TRestaurant.TCashierRole;
import simcity.interfaces.TCashier;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import simcity.mockrole.MockRole;
import junit.framework.*;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class TMockCustomer extends MockRole implements TCustomer {
	public EventLog log = new EventLog(); 
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public TCashier cashier;

	public TMockCustomer(String name) {
		super(name);
	}

	public void msgPleaseWait() {
	}

	public void msgSitAtTable(int tableNum, List<String> m) {
	}

	public void msgWhatWouldYouLike() {
		
	}

	public void msgHeresYourOrder() {
		
	}
	
	public void msgHereIsYourCheck(double cost) {
		
	}
	
	public void msgChange (double c) {
		log.add(new LoggedEvent("Received my change. Change = " + c));
		
	}
	
	public void setWaiter(TWaiter waiter) {
		
	}

	@Override
	public void msgRestaurantClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(TCashierRole cashier) {
		// TODO Auto-generated method stub
		
	}



}
