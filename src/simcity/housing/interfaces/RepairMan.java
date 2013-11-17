package simcity.housing.interfaces;

import simcity.PersonAgent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RepairMan {
	
	public abstract void NeedRepair(String building, Landlord l);
	
	public abstract void NeedRepair(String building, PersonAgent p);
	
	public abstract void HereIsPayment(double m);	

}
