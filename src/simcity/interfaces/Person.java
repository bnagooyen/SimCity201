package simcity.interfaces;

import java.util.List;
import java.util.Map;

import simcity.PersonAgent.HomeType;
import simcity.Transportation.CarAgent;
import simcity.gui.SimCityPanel.Location;
import simcity.housing.LandlordRole;
import agent.Role;




public interface Person {
	public abstract void msgTimeUpdate(int hr);
	
	public abstract void gotHungry();//from animation
		
	public abstract  void msgAtDestination(String destination);
	
	public abstract void msgBusIsHere(Bus b);
		
	public abstract void setMoney(double amt);
	
	public abstract void SetJob(Role j);
	
	public abstract void setCar(CarAgent c);
	public abstract Role GetJob();

	public abstract String getName();

	public abstract HomeType GetHomeState();

	public abstract int getHouseNum();

	public abstract int getAptNum();

	public abstract char getAptLet();

	public abstract void msgSetBuildingDirectory(
			Map<String, List<Location>> buildings);

	public abstract void msgSetBusDirectory(Map<String, List<Location>> busStops);
	
	public abstract void msgHereIsYourRentBill(Landlord l, double rentBill); 

}





	