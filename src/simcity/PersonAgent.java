package simcity;

import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DMenu;
import simcity.Drew_restaurant.Drew_CustomerRole;
import agent.Agent;
import agent.Role;
import simcity.BRestaurant.BCustomerRole;
import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankManagerRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
import simcity.TRestaurant.TCustomerRole;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.gui.PersonGui;
import simcity.gui.SimCityPanel.Business;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.TenantGui;
import simcity.interfaces.Host;
import simcity.interfaces.Landlord;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;








//import java.nio.file.DirectoryIteratorException;
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

	//time 
	int hour; 
	int startHour;
	
	Timer timer = new Timer();
	Random generator = new Random();
	public String jobString;
	private String name;
	public Role myJob;
	public String jobLocation;
	public List<Role> roles= new ArrayList<Role>();
	public double money=0;
	public String homeAddress;
	BusStopAgent busStop;
	public Map<String, BusStopAgent> busStops=new HashMap<String, BusStopAgent>(); 
	//States
	public int hungerLevel;
	enum EnergyState { awake, tired, sleeping};
	EnergyState energystate;
	enum PersonState { doingNothing, atRestaurant, workTime, asleep, dead, travelling };
	public enum TransitState {justLeaving, goToBus, walkingToBus, onBus, goToCar, inCar, getOutCar, walkingtoDestination, atDestination, atBusStop, waitingAtStop, getOnBus, getOffBus };
	enum LocationState {atHome, atRestaurant, atBank, atWork, atMarket};
	public enum MoneyState {poor, middle, rich};
	enum TravelPreference {walk, bus, car};
	TravelPreference myTravelPreference;
	private LocationState myLocation;
	private PersonState state;
	public MoneyState moneystate;
	private TransitState transit;
	private BusAgent bus;
	private CarAgent myCar;

	public String nearestStop;
	public String destStop;
	public String myDestination;
	//housing information
	private Landlord myLandlord; 
	private boolean needToPayRent = false; 
	private double rentBill; 

	//kitchen variable
	private int kitchenAmount;
	private static final int kitchenThreshold = 2;
	private static final int kitchenMax = 5;

	//GUI
	Semaphore atRestaurant = new Semaphore(0, true);
	Semaphore atLocation = new Semaphore(0, true);
	Semaphore travelSem = new Semaphore(0, true);

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
	public enum NextLoc {m1, r1, r3, r4, m3, b2, m4, r6, r5, m2, r2, b1, home, done };
	NextLoc tourState;
	public  Map<String, Business> directory = new HashMap<String, Business>();
	public String BankChoice;
	public String MarketChoice;
	public String RestChoice;
	
	//prices of market
	Map<String, Double> marketPrices = new HashMap<String, Double>();

	//visitor boolean
	public boolean goToAll;

	//Constructor
	public PersonAgent(String name) {
		super();
		goToAll=false;
		this.name = name;
		//transit=TransitState.goToBus;
		state=PersonState.doingNothing;
		hungerLevel=70;
		myLocation=LocationState.atHome;
		moneystate=MoneyState.poor;
		//address="House 1";
		myTravelPreference=TravelPreference.walk;
		BankChoice="Bank "+ Integer.toString(generator.nextInt(2)+1);        //CHANGE RANDOM TO 2 TO HAVE people go to both banks
		MarketChoice="Market "+ Integer.toString(generator.nextInt(4)+1);
		energystate=EnergyState.sleeping;

        marketPrices.put("Steak", 10.0);
        marketPrices.put("Chicken", 7.0);
        marketPrices.put("Salad", 3.0);
        marketPrices.put("Pizza", 5.0);
        
        
        //Random rand = new Random();
        //RestChoice= "Restaurant "+ Integer.toString(rand.nextInt(6)+1);
       
        
        tourState = NextLoc.m1;
		
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		Do("got time update. Time is " + hr);//+" Work Starts at " +myJob.startHour);
		hour = hr;
		if(myJob!=null) startHour=directory.get(jobLocation).openHour-2;
		else startHour=50;
		if(hr > 7 || hr==startHour) { 
			state = PersonState.doingNothing;
			energystate=EnergyState.awake;
		}
		if(hr == 0) { 
			energystate=EnergyState.tired;
		}
		if(myJob!=null){
			if(hr==directory.get(jobLocation).openHour-2) {
				Do("ITS WORK TIME!!!!! " + myJob);
				state=PersonState.workTime;
			}
		}

		// if job is a manager/host, send timeupdate
		if(myJob instanceof BankManagerRole || myJob instanceof MarketManagerRole || myJob instanceof Host) {
			if(myJob.isActive) {
				myJob.msgTimeUpdate(hr);
				Do("Gave Time to "+myJob);
			}
		}
		hungerLevel+=10;
		if(name.equals("qaz")) print("QUAZ GOT STATECHANGE");
		stateChanged();
	}

	public void msgBusIsHere(BusAgent b){
		travelSem.release();
		setBus(b);
		transit=TransitState.getOnBus;
		stateChanged();
	}

	public void msgAnimationAtBusStop(){

		travelSem.release();
		transit=TransitState.atBusStop;
		stateChanged();
	}

	public void msgAtStop(String destination){
		travelSem.release();
		System.out.println("getting off message ");
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

	public void msgWorkTime(){
		state=PersonState.workTime;
		energystate=EnergyState.awake;
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

	//        public void msgLeftLocation() {
		//                atLocation.release();
		//                stateChanged();
		//        }


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if(name.equals("qaz")) print("QUAZ IS IN STATECHANGE");

		if(energystate==EnergyState.sleeping||state==PersonState.dead){
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

			getOffBusAndWalkToWork();
			return true;
		}

		if(transit==TransitState.goToCar){
			goToCar();
			return true;

		}

		if(transit==TransitState.getOutCar){
			getOutCarAndGoToWork();
			return true;
		}

		for(Role r: roles) {
			boolean hasActiveRole=false;
			boolean rolePAEAA=false;
			if (r.isActive) {
				hasActiveRole=true;
				rolePAEAA = r.pickAndExecuteAnAction();
			}
			if(hasActiveRole) return rolePAEAA;
		}

		//GOING TO ALL PLACES
		if(goToAll){ //boolean that is set for visitors

			if(tourState.equals(NextLoc.m1)){
				if(!directory.get("Market 1").down){
					MarketChoice = "Market 1";
					money = 500;
					kitchenAmount = 0;
					GoToMarket();
				}
				tourState = NextLoc.r1;
				
				return true;
			}
			else if(tourState.equals(NextLoc.m2)){
				if(!directory.get("Market 2").down){
					MarketChoice = "Market 2";
					money = 500;
					kitchenAmount = 0;
					GoToMarket();
				}
				tourState = NextLoc.r2;
				
				return true;
			}
			else if(tourState.equals(NextLoc.m3)){
				if(!directory.get("Market 3").down){
					MarketChoice = "Market 3";
					money = 500;
					kitchenAmount = 0;
					GoToMarket();
				}
				tourState = NextLoc.b2;
				return true;
			}
			else if(tourState.equals(NextLoc.m4)){
				if(!directory.get("Market 4").down){
					MarketChoice = "Market 4";
					money = 500;
					kitchenAmount = 0;
					GoToMarket();
				}
				tourState = NextLoc.r6;
				return true;
			}
			else if(tourState.equals(NextLoc.b1)){
				if(!directory.get("Bank 1").down){
					BankChoice = "Bank 1";
					money = 1000;
					GoToBank();
				}
				tourState = NextLoc.home;
				return true;
			}
			else if(tourState.equals(NextLoc.b2)){
				if(!directory.get("Bank 2").down){
					BankChoice = "Bank 2";
					money = 1000;
					GoToBank();
				}
				tourState = NextLoc.m4;
				return true;
			}
			else if(tourState.equals(NextLoc.r1)){
				if(!directory.get("Restaurant 1").down){
					RestChoice = "Restaurant 1";
					GoToRestaurant();
				}
				tourState = NextLoc.r3;
				return true;
			}
			else if(tourState.equals(NextLoc.r2)){
				if(!directory.get("Restaurant 2").down){
					RestChoice = "Restaurant 2";
					GoToRestaurant();
				}
				tourState = NextLoc.b1;
				return true;
			}
			else if(tourState.equals(NextLoc.r3)){
				if(!directory.get("Restaurant 3").down){
					RestChoice = "Restaurant 3";
					GoToRestaurant();
				}
				tourState = NextLoc.r4;
				return true;
			}
			else if(tourState.equals(NextLoc.r4)){
				if(!directory.get("Restaurant 4").down){
					RestChoice = "Restaurant 4";
					GoToRestaurant();
				}
				tourState = NextLoc.m3;
				return true;
			}
			else if(tourState.equals(NextLoc.r5)){
				if(!directory.get("Restaurant 5").down){
					RestChoice = "Restaurant 5";
					GoToRestaurant();
				}
				tourState = NextLoc.m2;
				return true;
			}
			else if(tourState.equals(NextLoc.r6)){
				if(!directory.get("Restaurant 6").down){
					RestChoice = "Restaurant 6";
					GoToRestaurant();
				}
				tourState = NextLoc.r5;
				return true;
			}
			else if(tourState.equals(NextLoc.home)){
//				print("SHOULD BE GOING HOME");
				tourState = NextLoc.done; 
				goToAll = false;
				money = 75;
				hungerLevel = 0;
				kitchenAmount = 100;
				GoHome();
				return true;
			}


		}


		if (energystate==EnergyState.tired){
			GoToBed();
			state=PersonState.asleep;
			return true;
		}


		if (state==PersonState.workTime){
			goToWork();
			return true;
		}

		if(hungerLevel>50) {
			if (money >= 40 && hour >= directory.get("Restaurant 3").openHour) {
				GoToRestaurant();
			}
			else{
				EatAtHome(); 
			}
			return true;
		}

	
		if((money>depositThreshold||(money<withdrawalThreshold && moneystate!=MoneyState.poor)) && !directory.get(BankChoice).down/*||(moneystate==MoneyState.rich)*/){
			//System.err.println(!directory.get(BankChoice).down);
			GoToBank(); //going to have to choose which bank
			if(state==PersonState.workTime)Do("1234567890"+myJob);
			return true;
		}

		if(hour>=directory.get("Market 1").openHour && kitchenAmount < kitchenThreshold && money>= (marketPrices.get("Steak")+marketPrices.get("Chicken")+marketPrices.get("Salad")+marketPrices.get("Pizza"))) {
			GoToMarket();
			return true;
		}

		if(!(myLocation==LocationState.atHome)) {
			GoHome();
			return true;
		}
		return false;
	}








	//ACTIONS
	private void EatAtHome() {
		Do("Eating at Home");
		kitchenAmount--;
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
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.LeaveHouse(); 
			}
			else {
				tenantGui.LeaveHouse(); 
			}			
		}
		
		/******decide where to eat*******/
		//decision: closest restaurant to me
		int myRestaurantChoice = 1;
		int minTotDistance = Math.abs(directory.get("Restaurant 1").x- directory.get(homeAddress).x) + Math.abs(directory.get("Restaurant 1").y- directory.get(homeAddress).y);
		boolean restaurantIsOpen=false;
		for(int i=1; i<4; i++) {
			if(directory.get("Restaurant "+ Integer.toString(i)).openHour>=hour && directory.get("Restaurant "+ Integer.toString(i)).closeHour-1<hour) {
				restaurantIsOpen=true;
				int myDistance = Math.abs(directory.get("Restaurant "+ Integer.toString(i)).x- directory.get(homeAddress).x) + Math.abs(directory.get("Restaurant "+Integer.toString(i)).y- directory.get(homeAddress).y);
				if(myDistance < minTotDistance) {
					myRestaurantChoice = i;
					minTotDistance = myDistance;
				}
			}
		}
		
		if(!restaurantIsOpen) {
			System.out.println("There are no restaurants open.. must eat at home");
			EatAtHome();
			return;
		}
		
		RestChoice = "Restaurant "+ Integer.toString(myRestaurantChoice);

		DoGoTo(RestChoice);

		int restCustomerNum = Integer.parseInt(RestChoice.substring(RestChoice.length()-1));

		if (myTravelPreference == TravelPreference.walk) {
			Do("Going to "+RestChoice);
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

				if(restCustomerNum ==1){
					if(r instanceof LCustomerRole) {
						r.isActive=true;
						((LCustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
				else if(restCustomerNum ==2){
					if(r instanceof Drew_CustomerRole) {
						r.isActive=true;
						((Drew_CustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
				else if(restCustomerNum ==3){
					if(r instanceof DCustomerRole) {
						r.isActive=true;
						((DCustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
				else if(restCustomerNum ==4){
					if(r instanceof KCustomerRole) {
						r.isActive=true;
						((KCustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
				else if(restCustomerNum ==5){
					if(r instanceof BCustomerRole) {
						r.isActive=true;
						((BCustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
				else if(restCustomerNum ==6){
					if(r instanceof TCustomerRole) {
						r.isActive=true;
						((TCustomerRole) r).gotHungry();
						//((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
						//((DCustomerRole)r).ActivateRole();
					}
				}
			}
		}

	}

	private void GoToBusStop(){
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.LeaveHouse(); 
			}
			else {
				tenantGui.LeaveHouse(); 
			}			
		}
		
		

		transit=TransitState.walkingToBus;
		DoGoTo(nearestStop);
		try {
			travelSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	private void tellBusStop(){
		System.out.println("telling bus test");
		busStops.get(nearestStop).msgWaitingForBus(this);
		transit=TransitState.waitingAtStop;
		try {
			travelSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GoToMarket() {
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.LeaveHouse(); 
			}
			else {
				tenantGui.LeaveHouse(); 
			}			
		}

		DoGoTo(MarketChoice);
		int mktCustomerNum = Integer.parseInt(MarketChoice.substring(MarketChoice.length()-1));
//		print("MKTCUSTOMERNUM: "+mktCustomerNum);

		if (myTravelPreference == TravelPreference.walk) {
			Do("Going to "+MarketChoice);
			try {
				atRestaurant.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
			myLocation=LocationState.atMarket;

			//determine how much i can afford 
			boolean orderDetermined=false;
			int amountOrdering = (kitchenMax - kitchenAmount);
			while (!orderDetermined) {
				if(money>=amountOrdering * (marketPrices.get("Steak")+marketPrices.get("Chicken")+marketPrices.get("Salad")+marketPrices.get("Pizza"))) {
					orderDetermined=true;
				}
				else
					amountOrdering--;
			}
			
			//Do("here");
			for(Role r: roles) {
				if(r instanceof MarketCustomerRole ) {
					if(((MarketCustomerRole)(r)).num == mktCustomerNum) {
						
						((MarketCustomerRole) r).populateOrderList("Steak", amountOrdering);
						((MarketCustomerRole) r).populateOrderList("Chicken", amountOrdering);
						((MarketCustomerRole) r).populateOrderList("Salad", amountOrdering);
						((MarketCustomerRole) r).populateOrderList("Pizza", amountOrdering);
						
						print("STATE OF CUSTOMER IN GO TO: "+((MarketCustomerRole)r).state);
						
						r.isActive = true;
						
					}
				}
			}
			
			
			stateChanged();
		}

	}

	private void GoToBank() {
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.LeaveHouse(); 
			}
			else {
				tenantGui.LeaveHouse(); 
			}			
		}

		DoGoTo(BankChoice);
		int bCustomerNum = Integer.parseInt(BankChoice.substring(BankChoice.length()-1));

		if (myTravelPreference == TravelPreference.walk) {
			Do("Going to "+BankChoice);
			try {
				atRestaurant.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
			myLocation=LocationState.atBank;

			//state=PersonState.doingNothing;
			for(Role r: roles) {
				if(r instanceof BankCustomerRole) {
					if(((BankCustomerRole)(r)).num == bCustomerNum){
						
						if(money>depositThreshold) r.purpose="deposit";
						else if(money<withdrawalThreshold) r.purpose="withdraw";
						//else r.purpose="loan";
						r.isActive = true;
					}
				}
			}
			stateChanged();
		}
	}

	private void goToWork() {
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.LeaveHouse(); 
			}
			else {
				tenantGui.LeaveHouse(); 
			}			
		}

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
			 myDestination=jobLocation;
			 String tempAddress;
			 if(homeAddress.contains("Apartment")) {
				 tempAddress=homeAddress.substring(0, homeAddress.length()-1);
			 }
			 else{
				 tempAddress=homeAddress;
			 }
			nearestStop=directory.get(tempAddress).nearestBusStop;
             destStop=directory.get(jobLocation).nearestBusStop;
             transit=TransitState.goToBus;
             state=PersonState.travelling;
		}
		else if (myTravelPreference == TravelPreference.car) {
			DoGoTo("Car");
			myDestination=jobLocation;
			transit=TransitState.goToCar;
			state=PersonState.travelling;
		}

	}


	private void GoHome() {
		DoGoTo(homeAddress);
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myLocation=LocationState.atHome;
		if (myLocation == LocationState.atHome) {
			if (homeAddress.contains("House")) {
				residentGui.goToHomePosition(); 
			}
			else {
				tenantGui.goToHomePosition(); 
			}			
		}

		
	}        

	private void GoToBed() {
		if(myLocation!=LocationState.atHome) 
			DoGoTo(homeAddress);
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myLocation=LocationState.atHome;
		energystate=EnergyState.sleeping;

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
		energystate=EnergyState.sleeping;
	}

	private void getOnBus(){
		Do("getting on bus");
		transit=TransitState.onBus;
		PersonGui.DoGoTo(destStop);
		System.out.println("going to " +destStop);
		//PersonGui.setPresent(false);
		bus.msgGettingOn(this, destStop);
		
		try {
			travelSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getOffBusAndWalkToWork(){
		transit=TransitState.atDestination;
		System.out.println("getting off  "+ destStop );
		PersonGui.setPresent(true);
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

		//PersonGui.setPresent(false);
		PersonGui.takingCar(true);
		myCar.msgGoToDestination(myDestination, this);
		DoGoTo(myDestination);
		transit=TransitState.inCar;
		try {
			travelSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getOutCarAndGoToWork(){
		transit=TransitState.atDestination;
		PersonGui.takingCar(false);
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
//			if(r instanceof MarketCustomerRole){
//				print("NUMBERRR: "+((MarketCustomerRole)r).num);
//			}
			roles.add(r);
		}
		//}
	}

	public void setMoney(double money) {
		this.money=money;
	}

	public void SetJob(Role job, String location, String jobString) {
		myJob=job;
		this.jobString = jobString;
		jobLocation=location;
		roles.add(myJob);
		myJob.isActive=true;
	}

	@Override
	public String getName() {
		return name;
	}


	//utilities

	public String getJob() {
		return jobString;
	}
	public void setDirectory(Map<String, Business>direc){
		directory = direc;
		
	}

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
		travelSem.release();
		transit=TransitState.getOutCar;
        
        stateChanged();

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



