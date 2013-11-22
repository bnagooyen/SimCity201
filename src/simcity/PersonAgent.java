package simcity;

import agent.Agent;
import agent.Role;
import simcity.restaurant.interfaces.Cashier;
import simcity.BRestaurant.BCustomerRole;
import simcity.Bank.BankCustomerRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DCustomerRole.AgentEvent;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.gui.DCookGui;
import simcity.DRestaurant.gui.DCustomerGui;
import simcity.DRestaurant.gui.DHostGui;
import simcity.DRestaurant.gui.DWaiterGui;
import simcity.KRestaurant.KCustomerRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.Drew_restaurant.*;
import simcity.TTRestaurant.TCustomerRole;
import simcity.Transportation.BusAgent;
import simcity.Transportation.CarAgent;
import simcity.gui.PersonGui;
import simcity.gui.SimCityPanel;
import simcity.gui.SimCityPanel.Location;
import simcity.interfaces.*;

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
	BusAgent bus;
	CarAgent myCar;
	List<Role> roles = new ArrayList<Role>();
	//List<Role> customerRoles = new ArrayList<Role>();
	private Role myJob;
	public enum PersonState { none };
	public enum EnergyState {tired, asleep, awake, none };
	public enum LocationState { atHome, inTransit, atWork };
	public enum TransitState { };
	public enum MoneyState { poor, adequate, rich};
	private PersonState personState;
	private EnergyState energyState;
	private LocationState locationState;
	private TransitState transitState;
	private MoneyState moneyState;
	
	boolean flake;
	//boolean broke;
	boolean needToGoToWork = false;
	
    public PersonGui PersonGui = null;
    
    private int hour=0;
   
    public double money=200.00;
    public double depositThreshold=100.00;
    public double withdrawalThreshold=20.00;
    public int kitchenAmnt = 5;
    private int hungerLevel = 0; // out of 100.. anything over 50 means hungry
	
    private SimCityPanel panel;
    private Map<String, List<Location>> buildings = null;
    
	public PersonAgent(String name, Role job) {
		super();
		
		
		this.name = name;
		myJob = job;
		myJob.isActive = true;
		
		personState=PersonState.none;
		energyState=EnergyState.asleep;
		locationState=LocationState.atHome;
		moneyState=MoneyState.adequate;
		roles.add(myJob);

	}


	// The animation DoXYZ() routines
	
//	public void setHost(Role r){
//		host = r;
//	}

	public String getName() {
		return name;
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		hour = hr;
		if(hr == 7) { 
			energyState= energyState.awake;
		}
		if(hr ==24) { 
			energyState = energyState.asleep;
		}
		if(hr==myJob.startHour-1) {
			needToGoToWork=true;
		}
		hungerLevel+=10;
		stateChanged();
	}
	
//	public void gotHungry() {//from animation
//		print("I'm hungry");
//		state = personState.gotHungry; 
//		stateChanged();
//	}
//	
//	public void msgAtDestination(){
//		state = personState.arrived;
//		stateChanged();
//	}
//	
//	public void msgBusIsHere(Bus b){
//		state=PersonState.gettingOnBus;
//		stateChanged();
//	}
	
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
//		if(state==personState.gotHungry) {
//			GoToRestaurant();
//			return true;
//		}
//		if(state==personState.awake) {
//			
//		if(myLoc==location.atHome && hungerLevel>=50) {
//			EatFood(); 
//			return true;
//		}
		
		//boolean anyTrue = false;
//		for(Role r : roles) {
//			if(r.isActive) {
//				anyTrue = r.pickAndExecuteAnAction();
//				Do("returned "+anyTrue);
//			}
//		}
		
		//return anyTrue;
		//}
		
		if(energyState==EnergyState.none) {
			Die();
			return true;
		}
		
		boolean anyTrue=false;
		boolean activatedRole = false;
		for(Role r: roles) {
			if(r.isActive) {
				anyTrue=r.pickAndExecuteAnAction();
				activatedRole=true;
			}
		}
		if(activatedRole) return anyTrue;
		
		
		if(locationState==LocationState.atHome && !(energyState==EnergyState.asleep)) {
			if(hungerLevel>=50) {
				EatFood();
				return true;
			}
			if(needToGoToWork) {
				GoToWork();
				return true;
			}
			if(energyState==EnergyState.tired) {
				GoToBed();
				return true;
			}
			if(money>depositThreshold){
				deposit();
			}
			if(money<withdrawalThreshold){
				withdraw();
			}
			if(moneyState==MoneyState.rich && myCar==null){
				buyCar();
			}
		}
		
		if(locationState==LocationState.inTransit && !(energyState==EnergyState.asleep)) {
			//enter transit stuff here
		}
		
		return false;
		
	}


	// Actions
	
	private void Die() {
		DoDie();
		this.stopThread();
	}
	
	private void EatFood() {
		Do("eating food");
		hungerLevel=0;
	}
	
	private void GoToWork() {
		Do("going to work");
		needToGoToWork=false;
		//locationState=LocationState.inTransit;
	}
	
	private void GoToBed() {
		Do("going to bed");
		energyState=EnergyState.asleep;
	}
	
	private void deposit() {
		Do("Going to deposit Money");
		energyState=EnergyState.asleep;
	}
	
	private void withdraw() {
		Do("Going to Withdraw Money");
		energyState=EnergyState.asleep;
	}
	
	private void buyCar() {
		Do("Go buy car");
	}
	
	// utilities
	
	private void DoDie() {
		System.out.println("Dieing");
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
	
	public void setPanel(SimCityPanel p) {
		panel = p;
		this.buildings = p.buildings;
		
	}
}



