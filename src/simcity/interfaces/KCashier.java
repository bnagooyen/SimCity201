package simcity.interfaces;

import simcity.KRestaurant.KWaiterRole;

public interface KCashier {

	public abstract void msgBill(KCustomer c,  KWaiter w, String s);
	public abstract void msgPayment(double payment, KCustomer c, double check);
	public abstract void msgBillFromMarket(double check, MarketCashier cashier);
}
