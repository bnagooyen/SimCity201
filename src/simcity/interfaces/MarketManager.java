package simcity.interfaces;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Market.MarketManagerRole.MyCustomer;
import simcity.Market.MarketManagerRole.MyMarketCashier;
import simcity.Market.MarketManagerRole.workerState;
import agent.Role;

public interface MarketManager {

	public abstract void msgTimeUpdate(int hour);
	
	public void msgRestaurantClosed(Role r);
	public void msgRestaurantOpen(Role r);
	
	public abstract void msgIAmHere(Role r, String type);
	public abstract void msgIAmHere(Role r, List<MFoodOrder>need, String building, String type, RestaurantCashier cashier);
	
	public abstract void msgCustomerDone(MarketCashier mc, Role r);
	public abstract void msgHereIsMoney(double money);

	public abstract void msgLoadDeliveryTruck(MarketCashier cashier, List<MFoodOrder>deliver, String location, double bill, Cook r);
	public abstract void msgBackFromDelivery();
	
}
