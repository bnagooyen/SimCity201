package simcity.Transportation;
import agent.Agent;
import simcity.PersonAgent;
import simcity.Transportation.*;


import java.util.*;


public class CarAgent extends Agent  {
	
	PersonAgent driver;
	int destination;
	
	public enum carState
	{parked, receivedLocation, travelling, arrived};
	
	carState state;
	
	//MESSAGES
	
	public void msgGoToDestination(int location, PersonAgent person){
		driver=person;
		destination=location;
		state=carState.receivedLocation;
		stateChanged();
	}
	
	public void msgAtDestination(){
		state=carState.arrived;
	}
	
	//SCHEDULER
	
	protected boolean pickAndExecuteAnAction(){
		
		if(state==carState.receivedLocation){
			goToLocation();
			state=carState.travelling;
			return true;
		}
		
		if(state==carState.arrived){
			HaveArrived();
			state=carState.parked;
			return true;
		}
		
		return false;
	}
	
	
	//ACTIONS
	
	private void goToLocation(){
		//DoGoToLocation();
	}
	
	private void HaveArrived(){
		//driver.msgWeHaveArrived();
	}
	
	

}