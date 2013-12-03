package simcity;

import simcity.interfaces.DWaiter;
import simcity.DOrder.OrderState;
import simcity.DWaiterRole.WaiterState;

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
