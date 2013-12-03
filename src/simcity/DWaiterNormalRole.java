package simcity;

import simcity.interfaces.DWaiter;
import simcity.DOrder.OrderState;

public class DWaiterNormalRole extends DWaiterRole implements DWaiter {

	public DWaiterNormalRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void GiveCookOrder(DOrder o) {
		
		Do("giving order to cook");
		cook.msgHereIsAnOrder(o);
		
		o.state=OrderState.ordered;
		state=WaiterState.working; 
		
	}

}
