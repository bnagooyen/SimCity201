package simcity.housing;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import simcity.PersonAgent;
import simcity.housing.LandlordRole.Tenant;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan;
import agent.Role;

public class RepairManRole extends Role implements RepairMan {
	
	public List<MyJobs>jobs
	= new ArrayList<MyJobs>();
	
	class MyJobs {
		MyJobs(String b, Landlord l) {
			building = b; 
			employer = l;
		}
		
		MyJobs(String b, PersonAgent p) {
			building = b; 
			homeowner = p; 
		}
		
		Landlord employer;
		PersonAgent homeowner; 
		String building; 
	}
	
	final double bill = 30; //constant for the cost of utilities each day
	Timer timer = new Timer();
	
	public RepairManRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	public void msgNeedRepair(String building, Landlord l) {
		Do("Called for repairs");
		jobs.add(new MyJobs(building, l)); 
	}
	
	public void msgNeedRepair(String building, PersonAgent p) {
		Do("Called for repairs");
		jobs.add(new MyJobs(building, p)); 
	}
	
	public void msgHereIsPayment(double m) {
		
	}
	
	public boolean pickAndExecuteAnAction() {
		if (!jobs.isEmpty()) {
			CleanBuilding();
			return true; 
		}
		return false;
	}
	
	public void CleanBuilding() {
		Do("Cleaning the building");
		//DoGoToBuilding(jobs.get(0).building); 
		timer.schedule(new TimerTask() {
			public void run() {
				//animation to clean? 
			}
		}, 5000);
		
		if (jobs.get(0).employer != null) {
			jobs.get(0).employer.msgJobDone(jobs.get(0).building, bill);
		}
		/*
		else {
			jobs.get(0).homeowner.jobDone(this, bill); 
		}
		*/
		jobs.remove(0); 
	}
	
	//gui
	public void DoGoToBuilding(String location) {
		
	}

}
