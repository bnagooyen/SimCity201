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
	double revenue = 0;					//money landlord keeps to pay utilities
	final double rentBill = 25; 		//cost of rent each day
	Random WorkerToday = new Random();	//the worker landlord decides to call that day
	BankManager bankmanager; 
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
		Tenant (PersonAgent p, Integer a) {
			person = p;
			account = a; 
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
		if (hour == 20) {
			state = AgentState.askingForRent;
		}
		if (hour == 10) {
			state = AgentState.callMaintanence; 
		}
		stateChanged(); 
	}
	
	
	public void HereIsARentPayment(PersonAgent p, double amount) {
		for (Tenant t:myTenants) {
			if (t.person == p) {
				if (amount < rentBill) {
					t.ts = TenantState.ShortOnMoney; 
				}
				else {
					t.ts = TenantState.paid;
				}
			}
		}
	}
	
	public void jobDone(RepairMan w, double cost) {
		for (Worker current:myWorkers) {
			if (current.myWorker == w) {
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
		for (Worker w:myWorkers) {
			if (w.ws == WorkerState.paying) {
				PayMaintenance(w); 
				return true; 
			}
		}
		if (state == AgentState.callMaintanence) {
			CallMaintenance();
			return true; 
		}
		return false;
	}
	
	//actions
	
	private void CollectRent() {
		for(Tenant t:myTenants) {
			if (t.account == 0) {
				t.person.msgHereIsYourRentBill(rentBill); 
			}
			else {
				bankmanager.msgHereIsYourRentBill(this, t.account, rentBill);
			}
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
		//add money to the personAgent?
		state = AgentState.nothing; 
	}
	
	private void CallMaintenance() {
		int workerNumber; 
		if (myWorkers.size() <= 1) {
			workerNumber = 0; 
		}
		else {
			workerNumber = WorkerToday.nextInt(myWorkers.size()); 
		}
		
		for (Tenant t:myTenants) {
			myWorkers.get(workerNumber).myWorker.NeedRepair(t.location, this);
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
	
	public void NewTenant(PersonAgent p, Integer account) {
		myTenants.add(new Tenant(p, account));
	}
}
