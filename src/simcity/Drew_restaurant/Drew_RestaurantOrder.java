package simcity.Drew_restaurant;

import simcity.Drew_restaurant.interfaces.*;






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
