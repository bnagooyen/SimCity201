package simcity.interfaces;

import simcity.PersonAgent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RepairMan {
	
	public abstract void msgNeedRepair(String building, Landlord l);
	
	public abstract void msgNeedRepair(String building, PersonAgent p);
	
	public abstract void msgHereIsPayment(double m);	

}
