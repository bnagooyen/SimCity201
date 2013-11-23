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
import simcity.restaurant.interfaces.*;
import simcity.KRestaurant.KCustomerRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.Drew_restaurant.*;
import simcity.Drew_restaurant.interfaces.*;
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
public class PersonAgent extends Agent implements Person {//implements Person 

	Timer timer = new Timer();
	private String name;
	Bus bus;
	BusStop busStop;
	public CarAgent myCar=null;
	List<Role> roles = new ArrayList<Role>();
	Map<String,Role> possibleRoles = new HashMap<String,Role>();
	//List<Role> customerRoles = new ArrayList<Role>();
	private Role myJob;
	private Role neededRole;
	private String mydestination;
	private String jobLocation;

	public enum PersonState { none };
	public enum EnergyState {tired, asleep, awake, none };
	public enum LocationState { atHome, inTransit, atWork };
	public enum TransitState {walkingToBus, onBus, goToCar, getOutCar, walkingtoDestination, atDestination, atBusStop, waitingAtStop, getOnBus, getOffBus };
	public enum MoneyState { poor, adequate, rich};
	private PersonState personState;
	public EnergyState energyState;
	public LocationState locationState;
	public TransitState transitState;
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
		
		//Set location for go to work
		if(job instanceof BankTeller || job instanceof BankLoanOfficer || job instanceof BankManager){
			jobLocation="bank";
		}
		else if(job instanceof InventoryBoy || job instanceof MarketCashier || job instanceof MarketManager){
			jobLocation="market";
		}
		else if(job instanceof BCashier || job instanceof BCook || job instanceof BWaiter){
			jobLocation="brestaurant";
		}
		else if(job instanceof Cashier || job instanceof Cook || job instanceof Waiter){
			jobLocation="drestaurant";
		}
		else if(job instanceof Drew_Cashier || job instanceof Drew_Cook || job instanceof BWaiter){
			jobLocation="drew";
		}
		else if(job instanceof KCashier || job instanceof KCook || job instanceof KWaiter){
			jobLocation="krestaurant";
		}
		else if(job instanceof LCashier || job instanceof LCook || job instanceof LWaiter){
			jobLocation="lrestaurant";
		}
		else if(job instanceof TCashier || job instanceof TCook || job instanceof TWaiter){
			jobLocation="trestaurant";
		}
		else jobLocation="home";

		personState=PersonState.none;
		energyState=EnergyState.asleep;
		locationState=LocationState.atHome;
		moneyState=MoneyState.adequate;
		roles.add(myJob);

		possibleRoles.put("bank", new BankCustomerRole(this));
		possibleRoles.put("market", new MarketCustomerRole(this));
		possibleRoles.put("drestaurant", new DCustomerRole(this));
		possibleRoles.put("drew_restaurant", new Drew_CustomerRole(this));
		possibleRoles.put("brestaurant", new BCustomerRole(this));
		possibleRoles.put("krestaurant", new KCustomerRole(this));
		possibleRoles.put("trestaurant", new TCustomerRole(this));
		possibleRoles.put("lrestaurant", new LCustomerRole(this));

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
	public void msgAtStop(String destination){
		mydestination=destination;
		transitState = TransitState.getOffBus;
		stateChanged();
	}

	public void msgBusIsHere(Bus b){
		transitState=TransitState.getOnBus;
		stateChanged();
	}

	public void msgAtDestination(String destination){
		mydestination=destination;
		boolean haveRole=false;
		transitState=TransitState.getOutCar;
		stateChanged();
		neededRole=possibleRoles.get(destination);

		if(needToGoToWork){
			myJob.isActive=true;
			needToGoToWork=false;
		}
		else{
			for(Role role:roles){
				if(role==neededRole) role.isActive=true;
				haveRole=true;
			}
			if(!haveRole){
				roles.add(neededRole);
				neededRole.isActive=true;
			}
		}
	}



	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
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

			if (myCar==null){
				if(transitState==TransitState.walkingToBus){
					walkToBus();
					
					return true;
				}

				if (transitState==TransitState.atBusStop){
					tellBusStop();
					return true;
				}

				if(transitState==TransitState.getOnBus){
					getOnBus();
					return true;
				}

				if(transitState==TransitState.getOffBus){
					getOffBusAndWalk();
					return true;
				}
			}
			else if (myCar!=null){
				if (transitState==TransitState.goToCar){
					goToCar();
					return true;
				}

				if(transitState==TransitState.getOutCar){
					getOutCarAndWalk();
					return true;
				}

			}

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
		mydestination= jobLocation;
		locationState=LocationState.inTransit;
	}

	private void GoToBed() {
		Do("going to bed");
		energyState=EnergyState.asleep;
	}

	private void deposit() {
		Do("Going to deposit Money");

	}

	private void withdraw() {
		Do("Going to Withdraw Money");

	}

	private void buyCar() {
		Do("Go buy car");
	}

	
//Transit
	private void walkToBus(){
		Do("Walk To Bus");
		transitState=TransitState.atBusStop;
	}

	private void tellBusStop(){
		busStop.msgWaitingForBus(this);
		transitState=TransitState.waitingAtStop;
	}

	private void getOnBus(){
		bus.msgGettingOn(this, mydestination);
		transitState=TransitState.onBus;
	}

	private void getOffBusAndWalk(){
		//gui to get off 
		transitState=TransitState.walkingtoDestination;
		Do("Walk to Destination");
	}

	private void goToCar(){
		Do("Do go To car"); //gui?
		myCar.msgGoToDestination("location", this);
	}

	private void getOutCarAndWalk(){
		Do("Get out car");
		transitState=TransitState.atDestination;
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

	public void setBus(Bus b){
		bus=b;
	}

	public void setPanel(SimCityPanel p) {
		panel = p;
		this.buildings = p.buildings;

	}

	//utilities
	public Role getJob() {
		return myJob;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub

	}
}



