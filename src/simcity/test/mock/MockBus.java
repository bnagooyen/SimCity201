package simcity.test.mock;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.interfaces.Bus;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestaurantCashier;

public class MockBus extends Mock implements Bus {
	
	
	public EventLog log;
	
	public MockBus(String name) {
		super(name);
		log = new EventLog();
		
	}
	
	public void msgAtStop(String stop){
		
	}
	public  void msgAtDestination(){
		
	}
	public  void msgHereArePassengers(List<PersonAgent> people){
		log.add(new LoggedEvent("Should have received message from the bus Stop, but did not"));
	}
	
	public  void msgGettingOn(PersonAgent p, String destination){
		
	}

	
	
	

}
