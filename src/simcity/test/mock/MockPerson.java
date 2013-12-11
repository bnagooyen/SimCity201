package simcity.test.mock;


import java.util.List;
import java.util.Map;

import agent.Role;
import simcity.DRestaurant.DMenu;
import simcity.DRestaurant.DWaiterRole;
import simcity.Transportation.CarAgent;
import simcity.housing.LandlordRole;
import simcity.interfaces.Bus;
import simcity.interfaces.Landlord;
import simcity.interfaces.Person;
import simcity.mockrole.MockRole;

public class MockPerson extends MockRole implements Person {

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
	public void msgHereIsYourRentBill(Landlord l, double rentBill){
		
	}


	public void msgHereIsYourRentBill(LandlordRole landlordRole, double rentBill) {
		log.add(new LoggedEvent("Received message from Landlord"));
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJob() {
		// TODO Auto-generated method stub
		return null;
	}

}

