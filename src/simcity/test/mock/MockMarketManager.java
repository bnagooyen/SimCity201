package simcity.test.mock;

import agent.Role;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;

public class MockMarketManager extends Mock implements MarketManager{

	public EventLog log;
	public MarketCashier mc;
	public boolean open;
	
	public MockMarketManager(String name) {
		super(name);
		log = new EventLog();
	}

	public void msgTimeUpdate(int hour) {
		
	}

	
	public void msgIAmHere(Role r, String type) {
		LoggedEvent e = new LoggedEvent("got customer's message");
		log.add(e);
		if(open == true)
			((MarketCustomer) r).msgGoToCashier(mc);
		else
			((MarketCustomer) r).msgMarketClosed();
	}

	public void msgCustomerDone(MarketCashier mc, Role r) {
		LoggedEvent e = new LoggedEvent("Received msgCustomerDone from market cashier.");
		log.add(e);
	}

}
