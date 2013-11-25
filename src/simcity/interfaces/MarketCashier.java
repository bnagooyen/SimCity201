package simcity.interfaces;

import java.util.List;

import agent.Role;
import simcity.DRestaurant.DCashierRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Transportation.CarAgent;
import simcity.restaurant.interfaces.Cashier;

public interface MarketCashier {
	public abstract void msgOrder(MarketCustomer c, List<MFoodOrder> foods, String building);
	public abstract void msgOrder(Cook cook, List<MFoodOrder> foods, String building);
	public abstract void msgCanGive(MOrder o);
	public abstract void msgHereIsPayment(Role r, double payment);
	public abstract void msgGoHome(double paycheck);
	public abstract void msgCanGive(Car currCar, MOrder o);
	public abstract void msgCarOrder(MarketCustomer c, String building);

}
