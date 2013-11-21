package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Role;
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MarketCashierRole.orderState;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestaurantCashier;
import simcity.restaurant.interfaces.Cashier;

public class MOrder {
	public List<MFoodOrder>foodsNeeded = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	public List<MFoodOrder>canGive = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	public orderState state;
	public MarketCustomer c;
	public Cook cook;
	String building;
	public Cashier cashier;
	
	public MOrder(List<MFoodOrder> foods, String building, MarketCustomer c, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.c = c;
		this.state = state;
		cook = null;
		cashier = null;
	}
	
	public MOrder(List<MFoodOrder> foods, String building, Cook cook, Cashier c, orderState state){
		foodsNeeded = foods;
		this.building = building;
		this.cook = cook;
		c = null;
		this.state = state;
		cashier = c;
	}

	public MOrder find(Role r) {
		// TODO Auto-generated method stub
		return null;
	}
}
