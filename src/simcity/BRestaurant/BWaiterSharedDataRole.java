package simcity.BRestaurant;

import simcity.PersonAgent;
import simcity.BRestaurant.*;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.BWaiter;
import simcity.interfaces.KWaiter;
import simcity.KRestaurant.KRestaurantOrder;

public class BWaiterSharedDataRole extends BWaiterRole implements BWaiter{

    private BOrderStand theMonitor;

        
        public BWaiterSharedDataRole() {
                //super(g);
        }

        protected void giveCookOrder(myCustomer c) {
				AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "sending cook order of " + c.choice);
                Do(": sending cook order of " + c.choice);
                c.cusState = customerState.noAction;
                //waiterGui.DoGoToCook();
                try {
                        atCook.acquire();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                BRotatingOrders o = new BRotatingOrders(this, c.choice, c.tablenumber);
                theMonitor.insert(o);
                //waiterGui.DoLeaveCustomer();
        }
        
        public void setMonitor(BOrderStand m) {
                theMonitor = m;
        }
 
        
}