package simcity.interfaces;

import java.util.List;

import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.restaurant.CashierRole;

public interface MarketCashier {
	public abstract void msgOrder(MarketCustomer c, List<MFoodOrder> foods, String building);
	public abstract void msgOrder(Cook cook, List<MFoodOrder> foods, String building, RestaurantCashier c);
	public abstract void msgCanGive(MOrder o);
	public abstract void msgHereIsPayment(Role r, double payment);
	public abstract void msgGoHome();

}
