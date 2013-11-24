package simcity.restaurant.interfaces;

import simcity.DRestaurant.DCheck;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DCheck.CheckState;
import simcity.Market.MarketCashierRole;
import simcity.interfaces.MarketCashier;

public interface Cashier {

	public abstract void msgHereIsAPayment(Customer cust, int tnum, double valCustPaid);
	void msgComputeBill(String choice, Customer cust, String name, int tnum,
			Waiter wa);
	public abstract void setRegisterAmnt(double amnt);
	public abstract double getRegisterAmnt();
	public abstract void msgMadeInventoryOrder(int orderid, double billAmt);
	public abstract void msgBillFromMarket(double check,
			MarketCashier cashier);
		
	
}
