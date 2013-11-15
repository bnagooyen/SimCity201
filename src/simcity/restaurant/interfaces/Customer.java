package simcity.restaurant.interfaces;

import simcity.restaurant.Check;
import simcity.restaurant.CustomerRole;
import simcity.restaurant.Menu;

import simcity.restaurant.CustomerRole.AgentEvent;
import simcity.restaurant.WaiterRole;

public interface Customer {
	public abstract void gotHungry(); 
	public void msgNoRoomForYou();

	public abstract void msgFollowMe(Menu menu, int tnum, WaiterRole w);
	public abstract void msgHereIsYourBill(int tnum, double d);
	

	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void msgWhatWouldYouLike();
	public abstract void msgWhatWouldYouLike(String foodOutOfStock);
	
	public abstract void msgFoodIsServed();
	
	public abstract void msgHereIsYourReceiptAndChange(double num);
	public abstract void msgAnimationArrivedAtCashier();
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgGoToHangout();
	public abstract void msgYourTableIsReady();
}
