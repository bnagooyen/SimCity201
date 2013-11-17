package simcity.housing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;







//import restaurant.WaiterRoleTT.customers;
import simcity.PersonAgent;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan;
import agent.Role;

public class LandlordRole extends Role implements Landlord{
	//data
	int hour;  
	double revenue = 0;				//money landlord keeps to pay utilities
	final double rentBill = 50; 	//cost of rent each day
	public enum AgentState 
	{nothing, askingForRent, collectedRent, callMaintanence};
	private AgentState state = AgentState.nothing;
	
	private List<RepairMan>repairmen		//list of repairmen that the landlord can contact
	= Collections.synchronizedList(new ArrayList<RepairMan>()); 
	
	
	List<Tenant>myTenants
	= new ArrayList<Tenant>(); 
	List<Worker>myWorkers
	= new ArrayList<Worker>(); 
	
	class Worker {
		Worker (RepairMan r) {
			myWorker = r; 
		}
		RepairMan myWorker; 
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
	public void TimeUpdate(int hour) {
		this.hour = hour;
		if (hour == 20) {
			state = AgentState.askingForRent;
		}
		if (hour == 10) {
			state = AgentState.callMaintanence; 
		}
	}
	
	public void NewTenant(PersonAgent p) {
		myTenants.add(new Tenant(p));
	}
	
	public void HereIsARentPayment(PersonAgent p, double amount) {
		for (Tenant t:myTenants) {
			if (t.person == p) {
				t.ts = TenantState.paid; 
			}
		}
	}
	
	public void jobDone(RepairMan w, double cost) {
		
	}
	
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if (state == AgentState.askingForRent) {
			CollectRent(); 
		}
		return false;
	}
	
	//actions
	private void CollectRent() {
		for (Tenant t:myTenants) {
			t.person.HereIsYourRentBill(rentBill); 
			t.ts = TenantState.waitingForPayment; 
		}
	}
	
	private void DistributePayments() {
		for (Tenant t:myTenants) {
			t.ts = TenantState.nothing; 
		}
		revenue = revenue *.70;
		state = AgentState.nothing; 
	}
	
	private void CallMaintenance() {
		for (Tenant t:myTenants) {
			
		}
		state = AgentState.nothing; 
	}
	
	private void PayMaintenance() {
		
	}

	//utilities
	public void addRepairMan(RepairMan r) {
		repairmen.add((RepairMan) r);
	}

}
