package simcity.Drew_restaurant;

//import restaurant.Host.Table;
import simcity.Drew_restaurant.gui.Drew_CookGui;
import simcity.Drew_restaurant.gui.Drew_CustomerGui;
import simcity.Drew_restaurant.gui.Menu;
import simcity.Drew_restaurant.gui.Bill;
import agent.Role;
import simcity.PersonAgent;

import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

import simcity.gui.Gui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.*;
//import java.util.Random;


/**
 * Restaurant customer agent.
 */
public class Drew_CustomerRole extends Role implements Drew_Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private Drew_CustomerGui customerGui;
	private SimCityGui gui;
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore inLine = new Semaphore(0,true);
	
	Double bill=0.0;
	Double Money=9.00;
	public Double debt=0.00;
	int numberAhead=0;

	//Gives choices of food
	private Menu menu = null;

	// agent correspondents
	private Drew_Waiter waiter;
	private Drew_Host host;
	private Drew_Cashier cashier;

	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Deciding, ReadyToOrder, Ordered, Eating,Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, toldToWait, followHost, seated,Decided, WaiterArrived, Served, doneEating, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.gotHungry;

	public boolean gotBill=false;
	
	/**
	 * Constructor for CustomerAgent class
	 * @param gui 
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public Drew_CustomerRole(SimCityGui G){
		super();
		gui=G;
		//this.name = name;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Drew_Host host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgGoHome() {
		state=AgentState.Leaving;
		event=AgentEvent.doneLeaving;
		stateChanged();
	}

	public void gotHungry() {//from animation
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I'm hungry" );
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void wait(int numAH){
		//event = AgentEvent.toldToWait;
		numberAhead=numAH;
		stateChanged();
	}

	public void followMeToTable(Menu m) {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Received msg followMeToTable");
		print("Received msg followMeToTable");
		event = AgentEvent.followHost;
		menu=m;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void whatWouldYouLike(){
		event = AgentEvent.WaiterArrived;
		stateChanged();
	};
	
	public void hereIsYourFood(){
		event = AgentEvent.Served;
		stateChanged();
	};
	
	//From waiter when initial choice is out of stock
	public void outOfChoice(String choice){
		menu.remove(choice);
		event = AgentEvent.seated;
		state = AgentState.BeingSeated;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		if(debt>0){
			Money=20.00;
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I found $20.00!!!!");
			print("I found $20.00!!!!");
		}
		stateChanged();
	}
	
	public void giveCheck(Double b, Drew_Cashier c){
		cashier=c;
		bill=b;
		print(""+b);
		gotBill=true;
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "got the check");
		print("got the check");
		stateChanged();
	}
	
	public void giveChange(Double change){
		if(change>=0){
			Money=change;
			debt=0.0;
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Received $"+Money+" of change");
			print("Received $"+Money+" of change");
		}
		else{
			debt=change*-1;
			//Money=0.00;
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I owe $"+debt+" extra next time I eat");
			print("I owe $"+debt+" extra next time I eat");
		}
		event = AgentEvent.donePaying;
		stateChanged();
	}
	
	public void atCashier(){
		atCashier.release();
	}
	
	public void inLine(){
		inLine.release();
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
			SitDown();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.toldToWait ){
			//WaitInLine(numberAhead);
			considerLeaving(numberAhead);
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Deciding;
			makeChoice();
			return true;
		}
		if (state == AgentState.Deciding && event == AgentEvent.Decided){
			state = AgentState.ReadyToOrder;
			callWaiter();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.WaiterArrived){
			state = AgentState.Ordered;
			orderFood();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.Served){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating && gotBill){
			state = AgentState.Paying;
			print("calling Pay Cashier");
			payCashier();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			DoGoHome(); 
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Going to restaurant");
		Do("Going to restaurant");
		host.whatIsWait(this);
		if(customerGui == null) {
			customerGui = new Drew_CustomerGui(this, (Drew_HostRole) host);
			gui.myPanels.get("Restaurant 2").panel.addGui(customerGui);
			Do("plfghjk");
		}
		Do("YAYAYAYAYA");
		customerGui.DoGetInLine(numberAhead);
		try {
			inLine.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Being seated. Going to table");
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat();
	}
	
	private void makeChoice() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Deciding...");
		Do("Deciding...");
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.Decided;
				stateChanged();
			}
		},
		getHungerLevel() * 300);//how long to wait before running task
	}
	
	private void callWaiter() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Ready to order");
		Do("Ready To Order");
		waiter.readyToOrder(this);
		stateChanged();
	}
	
	private void orderFood() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Ordering");
		Do("Ordering");
		String choice = chooseFood();
		waiter.heresMyChoice(this, choice);
	}
	
	private String chooseFood(){
		/*for(int i=0; i<menu.choices.size(); i++){
			if(name.toLowerCase().equals(menu.getItem(i).toLowerCase())){					//Hack to make cust order specific food
				String choice=name;
				if(menu.getPrice(choice)<Money){
					AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I would like "+choice);
					print("I would like "+choice);
					return choice;
				}
			}
		}*/
		String choice="";
		int rand = (int)(Math.random() * menu.choices.size());
		if(menu.choices.isEmpty()){					//If cook has no food left
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Out of Food");
			print("Out of Food");
			return "";
		}
		for(int i=0; i<menu.choices.size();i++){				//Cycles through foods to find affordable
			choice=menu.getItem((rand+i)%menu.choices.size());
			if(menu.getPrice(choice)<Money){
				AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I would like "+choice);
				print("I would like "+choice);
				return choice;
			}
		}
		if(name.toLowerCase().equals("flake")){
			choice=menu.getItem(rand);
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I would like "+choice);
			print("I would like "+choice);
			return choice;
		}
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "I can't afford anything, I'm Leaving");
		print("I can't afford anything, I'm Leaving");
		state = AgentState.Paying;
		event = AgentEvent.donePaying;
		stateChanged();
		return "leaving";
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
			public void run() {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Leaving");
		Do("Leaving.");
		waiter.DoneEating(this);
		customerGui.DoExitRestaurant();
	}
	
	private void payCashier(){
		customerGui.goToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "Paid Cashier "+ Money+" dollars for a $"+bill+" bill.");
		print("Paid Cashier "+ Money+" dollars for a $"+bill+" bill.");
		cashier.payBill(bill, Money, this);
		Money=0.0;
	}
	
	private void considerLeaving(int numberAhead){
		int rand = (int)(Math.random() * 1000);
		if(numberAhead!=0){
			if(!(rand%numberAhead==0)){
				state =AgentState.Leaving;
				event =AgentEvent.doneLeaving;
				host.leaving(this);
				//customerGui.gui.setCustomerEnabled(this);
				customerGui.isHungry=false;
				AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCustomer", "CUSTOMER LEAVING BECAUSE OF WAIT!!!!!!!!CUSTOMER LEAVING BECAUSE OF WAIT!!");
				print("CUSTOMER LEAVING BECAUSE OF WAIT!!!!!!!!CUSTOMER LEAVING BECAUSE OF WAIT!!");
			}
		}
	}
	
	private void DoGoHome() {
		customerGui.DoExitRestaurant();
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

	public void setGui(Drew_CustomerGui g) {
		customerGui = g;
	}
	
	public void setWaiter(Drew_Waiter w) {
		waiter = w;
	}
	
	public Drew_Waiter getWaiter(){
		return waiter;
	}

	public Drew_CustomerGui getGui() {
		return customerGui;
	}
	
	public double getDebt(){
		return debt;
	}
}

