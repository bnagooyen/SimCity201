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

	EventLog log;

	@Override
	public void NeedRepair(String building, Landlord l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void NeedRepair(String building, PersonAgent p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsPayment(double m) {
		// TODO Auto-generated method stub
		
	}

}
