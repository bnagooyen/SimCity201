package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.interfaces.Landlord;
import simcity.interfaces.RepairMan; 

public class MockLandlord extends Mock implements Landlord{

	public MockLandlord(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	EventLog log;

	@Override
	public void TimeUpdate(int hour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void NewTenant(PersonAgent p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsARentPayment(PersonAgent p, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobDone(RepairMan w, double cost) {
		// TODO Auto-generated method stub
		
	}
}
