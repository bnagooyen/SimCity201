package simcity.Transportation;


import simcity.PersonAgent;
import java.util.*;
import agent.Agent;
import simcity.Transportation.BusAgent;


public class BusStopAgent extends Agent{
	
	List<BusAgent> buses;
	List<PersonAgent> waitingPassengers;
	
	
	//Messages
	
	public void msgWaitingForBus(PersonAgent p){
		waitingPassengers.add(p);
		stateChanged();
	}
	
	public void msgAnyPassengers(BusAgent b){
		buses.add(b);
		stateChanged();
	}
	
	
	//SCHEDULER
	
	protected boolean pickAndExecuteAnAction(){
	
		if(buses.size()>0){
			//givePassengerList(buses.get(0))
		}
		
		return false;
	}
	
	
	//ACTIONS
	
	private void givePassengerList(BusAgent b){
		b.msgHereArePassegers(waitingPassengers);
		waitingPassengers.clear();
		buses.remove(b);
	}
	
	
}
