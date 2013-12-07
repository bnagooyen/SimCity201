package simcity.interfaces;

import java.util.ArrayList;

public interface Market {
	
	public abstract void msgHereIsAnInventoryOrder(ArrayList<simcity.DRestaurant.DFoodOrder> orderToMarket, int id, DCashier c);
	public void msgHereIsAPayment(double val, DCashier ca);
	
}
