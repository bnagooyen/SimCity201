package simcity.housing;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import simcity.PersonAgent;
import simcity.housing.LandlordRole.Tenant;
import simcity.housing.interfaces.Landlord;
import simcity.housing.interfaces.RepairMan;
import agent.Role;

public class RepairManRole extends Role implements RepairMan {
	
	List<MyJobs>jobs
	= new ArrayList<MyJobs>();
	
	class MyJobs {
		MyJobs(String b, Landlord l) {
			building = b; 
			employer = l;
		}
		Landlord employer; 
		String building; 
	}
	
	Timer timer; 
	
	protected RepairManRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	
	public void NeedRepair(String building, Landlord l) {
		jobs.add(new MyJobs(building, l)); 
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
		jobs.remove(0); 
	}
	
	//gui
	
	public void DoGoToBuilding(String location) {
		
	}

}
