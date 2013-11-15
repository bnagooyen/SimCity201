package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCashierRole.orderState;
import simcity.restaurant.CashierRole;
import simcity.restaurant.interfaces.Cashier;

public class MOrder {
	List<MFoodOrder>foodsNeeded = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	List<MFoodOrder>canGive = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	orderState order;
	Role r;
	String building;
	CashierRole cashier;
	
	MOrder(List<MFoodOrder> foods, String building, Role r, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.r = r;
		order = state;
		cashier = null;
	}
	
	MOrder(List<MFoodOrder> foods, String building, Role r, CashierRole c, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.r = r;
		order = state;
		cashier = c;
	}
}
