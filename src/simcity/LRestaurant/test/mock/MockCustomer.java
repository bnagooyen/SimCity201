package simcity.LRestaurant.test.mock;


import simcity.LRestaurant.LCashierAgent;
import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.interfaces.LCashier;
import simcity.LRestaurant.interfaces.LCustomer;
import simcity.LRestaurant.interfaces.LWaiter;


public class MockCustomer extends Mock implements LCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public LCashier cashier;
	public EventLog log = new EventLog();
	
	public MockCustomer(String name) {
		super(name);
	}

	public void gotHungry(){
		
	}

	public void msgRestaurantIsFull(){
		
	}

	public void msgFollowMe(int tableNumber, LMenu m, LWaiter w){
		
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



}
