package simcity.DRestaurant;

import simcity.DRestaurant.DOrder.OrderState;
import simcity.interfaces.DWaiter;
import simcity.PersonAgent;

public class DWaiterNormalRole extends DWaiterRole implements DWaiter {

	public DWaiterNormalRole(PersonAgent name) {
		super(name);
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
