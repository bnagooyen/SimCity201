package simcity.Drew_restaurant.interfaces;

//import restaurant.CustomerRole;
//import restaurant.WaiterRole;
//import restaurant.CashierRole.owedBill;

/**
 * Cashier Interface
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {

	public abstract void calculateBill(Waiter w, String choice, int table, Double debt);

	public abstract void payBill(Double b, Double Money, Customer customer);
	
	//public abstract void marketBill(Double cost, Market m);

}