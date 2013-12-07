package simcity.mockrole;

import simcity.interfaces.Car;
import simcity.interfaces.Person;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;



public class MockRoleCar extends MockRole implements Car{

	public EventLog log;
	
	public MockRoleCar(String name) {
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