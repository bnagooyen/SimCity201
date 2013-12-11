package simcity.test.mock;


import agent.Role;
import simcity.PersonAgent;
import simcity.DRestaurant.DCashierRole;
import simcity.Market.MarketCashierRole;
import simcity.interfaces.DCustomer;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;


public class MockCashier extends Mock implements RestaurantCashier {
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public MarketCashier cashier;
	public EventLog log = new EventLog();
	public PersonAgent p;
	public DCashierRole cr;
	
	public MockCashier(String name) {
		super(name);
		cr = new DCashierRole();
	}

	@Override
	public void msgBillFromMarket(double check, MarketCashier marketCashier,
			MarketManager manager) {
		// TODO Auto-generated method stub
		
	}
	



}
