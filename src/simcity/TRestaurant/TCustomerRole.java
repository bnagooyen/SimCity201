package simcity.TRestaurant;

import simcity.TRestaurant.gui.TCustomerGui;
import simcity.TRestaurant.gui.TWaiterGui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import simcity.PersonAgent;
import agent.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random; 

/**
 * Restaurant customer agent.
 */
public class TCustomerRole extends Role implements TCustomer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private TCustomerGui customerGui;
	private int freeTable;
	public double myMoney;
	private double check = 0;
	private double moneyOwed = 0; 
	private String myChoice;
	public List<String> myMenu;
	private List<String> Ordered
	= new ArrayList<String>(); 
	private int timeOrdered = 0; 
	Random randomOrder = new Random();
	SimCityGui gui;
	// agent correspondents
	private THostRole host;
	private TCashierRole cashier; 
	private TWaiter waiter; 

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, ReOrdering, WaitingForOrder, Eating, AskingForCheck, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, waiting, wantsToWait, followHost, seated, givingOrder, receivedOrder, doneEating, givingMoney, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * @param gui 
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public TCustomerRole(SimCityGui gui){
//		super(p);
		//this.name = name;
		this.gui = gui; 
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(THostRole host) {
		this.host = host;
	}
	
	public void setCashier(TCashierRole cashier) {
		this.cashier = cashier; 
	}

	public void setWaiter(TWaiter waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void msgRestaurantClosed() {
		state = AgentState.Leaving;
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void gotHungry() {//from animation
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "I'm hungry");
		Do("I'm hungry");
		myMoney = randomOrder.nextInt(20); 
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgPleaseWait() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Waiting for a table");
		Do("Waiting for a table"); 
		event = AgentEvent.waiting; 
		stateChanged(); 
	}

	public void msgSitAtTable(int tableNum, List<String> m) {
		freeTable = tableNum; 
		myMenu = m; 
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Received msgSitAtTable");
		Do("Received msgSitAtTable");
		event = AgentEvent.followHost;
		stateChanged();
	}

	public void msgWhatWouldYouLike() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Telling waiter my order");
		Do("Telling waiter my order");
		state = AgentState.Ordering;
		event = AgentEvent.givingOrder; 
		stateChanged(); 
	}

	public void msgHeresYourOrder() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Received my order");
		Do("Received my order."); 
		event = AgentEvent.receivedOrder;
		stateChanged(); 
	}
	
	public void msgHereIsYourCheck(double cost) {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Received my check");
		Do("Received my check"); 
		event = AgentEvent.givingMoney; 
		stateChanged(); 
	}
	
	public void msgChange (double c) {
		if (c < 0) {
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Didn't have enough money to pay.");
			Do("Didn't have enough money to pay. I owe " + c);
			myMoney = 0; 
		}
		else {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Received change and leaving now!");
		Do("Received change and leaving now!");
		}
		event = AgentEvent.donePaying;
		stateChanged(); 
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}




	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.waiting) {
			WaitForTable(); 
			return true; 
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Ordering;
			LookAtMenu(); 
			return true;
		}

		if (state == AgentState.Ordering && event == AgentEvent.givingOrder){
			state = AgentState.WaitingForOrder; 
			giveOrder(); 
			return true; 
		}

		if (state == AgentState.WaitingForOrder && event == AgentEvent.receivedOrder){
			state = AgentState.Eating;
			print("Should be eating food next");
			EatFood(); 
			return true; 
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.AskingForCheck;
			AskCheck(); 
			return true;
		}
		if (state == AgentState.AskingForCheck && event == AgentEvent.givingMoney) {
			state = AgentState.Paying; 
			PayForFood();
			return true; 
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying) {
			state = AgentState.Leaving; 
			leaveTable();
			return true; 
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			leaveRestaurant(); 
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Going to restaurant");
		Do("Going to restaurant");
		if (customerGui == null) {
			customerGui = new TCustomerGui(this);
			gui.myPanels.get("Restaurant 6").panel.addGui(customerGui);
			print("CustomerGui is " + customerGui); 
		}
		host.msgIWantFood(this);	//send our instance, so he can respond 
	}
	
	private void WaitForTable() {
		int temp = randomOrder.nextInt(5); 
		if (temp == 0) {
			host.msgTiredOfWaiting(this);
			state = AgentState.Leaving; 
			event = AgentEvent.doneLeaving;
			print ("I don't want to wait, I'm leaving now."); 
		}
		else {
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "I'll wait for a table");
			Do("I'll wait for a table.");
			customerGui.DoWaitInLine(); 
			event = AgentEvent.wantsToWait; 
		}
	}

	private void SitDown() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Being seated. Going to table");
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(freeTable);
	}

	private void LookAtMenu() {
		timer.schedule(new TimerTask() {
			public void run() {
				Order();
			}
		},
		1000);//how long to wait before running task
	}

	private void Order() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Calling waiter to give my order.");
		Do("Calling waiter to give my order.");
		waiter.msgReadyToOrder(this); 
	}



	private void giveOrder() {
		Do("I have " + myMoney + " dollars."); 
		if (myMoney >= 14.99) {
			if (timeOrdered > 0) {
				while (Ordered.contains(myChoice)) {
				int x = randomOrder.nextInt(myMenu.size());
				myChoice = myMenu.get(x);
				}
			}
			else {
				int x = randomOrder.nextInt(myMenu.size());
				myChoice = myMenu.get(x);
			}
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
			Do("Giving waiter my order");
			timeOrdered++; 
			Ordered.add(myChoice);
			waiter.msgHeresMyChoice(this, myChoice);
		}
		else if (myMoney >= 10.99) {
			if (timeOrdered > 0) {
				while (Ordered.contains(myChoice)) {
				int x = randomOrder.nextInt(3); 
				switch(x) {
				case 0: myChoice = "Salad";
				case 1: myChoice = "Chicken"; 
				break; 
				case 2: myChoice = "Pizza";
				break; 
				}
				}
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
				Do("Giving waiter my order");
				timeOrdered++; 
				Ordered.add(myChoice);
				waiter.msgHeresMyChoice(this, myChoice);
			}
			else {
				int x = randomOrder.nextInt(3); 
				switch(x) {
				case 0: myChoice = "Salad";
				case 1: myChoice = "Chicken"; 
				break; 
				case 2: myChoice = "Pizza";
				break; 
				}
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
				Do("Giving waiter my order");
				timeOrdered++; 
				Ordered.add(myChoice);
				waiter.msgHeresMyChoice(this, myChoice);
			}
		}
		else if (myMoney >= 8.99) {
			if (timeOrdered > 0) {
				while (Ordered.contains(myChoice)) {
				int x = randomOrder.nextInt(2); 
				switch(x) {
				case 0: myChoice = "Salad";
				break; 
				case 1: myChoice = "Pizza"; 
				break;
				}
				}
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
				Do("Giving waiter my order");
				timeOrdered++; 
				Ordered.add(myChoice);
				waiter.msgHeresMyChoice(this, myChoice);
			}
			else {
				int x = randomOrder.nextInt(2); 
				switch(x) {
				case 0: myChoice = "Salad";
				break; 
				case 1: myChoice = "Pizza"; 
				break;
				}
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
				Do("Giving waiter my order");
				timeOrdered++; 
				Ordered.add(myChoice);
				waiter.msgHeresMyChoice(this, myChoice);
			}
		}
		else if (myMoney >= 5.99) {
			myChoice = "Salad";
			if (Ordered.contains(myChoice)) {
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Can't order anything else");
				Do("Can't order anything else.");
				waiter.msgLeavingTable(this);
				customerGui.DoExitRestaurant();
				state = AgentState.DoingNothing; 
				event = AgentEvent.none; 
			}
			else {
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
				Do("Giving waiter my order");
				timeOrdered++; 
				Ordered.add(myChoice);
				waiter.msgHeresMyChoice(this, myChoice);
			}
		}
		else if (myMoney <= 5.89){
			int x = randomOrder.nextInt(5); 
			if (x < 2) {
				myChoice = "Salad";
				if (Ordered.contains(myChoice)) {
					AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Can't order anything else");
					Do("Can't order anything else.");
					waiter.msgLeavingTable(this);
					customerGui.DoExitRestaurant();
					state = AgentState.DoingNothing; 
					event = AgentEvent.none; 
				}
				else {
					AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Giving waiter my order");
					Do("Giving waiter my order");
					timeOrdered++; 
					Ordered.add(myChoice);
					waiter.msgHeresMyChoice(this, myChoice);
				}
			}
			else if (x >= 2){
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Don't have enough money. Leaving now.");
				print("Don't have enough money. Leaving now.");
				waiter.msgLeavingTable(this);
				customerGui.DoExitRestaurant();
				state = AgentState.DoingNothing; 
				event = AgentEvent.none; 
			}
		}
		
	}

	private void EatFood() {
		//Do("Eating my " + myMenu[myChoice]);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Done eating");
				Do("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		1000);//how long to wait before running task
	}
	
	private void AskCheck() {
		waiter.msgReadyForCheck(this); 
	}
	
	private void PayForFood() {
		if (check > myMoney + moneyOwed) {
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "I can't pay for my food");
			Do("I can't pay for my food!"); 
		}
		else {
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Paying for my food now");
			Do("Paying for my food now"); 
		}
		cashier.msgHereIsMyMoney(this, myMoney, check); 
	}

	private void leaveTable() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TCustomerRole", "Leaving.");
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}
	
	private void leaveRestaurant() {
		customerGui.DoExitRestaurant(); 
		isActive = false;
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

	public void setGui(TCustomerGui g) {
		customerGui = g;
	}

	public TCustomerGui getGui() {
		return customerGui;
	}
}