package simcity.DRestaurant;

import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.interfaces.DWaiter;

public class DWaiterNormalRole extends DWaiterRole implements DWaiter {

	public DWaiterNormalRole() {
		super();
		// TODO Auto-generated constructor stub
		type="waiterNormal";
	}

	@Override
	protected void GiveCookOrder(DOrder o) {
		
		Do("giving order to cook");
		cook.msgHereIsAnOrder(o);
		
		o.state=OrderState.ordered;
		state=WaiterState.working; 
		
	}

}
