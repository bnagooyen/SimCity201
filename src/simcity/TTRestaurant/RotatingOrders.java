package simcity.TTRestaurant;

import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;

public class RotatingOrders {
	TWaiterRole w;
	String choice;
	int table;
	
	public RotatingOrders(TWaiterRole w, String choice, int table) {
		this.w = w;
		this.choice = choice;
		this.table = table;
	}
		
}
