package simcity.LRestaurant.interfaces;

import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LCustomerRole.AgentEvent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface LCustomer {
	public abstract void gotHungry();

	public abstract void msgRestaurantIsFull();

	public abstract void msgFollowMe(int tableNumber, LMenu m, LWaiter w);

	public abstract void msgPleaseReorder(LMenu m);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgHereIsFood();

	public abstract void msgHereIsCheck(int check);

	public abstract void msgHereIsChange(int cash);

	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract String getCustomerName();
}