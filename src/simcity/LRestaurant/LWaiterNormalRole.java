package simcity.LRestaurant;

import simcity.PersonAgent;
import simcity.interfaces.LWaiter;

public class LWaiterNormalRole extends LWaiterRole implements LWaiter{

	public LWaiterNormalRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers c) {
		//waiterGui.DoGoToCook();
       Do("Giving cook order");
       c.state = CustomerState.waitForFood;
       //print("Asking for a semaphore. CookOrder1");
       /**
       try {
               task.acquire();
       } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
       }
       */
       //print("Got a semaphore");
       cook.msgHereIsAnOrder(c.table, c.choice, this);
       
	}
	
	
}