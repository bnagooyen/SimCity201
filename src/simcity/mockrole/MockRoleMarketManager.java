package simcity.mockrole;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

public class MockRoleMarketManager extends MockRole implements MarketManager{

	public EventLog log;
	public MarketCashier mc;
	public boolean open;
	
	public MockRoleMarketManager(String name) {
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

	@Override
	public void msgIAmHere(Role r, List<MFoodOrder> need, String building,
			String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoadDeliveryTruck(MarketCashier cashier,
			List<MFoodOrder> deliver, String location, double bill, Cook r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackFromDelivery() {
		LoggedEvent e = new LoggedEvent("Received msgBackFromDelivery");
		log.add(e);
		
	}

	@Override
	public void msgHereIsMoney(double money) {
		// TODO Auto-generated method stub
		
	}

}
