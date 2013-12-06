package simcity.interfaces;

//import restaurant.CustomerRole;
//import restaurant.WaiterRole;
//import restaurant.CashierRole.owedBill;

/**
 * Cashier Interface
 *
 * @author Monroe Ekilah
 *
 */
public interface Drew_Cashier {

	public abstract void calculateBill(Drew_Waiter w, String choice, int table, Double debt);

	public abstract void payBill(Double b, Double Money, Drew_Customer customer);
	
	public abstract void msgGoHome(double pay);
	//public abstract void marketBill(Double cost, Market m);

}