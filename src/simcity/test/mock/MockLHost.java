package simcity.test.mock;


import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.LWaiterRole;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.test.mock.Mock;
import simcity.interfaces.Host;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCook;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LHost;
import simcity.interfaces.LWaiter;


public class MockLHost extends Mock implements LHost{

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public LCashier cashier;
	public EventLog log = new EventLog();
	
	public MockLHost(String name) {
		super(name);
	}

	public void msgLeftLine(){
		LoggedEvent e = new LoggedEvent("Received msgLeftLine");
		log.add(e);
	}

	@Override
	public void msgChoseToLeave(LCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgChoseToWait(LCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToWork(LWaiterRole waiterRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantToGoOnBreak(LWaiterRole waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToEat(LCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgEmptyTable(int tableNum, LWaiterRole waiter, LCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(LCook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(LWaiter lw) {
		// TODO Auto-generated method stub
		
	}
	
}

