package simcity.test.mock;

import java.util.List;

import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;

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
//		if(open == true)
//			((MarketCustomer) r).msgGoToCashier(mc);
//		else
//			((MarketCustomer) r).msgMarketClosed();
	}

	public void msgCustomerDone(MarketCashier mc, Role r) {
		LoggedEvent e = new LoggedEvent("Received msgCustomerDone from market cashier.");
		log.add(e);
	}


	@Override
	public void msgLoadDeliveryTruck(MarketCashier cashier,
			List<MFoodOrder> deliver, String location, double bill, Cook r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackFromDelivery() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMoney(double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantClosed(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantOpen(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmHere(Role r, List<MFoodOrder> need, String building,
			String type, RestaurantCashier cashier) {
		LoggedEvent e = new LoggedEvent("got order from cook");
		log.add(e);
			
	}

}
