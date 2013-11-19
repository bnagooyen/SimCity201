package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.RestaurantCashier;
import simcity.restaurant.CashierRole;
import simcity.restaurant.CookRole;

public class MockCook extends Mock implements Cook{

	public EventLog log;
	public MarketCashier mc;
	public CashierRole cash;
	public PersonAgent p;
	public CookRole cr;
	
	public MockCook(String name) {
		super(name);
		p = new PersonAgent("default");
		log = new EventLog();
		cr = new CookRole(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsDelivery(List<MFoodOrder> canGive) {
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