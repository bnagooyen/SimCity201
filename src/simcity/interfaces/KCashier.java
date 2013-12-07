package simcity.interfaces;

import java.util.List;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.Market.MFoodOrder;

public interface KCashier {

	void msgPayment(double myCash, KCustomerRole kCustomerRole, double check);

	void msgBill(KCustomer c, KWaiterRole kWaiterRole, String choice);

	public void msgConfirmingMarketCheck(boolean scammed, MarketManager m);

}
