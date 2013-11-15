package simcity.Market;

import java.util.List;

import agent.Role;
import simcity.Market.MarketCashierRole.orderState;
import simcity.restaurant.interfaces.Cashier;

public class MOrder {
	List<FoodOrder> foodsNeeded;
	List<FoodOrder> canGive;
	orderState state;
	
	Role r;
	String building;
	Cashier cashier;
}
