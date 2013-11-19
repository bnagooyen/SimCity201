package simcity.test.mock;


import simcity.interfaces.Bus;
import simcity.interfaces.Landlord;
import simcity.interfaces.Person;
import simcity.restaurant.Menu;
import simcity.restaurant.WaiterRole;
import simcity.restaurant.interfaces.Cashier;
import simcity.restaurant.interfaces.Customer;

public class MockPerson extends Mock implements Person {

public EventLog log;
	
	public MockPerson(String name) {
		super(name);
		log = new EventLog();
		
	}
	
	public  void gotHungry(){
		
	}
	
	public  void msgAtDestination(){
		log.add(new LoggedEvent("Received message from car that we have arrived."));
	}
	
	public  void msgBusIsHere(Bus b){
		
	}
		

}
