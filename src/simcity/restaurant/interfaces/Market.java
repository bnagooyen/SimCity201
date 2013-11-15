package simcity.restaurant.interfaces;

import java.util.ArrayList;

public interface Market {
	
	public abstract void msgHereIsAnInventoryOrder(ArrayList<simcity.restaurant.FoodOrder> orderToMarket, int id, Cashier c);
	public void msgHereIsAPayment(double val, Cashier ca);
	
}
