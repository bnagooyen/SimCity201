package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCashierRole.orderState;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestaurantCashier;
import simcity.restaurant.CashierRole;
import simcity.restaurant.interfaces.Cashier;

public class MOrder {
	List<MFoodOrder>foodsNeeded = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	List<MFoodOrder>canGive = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	orderState state;
	MarketCustomer c;
	Cook cook;
	String building;
	RestaurantCashier cashier;
	
	MOrder(List<MFoodOrder> foods, String building, MarketCustomer c, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.c = c;
		this.state = state;
		cook = null;
		cashier = null;
	}
	
	MOrder(List<MFoodOrder> foods, String building, Cook cook, RestaurantCashier c, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.cook = cook;
		c = null;
		this.state = state;
		cashier = c;
	}

	public MOrder find(MOrder o) {
		// TODO Auto-generated method stub
		return null;
	}

	public MOrder find(Role r) {
		// TODO Auto-generated method stub
		return null;
	}
}
