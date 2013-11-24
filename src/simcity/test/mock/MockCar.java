package simcity.test.mock;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Transportation.BusAgent;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;
import simcity.interfaces.Car;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.Person;
import simcity.interfaces.RestaurantCashier;

public class MockCar extends Mock implements Car {
	
	
	public EventLog log;
	
	public MockCar(String name) {
		super(name);
		log = new EventLog();
		
	}
	
	public void msgGoToDestination(String location, Person person){
		log.add(new LoggedEvent("received message"));
	}
	
	public void msgAtDestination(){
		log.add(new LoggedEvent("received message"));
	}
	
	

}
