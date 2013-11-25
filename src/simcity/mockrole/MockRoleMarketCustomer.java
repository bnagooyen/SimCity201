package simcity.mockrole;

import java.util.List;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCustomerRole.customerState;
import simcity.interfaces.Car;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

public class MockRoleMarketCustomer extends MockRole implements MarketCustomer{

	private String name;
	public EventLog log;
	
	public enum customerState { talkToManager, timeToOrder, waiting, paying, done, storeClosed, pending }
	public customerState state;
	public MockRoleMarketCashier mc;
	
	public MockRoleMarketCustomer(String name, PersonAgent p) {
		super(name, p);
		this.name = name;
		log = new EventLog();
		
	}

	public boolean pickAndExecuteAnAction() {
		if ( state == customerState.talkToManager) {
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		if ( state == customerState.timeToOrder ) {
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		if ( state == customerState.paying ) {
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		if ( state == customerState.storeClosed ) {
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		
		return false;
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

	@Override
	public void msgHereIsCarAndCheck(Car car, double check) {
		// TODO Auto-generated method stub
		
	}
	
}