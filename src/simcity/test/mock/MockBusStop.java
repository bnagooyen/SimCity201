package simcity.test.mock;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Transportation.BusAgent;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestaurantCashier;

public class MockBusStop extends Mock implements BusStop {
	
	
	public EventLog log;
	
	public MockBusStop(String name) {
		super(name);
		log = new EventLog();
		
	}
	
	public void msgWaitingForBus(PersonAgent p){
		
	}
	
	public void msgAnyPassengers(Bus b){
		
	}
	
	
	
	
}
