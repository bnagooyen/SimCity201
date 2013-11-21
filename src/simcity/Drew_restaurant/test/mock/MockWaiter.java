package simcity.Drew_restaurant.test.mock;


import java.util.ArrayList;
import java.util.List;

import simcity.Drew_restaurant.WaiterRole.MyCustomer;
import simcity.Drew_restaurant.gui.WaiterGui;
import simcity.Drew_restaurant.interfaces.Drew_Cashier;
import simcity.Drew_restaurant.interfaces.Drew_Cook;
import simcity.Drew_restaurant.interfaces.Drew_Customer;
import simcity.Drew_restaurant.interfaces.Drew_Waiter;
import simcity.Drew_restaurant.interfaces.Drew_Host;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Drew_Waiter {
	


	//Data
	public Drew_Cashier cashier;
	public Drew_Host host;
	public Drew_Customer customer;
	public EventLog log;
	public String name;
	public double debt;
	public WaiterGui waitergui;
	
	public List<MyCustomer> customers
	= new ArrayList<MyCustomer>();
	
	//functions
	public MockWaiter(String n) {
		super(n);
		//name=n;
	}
	
	public void sitAtTable(Drew_Customer c, int table){
		
	}
	
	public void readyToOrder(Drew_Customer c){
		
	}
	
	public void heresMyChoice(Drew_Customer c, String ch){
		
	}
	
	public void orderDone(int table, String choice){
		
	}
	
	public void DoneEating(Drew_Customer c){
		
	}
	
	public void outOf(String choice, int Table){
		
	}

	public void msgAtDest(){
		
	}

	public void breakResponse(boolean Response){
		
	}

	public void heresBill(Double bill, int table){
		System.out.println("Waiter: Bill is for $"+bill);
	}
	
	//Get & Set
	
	public void addCashier(Drew_Cashier c){
		
	}
	
	public void setHost(Drew_Host host){
		
	}
	
	public void setCook(Drew_Cook cook){
		
	}

	public String getName(){
		return name;
	}
	
	public WaiterGui getGui(){
		return waitergui;
	}

	public List<MyCustomer> getWaitingCustomers(){
		return customers;
	}

}
