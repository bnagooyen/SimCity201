package restaurant.interfaces;

import restaurant.Check;
import restaurant.CustomerRole;
import restaurant.WaiterAgent;
import restaurant.Check.CheckState;

public interface Cashier {

	public abstract void msgHereIsAPayment(Customer cust, int tnum, double valCustPaid);
	void msgComputeBill(String choice, Customer cust, String name, int tnum,
			Waiter wa);
	public abstract void setRegisterAmnt(double amnt);
	public abstract double getRegisterAmnt();
}
