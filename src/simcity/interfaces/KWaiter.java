package simcity.interfaces;

import simcity.KRestaurant.gui.KMovingFoodGui;

public interface KWaiter {
	public KCashier c = null;
	
	public abstract void msgHereIsCheck(KCustomer c, double price);

	
}
