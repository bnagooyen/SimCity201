package simcity.KRestaurant;

import simcity.PersonAgent;
import simcity.KRestaurant.KWaiterRole.customerstate;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.interfaces.KWaiter;
import simcity.KRestaurant.KRestaurantOrder;

public class KWaiterSharedDataRole extends KWaiterRole implements KWaiter{

    private ProducerConsumerMonitor theMonitor;

	
	public KWaiterSharedDataRole(PersonAgent p, KCookRole c, KRestaurantGui g,
			KHostRole h, KCashierRole cashier, int homepos) {
		super(p, c, g, h, cashier, homepos);
	}

	protected void giveCookOrder(MyCustomer c) {
		Do(": sending cook order of " + c.choice);
		c.s = customerstate.waitingForFood;
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		KRestaurantOrder o = new KRestaurantOrder(this, c.choice, c.table);
		theMonitor.insert(o);
		waiterGui.DoLeaveCustomer();
	}
	
	public void setMonitor(ProducerConsumerMonitor m) {
		theMonitor = m;
	}
 
	
}

