package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.interfaces.Landlord;
import simcity.mockrole.MockRole;

public class MockLandlord extends MockRole implements Landlord{
	
	public EventLog log = new EventLog(); 
	public LoggedEvent event; 

	public MockLandlord(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgTimeUpdate(int hour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsARentPayment(Integer AN, double bill) {
		LoggedEvent m = new LoggedEvent ("Received money for tenant"); 
		log.add(m);		
	}
	
	@Override
	public void msgCannotPayForRent(Integer AN) {
		// TODO Auto-generated method stub
	}
		

	/**
	@Override
	public void msgJobDone(String l, double cost) {
		LoggedEvent m = new LoggedEvent ("Received a bill from the repairman. Bill = " + cost); 
		log.add(m);		
	}
	*/


	@Override
	public void msgHereIsARentPayment(PersonAgent p, double amount) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgCannotPayForRent(PersonAgent p) {
		// TODO Auto-generated method stub
		
	}
}
