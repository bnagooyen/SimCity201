/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant;
 
import simcity.gui.SimCityGui;
import simcity.DRestaurant.DGui.DCustomerGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.DCustomer;
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
public class DCustomerRole extends Role implements DCustomer {
	private String name;
	private String myText;
	public String choice;
	private int hungerLevel = 5;        // determines length of meal
	
	//timer
	Timer timer = new Timer();
	private DCustomerGui customerGui;
	
	private boolean stayOrLeave;
	
	private DMenu myMenu;
	
	//semahores
	Semaphore atFront = new Semaphore(0, true);
	
	// agent correspondents
	public DHostRole host;
	private DWaiterRole waiter;
	public DCashierRole cashier;
	SimCityGui rGui;
	//table
	private int tableNum;

	DCheck myBill = null;
	
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Ordered, Eating, Paying, DoneEating, Leaving, Gone, GoingToCashier, Paid, WaitingForCheck, WaitingInHangout, AtFront};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, iKnowWhatIWant, waiterIsHere, waiterIsHereForReorder, foodIsHere, doneEating, doneLeaving, arrivedAtCashier, gotReceipt, restaurantIsFull, goToHangout, tableReady, restaurantIsClosed};
	AgentEvent event = AgentEvent.none; //initialized to none

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public DCustomerRole(SimCityGui gui){
		super();
		this.rGui=gui;

		state=AgentState.DoingNothing;
		
	}
	public void setChoice(String ch) {
		choice=ch;
	}
	
	public double getWallet() {
		return myPerson.money;
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

	public void gotHungry() {
		// TODO Auto-generated method stub
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "I'm hungry");
		print("I'm hungry");
		event = AgentEvent.gotHungry; //event is the state change
		stateChanged();
	}
	
	public void msgRestaurantIsClosed() {
		event=AgentEvent.restaurantIsClosed;
		stateChanged();
	}
	@Override
	public void msgNoRoomForYou() {
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "No room for me.");
		Do("no room for me!");
		event=AgentEvent.restaurantIsFull;
		stateChanged();
	}
	
	@Override
	public void msgGoToHangout() {
		event=AgentEvent.goToHangout;
		stateChanged();
	}
	
	@Override
	public void msgYourTableIsReady(){
//		System.err.println("receivedmsgtableready");
		event=AgentEvent.tableReady;
		stateChanged();
	}

	@Override
	public void msgFollowMe(DMenu menu, int tnum, DWaiterRole w) {
		myMenu=menu;
		waiter=w;
		tableNum=tnum;
		//System.out.println("received followme msg");
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourBill(int tnum, double d) {
		//System.out.println(tnum);
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Cust received bill");
		System.out.println("cust received bill");
		myBill = new DCheck(this, choice, tnum, d);
//		System.err.println(myBill);
		stateChanged();
	}
	
	public void msgAnimationArrivedAtFront() {
		atFront.release();
		stateChanged();
	}
	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "I got to my seat.");
		Do("I got to my seat!");
		event=AgentEvent.seated;
		stateChanged();
	}
	
	@Override
	public void msgWhatWouldYouLike() {
		event = AgentEvent.waiterIsHere;
		//System.out.println("received msg waiter here");
		stateChanged();
	}
	@Override
	public void msgWhatWouldYouLike(String foodOutOfStock) {
		event = AgentEvent.waiterIsHereForReorder;
		//System.out.println("received msg waiter here for reorder");
		stateChanged();
	}
	
	@Override
	public void msgFoodIsServed() {
		event = AgentEvent.foodIsHere;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourReceiptAndChange(double num) {
		if(num>=0) {
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Customer received change");
			System.out.println("customer received change of "+ num);
			myPerson.money+=num;
			//System.out.print("wallet now = "+ wallet);
		}
		else {
			//used this statement to test acquiring debt
			;//System.out.println("customer acquired debt of "+ (-1)*num);
		}
		event = AgentEvent.gotReceipt;
		stateChanged();
	}
	
	@Override
	public void msgAnimationArrivedAtCashier() {
		System.err.println("made it to cashier!");
		event = AgentEvent.arrivedAtCashier;
		stateChanged();
		
	}
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
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
		
		if(state==AgentState.WaitingInRestaurant && event == AgentEvent.restaurantIsClosed) {
			LeaveRestaurant();
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
			//EatFood();
			return true;
		}

		if (state == AgentState.Seated && event == AgentEvent.iKnowWhatIWant){
			CallWaiter();
			//leaveTable();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.waiterIsHere){
			//System.out.println("made it to giveorder call");
			GiveOrder();
			return true;
		}
		if(state==AgentState.Ordered && event == AgentEvent.waiterIsHereForReorder) {
			//System.out.println("got to new order");
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
		customerGui = new DCustomerGui(this, rGui);
		customerGui.setPresent(true);
		rGui.myPanels.get("Restaurant 3").panel.addGui(customerGui);
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Going to restaurant");
		Do("Going to restaurant");
		state = AgentState.WaitingInRestaurant;
		host.msgIWantFood(this);//send our instance, so he can respond to us

	}
	private void LeaveRestaurant() {
		DoLeaveRestaurant();
		state=AgentState.Leaving;
	}
	private void ShouldIStayOrShouldIGo() {
		//System.out.println("in shouldistay");
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
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Being seated. Going to table");
		Do("Being seated. Going to table");
		state = AgentState.BeingSeated;
		DoGoToSeat();

	}
	
	private void DecideWhatIWantToEat(final DMenu myMenu) {
		//Do("Deciding what to order...");
		state = AgentState.Seated;
		choice = myMenu.MostExpensiveICanAfford(myPerson.money);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "I know what I want");
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
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Giving waiter order");
		System.out.println("giving waiter order");
		state = AgentState.Ordered;
		if(myMenu.getPrice(choice)>myPerson.money) { //means can't afford what I ordered!
			System.out.println("dont have enough");
			String ch=myMenu.MostExpensiveICanAfford(myPerson.money);
			if(ch.equals("None")) { // means can't afford anything else
				if(!stayOrLeave) { //for scenario 1 commit, disregard input and leave if can't afford
					waiter.msgCantAffordNotStaying(this);
					DoLeaveRestaurant();
					state=AgentState.Leaving;
					return;
				}
			}
			else {
				
				AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Made new choice");
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
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Giving waiter new order");
		System.out.println("giving waiter new order");
		String ch=myMenu.OutOf(choice, myPerson.money);
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
			@Override
			public void run() {
			
				event = AgentEvent.doneEating;
				DoHideOrderCard();
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {

		System.out.println("In Customer action: Leave Table..");
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCustomerRole", "Leaving");
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
		cashier.msgHereIsAPayment(this, myBill.getTablenum(), myPerson.money);
		myPerson.money=0;
		state=AgentState.Paid;
	}
	
	private void LetsBounce() {
		DoLeaveRestaurant();
		state=AgentState.Leaving;
		Random generator = new Random();
		//myPerson.money+=generator.nextInt(25);
	}
	
	private void clearState() {
		state = AgentState.DoingNothing;
		isActive=false;
	}

	//DoXYZ commands
	
	private void DoGoToHangout() {
		customerGui.DoGoToHangout();
	}
	
	private void DoGoToFront() {
		customerGui.DoGoToFront();
	}
	
	private void DoGoToSeat() {
		customerGui.DoGoToSeat(tableNum, 1);
	}
	
	private void DoDisplayOrderCard() {
		customerGui.DoDisplayOrderCard(choice);
	}
	
	private void DoUpdateOrderCard() {
		customerGui.DoUpdateOrderCard();
	}
	
	private void DoHideOrderCard() {
		customerGui.DoHideOrderCard();
	}
	
	private void DoGoToCashier() {
		customerGui.DoGoToCashier();
	}
	
	private void DoLeaveRestaurant() {
		customerGui.DoExitRestaurant();
		//System.out.println("called doleave..");
		myBill=null; // wow, tricky little bug
		//myPerson.msgLeftLocation();
		isActive=false;
	}
	
	// Accessors, etc.

	@Override
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

	@Override
	public String toString() {
		return "customer " + getName();
	}

	public void setGui(DCustomerGui g) {
		customerGui = g;
	}

	public DCustomerGui getGui() {
		return customerGui;
	}



}

