package simcity.Transportation;
import agent.Agent;
import agent.Role;
import simcity.PersonAgent;
import simcity.Transportation.*;
import simcity.gui.BusGui;
import simcity.gui.PersonGui;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;

import java.util.*;
import java.util.concurrent.Semaphore;


public class BusAgent extends Agent implements Bus {
	public String currentStop;
	public Map<String, BusStopAgent> busStops=new HashMap<String, BusStopAgent>();  
	Semaphore atBusStop= new Semaphore(0, true);
	public enum busState {travelling, arrived, atStop, loading, waiting, readyToGo};
	public busState state;
	public List<MyPassenger> passengers=new ArrayList<MyPassenger>();
	public int hour;

	public BusGui bgui=null;


	public class MyPassenger{
		public PersonAgent p;
		public String destination;
		public boolean onBus;


	}

	public BusAgent(){
		currentStop="Stop4";
		state=busState.readyToGo;
		stateChanged();
	}

	public void setGui(BusGui gui){
		bgui=gui;
	}

	public BusGui getGui() {
		return bgui;
	}

	private void DoGoTo(String dest) {
		bgui.DoGoTo(dest);
	}

	public void setStops(Map<String, BusStopAgent> stops){
		busStops=stops;
	}
	//MESSAGES

	public void msgAtStop(String stop){
		atBusStop.release();
		currentStop=stop;
		state=busState.arrived;
		System.out.println(currentStop);
		System.out.println(state);
		stateChanged();
		
		

	}





	public void msgHereArePassengers(List<PersonAgent> people){
		
		if(people.size()>0){
		for (int i=0; i<people.size(); i++ ){
			MyPassenger thispassenger=new MyPassenger();
			thispassenger.p=people.get(i);
			passengers.add(thispassenger);

		}
		state=busState.loading;
		stateChanged();
		}
		
		else if (people.size()==0){
			state=busState.readyToGo;
			stateChanged();
		}
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

	/*  public void msgTimeUpdate(int hr) {
                Do("got time update.");
                hour = hr;
                if(hr%2==0) { 
                        busStops.get(0).msgAnyPassengers(this);
                }
                if(hr%2==1) { 
                        busStops.get(1).msgAnyPassengers(this);
                }



                }*/
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
			
			if(passengers.size()!=0){
			for (int i=0; i<passengers.size(); i++){
				 if(passengers.get(i).destination==currentStop){
					passengers.get(i).p.msgAtStop(passengers.get(i).destination);
					passengers.remove(i);
					i-=1;
				}
			}
			}
					BusStopAgent current = busStops.get(currentStop);
					current.msgAnyPassengers(this);
				
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
		if(currentStop=="Stop1")
			DoGoTo("Stop2");
		else if(currentStop=="Stop2")
			DoGoTo("Stop3");
		else if(currentStop=="Stop3")
			DoGoTo("Stop4");
		else if(currentStop=="Stop4")
			DoGoTo("Stop1");
		
		stateChanged();

	}



}