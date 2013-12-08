package simcity.DRestaurant;

import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.DWaiterRole.WaiterState;
import simcity.gui.SimCityGui;
import simcity.gui.DGui.DWaiterGui;
import simcity.interfaces.DWaiter;

public class DWaiterNormalRole extends DWaiterRole implements DWaiter {

	public DWaiterNormalRole(SimCityGui gui) {
		super(gui);
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

	@Override
	protected void tellHost() {
		// TODO Auto-generated method stub
			if(WaiterGui==null) {
				WaiterGui = new DWaiterGui(this);
				gui.myPanels.get("Restaurant 3").panel.addGui(WaiterGui);	
			}
			WaiterGui.setPresent(true);
			
			Do("Telling manager that I can work");
			state=WaiterState.working;
			host.msgIAmHere(this, "waiterNormal");
		
	}

}
