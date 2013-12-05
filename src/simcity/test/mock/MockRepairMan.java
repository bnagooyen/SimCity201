package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan; 

public class MockRepairMan extends Mock implements RepairMan{

	public MockRepairMan(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public EventLog log = new EventLog(); 
	public LoggedEvent event; 
	
	@Override
	public void msgNeedRepair(String building, Landlord l) {
		LoggedEvent m = new LoggedEvent ("Received a job for building " + building); 
		log.add(m);			
	}

	@Override
	public void msgNeedRepair(String building, PersonAgent p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double money) {
		LoggedEvent m = new LoggedEvent ("Received a payment for job for " + money); 
		log.add(m);			
	}

}
