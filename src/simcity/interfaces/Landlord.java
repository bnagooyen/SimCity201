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
	
	public abstract void msgTimeUpdate(int hour);
	
	public abstract void msgHereIsARentPayment(Integer AN, double cost);
	
	public abstract void msgHereIsARentPayment(PersonAgent p, double amount); 
	
	public abstract void msgCannotPayForRent(Integer AN);

	public abstract void msgCannotPayForRent(PersonAgent p); 
	
	public abstract void msgJobDone(String l, double cost);
	
}
