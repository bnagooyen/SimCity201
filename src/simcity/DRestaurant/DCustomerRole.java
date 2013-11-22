package simcity.DRestaurant;
 
import simcity.DRestaurant.gui.DCustomerGui;
import simcity.DRestaurant.gui.DRestaurantGui;
import simcity.restaurant.interfaces.Customer;
import simcity.PersonAgent;
import agent.Agent;
import agent.Role;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.concurrent.Semaphore;


/**
 * Restaurant customer agent.
 */
public class DCustomerRole extends Role implements Customer {
	private String name;
	private String myText;
	//parse through string
	public String choice;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private DCustomerGui customerGui;
	
	private boolean stayOrLeave;
	
	private DMenu myMenu;
	private double wallet;
	
	//semaphores
	Semaphore atFront = new Semaphore(0, true);
	
	// agent correspondents
	private DHostRole host;
	private DWaiterRole waiter;
	private DCashierRole cashier;
	
	//table
	private int tableNum;
	
	//check
	DCheck myBill = null;
	
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Ordered, Eating, Paying, DoneEating, Leaving, Gone, GoingToCashier, Paid, WaitingForCheck, WaitingInHangout, AtFront};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, iKnowWhatIWant, waiterIsHere, waiterIsHereForReorder, foodIsHere, doneEating, doneLeaving, arrivedAtCashier, gotReceipt, restaurantIsFull, goToHangout, tableReady};
	public AgentEvent event = AgentEvent.none; //initialized to none

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public DCustomerRole(PersonAgent p){
		super(p);
		//host = (DHostRole) r;
		name = p.getName();

		state=AgentState.DoingNothing;
		event = AgentEvent.gotHungry; //event is the state change		
	}
	
	public double getWallet() {
		return wallet;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(DHostRole host) {
		this.host = host;
	}

	public void setCashier(DCashierRole ca) {
		cashier= ca;
	}
	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry; //event is the state change
		stateChanged();
	}
	
	public void msgNoRoomForYou() {
		System.out.println("no room for me!");
		event=AgentEvent.restaurantIsFull;
		stateChanged();
	}
	
	public void msgGoToHangout() {
		event=AgentEvent.goToHangout;
		stateChanged();
	}
	
	public void msgYourTableIsReady(){
		event=AgentEvent.tableReady;
		stateChanged();
	}

	public void msgFollowMe(DMenu menu, int tnum, DWaiterRole w) {
		myMenu=menu;
		waiter=w;
		tableNum=tnum;
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	public void msgHereIsYourBill(int tnum, double d) {
		System.out.println("cust received bill");
		myBill = new DCheck((Customer)this, choice, tnum, d);
		stateChanged();
	}
	
	public void msgAnimationArrivedAtFront() {
		atFront.release();
		stateChanged();
	}
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		Do("I got to my seat!");
		event=AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.waiterIsHere;
		stateChanged();
	}
	public void msgWhatWouldYouLike(String foodOutOfStock) {
		event = AgentEvent.waiterIsHereForReorder;
		stateChanged();
	}
	
	public void msgFoodIsServed() {
		event = AgentEvent.foodIsHere;
		stateChanged();
	}
	
	public void msgHereIsYourReceiptAndChange(double num) {
		if(num>=0) {
			System.out.println("customer received change of "+ num);
			wallet+=num;
		}
		else {
		}
		event = AgentEvent.gotReceipt;
		stateChanged();
	}
	
	public void msgAnimationArrivedAtCashier() {
		System.err.println("made it to cashier!");
		event = AgentEvent.arrivedAtCashier;
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
			goToRestaurant();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.restaurantIsFull ){
			ShouldIStayOrShouldIGo();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.goToHangout ){
			GoToHangout();
			return true;
		}
		
		if (state == AgentState.WaitingInHangout && event == AgentEvent.tableReady ){
			GoToFront();
			return true;
		}
		
		if (state == AgentState.AtFront && event == AgentEvent.followHost ){
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			DecideWhatIWantToEat(myMenu);
			return true;
		}

		if (state == AgentState.Seated && event == AgentEvent.iKnowWhatIWant){
			CallWaiter();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.waiterIsHere){
			GiveOrder();
			return true;
		}
		if(state==AgentState.Ordered && event == AgentEvent.waiterIsHereForReorder) {
			GiveNewOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.foodIsHere){
			//no action
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating && myBill!=null){
			leaveTable();
			return true;
		}
		
		if(state==AgentState.GoingToCashier && event == AgentEvent.arrivedAtCashier && myBill!= null) {
			PayMyBill();
			return true;
		}
		
		if(state == AgentState.Paid && event == AgentEvent.gotReceipt) {
			LetsBounce(); // lol
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			clearState();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		state = AgentState.WaitingInRestaurant;
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
	
	private void ShouldIStayOrShouldIGo() {
		if(!stayOrLeave) {
			host.msgIDontWantToWait(this);
			DoLeaveRestaurant();
			state=AgentState.Leaving;
		}
		else 
			event=AgentEvent.none; // reset so won't call again
	}

	private void GoToHangout() {
		state=AgentState.WaitingInHangout;
		DoGoToHangout();
	}
	
	private void GoToFront() {
		DoGoToFront();
		
		try {
			atFront.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state=AgentState.AtFront;
		System.out.println("made it to front and sent msg to host that is ready");
		host.msgHereToGetSeated(this);
	}
	private void SitDown() {
		Do("Being seated. Going to table");
		state = AgentState.BeingSeated;
		DoGoToSeat();
		
	}
	
	private void DecideWhatIWantToEat(final DMenu myMenu) {
		//Do("Deciding what to order...");
		state = AgentState.Seated;
		timer.schedule(new TimerTask() {
			public void run() {
				
				Do("I know what I want!");
				event = AgentEvent.iKnowWhatIWant;
				//isHungry = false;
				stateChanged();
			}
		},
		3000);
		
		
	}

	private void CallWaiter() {
		state = AgentState.ReadyToOrder;
		waiter.msgImReadyToOrder(this);
	}
	
	private void GiveOrder() {
		System.out.println("giving waiter order");
		state = AgentState.Ordered;
		if(myMenu.getPrice(choice)>wallet) { //means can't afford what I ordered!
			System.out.println("dont have enough");
			String ch=myMenu.MostExpensiveICanAfford(wallet);
			if(ch.equals("None")) { // means can't afford anything else
				if(!stayOrLeave) { //for scenario 1 commit, disregard input and leave if can't afford
					waiter.msgCantAffordNotStaying(this);
					DoLeaveRestaurant();
					state=AgentState.Leaving;
					return;
				}
			}
			else {
				System.out.println("made new choice");
				choice = ch;
			}
		}
			//can afford
			waiter.msgHereIsMyChoice(this, choice);
			DoDisplayOrderCard();
			
	}
		
	
	private void GiveNewOrder() {
		if(!stayOrLeave) { //will leave if restaurant doesn't have their only choice 
			waiter.msgCantAffordNotStaying(this);
			DoLeaveRestaurant();
			state=AgentState.Leaving;
			DoHideOrderCard();
			return;
		}
		System.out.println("giving waiter new order");
		String ch=myMenu.OutOf(choice, wallet);
		System.out.println("menu recommended " + ch);
		
		if(ch.equals("None")) {
			state=AgentState.Leaving;
			waiter.msgCantAffordNotStaying(this);
			DoHideOrderCard();
			DoLeaveRestaurant();
			return;
		}
		Do("I'm ordering " +choice);
		waiter.msgHereIsMyChoice(this, choice);
		DoDisplayOrderCard();
		event=AgentEvent.none; // so doesn't pick new order again
	}
	
	private void EatFood() {
		state = AgentState.Eating;
		Do("Eating Food");
		DoUpdateOrderCard();
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				DoHideOrderCard();
				//isHungry = false;
				stateChanged();
			}
		},
		5000);
	}

	private void leaveTable() {
		System.out.println("In Customer action: Leave Table..");
		Do("Leaving.");
		//state = AgentState.Leaving;
		state = AgentState.GoingToCashier;
		waiter.msgDoneEatingAndLeaving(this);
		//DoLeaveRestaurant();
		DoGoToCashier();
	}
	
	
	private void PayMyBill() {
		DecimalFormat df = new DecimalFormat("###.##");
		System.err.println("PAYING BILL...");
		cashier.msgHereIsAPayment(this, myBill.getTablenum(), wallet);
		wallet=0;
		state=AgentState.Paid;
	}
	
	private void LetsBounce() {
		DoLeaveRestaurant();
		state=AgentState.Leaving;
		Random generator = new Random();
		wallet+=(double)generator.nextInt(25);
	}
	
	private void clearState() {
		state = AgentState.DoingNothing;
	}

	//DoXYZ commands
	
	private void DoGoToHangout() {
//		customerGui.DoGoToHangout();
	}
	
	private void DoGoToFront() {
//		customerGui.DoGoToFront();
	}
	
	private void DoGoToSeat() {
//		customerGui.DoGoToSeat(tableNum, 1);
	}
	
	private void DoDisplayOrderCard() {
//		customerGui.DoDisplayOrderCard(choice);
	}
	
	private void DoUpdateOrderCard() {
//		customerGui.DoUpdateOrderCard();
	}
	
	private void DoHideOrderCard() {
//		customerGui.DoHideOrderCard();
	}
	
	private void DoGoToCashier() {
//		customerGui.DoGoToCashier();
	}
	
	private void DoLeaveRestaurant() {
//		customerGui.DoExitRestaurant();
		//System.out.println("called doleave..");
		myBill=null; // wow, tricky little bug
	}
	
	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getText() {
		return myText;
	}
	
	public void setText(String t) {
		myText=t;
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

	public void setGui(DCustomerGui g) {
//		customerGui = g;
	}

	public DCustomerGui getGui() {
		return customerGui;
	}

}

