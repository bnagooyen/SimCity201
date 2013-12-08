package simcity.test.mock;

import java.util.List;

import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.DeliveryTruck;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.interfaces.RestaurantCashier;
import simcity.mockrole.MockRole;

public class MockDeliveryTruck extends MockRole implements DeliveryTruck{
	
	String name;
	public EventLog log;
	
	public MockDeliveryTruck(String name) {
		super(name);
		this.name = name;
		log = new EventLog();
	}


	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToDestination(MarketCashier cashier,
			List<MFoodOrder> deliver, String location, double bill, Cook c,
			RestaurantCashier rcashier) {
		LoggedEvent e = new LoggedEvent("Received msgGoToDestination");
		log.add(e);		
	}
	
}