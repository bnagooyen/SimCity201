package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MarketCashierRole {
	List<Order> orders =Collections.synchronizedList(new ArrayList<Order>());
	
	boolean active;
	double marketMoney;
	
	Person p;
	InventoryBoy ib;
	MarketManager manager; 
	
	enum orderState{pending, inquiring, ready, given, paid};
	enum myState{arrived, working, goHome, unavailable};
	
	//Utilities
	
	class Order{
			List<FoodOrder>foodsNeeded = Collections.synchronizedList
	}
}
