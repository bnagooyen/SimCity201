package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.RestaurantCashier;

public class MockCook extends Mock implements Cook{

	public EventLog log;
	public MarketCashier mc;
	public DCashierRole cash;
	public PersonAgent p;
	public DCookRole cr;
	
	public MockCook(String name) {
		super(name);
		p = new PersonAgent("default", cr);
		log = new EventLog();
		cr = new DCookRole(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsDelivery(List<MFoodOrder> canGive, double check, MarketCashier mc) {
		LoggedEvent e = new LoggedEvent("Received msgHereIsDelivery from market manager.");
		log.add(e);
		
	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		LoggedEvent e = new LoggedEvent("Received msgGoToCashier from market manager.");
		log.add(e);
		
	}

	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}
	
}