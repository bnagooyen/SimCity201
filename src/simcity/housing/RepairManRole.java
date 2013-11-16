package simcity.housing;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import simcity.PersonAgent;
import simcity.housing.LandlordRole.Tenant;
import agent.Role;

public class RepairManRole extends Role{
	
	List<MyJobs>jobs
	= new ArrayList<MyJobs>();  
	class MyJobs {
		MyJobs(String b, PersonAgent p) {
			building = b; 
			person = p; 
		}
		LandlordRole employer; 
		String building; 
	}
	
	Timer timer; 
	
	protected RepairManRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	
	public void NeedRepair(String building, PersonAgent p) {
		jobs.add(new MyJobs(building, p)); 
	}

	public void HereIsPayment(double m) {
		
	}
	
	public boolean pickAndExecuteAnAction() {
		if (!jobs.isEmpty()) {
			CleanBuilding(); 
		}
		return false;
	}
	
	public void CleanBuilding() {
		DoGoToBuilding(jobs.get(0).building); 
		//setTimer
		jobs.get(0).employer.jobDone(this, bill); 
		jobs.get(0).remove; 
	}
	
	//gui
	
	public void DoGoToBuilding(String location) {
		
	}

}
