package simcity.test.mock;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Market.MarketCashierRole;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.restaurant.interfaces.Cashier;

public class MockMarketCashier extends Mock implements MarketCashier {
	
	public InventoryBoy ib;
	public MarketManager m;
	public EventLog log;
	public PersonAgent p;
	public MarketCashierRole mc;
	
	public MockMarketCashier(String name) {
		super(name);
		log = new EventLog();
		mc = new MarketCashierRole(p);
		
	}

	public void msgOrder(MarketCustomer c, List<MFoodOrder> foods,
			String building) {
		LoggedEvent e = new LoggedEvent("got customer's order");
		log.add(e);
		
		double check = 0;
		for(MFoodOrder o :foods) {
			check += o.amount * o.price;
		}
		
		c.msgHereIsOrderAndCheck(foods, check);
	}

	public void msgOrder(Cook cook, List<MFoodOrder> foods, String building,
			Cashier c) {
		// TODO Auto-generated method stub
		
	}

	public void msgCanGive(MOrder o) {
		LoggedEvent e = new LoggedEvent("got order back of what we can give customer");
		log.add(e);
	}

	public void msgHereIsPayment(Role r, double payment) {
		LoggedEvent e = new LoggedEvent("received payment");
		log.add(e);
	}

	public void msgGoHome() {
		LoggedEvent e = new LoggedEvent("Gone Home");
		log.add(e);
		
	}

}
