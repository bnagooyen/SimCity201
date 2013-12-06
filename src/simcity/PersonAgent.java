package simcity;

import agent.Agent;
import agent.Role;
import simcity.Bank.BankCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.gui.PersonGui;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.TenantGui;

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
	Random generator = new Random();
	
	private String name;
	public Role myJob;
    List<Role> roles= new ArrayList<Role>();
    public double money=0;
	public String homeAddress;
	public String BankChoice;
	
	//States
	public int hungerLevel;
	enum PersonState { doingNothing, atRestaurant, workTime, tired, asleep, dead };
	enum LocationState {atHome, atRestaurant, atBank};
	enum TravelPreference {walk, bus, car};
	TravelPreference myTravelPreference;
	private LocationState myLocation;
	private PersonState state;

	
	//GUI
	Semaphore atRestaurant = new Semaphore(0, true);
    Semaphore atLocation = new Semaphore(0, true);
    
    //home semaphores
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
	private Semaphore atBed = new Semaphore(0,true);
	
    public PersonGui PersonGui = null;
    public ResidentGui residentGui = null; 
	public TenantGui tenantGui = null;

	
	//Hacks for testing
	public boolean marketTime = false;
	public boolean bankTime=false;
	
	
	//Constructor
	public PersonAgent(String name) {
		super();
		
		this.name = name;
		state=PersonState.doingNothing;
		hungerLevel=70;
		myLocation=LocationState.atHome;
		//address="House 1";
		myTravelPreference=TravelPreference.walk;
        BankChoice="Bank "+ Integer.toString(generator.nextInt(1));        //CHANGE RANDOM TO 2 TO HAVE people go to both banks
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		Do("got time update. Time is " + hr+" Work Starts at " +myJob.startHour);
		int hour = hr;
		if(hr == 6) { 
			state = PersonState.doingNothing;
		}
		if(hr ==24) { 
			state = PersonState.tired;
		}
		if(hr==myJob.startHour-1) {
			state=PersonState.workTime;
		}
		
		hungerLevel+=10;
		stateChanged();
	}
	
	
	//Animation Messages
	public void msgAnimationArivedAtRestaurant() {
		atRestaurant.release();
		Do("JKNDSVJKSBNDVJKBSDJKVBSJFKBNVSJKF MSGANIMATIONAR");
		stateChanged();
	}
	
	public void msgAtFridge() {
		atFridge.release();
		stateChanged();
	}
	public void msgAtBed() {
		atBed.release();
		stateChanged();
	}
	public void msgAtGrill() {
		atGrill.release();
		stateChanged();
	}
	
//	public void msgLeftLocation() {
//		atLocation.release();
//		stateChanged();
//	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {

		
		if(state==PersonState.asleep||state==PersonState.dead){
			return false;
		}
		
		for(Role r: roles) {
			boolean hasActiveRole=false;
			boolean rolePAEAA=false;
			if (r.isActive) {
				hasActiveRole=true;
				rolePAEAA = r.pickAndExecuteAnAction();
			}
			else Do("INACTIVE BIATCH"+r.purpose);
			if(hasActiveRole) return rolePAEAA;
		}
		
		/************* hack to test behavior*******************/
		if(marketTime) {
			GoToMarket();
			return true;
		}
		
		//if(bankTime){
			//GoToBank();
		//}	
	/******************************************************/
		
		if (state==PersonState.tired){
			GoToBed();
			state=PersonState.asleep;
			return true;
		}
		
		
		if (state==PersonState.workTime){
			goToWork();
			return true;
		}
		
		if(hungerLevel>50) {
			if (money >= 40) {
				GoToRestaurant();
			}
			else {
				EatAtHome(); 
			}
			return true;
		}
		
		if(bankTime){
			GoToBank();
		}	
		
		if(!(myLocation==LocationState.atHome)) {
			GoHome();
			return true;
		}
		return false;
	}

	
	
	
	

	
	
	//ACTIONS
	private void EatAtHome() {
		if (homeAddress.equals("House")) {
			residentGui.goToFridge();
			try {
				atFridge.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			residentGui.makeFood();
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			residentGui.goToTable();
		}
		else {
			tenantGui.goToFridge();
			try {
				atFridge.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			tenantGui.makeFood();
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tenantGui.goToTable();
		}
		hungerLevel = 0;
	}

	// Actions
	private void GoToRestaurant() {
		DoGoTo("Restaurant 3");
		Do("Going To Restaurant");
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myLocation=LocationState.atRestaurant;
		hungerLevel=0;
		state=PersonState.doingNothing;
		for(Role r: roles) {
			if(r instanceof DCustomerRole) {
				r.isActive=true;
				DMenu myMenu = new DMenu();
				((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
				((DCustomerRole)r).ActivateRole();
			}
		}
		
	}
	
	private void GoToMarket() {
		marketTime = false;
		Do("here");
		for(Role r: roles) {
			if(r instanceof MarketCustomerRole) {
				r.isActive = true;
				((MarketCustomerRole) r).populateOrderList("Steak", 1);
			}
		}

	}
	
	private void GoToBank() {
		DoGoTo("Bank 1");
		Do("Going to Bank");
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		myLocation=LocationState.atBank;
		bankTime = false;
		state=PersonState.doingNothing;
		for(Role r: roles) {
			if(r instanceof BankCustomerRole) {
				Do("YESSER");
				r.isActive = true;
				r.purpose="withdraw";
			}
		}
		money=200;
	}
	
	private void goToWork() {
		state=PersonState.doingNothing;
		Do("Going to work");
		myJob.isActive=true;
	}
	

	private void GoHome() {
		DoGoTo(homeAddress);
		myLocation=LocationState.atHome;
	}	

	private void GoToBed() {
		if(myLocation!=LocationState.atHome) DoGoTo(homeAddress);
		myLocation=LocationState.atHome;
		
		
		if (homeAddress.contains("House")) {
			residentGui.goToBed();
			try {
				atBed.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			tenantGui.goToBed();
			try {
				atBed.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void DoGoTo(String dest) {
		PersonGui.DoGoTo(dest);
	}
	
	
	
	//Getters & Setters
	public void SetHomeAddress(String ad) {
		homeAddress=ad;
		//System.err.println("home address added ... "+ homeAddress);
	}

	public void SetTravelPreference(String choice) {
		if (choice.equals("Walk")) {
			myTravelPreference=TravelPreference.walk;
		}
		else if (choice.equals("Bus")) {
			myTravelPreference=TravelPreference.bus;
		}
		else if (choice.equals("Car")) {
			myTravelPreference=TravelPreference.car;
		}
	}
	
	public void addCustomerRoles(Role r) {
		//for (Role r: roles) {
			r.myPerson=this;
			roles.add(r);
		//}
	}
	
	public void setMoney(double money) {
		this.money=money;
	}

	public void SetJob(Role job) {
		myJob=job;
		roles.add(myJob);
		myJob.isActive=true;
	}
		
	@Override
	public String getName() {
		return name;
	}
	
	
	//utilities

	public void setGui(PersonGui gui) {
		PersonGui = gui;
	}
	
	public void setGui(ResidentGui pg) {
		residentGui = pg; 
	}
	
	public void setGui(TenantGui pg) {
		tenantGui = pg; 
	}

	
	public PersonGui getGui() {
		return PersonGui;
	}

		
}



