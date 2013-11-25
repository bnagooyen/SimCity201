package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.DeliveryTruck;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.mockrole.MockRole;

public class MockDeliveryTruck extends MockRole implements DeliveryTruck{
	
	String name;
	public EventLog log;
	
	public MockDeliveryTruck(String name, PersonAgent p) {
		super(name, p);
		this.name = name;
		log = new EventLog();
	}

	@Override
	public void msgGoToDestination(MarketCashier cashier,List<MFoodOrder> deliver, String location, double bill, Cook c) {
		
		LoggedEvent e = new LoggedEvent("Received msgGoToDestination");
		log.add(e);
	}


	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}
	
}