package simcity.interfaces;

import simcity.KRestaurant.gui.KMovingFoodGui;

public interface KWaiter {
	public KCashier c = null;
	
	public abstract void msgHereIsCheck(KCustomer c, double price);

	public abstract void msgOutOf(int table, String choice);

	public abstract void msgOrderIsReady(String choice, int table,
			KMovingFoodGui gui, int grill);

	
}
