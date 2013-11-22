package simcity.Transportation;
import agent.Agent;
import simcity.PersonAgent;
import simcity.Transportation.*;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;

import java.util.*;


public class BusAgent extends Agent implements Bus {
	String currentStop;
	public Map<String, BusStop> busStops=new HashMap<String, BusStop>();  
	
	public enum busState {travelling, arrived, atStop, loading, waiting, readyToGo};
	public busState state;
	public List<MyPassenger> passengers=new ArrayList<MyPassenger>();
	
	
	public class MyPassenger{
		public PersonAgent p;
		public String destination;
		public boolean onBus;
		
		
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
	
	public void msgHereArePassengers(List<PersonAgent> people){
		for (int i=0; i<people.size(); i++ ){
			MyPassenger thispassenger=new MyPassenger();
			thispassenger.p=people.get(i);
			passengers.add(thispassenger);
			
		}
		stateChanged();
	}
	
	public void msgGettingOn(PersonAgent p, String destination){ // first loop to get everyone on bus 
		
		for(MyPassenger currentPassenger: passengers){
			if(p==currentPassenger.p){
				currentPassenger.destination=destination;
				currentPassenger.onBus=true;
				}
			}
		
		for(MyPassenger currentPassenger: passengers){ //second loop to make sure everyone on the bus is actually on bus?
			                                          // not sure if i need this but it was in design so i added it 
			if(currentPassenger.onBus==false){
				state=null;
				break;
			}
			else{
				state=busState.readyToGo;
				stateChanged(); //not sure if i need this here
			}
		}
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction(){
		
		if (state==busState.arrived){
			arriveAtStop();
			return true;
		}
			
		
		else if (state==busState.loading){
			loadBus();
			return true;
		}
		
		else if (state==busState.readyToGo){
			goToNextStop();
			return true;
		}
		
		
		return false;
	}
	
	//Actions
	
	private void arriveAtStop(){
		state=busState.atStop;
		synchronized(passengers){
		for (int i=0; i<passengers.size(); i++){
			if(passengers.get(i).destination==currentStop){
				passengers.get(i).p.msgAtStop(passengers.get(i).destination);
				passengers.remove(i);
				i-=1;
				BusStop current = busStops.get(currentStop);
				current.msgAnyPassengers(this);
			}
		}
	 }
	}
	
	private void loadBus() {
		state = busState.waiting;
		synchronized(passengers){
		for(MyPassenger currentPassenger: passengers){
			if (currentPassenger.onBus==false){
				currentPassenger.p.msgBusIsHere(this);
			}
		}
		}
	}
	
	private void goToNextStop(){
		state=busState.travelling;
		Do("Go To Next Stop");
	}
	
	

}