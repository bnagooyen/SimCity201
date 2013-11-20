package simcity.test.mock;

import simcity.interfaces.KCashier;
import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;

public class MockKRestaurantWaiter extends Mock implements KWaiter{

	public KCashier cashier; 
	public EventLog log;
	
	public MockKRestaurantWaiter(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgHereIsCheck(KCustomer c, double price) {
		LoggedEvent e = new LoggedEvent("received check from cashier");
		log.add(e);
	}

}
