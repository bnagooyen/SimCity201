package simcity;

import agent.Agent;
import agent.Role;
import simcity.BRestaurant.BCustomerRole;
import simcity.Bank.BankCustomerRole;
//import simcity.Bank.BankManagerRole.MyClient;
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
import simcity.Drew_restaurant.interfaces.*;
import simcity.TTRestaurant.TCustomerRole;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.gui.PersonGui;
import simcity.gui.SimCityPanel;
import simcity.gui.SimCityPanel.Location;
import simcity.housing.LandlordRole;
import simcity.housing.gui.HomePersonGui;
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
	String currentStop;
	Timer timer = new Timer();
	private String name;
	Bus bus;
	BusStop busStop;
	public Car myCar=null;
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	Map<String,Role> possibleRoles = new HashMap<String,Role>();
	//List<Role> customerRoles = new ArrayList<Role>();
	private Semaphore inKitchen = new Semaphore(0, true);
	public Role myJob;
	public Role neededRole;
	public String mydestination;
	public String mylocation="home";
	public String jobLocation;
	
	
	public enum HomeType{apartment, house, homeless};
	HomeType homeType;
	int houseNum;
	int aptNum;
	char aptLet;
	public enum PersonState { none };
	public enum EnergyState {tired, asleep, awake, none };
	public enum LocationState { atHome, inTransit, atWork, Out };
	public enum TransitState {justLeaving, walkingToBus, onBus, goToCar, inCar, getOutCar, walkingtoDestination, atDestination, atBusStop, waitingAtStop, getOnBus, getOffBus };
	public enum MoneyState { poor, adequate, rich, haveLoan};
	private PersonState personState;
	public EnergyState energyState;
	public LocationState locationState;
	public TransitState transitState;
	private MoneyState moneyState;

	boolean flake;
	//boolean broke;
	public boolean needToGoToWork = false;
	public boolean activatedRole;

	public PersonGui PersonGui = null;
	public HomePersonGui homePersonGui = null; 

	private int hour=0;

	public double money=200.00;
	public double depositThreshold=100.00;
	public double withdrawalThreshold=20.00;
	public int kitchenAmnt = 5;
	private int hungerLevel = 0; // out of 100.. anything over 50 means hungry
	private int meals=0; //Don't need to keep track of what type of food customer buys?

	private SimCityPanel panel;
	private Landlord myLandlord; 
	private boolean needToPayRent = false; 
	private double rentBill; 
	
	//directories
	public Map<String, List<Location>> buildings = null;
	public Map<String, BusStop> busStops=new HashMap<String, BusStop>(); //STILL BLANK
	

	
	public PersonAgent(String name) {
		super();


		this.name = name;
		currentStop="stop1";

		personState=PersonState.none;
		energyState=EnergyState.asleep;
		locationState=LocationState.atHome;
		moneyState=MoneyState.adequate;
		transitState=TransitState.justLeaving;
		
		
//
//		possibleRoles.put("bank", new BankCustomerRole());
//		possibleRoles.put("market", new MarketCustomerRole());
//		possibleRoles.put("drestaurant", new DCustomerRole());
//		possibleRoles.put("drew_restaurant", new Drew_CustomerRole());
//		possibleRoles.put("brestaurant", new BCustomerRole());
//		possibleRoles.put("krestaurant", new KCustomerRole());
//		possibleRoles.put("trestaurant", new TCustomerRole());
//		possibleRoles.put("lrestaurant", new LCustomerRole());
	}
	
	public void setCustomerRoles(List<Role> role) {
		for(Role r: role) {
			if(r instanceof BankCustomer) {
				possibleRoles.put("bank", r);
			}
			else if(r instanceof MarketCustomer) {
				possibleRoles.put("market", r);
			}
			else if(r instanceof Drew_CustomerRole) {
				possibleRoles.put("drew_restaurant", r);
			}
			else if(r instanceof DCustomer) {
				possibleRoles.put("drestaurat", r);
			}
			else if(r instanceof BCustomer) {
				possibleRoles.put("drestaurat", r);
			}
			else if(r instanceof KCustomer) {
				possibleRoles.put("krestaurat", r);
			}
			else if(r instanceof TCustomer) {
				possibleRoles.put("trestaurat", r);
			}
			else if(r instanceof LCustomer) {
				possibleRoles.put("lrestaurat", r);
			}
		}
	}

	public void SetJob(Role job) {
		Do("job set to " + job);
		myJob =job;
		Do("my job is " + myJob);
		roles.add(myJob);
		
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
		else if(job instanceof DCashier || job instanceof Cook || job instanceof DWaiter){
			jobLocation="drestaurant";
		}
		else if(job instanceof Drew_Cashier || job instanceof Drew_Cook || job instanceof Drew_Waiter){
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
		
	}
	// The animation DoXYZ() routines

	//	public void setHost(Role r){
	//		host = r;
	//	}
	//utilities

	public void msgSetBuildingDirectory(Map<String, List<Location>> bu) {
		buildings = bu;
	}
	
	public  void msgSetBusDirectory(Map<String, BusStop> bs) {
		busStops=bs;
		setBusStop(busStops.get(currentStop));
		
	}
	public String getName() {
		return name;
	}

	public void msgInKitchen() {
		inKitchen.release();
	}

	// Messages
	public void msgTimeUpdate(int hr) {
		Do("got time update. Time is " + hr+" Work Starts at " +myJob.startHour);
		hour = hr;
		if(hr == 6) { 
			energyState= energyState.awake;
		}
		if(hr ==24) { 
			energyState = energyState.asleep;
		}
		if(hr==myJob.startHour-1) {
			needToGoToWork=true;
		}
		
		synchronized(roles) {
			for(Role r: roles) {
				if(r.isActive) {
					r.timeUpdate(hr);
				}
			}
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
		currentStop=destination;
		mydestination=destination;
		setBusStop(busStops.get(currentStop));
		transitState = TransitState.getOffBus;
		stateChanged();
	}

	public void msgBusIsHere(Bus b){
		setBus(b);
		transitState=TransitState.getOnBus;
		stateChanged();
	}

	public void msgAtDestination(String destination){
		mydestination=destination;
		mylocation=mydestination;
		//boolean haveRole=false;
		transitState=TransitState.getOutCar;
		stateChanged();
		/**
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
		*/
	}
	
	public void msgHereIsYourRentBill(Landlord l, double rentBill) {
		Do("Receiving rent bill");
		myLandlord = l;
		this.rentBill = rentBill; 
		needToPayRent = true; 
		stateChanged(); 
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
		/*boolean*/ activatedRole = false;
		synchronized(roles) {
			for(Role r: roles) {
				if(r.isActive) {
					anyTrue=r.pickAndExecuteAnAction();
					activatedRole=true;
				}
			}
		}
		if(activatedRole) return anyTrue;


		if(locationState!=LocationState.inTransit && !(energyState==EnergyState.asleep)) {
			if(moneyState==MoneyState.haveLoan){
				buyCar();
				return true;
			}

			if(needToGoToWork) {
				GoToWork();
				return true;
			}

			if(money>depositThreshold){
				deposit();
				return true;
			}
			if(money<withdrawalThreshold && moneyState!=MoneyState.poor){
				withdraw();
				return true;
			}
			if(hungerLevel>=50) {
				EatFood();
				return true;
			}
			if(moneyState==MoneyState.rich && myCar==null){
				getCarLoan();
				return true;
			}
			if(energyState==EnergyState.tired && locationState==LocationState.Out) {
				GoHome();
				return true;
			}
		}
		
		if(locationState==LocationState.atHome && !(energyState==EnergyState.asleep)) {
			if (needToPayRent) {
				payRent(); 
				return true; 
			}
			if(energyState==EnergyState.tired) {
				GoToBed();
				return true;
			}
		}
		
			//Don't travel if your already in the right place
		if(locationState==LocationState.inTransit && !(energyState==EnergyState.asleep)) {

			if (myCar==null){
				if(transitState==TransitState.justLeaving){
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
				if (transitState==TransitState.justLeaving){
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
		if(energyState!=EnergyState.asleep){
			if(moneyState==MoneyState.rich && hour>7 && hour<23){
				String choice=chooseRestaurant();
				mydestination=choice;
				locationState=LocationState.inTransit;
				homePersonGui.LeaveHouse(); 
			}
			else if(meals>0){
				Do("Eat at home");
				mydestination="home";
				homePersonGui.makeFood();
				try {
					inKitchen.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				meals--;
				homePersonGui.goToTable(); 
				
				
			}
			else if(moneyState==MoneyState.adequate && hour>7 && hour<23){
				String choice=chooseRestaurant();
				mydestination=choice;
				locationState=LocationState.inTransit;
				homePersonGui.LeaveHouse(); 
			}
			else if(moneyState==MoneyState.poor && hour>7 && hour<23){
				mydestination="market";
				locationState=LocationState.inTransit;
				possibleRoles.get("market").purpose="food";
				Role r = possibleRoles.get("market");
				((MarketCustomerRole) r).populateOrderList("Chicken", 1);
				((MarketCustomerRole) r).populateOrderList("P", 1);
				homePersonGui.LeaveHouse(); 

			}
			else{
				energyState=EnergyState.tired;
			}
		hungerLevel=0;
		}
	}
	
	private void payRent() {
		needToPayRent = false;
		if (money >= rentBill) {
			money -= rentBill; 
			myLandlord.msgHereIsARentPayment(this, rentBill);
		}
		else {
			myLandlord.msgCannotPayForRent(this); 
		}
	}
	
	private void GoHome() {
		Do("going home");
		mydestination="home";
		locationState=LocationState.inTransit;
		
	}

	private void GoToWork() {
		Do("going to work");
		mydestination= jobLocation;
		locationState=LocationState.inTransit;
		//homePersonGui.LeaveHouse(); 					ACTIVATE FOR GUI
	}

	private void GoToBed() {
		Do("going to bed"); 
		energyState=EnergyState.asleep;
		//homePersonGui.goToBed();						ACTIVATE FOR GUI

	}

	private void deposit() {
		Do("Going to deposit Money");
		mydestination="bank";
		//homePersonGui.LeaveHouse();						ACTIVATE FOR GUI 
		locationState=LocationState.inTransit;
		possibleRoles.get("bank").purpose="deposit";
		
	}

	private void withdraw() {
		Do("Going to Withdraw Money");
		mydestination="bank";
		//homePersonGui.LeaveHouse();						ACTIVATE FOR GUI 
		locationState=LocationState.inTransit;
		possibleRoles.get("bank").purpose="withdraw";

	}

	private void getCarLoan(){
		//homePersonGui.LeaveHouse(); 						ACTIVATE FOR GUI
		mydestination="bank";
		locationState=LocationState.inTransit;
		possibleRoles.get("bank").purpose="loan";
	}
	
	private void buyCar() {
		//homePersonGui.LeaveHouse();												ACTIVATE FOR GUI 
		Do("Go buy car");
		mydestination="market";
		locationState=LocationState.inTransit;
		possibleRoles.get("market").purpose="car";
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
		Do("getting on bus");
		bus.msgGettingOn(this, mydestination);
		transitState=TransitState.onBus;
	}

	private void getOffBusAndWalk(){
		//gui to get off
		Do("Walk to Destination");
		transitState=TransitState.atDestination;
		
		if (mydestination != "home") {
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
			}
		}
	}

	private void goToCar(){
		Do("Do go To car"); //gui?
		myCar.msgGoToDestination(mydestination, this);
		transitState=TransitState.inCar;
	}

	private void getOutCarAndWalk(){
		Do("Get out car");
		transitState=TransitState.atDestination;
		
		if (mydestination != "home") {
			boolean haveRole=false;
			locationState=LocationState.Out;
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
			}
		}
		else if(mydestination.equals("home")) locationState=LocationState.atHome;
	}
	// utilities

	private String chooseRestaurant(){
		Random generator=new Random();
		int choice = generator.nextInt(5);
		switch (choice){
			case 0: return "drew_restaurant";
			case 1: return "drestaurant";
			case 2: return "brestaurant";
			case 3: return "krestaurant";
			case 4: return "trestaurant";
			case 5: return "lrestaurant";
		}
		return "";
	}
	
	private void DoDie() {
		System.out.println("Dieing");
	}

	public void addRole(Role r) {
		roles.add(r);
	}

	public void setGui(PersonGui gui) {
		PersonGui = gui;
	}
	
	public void setGui(HomePersonGui gui) {
		homePersonGui = gui; 
	}

	public PersonGui getGui() {
		return PersonGui;
	}

	public void setBus(Bus b){
		bus=b;
	}
	
	public void setBusStop(BusStop b){
		busStop=b;
		
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
	
	public void setMoney(double amount){
		money=amount;
	}

	@Override
	public void setCar(CarAgent c) {
		// TODO Auto-generated method stub
		myCar= c;
		c.startThread();
	}

	@Override
	public Role GetJob() {
		// TODO Auto-generated method stub
		return myJob;
	}

	public void SetHome(HomeType homeorapt) {
		homeType = homeorapt;
	}

	public void SetApatmentLocation(int num, char let) {
		// TODO Auto-generated method stub
		aptNum = num;
		aptLet = let;
	}
	public void SetHouseLocation(int num) {
		houseNum = num;
	}

	@Override
	public HomeType GetHomeState() {
		// TODO Auto-generated method stub
		return homeType;
	}

	@Override
	public int getHouseNum() {
		// TODO Auto-generated method stub
		return houseNum;
	}

	@Override
	public int getAptNum() {
		// TODO Auto-generated method stub
		return aptNum;
	}

	@Override
	public char getAptLet() {
		// TODO Auto-generated method stub
		return aptLet;
	}

	
	

}



