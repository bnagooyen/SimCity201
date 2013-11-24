package simcity.test.mock;


import agent.Role;
import simcity.PersonAgent.HomeType;
import simcity.DRestaurant.DMenu;
import simcity.DRestaurant.DWaiterRole;
import simcity.Transportation.CarAgent;
import simcity.interfaces.Bus;
import simcity.interfaces.Landlord;
import simcity.interfaces.Person;
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
	
	public  void msgAtDestination(String destination){
		log.add(new LoggedEvent("Received message from car that we have arrived."));
	}
	
	public  void msgBusIsHere(Bus b){
		
	}

	@Override
	public void msgTimeUpdate(int hr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(double amt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetJob(Role j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCar(CarAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Role GetJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeType GetHomeState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHouseNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAptNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getAptLet() {
		// TODO Auto-generated method stub
		return 0;
	}
		

}
