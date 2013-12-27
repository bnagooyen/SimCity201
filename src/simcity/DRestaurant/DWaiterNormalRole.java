/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant;

import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.DWaiterRole.WaiterState;
import simcity.gui.SimCityGui;
import simcity.DRestaurant.DGui.DWaiterGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.DWaiter;

public class DWaiterNormalRole extends DWaiterRole implements DWaiter {

	public DWaiterNormalRole(SimCityGui gui) {
		super(gui);
		// TODO Auto-generated constructor stub
		type="waiterNormal";
	}

	@Override
	protected void GiveCookOrder(DOrder o) {
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterNormalRole", "Giving order to cook");
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
			
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterNormalRole", "Telling manager that I can work");
			Do("Telling manager that I can work");
			state=WaiterState.waitingForOnDuty;
			host.msgIAmHere(this, "waiterNormal");
		
	}

}
