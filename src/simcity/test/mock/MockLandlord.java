package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan; 

public class MockLandlord extends Mock implements Landlord{
	
	public EventLog log = new EventLog(); 
	public LoggedEvent event; 

	public MockLandlord(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void TimeUpdate(int hour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsARentPayment(Integer AN, double bill) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void CannotPayForRent(Integer AN) {
		// TODO Auto-generated method stub
	}
		

	@Override
	public void jobDone(RepairMan w, double cost) {
		LoggedEvent m = new LoggedEvent ("Received a bill from the repairman. Bill = " + cost); 
		log.add(m);		
	}
}
