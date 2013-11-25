package simcity.interfaces;

import simcity.DRestaurant.DMenu;
import simcity.DRestaurant.DWaiterRole;

public interface DCustomer {
	public abstract void gotHungry(); 
	public void msgNoRoomForYou();

	public abstract void msgFollowMe(DMenu menu, int tnum, DWaiter w);
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
