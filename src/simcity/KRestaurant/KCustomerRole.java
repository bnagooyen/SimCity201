package simcity.KRestaurant;

import simcity.PersonAgent;
import simcity.KRestaurant.KMenu.Food;
import simcity.KRestaurant.gui.KCustomerGui;
import simcity.KRestaurant.gui.KFoodGui;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.interfaces.KCustomer;
import agent.Agent;
import agent.Role;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.KRestaurant.KMenu;

/**
 * Restaurant customer agent.
 */
public class KCustomerRole extends Role implements KCustomer{
	private String name;
	private int hungerLevel = 8;        // determines length of meal
	Timer timer = new Timer();
	private KCustomerGui customerGui;
	private KMenu menu = null;
	private String choice = "";
	private KFoodGui orderGui = null;
	private KRestaurantGui gui;
	private boolean hungry;
	private double check;
	private double myCash;
	private boolean willWait;
	private boolean flake;
	
	private Semaphore atRestaurant= new Semaphore(0, true);

	int table;
	int waitingPos;
	// agent correspondents
	private KHostRole host;
	private KWaiterRole waiter = null;
	private KCashierRole cashier = null;
	
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{doingNothing, waitingInRestaurant, beingSeated, lookingAtMenu, orderAgain, readytoOrder, callingWaiter, waitingForFood, eating, doneEating, leaving, makingDecision}
	
	private AgentState state = AgentState.doingNothing;

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, hostBack, gotNewMenu, foodHere, doneEating, doneLeaving, noMoneyLeaving, restaurantFull, goToWaiter};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public KCustomerRole(PersonAgent p){
		super(p);
		this.name = name;
		this.hungry = hungry;
		check = 0;
		myCash = 20;
		choice = null;
		willWait = true;
		flake = false;
		
//		String [] result = name.split("-");
//		int count = 0;
//		for(String s: result) {
//			if(count == 0){
//				if(s.equals("Steak")){
//					choice = "Steak";
//				}
//				else if (s.equals("Chicken")){
//					choice = "Chicken";
//				}
//				else if (s.equals("Salad")) {
//					choice = "Salad";
//				}
//				else if (s.equals("Pizza")) {
//					choice = "Pizza";
//				}
//				System.out.println("my choice is " + choice);
//			}
//			
//			if (count ==1){
//				try {
//					double money = Double.parseDouble(s);
//					if(money>=0 && money<=20) {
//						myCash = money;
//						Do("I've got $" +myCash);
//					}
//				}
//				catch(NumberFormatException e) {}
//			}
//			
//			if (count == 2) {
//				if(s.equals("flake")) {
//					System.out.println("I'm a flake");
//					flake = true;
//				}
//				else if(s.equals("impatient")) {
//					Do("I'm impatient and don't wanna wait");
//					willWait = false;
//				}
//			}
//			count++;
//		}
		
		
		if(hungry) {
			gotHungry();
		}
	}

	public void setHost(KHostRole host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		//print("I'm hungry");
		event = AgentEvent.gotHungry;
		
		stateChanged();
	}

	public void msgRestaurantFull(int waitingSpot) {
		Do("host said restaurant is full :(");
		event = AgentEvent.restaurantFull;
		waitingPos = waitingSpot;
		stateChanged();
	}
	
	public void msgGoToSeeWaiter() {
		event = AgentEvent.goToWaiter;
		stateChanged();
	}
	
	public void msgSitAtTable(int tableNum, KWaiterRole w, KMenu m) {
		print( "Received msgSitAtTable");
		event = AgentEvent.followHost;
		table = tableNum;
		waiter = w;
		menu = m;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.hostBack;
		System.out.println(this.name+": Asked what I want to order");
		stateChanged();
	}
	
	public void msgOutOfChoice(KMenu m) {
		menu = m;
		state = AgentState.orderAgain;
		event = AgentEvent.gotNewMenu;
		orderGui.setPresent(false);
		Random rand = new Random();
		int choicenum = rand.nextInt(menu.foods.size());
		choice = menu.foods.get(choicenum).type;
		if(!flake) {
			double cheapestFood = menu.foods.get(0).price;
			int indexCheapest = 0;
			int counter = 0;
			double secondCheapest = menu.foods.get(0).price;
			for(Food i : menu.foods) {
				if(i.price < cheapestFood) {
					secondCheapest = cheapestFood;
					cheapestFood = i.price;
					indexCheapest = counter;
				}
				counter++;
			}
			if( myCash < cheapestFood )
			{
				// if not a flake
				choice = "TooExpensive";
			}
			else if( myCash < secondCheapest && myCash >= cheapestFood) {
				choice = menu.foods.get(indexCheapest).type;
				Do("My choice is " + choice +" because I don't have money for anything else");
			}
		}
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		System.out.println(this.name+": got my food!");
		event = AgentEvent.foodHere;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	public void msgHereIsCheck( double check) {
		this.check = check;
		Do("got the check");
	}
	
	public void msgChange(double change) {
		myCash += change;
		Do("got change back " + change);
	}
	
	public void msgOrderChoiceTimerDone() {
		state = AgentState.readytoOrder;
		stateChanged();
	}
	
	public void msgOkLeave() {
		event = AgentEvent.noMoneyLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.doingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.waitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if(state == AgentState.waitingInRestaurant && event == AgentEvent.restaurantFull) {
			state = AgentState.makingDecision;
			tellHostIfWaiting();
			return true;
		}
		if(state == AgentState.makingDecision && event == AgentEvent.goToWaiter) {
			state = AgentState.waitingInRestaurant;
			GoToWaiter();
			return true;
		}
		if ((state == AgentState.waitingInRestaurant || state == AgentState.makingDecision) && event == AgentEvent.followHost ){
			state = AgentState.beingSeated;
			SitDown(table);
			return true;
		}
		if (state == AgentState.readytoOrder && event == AgentEvent.seated) {
			state = AgentState.callingWaiter;
			callWaiter();
			return true;
		}
		if (state == AgentState.orderAgain && event == AgentEvent.gotNewMenu) {
			state = AgentState.callingWaiter;
			callWaiterAgain();
			return true;
		}
		if (state == AgentState.callingWaiter && event == AgentEvent.hostBack) {
			state = AgentState.waitingForFood;
			orderFood();
			return true;
		}
		if (state == AgentState.waitingForFood&& event == AgentEvent.foodHere){
			state = AgentState.eating;
			EatFood();
			return true;
		}

		if (state == AgentState.eating && event == AgentEvent.doneEating){
			state = AgentState.leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.leaving && event == AgentEvent.doneLeaving){
			state = AgentState.doingNothing;
			//no action
			return true;
		}
		if (state == AgentState.waitingForFood && event == AgentEvent.noMoneyLeaving) {
			state = AgentState.leaving;
			leaveTable();
			return true;
		}
		return false;
	}

	// Actions
	
	private void goToRestaurant() {
		Do("Going to restaurant");
		//customerGui.DoGoToRestaurant();
		host.msgIWantFood(this);
	}
	
	private void GoToWaiter() {
		//customerGui.DoGoToRestaurant();
	}

	private void tellHostIfWaiting() {
		Do("telling host if i'll wait");
		host.msgDecideToWait(this, willWait);
		if(willWait) {
			//customerGui.DoGoToWaiting(waitingPos);
		}
	}
	
	private void SitDown(int tableNum) {
		Do("Being seated. Going to table");
		//customerGui.DoGoToSeat(tableNum);
		if(choice == null) {
			Random rand = new Random();
			int choicenum = rand.nextInt(menu.foods.size());
			choice = menu.foods.get(choicenum).type;
		}
		if(!flake){
			double cheapestFood = menu.foods.get(0).price;
			int indexCheapest = 0;
			int counter = 0;
			double secondCheapest = menu.foods.get(0).price;
			for(Food i : menu.foods) {
				if(i.price < cheapestFood) {
					secondCheapest = cheapestFood;
					cheapestFood = i.price;
					indexCheapest = counter;
				}
				counter++;
			}
			if( myCash < cheapestFood )
			{
				// if not a flake
				choice = "TooExpensive";
			}
			else if( myCash < secondCheapest && myCash >= cheapestFood) {
				choice = menu.foods.get(indexCheapest).type;
				Do("My choice is " + choice +" because I don't have money for anything else");
			}
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done deciding");
				msgOrderChoiceTimerDone();
				
			}
		},
		hungerLevel*1000);
	}
	
	private void callWaiterAgain() {
		
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done deciding");
				state = AgentState.readytoOrder;
				event = AgentEvent.seated;
				stateChanged();
			}
		},
		hungerLevel*1000);
	}
	
	private void callWaiter() {
		waiter.msgReadytoOrder(this);
		
	}

	private void orderFood() {
		if(choice.equals("TooExpensive")) {
			Do("Telling waiter food is too expensive");
			waiter.msgTooExpensiveLeaving(this);
		}
		else{
			System.out.println(this.name+": told waiter my order is " + choice);
			waiter.msgHereIsChoice(this, choice);
			orderGui = new KFoodGui(choice, gui, table);
			gui.animationPanel.addGui(orderGui);
		}
	}
	
	private void EatFood() {
		//orderGui.gotFood();
		Do("Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		hungerLevel*2000);
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		//customerGui.DoExitRestaurant();
		if(!choice.equals("TooExpensive")) {
			cashier.msgPayment(myCash, this, check);
			myCash = 0;
		}
		if(flake){
			myCash = 35;
		}
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(KCustomerGui g) {
		customerGui = g;
	}

	public KCustomerGui getGui() {
		return customerGui;
	}
	
}

