package simcity.LRestaurant;

import simcity.PersonAgent;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.LWaiter;
import simcity.test.mock.LoggedEvent;

public class LWaiterNormalRole extends LWaiterRole implements LWaiter{

	public LWaiterNormalRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers c) {
		LoggedEvent err = new LoggedEvent("Giving order cook");
        log.add(err);
        
		waiterGui.DoGoToCook();
        AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LNormWaiterRole", "Giving cook order");
       Do("Giving cook order");
       c.state = CustomerState.waitForFood;
       //print("Asking for a semaphore. CookOrder1");
       try {
               task.acquire();
       } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
       }
       //print("Got a semaphore");
       cook.msgHereIsAnOrder(c.table, c.choice, this);
       
	}
	
	
}