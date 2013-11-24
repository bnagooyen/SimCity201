package simcity.housing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import simcity.PersonAgent;
import simcity.interfaces.BankManager;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan;
import agent.Role;

public class LandlordRole extends Role implements Landlord{
	//data
	int hour;  
	public double revenue = 0;					//money landlord keeps to pay utilities
	final double rentBill = 25; 				//cost of rent each day
	Random WorkerToday = new Random();			//the worker landlord decides to call that day
	BankManager bankmanager; 
	public enum AgentState 
	{nothing, askingForRent, collectedRent, callMaintanence};
	private AgentState state = AgentState.nothing;
	
	public List<RepairMan>repairmen		//list of repairmen that the landlord can contact
	= Collections.synchronizedList(new ArrayList<RepairMan>()); 
	
	
	public List<Tenant>myTenants
	= Collections.synchronizedList(new ArrayList<Tenant>()); 
	public List<Worker>myWorkers
	= Collections.synchronizedList(new ArrayList<Worker>()); 
	
	public class Worker {
		Worker (RepairMan r, String l) {
			myWorker = r; 
			location = l;
			ws = WorkerState.working;
		}
		public String location; 
		public RepairMan myWorker; 
		double bill; 
		WorkerState ws;
	}
	
	class Tenant {
		Tenant (String l) {
			location = l;
			isOccupied = false; 
		}
		
		public void addTenant(PersonAgent p, Integer a) {
			person = p; 
			account = a;
		}
		
		PersonAgent person;
		Integer account; 
		String location; 
		TenantState ts; 
		boolean isOccupied; 
	}
	

	
	enum WorkerState 
	{working, paying};
	
	enum TenantState
	{nothing, waitingForPayment, paid, ShortOnMoney}; 
	

	public LandlordRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	public void msgTimeUpdate(int hour) {
		this.hour = hour;
		if (hour == 0) {
			state = AgentState.askingForRent;
		}
		if (hour == 10) {
			state = AgentState.callMaintanence; 
		}
		if (hour == 20) {
			state = AgentState.collectedRent;
		}
		stateChanged(); 
	}
	
	
	public void msgHereIsARentPayment(Integer AN, double amount) {
		Do("Receiving rent");
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.account == AN) {
					t.ts = TenantState.paid;
					revenue += amount; 
			}
		}
		}
	}
	
	
	public void msgHereIsARentPayment(PersonAgent p, double amount) {
		Do("Receiving rent");
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.person == p) {
					t.ts = TenantState.paid;
					revenue += amount; 
			}
		}
		}
	}
	
	public void msgCannotPayForRent(Integer AN) {
		Do("What do you mean the tenant can't pay?");
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.account == AN) {
					t.ts = TenantState.ShortOnMoney;
			}
		}
		}
	}
	
	public void msgCannotPayForRent(PersonAgent p) {
		Do("What do you mean the tenant can't pay?");
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.person == p) {
					t.ts = TenantState.ShortOnMoney;
			}
		}
		}
	}		
	
	public void msgJobDone(String l, double cost) {
		Do("Told job was finished");
		synchronized(myWorkers) {
		for (Worker current:myWorkers) {
			if (current.location == l) {
				current.bill = cost;
				current.ws = WorkerState.paying; 
			}
		}
		}
		stateChanged();
	}
	
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if (state == AgentState.askingForRent) {
			CollectRent();
			return true; 
		}
		
		if(state == AgentState.collectedRent) {
			DistributePayments();
			return true; 
		}
		
		if (state == AgentState.callMaintanence && !repairmen.isEmpty()) {
			CallMaintenance();
			return true; 
		}
		synchronized(myWorkers){
		for (Worker w:myWorkers) {
			if (w.ws == WorkerState.paying) {
				PayMaintenance(w); 
				return true; 
			}
		}
		}
		return false;
	}
	
	//actions
	
	private void CollectRent() {
		Do("Collecting rent");
		synchronized(myTenants) {
		for(Tenant t:myTenants) {
			if (t.isOccupied == true) {
				/**
				if (t.account == 0) {
					t.person.msgHereIsYourRentBill(rentBill); 
				}
				else {
				*/
					bankmanager.msgHereIsYourRentBill(this, t.account, rentBill);
				//}
			}
		}
		}
		state = AgentState.nothing; 
	}
	
	
	private void DistributePayments() {
		Do("Distributing pay");
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.isOccupied == true) {
				/**
				if (t.ts == TenantState.ShortOnMoney) {
					t.person.msgEvicted(); 
					t.isOccupied = false; 
					t.account = 0; 
					t.person = null; 
				}
				else {
				*/
				t.ts = TenantState.nothing; 
			}
		}
		}
		revenue = revenue *.70;
		//add money to the personAgent
		state = AgentState.nothing; 
	}
	
	private void CallMaintenance() {
		Do("Calling maintenance over");
		int workerNumber; 
		if (repairmen.size() <= 1) {
			workerNumber = 0; 
		}
		else {
			workerNumber = WorkerToday.nextInt(repairmen.size()); 
		}
		synchronized(myTenants) {
		for (Tenant t:myTenants) {
			if (t.isOccupied) {
				myWorkers.add(new Worker(repairmen.get(workerNumber), t.location));
				repairmen.get(workerNumber).msgNeedRepair(t.location, this);
			}
			
		}
		}
		state = AgentState.nothing; 
	}
	
	private void PayMaintenance(Worker w) {
		Do("Paying for maintenance");
		revenue -= w.bill; 
		w.myWorker.msgHereIsPayment(w.bill);
		myWorkers.remove(w); 
	}

	//utilities
	public void addRepairMan(RepairMan r) {
		repairmen.add((RepairMan) r);
	}
	
	public void addBankManager(BankManager b) {
		bankmanager = b;
	}
	
	public void addTenant(PersonAgent p, Integer account) {
		boolean found = false; 
		synchronized(myTenants) {
		for (Tenant t: myTenants) {
			if (!found) {
				if (!t.isOccupied) {
				t.addTenant(p, account);
				t.isOccupied = true; 
				found = true; 
				}
			}
		}
		}
		if (!found) {
			//should you send a message to the person? Or should you get a new building? 
		}
	}
	
	public void addUnit (String l) {
		myTenants.add(new Tenant(l));
	}
}
