package simcity.KRestaurant;

import simcity.PersonAgent;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.interfaces.KWaiter;

public class KWaiterNormalRole extends KWaiterRole implements KWaiter{

	public KWaiterNormalRole(PersonAgent p, KCookRole c, KRestaurantGui g,
			KHostRole h, KCashierRole cashier, int homepos) {
		super(p, c, g, h, cashier, homepos);
	}

	@Override
	protected void giveCookOrder(MyCustomer c) {
		Do(": sending cook order of " + c.choice);
		c.s = customerstate.waitingForFood;
		cook.msgHereIsAnOrder(this, c.choice, c.table);
		waiterGui.DoLeaveCustomer();
	}

}
