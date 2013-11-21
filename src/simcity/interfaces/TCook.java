package simcity.interfaces;

import java.util.Map;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TCook {
	
	public abstract void msgHereIsAnOrder(int t, String choice, TWaiter headWaiterRoleTT); 

	public abstract void msgOrderFulfilled(Map<String, Integer> nS);
	
	public abstract void msgUnfulfilledStock(); 

}
