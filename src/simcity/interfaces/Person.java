package simcity.interfaces;

import simcity.PersonAgent.HomeType;
import simcity.Transportation.CarAgent;
import agent.Role;




public interface Person {

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

}





	