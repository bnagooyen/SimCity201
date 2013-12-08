package simcity.interfaces;

import java.util.List;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.Market.MFoodOrder;

public interface KCashier {

	void msgPayment(double myCash, KCustomer kCustomerRole, double check);

	void msgBill(KCustomer c, KWaiter kWaiter, String choice);

	public void msgConfirmingMarketCheck(boolean scammed, MarketManager m);

}
