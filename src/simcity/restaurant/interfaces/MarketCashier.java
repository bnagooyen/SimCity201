package simcity.restaurant.interfaces;

import java.util.List;

import agent.Role;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;

public interface MarketCashier {
	public abstract void msgOrder(Role r, List<MFoodOrder> order, String s);
	public abstract void msgCanGive(MOrder o);
	public abstract void msgHereIsPayment(Role r, double payment);
}
