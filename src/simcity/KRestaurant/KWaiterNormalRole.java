package simcity.KRestaurant;

import simcity.PersonAgent;
import simcity.gui.SimCityGui;
import simcity.interfaces.KWaiter;

public class KWaiterNormalRole extends KWaiterRole implements KWaiter{

	public KWaiterNormalRole(SimCityGui gui) {
		super(gui);
	}

	@Override
	protected void giveCookOrder(MyCustomer c) {
		Do(": sending cook order of " + c.choice);
		c.s = customerstate.waitingForFood;
		cook.msgHereIsAnOrder(this, c.choice, c.table);
		waiterGui.DoLeaveCustomer();
	}

}
