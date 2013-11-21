package simcity.LRestaurant.interfaces;

import restaurant.Menu;
import restaurant.WaiterRole;
import restaurant.CustomerRole.AgentEvent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	public abstract void gotHungry();

	public abstract void msgRestaurantIsFull();

	public abstract void msgFollowMe(int tableNumber, Menu m, Waiter w);

	public abstract void msgPleaseReorder(Menu m);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgHereIsFood();

	public abstract void msgHereIsCheck(int check);

	public abstract void msgHereIsChange(int cash);

	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract String getCustomerName();
}