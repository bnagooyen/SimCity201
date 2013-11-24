package simcity.LRestaurant;

import simcity.PersonAgent;
import simcity.LRestaurant.LRestaurantOrder;
import simcity.LRestaurant.ProducerConsumerMonitor;
import simcity.LRestaurant.LWaiterRole.CustomerState;
import simcity.interfaces.LWaiter;

public class LWaiterSharedDataRole extends LWaiterRole implements LWaiter{

	private ProducerConsumerMonitor theMonitor;
	
	public LWaiterSharedDataRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers c) {
		waiterGui.DoGoToCook();
		Do("Sending cook order");
		c.state = CustomerState.waitForFood;
		
		
		try {
			task.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LRestaurantOrder o = new LRestaurantOrder(this, c.choice, c.table);
		theMonitor.insert(o);
		waiterGui.DoLeaveCustomer();
		
	}
	
	public void setMonitor(ProducerConsumerMonitor m) {
		theMonitor = m;
	}
	
	
}