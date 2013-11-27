package simcity.test.mock;


import java.util.List;
import java.util.Map;

import agent.Role;
import simcity.PersonAgent.HomeType;
import simcity.DRestaurant.DMenu;
import simcity.DRestaurant.DWaiterRole;
import simcity.Transportation.CarAgent;
import simcity.gui.SimCityPanel.Location;
import simcity.housing.LandlordRole;
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
	public void msgHereIsYourRentBill(Landlord l, double rentBill){
		
	}

	@Override
	public void msgSetBuildingDirectory(Map<String, List<Location>> buildings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSetBusDirectory(Map<String, List<Location>> busStops) {
		// TODO Auto-generated method stub
		
	}


	public void msgHereIsYourRentBill(LandlordRole landlordRole, double rentBill) {
		log.add(new LoggedEvent("Received message from Landlord"));
		
	}

}

