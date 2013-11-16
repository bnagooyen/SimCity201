package simcity.Transportation;
import agent.Agent;
import simcity.PersonAgent;
import simcity.Transportation.*;


import java.util.*;


public class BusAgent extends Agent  {
	String currentStop;
	Map<String, BusStopAgent> busStops=new HashMap<String, BusStopAgent>();  
	
	public enum busState {travelling, arrived, atStop, loading, waiting, readyToGo};
	busState state;
	List<MyPassenger> passengers;
	
	public class MyPassenger{
		PersonAgent p;
		String destination;
		boolean onBus=false;
		
		
	}

	public BusAgent(){
		currentStop=null;
		state=null;
		stateChanged();
		}
	
	
	//MESSAGES
	
	public void msgAtStop(String stop){
		currentStop=stop;
		state=busState.arrived;
		stateChanged();
		
	}
	
	public void msgHereArePassegers(List<PersonAgent> people){
		for (int i=0; i<people.size(); i++ ){
			MyPassenger thispassenger=new MyPassenger();
			thispassenger.p=people.get(i);
			passengers.add(thispassenger);
			
		}
		stateChanged();
	}
	
	public void msgGettingOn(PersonAgent p, String destination){ // first loop to get everyone on bus 
		for(MyPassenger currentPassenger: passengers){
			if(currentPassenger.p==p){
				currentPassenger.destination=destination;
				currentPassenger.onBus=true;
				}
			}
		
		for(MyPassenger currentPassenger: passengers){ //second loop to make sure everyone on the bus is actually on bus?
			                                          // not sure if i need this but it was in design so i added it 
			if(currentPassenger.onBus=false)
				state=null;
			else
				state=busState.readyToGo;
				stateChanged(); //not sure if i need this here
		}
	}
	
	//Scheduler
	
	protected boolean pickAndExecuteAnAction(){
		
		if (state==busState.arrived){
			arriveAtStop();
			return true;
		}
			
		
		else if (state==busState.loading){
			loadBus();
			return true;
		}
		
		if (state==busState.readyToGo){
			goToNextStop();
			return true;
		}
		
		
		return false;
	}
	
	//Actions
	
	private void arriveAtStop(){
		state=busState.atStop;
		
		for (MyPassenger currentPassenger: passengers){
			if(currentPassenger.destination==currentStop){
				//currentPassenger.p.msgAtDestination();
				passengers.remove(currentPassenger);
				BusStopAgent current = busStops.get(currentStop);
				current.msgAnyPassengers(this);
			}
		}
	}
	
	private void loadBus() {
		state = busState.waiting;
		for(MyPassenger currentPassenger: passengers){
			if (currentPassenger.onBus==false){
				//currentPassenger.p.BusIsHere(this);
			}
		}
		
	}
	
	private void goToNextStop(){
		state=busState.travelling;
		//DoGoToNextStop();
	}
	
	

}