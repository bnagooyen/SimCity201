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
	final double rentBill = 25; 		//cost of rent each day
	Random WorkerToday = new Random();	//the worker landlord decides to call that day
	BankManager bankmanager; 
	public enum AgentState 
	{nothing, askingForRent, collectedRent, callMaintanence};
	private AgentState state = AgentState.nothing;
	
	public List<RepairMan>repairmen		//list of repairmen that the landlord can contact
	= Collections.synchronizedList(new ArrayList<RepairMan>()); 
	
	
	public List<Tenant>myTenants
	= new ArrayList<Tenant>(); 
	public List<Worker>myWorkers
	= new ArrayList<Worker>(); 
	
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
		Tenant (PersonAgent p, Integer a) {
			person = p;
			account = a;
			location = "B2"; 
		}
		PersonAgent person;
		Integer account; 
		String location; 
		TenantState ts; 
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
	public void TimeUpdate(int hour) {
		this.hour = hour;
		if (hour == 10) {
			state = AgentState.callMaintanence; 
		}
		if (hour == 12) {
			state = AgentState.askingForRent;
		}
		if (hour == 18) {
			state = AgentState.collectedRent;
		}
		stateChanged(); 
	}
	
	
	public void HereIsARentPayment(Integer AN, double amount) {
		for (Tenant t:myTenants) {
			if (t.account == AN) {
					t.ts = TenantState.paid;
					revenue += amount; 
			}
		}
	}
	
	public void CannotPayForRent(Integer AN) {
		for (Tenant t:myTenants) {
			if (t.account == AN) {
					t.ts = TenantState.ShortOnMoney;
			}
		}
	}		
	
	public void jobDone(String l, double cost) {
		for (Worker current:myWorkers) {
			if (current.location == l) {
				current.bill = cost;
				current.ws = WorkerState.paying; 
				stateChanged();
			}
		}
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
		
		if (state == AgentState.callMaintanence) {
			CallMaintenance();
			return true; 
		}
		
		for (Worker w:myWorkers) {
			if (w.ws == WorkerState.paying) {
				PayMaintenance(w); 
				return true; 
			}
		}
		return false;
	}
	
	//actions
	
	private void CollectRent() {
		for(Tenant t:myTenants) {
			/**
			if (t.account == 0) {
				t.person.msgHereIsYourRentBill(rentBill); 
			}
			else {
			*/
				bankmanager.msgHereIsYourRentBill(this, t.account, rentBill);
			//}
		}
		state = AgentState.nothing; 
	}
	
	
	private void DistributePayments() {
		for (Tenant t:myTenants) {
			/**
			if (t.ts == TenantState.ShortOnMoney) {
				t.person.msgEvicted(); 
				myTenants.remove(t); 
			}
			else {
			*/
			t.ts = TenantState.nothing; 
		}
		revenue = revenue *.70;
		//add money to the personAgent
		state = AgentState.nothing; 
	}
	
	private void CallMaintenance() {
		int workerNumber; 
		if (repairmen.size() <= 1) {
			workerNumber = 0; 
		}
		else {
			workerNumber = WorkerToday.nextInt(repairmen.size()); 
		}
		for (Tenant t:myTenants) {
			myWorkers.add(new Worker(repairmen.get(workerNumber), t.location));
			repairmen.get(workerNumber).NeedRepair(t.location, this);
			
		}
		state = AgentState.nothing; 
	}
	
	private void PayMaintenance(Worker w) {
		revenue -= w.bill; 
		w.myWorker.HereIsPayment(w.bill);
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
		myTenants.add(new Tenant(p, account));
	}
}
