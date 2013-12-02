package simcity.interfaces;

import java.util.ArrayList;

public interface Market {
	
	public abstract void msgHereIsAnInventoryOrder(ArrayList<simcity.DFoodOrder> orderToMarket, int id, Cashier c);
	public void msgHereIsAPayment(double val, Cashier ca);
	
}
