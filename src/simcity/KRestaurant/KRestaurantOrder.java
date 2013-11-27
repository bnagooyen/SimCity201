package simcity.KRestaurant;

import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;

public class KRestaurantOrder {
	KWaiterRole w;
	String choice;
	int table;
	
	public KRestaurantOrder(KWaiterRole w, String choice, int table) {
		this.w = w;
		this.choice = choice;
		this.table = table;
	}
		
}
