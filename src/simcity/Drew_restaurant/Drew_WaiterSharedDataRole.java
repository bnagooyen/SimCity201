package simcity.Drew_restaurant;

import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Drew_Waiter;





public class Drew_WaiterSharedDataRole extends Drew_WaiterRole implements Drew_Waiter{

    private ProducerConsumerMonitor theMonitor;

	
	public Drew_WaiterSharedDataRole(SimCityGui gui) {
		super(gui);
	}

	protected void putInOrder(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewWaiter", "sending cook order of " + c.choice);
		Do(": sending cook order of " + c.choice);
		c.s = CustomerState.waitingForFood;
		//LoggedEvent e = new LoggedEvent("putting order on rotating stand");
		//log.add(e);
		waitergui.goToKitchen();
		finishTask();
		Drew_RestaurantOrder o = new Drew_RestaurantOrder(this, c.choice, c.t);
		theMonitor.insert(o);
		//waiterGui.DoLeaveCustomer();
	}
	
	public void setMonitor(ProducerConsumerMonitor m) {
		theMonitor = m;
	}
 
	
}

