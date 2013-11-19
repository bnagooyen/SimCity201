package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCustomerRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer{

	public EventLog log;
	public MarketCashier mc;
	public MarketCustomerRole cr;
	public PersonAgent p;
	
	public MockMarketCustomer(String name) {
		super(name);
		p = new PersonAgent("default");
		log = new EventLog();
		cr = new MarketCustomerRole(p);
	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		LoggedEvent e = new LoggedEvent("Received msgGoToCashier from market manager.");
		log.add(e);
		
	}

	@Override
	public void msgHereIsOrderAndCheck(List<MFoodOrder> canGive, double check) {
		LoggedEvent e = new LoggedEvent("Received msgHereIsOrderAndCheck from market cashier.");
		log.add(e);
	}

	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}

}
