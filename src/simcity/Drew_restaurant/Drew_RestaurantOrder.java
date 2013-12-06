package simcity.Drew_restaurant;

import simcity.interfaces.*;






public class Drew_RestaurantOrder {
	Drew_Waiter w;
	String choice;
	int table;
	
	public Drew_RestaurantOrder(Drew_Waiter w, String choice, int table) {
		this.w = w;
		this.choice = choice;
		this.table = table;
	}
		
}
