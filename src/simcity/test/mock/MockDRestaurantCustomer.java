package simcity.test.mock;


import simcity.DRestaurant.DMenu;
import simcity.DRestaurant.DWaiterRole;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCustomer;
import simcity.interfaces.RestaurantCashier;

public class MockDRestaurantCustomer extends Mock implements DCustomer {
	
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantCashier cashier;
	public EventLog log;
	
	public MockDRestaurantCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgNoRoomForYou() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(DMenu menu, int tnum, DWaiterRole w) {
		// TODO Auto-generated method stub
		LoggedEvent e = new LoggedEvent("told to sit at table");
		log.add(e);
	}

	@Override
	public void msgHereIsYourBill(int tnum, double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(String foodOutOfStock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodIsServed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourReceiptAndChange(double num) {
		// TODO Auto-generated method stub
		LoggedEvent e = new LoggedEvent("Received msgChange from cashier. Total = " + num);
		log.add(e);
	}

	@Override
	public void msgAnimationArrivedAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToHangout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYourTableIsReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantIsClosed() {
		// TODO Auto-generated method stub
		
	}


}
