package simcity.interfaces;
/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TCashier {
	
	public abstract void msgComputeBill(TWaiter w, TCustomer c, String order);
	
	
	public abstract void msgHereIsMyMoney(TCustomer c, double m, double cost);

}
