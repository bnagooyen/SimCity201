package simcity.interfaces;

import agent.Role;




public interface Person {

	public abstract void gotHungry();//from animation
		
	public abstract  void msgAtDestination(String destination);
	
	public abstract void msgBusIsHere(Bus b);
		
	public abstract void setMoney(double amt);
	
	public abstract void SetJob(Role j);
}





	