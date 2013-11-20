package simcity.interfaces;

import simcity.PersonAgent;
//import simcity.housing.LandlordRole.State;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Landlord {
	
	public abstract void TimeUpdate(int hour);
	
	public abstract void HereIsARentPayment(Integer AN, double cost);
	
	public abstract void CannotPayForRent(Integer AN); 
	
	public abstract void jobDone(String l, double cost);
}
