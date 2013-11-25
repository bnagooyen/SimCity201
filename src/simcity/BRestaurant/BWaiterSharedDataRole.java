package simcity.BRestaurant;

import simcity.PersonAgent;
import simcity.BRestaurant.*;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.interfaces.BWaiter;
import simcity.interfaces.KWaiter;
import simcity.KRestaurant.KRestaurantOrder;

public class BWaiterSharedDataRole extends BWaiterRole implements BWaiter{

    private BOrderStand theMonitor;

	
	public BWaiterSharedDataRole(PersonAgent p) {
		super(p);
	}

	protected void giveCookOrder(myCustomer c) {
		Do(": sending cook order of " + c.choice);
		c.cusState = customerState.noAction;
		//waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BRotatingOrders o = new BRotatingOrders(this, c.choice, c.tablenumber);
		theMonitor.insert(o);
		//waiterGui.DoLeaveCustomer();
	}
	
	public void setMonitor(BOrderStand m) {
		theMonitor = m;
	}
 
	
}

