package DRestaurant;

import DRestaurant.DOrder.OrderState;
import DRestaurant.DWaiterRole.WaiterState;
import simcity.interfaces.DWaiter;

public class DWaiterSharedDataRole extends DWaiterRole implements DWaiter {

	private DProducerConsumerMonitor theMonitor;
	
	public DWaiterSharedDataRole() {
		super();
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
