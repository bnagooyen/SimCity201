package simcity;

import agent.Agent;
import agent.Role;
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
	private String name;
	public int hungerLevel;
	enum PersonState { doingNothing, gotHungry, atRestaurant };
	enum LocationState {atHome, atRestaurant};
	private LocationState myLocation;
	private PersonState state;
	//private final int NUM_MARKETS = 3;
    Semaphore atRestaurant = new Semaphore(0, true);
    Semaphore atLocation = new Semaphore(0, true);
    //home semaphores
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
    public PersonGui PersonGui = null;
    public ResidentGui residentGui = null; 
	public TenantGui tenantGui = null;
    Role myJob;
    List<Role> roles= new ArrayList<Role>();
    public double money=0;
	public String homeAddress;
	
	
	public boolean marketTime = false;
	
	public PersonAgent(String name) {
		super();
		
		this.name = name;
		state=PersonState.doingNothing;
		hungerLevel=70;
		myLocation=LocationState.atHome;
		//address="House 1";
	}
	
	public void SetHomeAddress(String ad) {
		homeAddress=ad;
		//System.err.println("home address added ... "+ homeAddress);
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

	// The animation DoXYZ() routines
	


	@Override
	public String getName() {
		return name;
	}

	// Messages

//	public void gotHungry() {//from animation
//		print("I'm hungry");
//		state = PersonState.gotHungry; //event is the state change
//		stateChanged();
//	}
	
	public void msgAnimationArivedAtRestaurant() {
		atRestaurant.release();
		stateChanged();
	}
	
	public void msgAtFridge() {
		atFridge.release();
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
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//System.err.println("aaah");

//		if(state==PersonState.gotHungry) {
//			GoToRestaurant();
//			return true;
//		}
		
/************* hack to test behavior*******************/
		if(marketTime) {
			GoToMarket();
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
		
		for(Role r: roles) {
			boolean hasActiveRole=false;
			boolean rolePAEAA=false;
			if (r.isActive) {
				hasActiveRole=true;
				rolePAEAA = r.pickAndExecuteAnAction();
			}
			if(hasActiveRole) return rolePAEAA;
		}
		
		if(!(myLocation==LocationState.atHome)) {
			GoHome();
			return true;
		}
		
		return false;
		
		//return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


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
		try {
			atRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myLocation=LocationState.atRestaurant;
		hungerLevel=0;
		state=PersonState.atRestaurant;
		for(Role r: roles) {
			if(r instanceof DCustomerRole) {
				r.isActive=true;
				DMenu myMenu = new DMenu();
				((DCustomerRole)r).setChoice(myMenu.MostExpensiveICanAfford(money));
				((DCustomerRole)r).ActivateRole();
//				((CustomerRole)r).gotHungry();
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
	

	private void GoHome() {
		DoGoTo(homeAddress);
		myLocation=LocationState.atHome;
	}
	
	private void DoGoTo(String dest) {
		PersonGui.DoGoTo(dest);
	}
		
		
}



