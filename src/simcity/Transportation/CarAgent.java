package simcity.Transportation;
import agent.Agent;
import simcity.PersonAgent;
import simcity.Transportation.*;
import simcity.interfaces.Car;
import simcity.interfaces.Person;

import java.util.*;


public class CarAgent extends Agent implements Car {
	
	public Person driver=null;
	public String destination;
	
	public enum carState
	{parked, receivedLocation, travelling, arrived};
	
	public carState state;
	
	//MESSAGES
	
	public void msgGoToDestination(String location, Person person){
		driver=person;
		destination=location;
		state=carState.receivedLocation;
		stateChanged();
	}
	
	public void msgAtDestination(){
		state=carState.arrived;
		stateChanged();
	}
	
	//SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		
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
		driver.msgAtDestination();
	}
	
	

}