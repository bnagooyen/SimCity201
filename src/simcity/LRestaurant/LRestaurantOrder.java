package simcity.LRestaurant;

import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;

public class LRestaurantOrder {
	LWaiterRole w;
	String choice;
	int table;
	
	public LRestaurantOrder(LWaiterRole w, String choice, int table) {
		this.w = w;
		this.choice = choice;
		this.table = table;
	}
		
}
