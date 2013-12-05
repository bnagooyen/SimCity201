package simcity.test.mock;


import java.util.List;

import simcity.interfaces.TCashier;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import junit.framework.*;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class TMockCustomer extends Mock implements TCustomer {
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



}
