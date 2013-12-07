package simcity.interfaces;

//import simcity.Drew_restaurant.Drew_CashierRole;
//import simcity.Drew_restaurant.Drew_CustomerRole.AgentEvent;
//import simcity.Drew_restaurant.Drew_CustomerRole.AgentState;
import simcity.Drew_restaurant.gui.Drew_CustomerGui;
import simcity.Drew_restaurant.gui.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Drew_Customer {
	
	public abstract void gotHungry();
	
	public abstract void wait(int numAH);

	public abstract void followMeToTable(Menu m);
	
	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void whatWouldYouLike();
	
	public abstract void hereIsYourFood();
	
	//From waiter when initial choice is out of stock
	public abstract void outOfChoice(String choice);
	
	public abstract void msgAnimationFinishedLeaveRestaurant();
	
	public abstract void giveCheck(Double b, Drew_Cashier c);
	
	public abstract void giveChange(Double change);
	
	public abstract void atCashier();
	
	public abstract void msgGoHome();
	
	
	//accessors
	public void setWaiter(Drew_Waiter w);

	public String getName();
	
	public Drew_CustomerGui getGui();
	
	public double getDebt();
	
}

