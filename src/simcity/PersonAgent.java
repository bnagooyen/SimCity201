package simcity;

import agent.Agent;
import agent.Role;
import simcity.restaurant.CustomerRole;
import simcity.restaurant.CustomerRole.AgentEvent;
import simcity.restaurant.Order.OrderState;
import simcity.restaurant.gui.CookGui;
import simcity.restaurant.gui.CustomerGui;
import simcity.restaurant.gui.HostGui;
import simcity.restaurant.gui.WaiterGui;
import simcity.restaurant.interfaces.Cashier;
import simcity.Transportation.BusAgent;
import simcity.Transportation.CarAgent;
import simcity.gui.PersonGui;
//import simcity.interfaces.Bus;

//import simcity.interfaces.Person;


import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class PersonAgent extends Agent {//implements Person 

	Timer timer = new Timer();
	private String name;
	Role host = null;
	BusAgent bus;
	CarAgent myCar;
	
	List<Role> roles = new ArrayList<Role>();
	public enum personState { doingNothing, gotHungry, atRestaurant, dead, gettingOnBus, awake, sleep, arrived, sleeping };
	private personState state;
	boolean flake;
	boolean broke;
    
    public PersonGui PersonGui = null;
    
    private int hour;
    public double money=0.00;
    public double depositThreshold=100.00;
    public double withdrawalThreshold=20.00;
	
	public PersonAgent(String name) {
		super();
		
		this.name = name;
	
		state=personState.doingNothing;
	}


	// The animation DoXYZ() routines
	
	public void setHost(Role r){
		host = r;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		hour = hr;
		if(hr == 7) state= personState.awake;
		if(hr ==23) state= personState.sleep;

	}
	
	public void gotHungry() {//from animation
		print("I'm hungry");
		state = personState.gotHungry; 
		stateChanged();
	}
	
	public void msgAtDestination(){
		state = personState.arrived;
		stateChanged();
	}
	
//	public void msgBusIsHere(Bus b){
//		state=PersonState.gettingOnBus;
//		stateChanged();
//	}
	
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(state==personState.gotHungry) {
			GoToRestaurant();
			return true;
		}
		boolean anyTrue = false;
		for(Role r : roles) {
			if(r.isActive) {
				anyTrue = r.pickAndExecuteAnAction();
				Do("returned "+anyTrue);
			}
		}
		
		return anyTrue;
	
	}


	// Actions
	private void GoToRestaurant() {
		state=personState.atRestaurant;
		roles.add(new CustomerRole(this, host));
		roles.get(0).isActive = true;
		print("I'm going to the restaurant");
	}
	
	private void GetOnBus(){
		bus.msgGettingOn(this, "destination");
	}
		
	private void GoToSleep() {
		state=personState.sleeping;
	    DoGoToSleep();
	}	
	// utilities

	private void DoGoToSleep() {
		
	}


	public void addRole(Role r) {
		roles.add(r);
	}
	
	public void setGui(PersonGui gui) {
		PersonGui = gui;
	}
		
	public PersonGui getGui() {
		return PersonGui;
	}
		
	public void setBus(BusAgent b){
		bus=b;
	}
}



