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
import simcity.gui.PersonGui;
import simcity.interfaces.Bus;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class PersonAgent extends Agent {

	Timer timer = new Timer();
	private String name;
	Role host = null;
	BusAgent bus;
	
	List<Role> roles = new ArrayList<Role>();
	public enum PersonState { doingNothing, gotHungry, atRestaurant, dead };
	private PersonState state;
    
    public PersonGui PersonGui = null;
    
    public double money=0.00;
    public double depositThreshold=100.00;
    public double withdrawalThreshold=20.00;
	

	
	
	public PersonAgent(String name) {
		super();
		
		this.name = name;
	
		state=PersonState.doingNothing;
	}


	// The animation DoXYZ() routines
	
	public void setHost(Role r){
		host = r;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		state = PersonState.gotHungry; 
		stateChanged();
	}
	
	public void msgAtDestination(){
		
	}
	
	public void msgBusIsHere(Bus b){
		
	}
	
	
	//utilities

	public void setGui(PersonGui gui) {
		PersonGui = gui;
	}
	
	public PersonGui getGui() {
		return PersonGui;
	}
	
	public void setBus(BusAgent b){
		bus=b;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(state==PersonState.gotHungry) {
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
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
	private void GoToRestaurant() {
		state=PersonState.atRestaurant;
		roles.add(new CustomerRole(this, host));
		roles.get(0).isActive = true;
		print("I'm going to the restaurant");
	}
	
	private void GetOnBus(){
		bus.msgGettingOn(this, "destination");
	}
		
		
	// utilities
	public Role getHostRole() {
		return roles.get(0);
	}
	
	public Role getWaiterRole() {
		return roles.get(0);
	}
	
	public void addRole(Role r) {
		roles.add(r);
	}
	
	
}



