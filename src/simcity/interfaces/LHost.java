package simcity.interfaces;

import agent.Role;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.LMenu;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LCustomerRole.AgentEvent;
import simcity.LRestaurant.LHostRole.CustomerState;
import simcity.LRestaurant.LHostRole.MyCustomers;
import simcity.LRestaurant.LHostRole.Table;
import simcity.LRestaurant.LHostRole.WaiterState;
import simcity.LRestaurant.LHostRole.myWaiter;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface LHost extends Host {
	public abstract void msgLeftLine();
	 public void msgIAmHere(Role r, String type);
 
 public abstract void msgChoseToLeave(LCustomer c);

 public abstract void msgChoseToWait(LCustomer c);

 public abstract void msgReadyToWork(LWaiterRole waiterRole);

 public abstract void msgWantToGoOnBreak(LWaiterRole waiter);
 public abstract void msgIWantToEat(LCustomer c);


 public abstract void msgEmptyTable(int tableNum, LWaiterRole waiter, LCustomer c);

public abstract void setCook(LCook cook);

public abstract void addWaiter(LWaiter lw);

public abstract void msgHereIsMoney(double money);

}