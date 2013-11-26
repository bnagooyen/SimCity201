package simcity.DRestaurant;

import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.DWaiterRole.WaiterState;
import simcity.interfaces.DWaiter;
import simcity.PersonAgent;

public class DWaiterSharedRole extends DWaiterRole implements DWaiter {

	private DProducerConsumerMonitor theMonitor;
	
	public DWaiterSharedRole(PersonAgent name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void GiveCookOrder(DOrder o) {
		// TODO Auto-generated method stub
		o.state=OrderState.ordered;
		state=WaiterState.working; 
		
		theMonitor.insert(o);
		//System.err.println("added order to monitor");
	}
	
	public void setMonitor(DProducerConsumerMonitor m) {
		theMonitor = m;
	}

}
