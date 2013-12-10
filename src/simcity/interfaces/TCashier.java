package simcity.interfaces;
/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TCashier {
	
	public abstract void msgComputeBill(TWaiter w, TCustomer c, String order);
	
	public abstract void msgBillFromMarket(double check, MarketCashier m, MarketManager manager); 
	
	public abstract void msgGoHome(double moneys); 

}
