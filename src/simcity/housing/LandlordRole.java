package simcity.housing;

import java.util.ArrayList;
import java.util.List;

//import restaurant.WaiterRoleTT.customers;
import simcity.PersonAgent;
import agent.Role;

public class LandlordRole extends Role{
	int hour;  
	double revenue = 0;				//money landlord keeps to pay utilities
	final double rentBill = 50; 	//cost of rent each day
	public enum State 
	{nothing, askingForRent, collectedRent, callMaintanence};
	private State state = State.nothing; 
	
	List<Tenant>myTenants
	= new ArrayList<Tenant>(); 
	List<Worker>myWorkers
	= new ArrayList<Worker>(); 
	
	class Worker {
		Worker (RepairManRole r) {
			myWorker = r; 
		}
		RepairManRole myWorker; 
		double bill; 
		WorkerState ws; 
	}
	
	class Tenant {
		Tenant (PersonAgent p) {
			person = p; 
		}
		PersonAgent person; 
		String location; 
		TenantState ts; 
	}
	
	enum WorkerState 
	{working, paying};
	
	enum TenantState
	{nothing, waitingForPayment, paid, ShortOnMoney}; 
	

	protected LandlordRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	public void TimeUpdate(int hr) {
		hour = hr; 
		if (hour == 20) {
			state = State.askingForRent;
		}
		if (hour == 10) {
			state = State.callMaintanence; 
		}
	}
	
	public void AddWorker (RepairManRole w) {
		myWorkers.add(new Worker(w)); 
	}
	
	public void NewTenant(PersonAgent p) {
		myTenants.add(new Tenant(p));
	}
	
	public void HereIsARentPayment(PersonAgent p, double amount) {
		for (PersonAgent p:myTenants.person) {
			
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}


}
