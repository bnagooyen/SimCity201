package simcity.test.mock;

import agent.Role;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;

public class MockMarketManager extends Mock implements MarketManager{

	public EventLog log;
	public MarketCashier mc;
	
	public MockMarketManager(String name) {
		super(name);
		log = new EventLog();
	}

	public void msgTimeUpdate(int hour) {
		
	}

	
	public void msgIAmHere(Role r, String type) {
		LoggedEvent e = new LoggedEvent("got customer's message");
		log.add(e);
		((MarketCustomer) r).msgGoToCashier(mc);
	}

	public void msgCustomerDone(MarketCashier mc, Role r) {
		
	}

}
