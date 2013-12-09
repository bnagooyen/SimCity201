package simcity;

import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DMenu;
import simcity.Drew_restaurant.Drew_CustomerRole;
import agent.Agent;
import agent.Role;
import simcity.Bank.BankCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.gui.PersonGui;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.TenantGui;
import simcity.interfaces.Landlord;
import simcity.interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class PersonAgent extends Agent implements Person {

	Timer timer = new Timer();
	Random generator = new Random();

	private String name;
	public Role myJob;
	public String jobLocation;
	public List<Role> roles= new ArrayList<Role>();
	public double money=0;
	public String homeAddress;
	public String BankChoice;
	BusStopAgent busStop;
	public Map<String, BusStopAgent> busStops=new HashMap<String, BusStopAgent>(); 
	//States
	public int hungerLevel;
	enum PersonState { doingNothing, atRestaurant, workTime, tired, asleep, dead };
	public enum TransitState {justLeaving, goToBus, walkingToBus, onBus, goToCar, inCar, getOutCar, walkingtoDestination, atDestination, atBusStop, waitingAtStop, getOnBus, getOffBus,nothing };
	enum LocationState {atHome, atRestaurant, atBank, atWork};
	public enum MoneyState {poor, middle, rich};
	enum TravelPreference {walk, bus, car};
	TravelPreference myTravelPreference;
	private LocationState myLocation;
	private PersonState state;
	public MoneyState moneystate;
	private TransitState transit;
	private BusAgent bus;
	private CarAgent myCar;

	public String nearestStop="Stop4";
	public String destStop="Stop1";

	//housing information
	private Landlord myLandlord; 
	private boolean needToPayRent = false; 
	private double rentBill; 


	//GUI
	Semaphore atRestaurant = new Semaphore(0, true);
	Semaphore atLocation = new Semaphore(0, true);

	//home semaphores
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
	private Semaphore atBed = new Semaphore(0,true);
	Semaphore atBusStop= new Semaphore(0, true);

	public PersonGui PersonGui = null;
	public ResidentGui residentGui = null; 
	public TenantGui tenantGui = null;


	//Bank Constants
	public double depositThreshold=150.00;
	public double withdrawalThreshold=50;

	//Hacks for testing
	public boolean marketTime = false;
	public boolean bankTime=false;


	//Constructor
	public PersonAgent(String name) {
		super();

		this.name = name;
		transit=TransitState.goToBus;
		state=PersonState.doingNothing;
		hungerLevel=70;
		myLocation=LocationState.atHome;
		moneystate=MoneyState.poor;
		//address="House 1";
		myTravelPreference=TravelPreference.walk;
		BankChoice="Bank "+ Integer.toString(generator.nextInt(1)+1);        //CHANGE RANDOM TO 2 TO HAVE people go to both banks
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		Do("got time update. Time is " + hr);//+" Work Starts at " +myJob.startHour);
		int hour = hr;
		if(hr == 6) { 
			state = PersonState.doingNothing;
		}
		if(hr ==24) { 
			state = PersonState.tired;
		}
		if(myJob!=null){
			Do("Employed :)   (IN TIME UPDATE FOR PERSON  "+ myJob.startHour );
			if(hr==myJob.startHour-1) {
				Do("ITS WORK TIME!!!!!");
				state=PersonState.workTime;
			}
		}

		hungerLevel+=10;
		stateChanged();
	}

	public void msgBusIsHere(BusAgent b){
		setBus(b);
		transit=TransitState.getOnBus;
		stateChanged();
	}

	 public void msgAnimationAtBusStop(){
		
         atBusStop.release();
         transit=TransitState.atBusStop;
         stateChanged();
 }
	 
	public void msgAtStop(String destination){
		System.out.println("getting off message");
		//mydestination=destination;
		transit = TransitState.walkingtoDestination;
		stateChanged();
	}

	public void msgHereIsYourRentBill(Landlord l, double rentBill) {
		Do("Receiving rent bill");
		myLandlord = l;
		this.rentBill = rentBill; 
		needToPayRent = true; 
		stateChanged(); 
	}

	//Animation Messages
	public void msgAnimationArivedAtRestaurant() {
		atRestaurant.release();
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

	public void msgLeftBuilding() {
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
		
		if(transit==TransitState.goToBus) {
			
            GoToBusStop();
            return true;
		}
		
		if(transit==TransitState.atBusStop){
			tellBusStop();
			return true;
		}
		
    if(transit==TransitState.getOnBus){
            getOnBus();
            return true;
    }

    if(transit==TransitState.walkingtoDestination){

            getOffBusAndWalk();
           
            return true;
    }

		for(Role r: roles) {
			boolean hasActiveRole=false;
			boolean rolePAEAA=false;
			if (r.isActive) {
				if(name.equals("doreenHost")) Do("YAYAYAYAYAYAYYAYAYAYAYAYAYAYAYYA (Sched of personagent)"+r);
				hasActiveRole=true;
				rolePAEAA = r.pickAndExecuteAnAction();
			}
			if(hasActiveRole) return rolePAEAA;
		}

		//************* hack to test behavior*******************//*
	/*	if(marketTime) {
			GoToMarket();
			return true;
		}

		if(bankTime){
			GoToBank();
			return true;
		}	*/
		//******************************************************//*

		/*if (state==PersonState.tired){
			GoToBed();
			state=PersonState.asleep;
			return true;
		}


		if (state==PersonState.workTime){
			goToWork();
			return true;
		}

		if(hungerLevel>50 && transit==TransitState.walkingtoDestination) {
			if (money >= 40) {
				GoToRestaurant();
			}
			else {
				EatAtHome(); 
			}
			return true;
		}


		if(money>depositThreshold||(money<withdrawalThreshold && moneystate!=MoneyState.poor)||(moneystate==MoneyState.rich)){
			GoToBank();
			return true;
		}


		if(!(myLocation==LocationState.atHome)) {
			GoHome();
			return true;
		}*/
		return false;
	}








	//ACTIONS
	private void EatAtHome() {
		Do("Eating at Home");
		/*if(myLocation!=LocationState.atHome)*/ DoGoTo(homeAddress);
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myLocation=LocationState.atHome;

		if (homeAddress.contains("House")) {
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
			Do("Went to table in house");
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
			Do("Went to table in Apartment");
		}
		hungerLevel = 0;
	}

	// Actions
	private void GoToRestaurant() {
		DoGoTo("Restaurant 2");
		Do("Going To Restaurant");
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		myLocation=LocationState.atRestaurant;
		hungerLevel=0;
		//state=PersonState.doingNothing;
		for(Role r: roles) {
			if(r instanceof Drew_CustomerRole) {
				r.isActive=true;
				DMenu myMenu = new DMenu();
				//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
				//((DCustomerRole)r).ActivateRole();
			}
		}

	}
	
	 private void GoToBusStop(){

		 
		 transit=TransitState.walkingToBus;
		 DoGoTo(nearestStop);



 }
 private void tellBusStop(){
	 	System.out.println("telling bus test");
	 	transit=TransitState.waitingAtStop;
	 	busStops.get(nearestStop).msgWaitingForBus(this);
		
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
		DoGoTo(BankChoice);
		if (myTravelPreference == TravelPreference.walk) {
			Do("Going to Bank");
			try {
				atRestaurant.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			myLocation=LocationState.atBank;
			bankTime = false;
			//state=PersonState.doingNothing;
			for(Role r: roles) {
				if(r instanceof BankCustomerRole) {
					r.isActive = true;
					if(money>depositThreshold) r.purpose="deposit";
					else if(money<withdrawalThreshold) r.purpose="withdraw";
					else r.purpose="loan";
				}
			}
			stateChanged();
		}
	}

	private void goToWork() {
		if (myTravelPreference == TravelPreference.walk) {
			DoGoTo(jobLocation); 
			Do("Going to Work at"+ jobLocation);
			try {
				atRestaurant.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			myLocation=LocationState.atWork;
			myJob.isActive=true;
	
			state= PersonState.doingNothing;
			stateChanged();
		}
		else if (myTravelPreference == TravelPreference.bus) {
			DoGoTo("Bus Stop"); 
		}
		else if (myTravelPreference == TravelPreference.car) {
			DoGoTo("Car");
		}

	}


	private void GoHome() {
		DoGoTo(homeAddress);
		myLocation=LocationState.atHome;
	}	

	private void GoToBed() {
		if(myLocation!=LocationState.atHome) DoGoTo(homeAddress);
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private void getOnBus(){
		Do("getting on bus");

		PersonGui.DoGoTo(destStop);
		PersonGui.setPresent(false);
		bus.msgGettingOn(this, destStop);
		transit=TransitState.onBus;
	}

	private void getOffBusAndWalk(){
		
		Do("Walk to Destination");
		PersonGui.setPresent(true);
		//GoToRestaurant();
		
		transit=TransitState.nothing;
		


		/*  if (mydestination != "home") {
                 boolean haveRole=false;
                 neededRole=possibleRoles.get(mydestination);

                 if(needToGoToWork){
                         myJob.isActive=true;
                         needToGoToWork=false;
                 }
                 else{
                         for(Role role:roles){
                                 if(role==neededRole){
                                         role.isActive=true;                
                                         haveRole=true;
                                 }
                         }
                         if(!haveRole){
                                 roles.add(neededRole);
                                 neededRole.isActive=true;
                         }
                 }*/
	}

	private void goToCar(){
		Do("Do go To car");//gui?

		PersonGui.setPresent(false);
		myCar.msgGoToDestination(destStop, this);
		DoGoTo(destStop);
		transit=TransitState.inCar;
	}

	private void getOutCar(){
		PersonGui.setPresent(true);
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

	public void addCustomerRoles(ArrayList<Role> arrayList) {
		//for (Role r: roles) {
		for(Role r: arrayList) {
			roles.add(r);
		}
		//}
	}

	public void setMoney(double money) {
		this.money=money;
	}

	public void SetJob(Role job, String location) {
		myJob=job;
		jobLocation=location;
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

	public void setBus(BusAgent b){
		bus=b;
	}

	public void setCar(CarAgent c){
		myCar=c;
	}
	public void setStops(Map<String, BusStopAgent> stops){
		busStops=stops;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAtDestination(String destination) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getHouseNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAptNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getAptLet() {
		// TODO Auto-generated method stub
		return 0;
	}
}






