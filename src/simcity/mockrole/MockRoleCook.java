package simcity.mockrole;

import java.util.List;

import simcity.test.mock.LoggedEvent;
import simcity.PersonAgent;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.mockrole.MockRoleMarketCustomer.customerState;
import simcity.test.mock.EventLog;

public class MockRoleCook extends MockRole implements Cook{

	public EventLog log;
	public MarketCashier mc;
	public LCashierRole cash;
	public PersonAgent p;
	public LCookRole cr;
	
	public MockRoleCook(String name) {
		super(name);
		p = new PersonAgent("default");
		log = new EventLog();
		cr = new LCookRole();
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public void msgHereIsDelivery(List<MFoodOrder> canGive, double check,
			MarketManager manager, MarketCashier mc) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean pickAndExecuteAnAction() {
	
		return false;
	}
	
}