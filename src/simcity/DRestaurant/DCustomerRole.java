package simcity.DRestaurant;
 
import simcity.PersonAgent;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DHost;
import simcity.interfaces.DWaiter;
import simcity.DRestaurant.gui.DCustomerGui;
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
	//parse through string
	public String choice;
	private int hungerLevel = 5;        // determines length of meal
	//private int sittingAt; //table handling
	Timer timer = new Timer();
	private DCustomerGui customerGui;
	
	private boolean stayOrLeave;
	
	private DMenu myMenu;
	private double wallet;
//	private double debt;
	
	//semahores
	Semaphore atFront = new Semaphore(0, true);
	
	// agent correspondents
	private DHost host;
	private DWaiter waiter;
	private DCashier cashier;
	
	//table
	private int tableNum;
	
	//check
	DCheck myBill = null;
	
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Ordered, Eating, Paying, DoneEating, Leaving, Gone, GoingToCashier, Paid, WaitingForCheck, WaitingInHangout, AtFront};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, iKnowWhatIWant, waiterIsHere, waiterIsHereForReorder, foodIsHere, doneEating, doneLeaving, arrivedAtCashier, gotReceipt, restaurantIsFull, goToHangout, tableReady};
	AgentEvent event = AgentEvent.none; //initialized to none

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public DCustomerRole(){
		//super(p);
		
		//parsing string
		//Input: Name, MoneyVal, Order , Stay/Leave
		//String[] inputs = name.split(", "); 
//		String[] inputs = name.split(", "); 
		
		
//		this.myText = name;
//		wallet= Double.parseDouble(inputs[1].trim());
//		this.name=inputs[0].trim();
//		choice=inputs[2].trim();
//		//System.out.println(inputs[3]);
//		if(inputs[3].trim().equals("Stay")) {
//			stayOrLeave=true;
//			//System.out.println("staying.");
//		}
//		if(inputs[3].trim().equals("Leave")) {
//			stayOrLeave=false;
//		}
		//}
		//System.out.printf("Name: "+ name + " Money: "+ "%d ", wallet);
		//System.out.print(wallet);

//		state=AgentState.DoingNothing;
//		debt=0;
		
	}
	
	public double getWallet() {
		return wallet;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(DHost host2) {
		this.host = host2;
	}

	public void setCashier(DCashier cook) {
		cashier= cook;
	}
	public String getCustomerName() {
		return name;
	}
	// Messages

	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry; //event is the state change
		stateChanged();
	}
	
	@Override
	public void msgNoRoomForYou() {
		System.out.println("no room for me!");
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
			System.out.println("customer received change of "+ num);
			wallet+=num;
			//System.out.print("wallet now = "+ wallet);
		}
		else {
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
		Do("Going to restaurant");
		state = AgentState.WaitingInRestaurant;
		host.msgIWantFood(this);//send our instance, so he can respond to us
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
		Do("Being seated. Going to table");
		state = AgentState.BeingSeated;
		DoGoToSeat();
		//customerGui.DoGoToSeat(sittingAt, 1);//hack; only one table
		//do is being called by waitergui
	}
	
	private void DecideWhatIWantToEat(final DMenu myMenu) {
		//Do("Deciding what to order...");
		state = AgentState.Seated;
		/*timer.schedule(new TimerTask() {
			public void run() {
				Random generator= new Random();
				choice= myMenu.myOptions[generator.nextInt(myMenu.getSize())];
				Do("I'm ordering " +choice);
				event = AgentEvent.iKnowWhatIWant;
				//isHungry = false;
				stateChanged();
			}
		},
		3000); */
		timer.schedule(new TimerTask() {
			@Override
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
		//waiter.msgHereIsMyChoice(this, choice);
		//DoDisplayOrderCard();
	//}
	
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
		/* if requirement allows user to eat anyway...
		if(ch.equals("None") && !stayOrLeave) {
			waiter.msgCantAffordNotStaying(this);
			DoLeaveRestaurant();
			return;
		}
		else choice=ch; */
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
				print("Done eating, cookie=" + cookie);
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
		Do("Leaving.");
		//state = AgentState.Leaving;
		state = AgentState.GoingToCashier;
		waiter.msgDoneEatingAndLeaving(this);
		//DoLeaveRestaurant();
		DoGoToCashier();
	}
	
//	private void PayMyBill() {
//		DecimalFormat df = new DecimalFormat("###.##");
//		//normative scenario.. pays amount due;
//		System.err.println("PAYING BILL...");
//		if(debt>0) {
//			;//System.out.print("adding my debt of " + debt+ " to bill.. paying "+ (wallet+debt));
//		}
//		double myPay=  Double.valueOf(df.format(wallet+debt));
//		myBill.setCustomerPaid(myPay);
//		debt=0; // paid it, no more
//		cashier.msgHereIsAPayment(this, myBill.getTablenum(), myBill.getCustomerPaid());
//		if((myBill.getBillAmnt()+debt)>wallet) {
//			System.out.print("bill: "+ myBill.getBillAmnt() + " Wallet: "+ wallet + " Debt: "+ debt);
//
//			double myDebt = Double.valueOf(df.format(myBill.getBillAmnt()+debt-wallet));
//			
//			System.out.print("added "+ myDebt + " debt");
//			debt+=myDebt;
//		}
//		wallet=0;
//		state=AgentState.Paid;
//	}
	
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
		wallet+=generator.nextInt(25);
	}
	
	private void clearState() {
		state = AgentState.DoingNothing;
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

