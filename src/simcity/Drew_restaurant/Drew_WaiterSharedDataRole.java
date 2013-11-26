package simcity.Drew_restaurant;

import simcity.Drew_restaurant.interfaces.Drew_Waiter;





public class Drew_WaiterSharedDataRole extends Drew_WaiterRole implements Drew_Waiter{

    private ProducerConsumerMonitor theMonitor;

	
	public Drew_WaiterSharedDataRole() {
		super();
	}

	protected void putInOrder(MyCustomer c) {
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

