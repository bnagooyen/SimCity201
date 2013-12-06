package simcity.interfaces;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface THost {
	
	public abstract void msgIWantFood(TCustomer cust);

	public abstract void msgCustomerLeft(TCustomer cust);
	
	public abstract void msgTiredOfWaiting(TCustomer cust);
	
	public abstract void msgBreakPlease(TWaiter waiter);


}
