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

public class DWaiterSharedDataRole extends DWaiterRole implements DWaiter {

	private DProducerConsumerMonitor theMonitor;
	
	public DWaiterSharedDataRole(SimCityGui gui) {
		super(gui);
		// TODO Auto-generated constructor stub
		type = "waiterShared";
	}

	@Override
	protected void GiveCookOrder(DOrder o) {
		// TODO Auto-generated method stub
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterSharedDataRole", "Sending order to cook");
		o.state=OrderState.ordered;
		state=WaiterState.working; 
		
		theMonitor.insert(o);
		//System.err.println("added order to monitor");
	}
	
	public void setMonitor(DProducerConsumerMonitor m) {
		theMonitor = m;
	}

	@Override
	protected void tellHost() {
		// TODO Auto-generated method stub
			if(WaiterGui==null) {
				WaiterGui = new DWaiterGui(this);
				gui.myPanels.get("Restaurant 3").panel.addGui(WaiterGui);	
			}
			WaiterGui.setPresent(true);
			
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterSharedDataRole", "Telling manager that I can work");
			Do("Telling manager that I can work");
			state=WaiterState.waitingForOnDuty;
			host.msgIAmHere(this, "waiterShared");
		
	}

}
