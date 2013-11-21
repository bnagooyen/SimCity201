package simcity.LRestaurant.test.mock;


import restaurant.CashierAgent;
import restaurant.Menu;
import restaurant.WaiterRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;


public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	
	public MockCustomer(String name) {
		super(name);
	}

	public void gotHungry(){
		
	}

	public void msgRestaurantIsFull(){
		
	}

	public void msgFollowMe(int tableNumber, Menu m, Waiter w){
		
	}

	public void msgPleaseReorder(Menu m){
		
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
