package simcity.Drew_restaurant.interfaces;

import java.util.List;

import simcity.Drew_restaurant.CashierRole;
import simcity.Drew_restaurant.CookRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.CustomerRole.AgentEvent;
import simcity.Drew_restaurant.CustomerRole.AgentState;
//import restaurant.WaiterAgent.CustomerState;
import simcity.Drew_restaurant.WaiterRole.MyCustomer;
import simcity.Drew_restaurant.gui.Menu;
import simcity.Drew_restaurant.gui.WaiterGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter {
	
	public abstract void sitAtTable(Customer c, int table);
	
	public abstract void readyToOrder(Customer c);
	
	public abstract void heresMyChoice(Customer c, String ch);
	
	public abstract void orderDone(int table, String choice);
	
	public abstract void DoneEating(Customer c);
	
	public abstract void outOf(String choice, int Table);

	public abstract void msgAtDest();

	public abstract void breakResponse(boolean Response);

	public abstract void heresBill(Double bill, int table);
	
	//Get & Set
	
	public void addCashier(Cashier c);
	
	public void setHost(Host host);
	
	public void setCook(Cook cook);

	public String getName();
	
	public WaiterGui getGui();

	public List<MyCustomer> getWaitingCustomers();

}