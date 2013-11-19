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
	public MarketCustomerRole c;
	public PersonAgent p;
	
	public MockMarketCustomer(String name) {
		super(name);
		log = new EventLog();
		c = new MarketCustomerRole(p);
	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		// TODO Auto-generated method stub
		
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
