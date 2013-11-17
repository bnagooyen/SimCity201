package simcity.housing.interfaces;

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
	
	public abstract void NewTenant(PersonAgent p);
	
	public abstract void HereIsARentPayment(PersonAgent p, double amount);
	
	public abstract void jobDone(RepairMan w, double cost);
}
