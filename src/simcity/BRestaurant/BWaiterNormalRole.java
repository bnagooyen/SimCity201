package simcity.BRestaurant;

import simcity.PersonAgent;
import simcity.BRestaurant.gui.BRestaurantGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.BWaiter;
import simcity.BRestaurant.*;

public class BWaiterNormalRole extends BWaiterRole implements BWaiter{

        public BWaiterNormalRole() {
                //super(p);
        }

        
        public void giveCookOrder(myCustomer c) {
				AlertLog.getInstance().logMessage(AlertTag.BRestaurant, "BWaiter", "sending cook order of " + c.choice);
                Do(": sending cook order of " + c.choice);
                c.cusState = customerState.noAction;
                cook.msgHereisanOrder(this, c.choice, c.tablenumber);
                //waiterGui.DoLeaveCustomer();
        }

}