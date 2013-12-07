package simcity.KRestaurant;

import simcity.PersonAgent;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.KWaiter;

public class KWaiterNormalRole extends KWaiterRole implements KWaiter{

	public KWaiterNormalRole(SimCityGui gui) {
		super(gui);
	}

	@Override
	protected void giveCookOrder(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "sending cook order of " + c.choice);
		Do(": sending cook order of " + c.choice);
		c.s = customerstate.waitingForFood;
		cook.msgHereIsAnOrder(this, c.choice, c.table);
		waiterGui.DoLeaveCustomer();
	}

}
