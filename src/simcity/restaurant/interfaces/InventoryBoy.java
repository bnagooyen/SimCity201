package simcity.restaurant.interfaces;

import simcity.Market.MOrder;

public interface InventoryBoy {
	public abstract void msgCheckInventory(MOrder o);
	public abstract void msgGoHome();

}
