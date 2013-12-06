package simcity.LRestaurant;

import simcity.PersonAgent;
import simcity.LRestaurant.LRestaurantOrder;
import simcity.LRestaurant.ProducerConsumerMonitor;
import simcity.LRestaurant.LWaiterRole.CustomerState;
import simcity.interfaces.LWaiter;
import simcity.test.mock.LoggedEvent;

public class LWaiterSharedDataRole extends LWaiterRole implements LWaiter{

	private ProducerConsumerMonitor theMonitor;
	
	public LWaiterSharedDataRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers c) {
		System.out.println(c.state);
		LoggedEvent e = new LoggedEvent("Giving order cook");
        log.add(e);
		
		//waiterGui.DoGoToCook();
		Do("Sending cook order");
		c.state = CustomerState.waitForFood;
		
		/**
		try {
			task.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		LRestaurantOrder o = new LRestaurantOrder(this, c.choice, c.table);
		theMonitor.insert(o);
		//waiterGui.DoLeaveCustomer();
		
	}
	
	public void setMonitor(ProducerConsumerMonitor m) {
		theMonitor = m;
	}
	
	
}