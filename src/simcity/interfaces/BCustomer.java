package simcity.interfaces;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;
import simcity.BRestaurant.gui.BCustomerGui;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface BCustomer {
       
	
	public abstract void msgHereisYourCheck(BCheck check);
	public abstract void setWaiter(BWaiter waiter);
	
	public abstract void gotHungry();
	
	public abstract void msgSitAtTable(int tablenumber, BMenu menu, BWaiter waiter);
	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgWhatWouldYouLike(BWaiter waiter);
	public abstract void msgHereisYourOrder(String choice);
	public abstract void msgdecidedOrder();
	public abstract void msgReorder();
	public abstract void setCashier(BCashier cashier);
	public abstract int getHungerLevel();
	public abstract void setHungerLevel(int hungerLevel);
	
	public abstract void setGui(BCustomerGui g);
	public abstract BCustomerGui getGui();
	
	

}