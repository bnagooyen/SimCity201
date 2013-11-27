package simcity.test.mock;


import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.LWaiterRole;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.test.mock.Mock;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;


public class MockLCustomer extends Mock implements LCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public LCashier cashier;
	public EventLog log = new EventLog();
	
	public MockLCustomer(String name) {
		super(name);
	}

	public void gotHungry(){
		
	}

	public void msgRestaurantIsFull(){
		
	}

	public void msgFollowMe(int tableNumber, LMenu m, LWaiter w){
		LoggedEvent e = new LoggedEvent("Received msgFollowMe");
		log.add(e);
	}

	public void msgPleaseReorder(LMenu m){
		
	}

	public void msgWhatWouldYouLike(){
		
	}

	public void msgHereIsFood(){
		
	}

	public void msgHereIsCheck(int check){
		
	}

	public void msgHereIsChange(int cash) {
		if(cash == -1){
			log.add(new LoggedEvent("Received msgHereIsChange from cashier, owe money."));
		}
		else{
			log.add(new LoggedEvent("Received msgHereIsChange from cashier."));
		}
	}
	
	public void msgAnimationFinishedGoToSeat(){
		
	}
	
	public void msgAnimationFinishedLeaveRestaurant(){
		
	}


	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgRestaurantClosed() {
		// TODO Auto-generated method stub
		
	}



}
