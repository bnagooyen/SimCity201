package simcity.BRestaurant;

import simcity.PersonAgent;
import simcity.BRestaurant.*;
import simcity.interfaces.*;
import simcity.BRestaurant.gui.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import agent.Role;

/**
 * Restaurant customer agent.
 */
public class BCustomerRole extends Role implements BCustomer {
	private String name;
	private int hungerLevel = 5;// determines length of meal
	private int mytable;
	public int order;
    private int money;
    public int finalmoney;
	
	Timer timer = new Timer();
	private BCustomerGui customerGui;
	// agent correspondents
	private BWaiter waiter;
	private BHostRole host;
	private BCashier cashier;
	private BMenu menu;
	private BCheck mycheck;
	

	

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadytoOrder, Waiting, waiterCalled, Eating, DoneEating, GettingCheck, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, waiterComing, doneEating, decided, foodGiven, reorder, receivedCheck, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	

	public BCustomerRole(PersonAgent p){
		super(p);
		host = (BHostRole) r;
		name = p.getName();

		state=AgentState.DoingNothing;
		event = AgentEvent.gotHungry; //event is the state change		
	}
	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(BHostRole host) {
		this.host = host;
	}
	
	public void setWaiter(BWaiter waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}
	
	private void setToPause(){
		
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	
	
	public void msgSitAtTable(int tablenumber, BMenu menu, BWaiter waiter) {
		this.waiter=waiter;
		this.menu=menu;
		mytable=tablenumber;
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
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

	public void msgWhatWouldYouLike(BWaiter waiter){
		event = AgentEvent.waiterComing;
		stateChanged(); 
	}

	public void msgHereisYourOrder(String choice){
		event = AgentEvent.foodGiven;
		stateChanged();
	}
	
	public void msgHereisYourCheck(BCheck check){
		mycheck=check;
		event=AgentEvent.receivedCheck;
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
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown(mytable, waiter);
			return true;
		}


		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadytoOrder;
			makeChoice();

			return true;
		}

		if (state == AgentState.ReadytoOrder && event == AgentEvent.decided){
			state = AgentState.waiterCalled;
			waiterCall();
			return true;
		}

		if (state == AgentState.waiterCalled && event == AgentEvent.waiterComing){
			state = AgentState.Waiting;
			orderFood();
			return true;
		}
		
		if(event == AgentEvent.reorder) {
			reOrder();
			print("reorder test in scheduer");
	    	return true;
		}
	    	


		if (state == AgentState.Waiting && event == AgentEvent.foodGiven){
			state = AgentState.Eating;
			EatFood();
			

			return true;
		}




		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.GettingCheck;
			requestCheck();
			return true;
		}
		
		if (state == AgentState.GettingCheck && event == AgentEvent.receivedCheck){
			
			state = AgentState.DoingNothing;
			payCheck();
			leaveTable();
			return true;
		}
		
		
		
		
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown(int mytable, BWaiter w) {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(mytable);//hack; only one table
		w.msgSitCustomerAtTable(this, mytable);
	}



	private void makeChoice(){
		msgdecidedOrder();
	}


	public void msgdecidedOrder(){
		event=AgentEvent.decided;
		stateChanged();
	}

	public void msgReorder() {
    	event=AgentEvent.reorder;
    	stateChanged();

    }

	//actions
	private void waiterCall(){
		print("I decided!");
		waiter.msgReadytoOrder(this);

	}

	private void orderFood(){
		
		
		
		if (money==6){
			String choice = new String();
			choice=menu.choices[2];
			waiter.msgHereismyChoice(this, choice);
			print(choice);
		}

		else {
		String choice = new String();
		choice=menu.choices[(int)(Math.random()*4)];
		waiter.msgHereismyChoice(this, choice);
		print(choice);
		}
	}

	private void reOrder(){
		
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}
	
	private void requestCheck(){
		waiter.msgIWantCheck(this);
		stateChanged();
	}


	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void payCheck(){
		if (money<mycheck.price){
			leaveTable();
			waiter.msgCustomerNoMoney();
			stateChanged();
		}
		
		else{
		cashier.msgPayCheck(mycheck);
		money=money-mycheck.price;
		stateChanged();
		}
	}

	 public void setCashier(BCashier cashier){
			this.cashier = cashier;
	    }
	 
	private void leaveTable() {
		Do("Leaving.");
		host.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	public int getmyTable(){
		return mytable;
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

	public void setGui(BCustomerGui g) {
		customerGui = g;
	}

	public BCustomerGui getGui() {
		return customerGui;
	}
}