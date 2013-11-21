package simcity.Drew_restaurant.interfaces;

import simcity.Drew_restaurant.CashierRole;
import simcity.Drew_restaurant.CustomerRole.AgentEvent;
import simcity.Drew_restaurant.CustomerRole.AgentState;
import simcity.Drew_restaurant.gui.CustomerGui;
import simcity.Drew_restaurant.gui.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public abstract void gotHungry();
	
	public abstract void wait(int numAH);

	public abstract void followMeToTable(Menu m);
	
	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void whatWouldYouLike();
	
	public abstract void hereIsYourFood();
	
	//From waiter when initial choice is out of stock
	public abstract void outOfChoice(String choice);
	
	public abstract void msgAnimationFinishedLeaveRestaurant();
	
	public abstract void giveCheck(Double b, Cashier c);
	
	public abstract void giveChange(Double change);
	
	public abstract void atCashier();
	
	
	//accessors
	public void setWaiter(Waiter w);

	public String getName();
	
	public CustomerGui getGui();
	
	public double getDebt();
	
}

