package simcity.KRestaurant;

import agent.Agent;
import agent.Role;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KMovingFoodGui;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.KCashier;
import simcity.interfaces.Cook;
import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.KRestaurant.KCustomerRole.AgentEvent;
import simcity.KRestaurant.KHostRole.state;
import simcity.KRestaurant.KMenu;
import simcity.Market.gui.IBGui;

/**
 * Restaurant Waiter Agent
 */

public abstract class KWaiterRole extends Role implements KWaiter{
	static final int NTABLES = 4;
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore takeOrder = new Semaphore(0, true);
	protected Semaphore atCook = new Semaphore(0, true);
	private Semaphore canGetCustomer = new Semaphore(0, true);
	private Semaphore pickUpFood = new Semaphore(0, true);
	
	public KWaiterGui waiterGui = null;
	public KHostRole host = null;
	public KCookRole cook = null;
	public KCashier cashier = null;
	public KMenu menu = null;
	private SimCityGui gui;

	private Timer timer;
    public EventLog log;

    public boolean arrived;
	int homepos;
	
	public WaiterState mystate;
	
	public enum WaiterState 
	{
		working, wantToGoOnBreak, askingToGoOnBreak, canGoOnBreak, onBreak, backFromBreak, leave, unavailable
	}
	public enum customerstate 
	{ waiting, seated, readyToOrder, askedToOrder, ordered, needToOrderAgain, waitingForFood,
		orderIsReady, eating, leaving, gone , checkReady, givenCheck, cantPay}
	
	public KWaiterRole(SimCityGui gui) {
		super();
		this.gui = gui;
		
		mystate = WaiterState.working;
		menu = new KMenu();
		log = new EventLog();
		arrived = true;
		startHour = 11;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	// Messages
	public void msgGoHome(double paycheck) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "told to go home");
		System.out.println(myPerson.getName() + ": " +"told to go home");
		myPerson.money += paycheck;
		mystate = WaiterState.leave;
		stateChanged();
	}
	
	public void msgSeatCustomer(KCustomer cust, int table) {
		MyCustomer c = null;
		for(MyCustomer mc : customers) {
			if (mc.c == cust) {
				c = mc;
			}
		}
		if(c == null) {
			customers.add(new MyCustomer(cust, table, customerstate.waiting));
		}
		else {
			c.s = customerstate.waiting;
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "got msg from host to seat customer");
		System.out.println(myPerson.getName() + ": " +": got msg from host to seat customer");
		LoggedEvent e = new LoggedEvent("got msg to seat customer");
		log.add(e);
		stateChanged();
	}

	public void msgCanGetCustomer() {
		canGetCustomer.release();
	}
	
	public void msgLeavingTable(KCustomer cust) {
		for (MyCustomer c : customers) {
			if (c.c == cust) {
				c.s = customerstate.leaving;
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "customer said he's leaving");
		System.out.println(myPerson.getName() + ": " +": customer said he's leaving");
		stateChanged();
	}

	public void msgAtTable() {
		atTable.release();
	}
	
	public void msgAtCook() {
		atCook.release();
	}
	
	public void msgGetFood() {
		pickUpFood.release();
	}

	public void msgReadytoOrder(KCustomerRole cust) {
		for(MyCustomer c : customers) {
			if (c.c == cust) {
				c.s = customerstate.readyToOrder;
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "customer says he's ready to order");
		System.out.println(myPerson.getName() + ": " + ": customer says he's ready to order");
		stateChanged();
	}
	
	public void msgOutOf(int table, String choice) {
		MyCustomer c = null;
		for(MyCustomer cust : customers) {
			if (cust.table == table) {
				c = cust;
			}
		}
		c.s = customerstate.needToOrderAgain;
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","cook said we're out of " + choice);
		System.out.println(myPerson.getName() + ": " +"cook said we're out of " + choice);
		stateChanged();
	}
	
	public void msgHereIsChoice(KCustomer cust, String choice) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","customer is ready to order");
		LoggedEvent e = new LoggedEvent("customer is ready to order");
		log.add(e);
		for(MyCustomer c : customers) {
			if(c.c == cust) {
				c.s = customerstate.ordered;
				c.choice = choice;
			}
		}
		takeOrder.release();
		stateChanged();
	}
	
	public void msgOrderIsReady(String choice, int table, KMovingFoodGui g, int place) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","cook said order is ready");
		System.out.println(myPerson.getName() + ": " +": cook said order is ready");
		for(MyCustomer c : customers) {
			if(c.table == table && c.s == customerstate.waitingForFood) {
				c.s = customerstate.orderIsReady;
				c.f = g;
				c.foodPickup = place;
			}
		}
		stateChanged();
	}

	public void msgHereIsCheck(KCustomer c, double price) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","got check from cashier");
		System.out.println(myPerson.getName() + ": " +"got check from cashier");
		for ( MyCustomer cust: customers) {
			if ( cust.c == c) {
				cust.check = price;
				cust.s =customerstate.checkReady;
			}
		}
		stateChanged();
	}
	public void msgYesBreak(boolean decision) {
		if( decision) {
			mystate = WaiterState.onBreak;
		}
		else {
			mystate = WaiterState.working;
		}
		stateChanged();
	}
	
	public void msgBackToWork() {
		mystate = WaiterState.backFromBreak;
		stateChanged();
	}
	
	public void msgTooExpensiveLeaving(KCustomerRole customer) {
		for ( MyCustomer cust: customers) {
			if ( cust.c == customer) {
				cust.s =customerstate.cantPay;
				cust.choice = "TooExpensive";
				AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","Customer is leaving because food's too expensive");
				System.out.println(myPerson.getName() + ": " +"Customer is leaving cuz food's too expensive");
			}
		}
		takeOrder.release();
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(arrived) {
			tellHost();
			return true;
		}
		if ( mystate == WaiterState.wantToGoOnBreak ) {
			askToGoOnBreak();
			return true;
		}
		if (mystate == WaiterState.backFromBreak) {
			tellHostICanWorkAgain();
			return true;
		}
		try{
			for(MyCustomer c: customers) {
				if(c.s == customerstate.orderIsReady) {
					serveFood(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for( MyCustomer c : customers) {
				if (c.s == customerstate.waiting) {
					seatCustomer(c, c.table);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c : customers) {
				if (c.s == customerstate.needToOrderAgain) {
					giveNewMenu(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c : customers) {
				if(c.s == customerstate.readyToOrder) {
					takeOrder(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c: customers) {
				if(c.s == customerstate.leaving) {
					cleanTable(c, c.table);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c: customers) {
				if(c.s == customerstate.ordered) {
					giveCookOrder(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c : customers) {
				if ( c.s == customerstate.checkReady) {
					giveCheck(c);
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
			for(MyCustomer c : customers) {
				if ( c.s == customerstate.cantPay) {
					tellCustomerToLeave(c);
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		if ( mystate == WaiterState.leave ) {
			goHome();
			return true;
		}
		return false;
	}

	// Actions

	private void tellHost() {
		if(waiterGui == null) {
			waiterGui = new KWaiterGui(this, 1);
			gui.myPanels.get("Restaurant 1").panel.addGui(waiterGui);
		}
		waiterGui.setPresent(true);
		waiterGui.DoGoToWork();
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","telling manager I can work");
		System.out.println(myPerson.getName() + ": " +"telling manager I can work");
		arrived = false;
		host.msgIAmHere(this, "waiter");
	}

	private void goHome() {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","going home");
		System.out.println(myPerson.getName() + ": " +"going home");
		isActive = false;
		mystate = WaiterState.unavailable;
		DoGoHome();
		
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
		
		arrived = true;
	}

	private void DoGoHome() {
		waiterGui.DoGoHome();
	}

	private void askToGoOnBreak() {
		mystate = WaiterState.askingToGoOnBreak;
		host.msgWouldLikeToGoOnBreak(this);
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","asked to go on break");
		System.out.println(myPerson.getName() + ": " +"asked to go on break");
	}
	
	private void tellHostICanWorkAgain() {
		mystate = WaiterState.working;
		host.msgBackFromBreak(this);
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","told host i'm back from break");
		System.out.println(myPerson.getName() + ": " +"told host I'm back from break");
	}
	
	private void serveFood(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","going to cook to get order");
		System.out.println(myPerson.getName() + ": " +"going to cook to get order");
		DoGetFood(c.foodPickup);
		try {
			pickUpFood.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.f.DoGoToSeat(c.table);
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","got food, going to table");
		System.out.println(myPerson.getName() + ": " +"got food, going to table");
		DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.c.msgHereIsYourFood();
		c.s = customerstate.eating;
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","gave food to customer");
		System.out.println(myPerson.getName() + ": " +": gave food to customer");
		waiterGui.DoLeaveCustomer();
		cashier.msgBill(c.c, this, c.choice);
	}
	
	private void DoGetFood(int place){
		waiterGui.DoGetFood(place);
	}
	
	private void giveNewMenu(MyCustomer c) {
		c.s = customerstate.seated;
		DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		KMenu newm = new KMenu();
		for(int i = 0; i< newm.foods.size(); i++) {
			if(newm.foods.get(i).type.equals(c.choice)) {
				newm.foods.remove(i);
			}
		}
		c.c.msgOutOfChoice(newm);
		waiterGui.DoLeaveCustomer();
	}
	
	private void seatCustomer(MyCustomer customer, int table) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "seating customer");
		System.out.println(myPerson.getName() + ": " +"seating customer");
		LoggedEvent ee = new LoggedEvent("seating customer");
		log.add(ee);
		DoPickUpCustomer();
		try {
			canGetCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.c.msgSitAtTable(table, this, menu);
		DoSeatCustomer(customer.c, table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.s = customerstate.seated;
		waiterGui.DoLeaveCustomer();
	}

	private void DoPickUpCustomer() {
		waiterGui.DoGetCustomer();
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "getting customer");
		System.out.println(myPerson.getName() + ": " +"getting customer");
	}
	
	private void DoSeatCustomer(KCustomer customer, int table) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter","Seating " + customer + " at " + table);
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(table); 
	}
	
	private void takeOrder(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "going to table to ask for his order");
		System.out.println(myPerson.getName() + ": " +": going to table to ask for his order");
		;
		DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.c.msgWhatWouldYouLike();
		c.s = customerstate.askedToOrder;
		try {
			takeOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!c.choice.equals("TooExpensive")) {
			waiterGui.DoLeaveCustomer();
		}
	}
	
	private void DoGoToTable(int table) {
		print("Going to " + table);
		waiterGui.DoGoToTable(table);
	}
	
	protected abstract void giveCookOrder(MyCustomer c); //{
//		System.out.println(myPerson.getName() + ": " +": giving cook order of " + c.choice);
//		c.s = customerstate.waitingForFood;
//		cook.msgHereIsAnOrder(this, c.choice, c.table);
//		waiterGui.DoLeaveCustomer();
//	}
	
	private void DoGoToCook() {
		waiterGui.DoGoToCook();
	}
	
	public void giveCheck(MyCustomer c) {
		DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "Gave customer check");
		System.out.println(myPerson.getName() + ": " +"Gave customer check");
		c.c.msgHereIsCheck(c.check);
		c.s = customerstate.givenCheck;
		waiterGui.DoLeaveCustomer();
	}
	
	private void cleanTable(MyCustomer c, int table) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KWaiter", "customer told me he's leaving. telling host");
		System.out.println(myPerson.getName() + ": " +": customer told me he's leaving. telling host");
		c.s = customerstate.gone;
		host.msgLeavingTable(c.c);
		DoGoToTable(table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!c.choice.equals("TooExpensive")) {
			if(c.f != null)
				c.f.setPresent(false);
		}
		waiterGui.DoLeaveCustomer();
	}
	
	public void tellCustomerToLeave(MyCustomer c) {
		c.c.msgOkLeave();
	}

	//utilities

//	public void setInitial( KCookRole c, SimCityGui g, KHostRole h, KCashierRole cashier, int homepos) {
//		cook = c;
//		gui = g;
//		host = h;
//		this.cashier = cashier;
//		this.homepos = homepos;
//	}
	
	public void setCook(KCookRole cook2) {
		cook = cook2;

	}
	public void setHost(KHostRole h) {
		host = h;
	}
	public void setCashier(KCashier cashier) {
		this.cashier = cashier;
	}
	public boolean onBreak() {
		return (mystate == WaiterState.onBreak);
	}
	
	public void setWantBreak() {
		System.out.println(myPerson.getName() + ": " +"i wanna go on break");
		mystate = WaiterState.wantToGoOnBreak;
		stateChanged();
	}
	
	public void setBackToWork() {
		System.out.println(myPerson.getName() + ": " +"going back to work");
		msgBackToWork();
	}
	public void setGui(KWaiterGui gui) {
		waiterGui = gui;
	}

	public KWaiterGui getGui() {
		return waiterGui;
	}

	public class MyCustomer {
		KCustomer c;
		int table;
		String choice;
		public customerstate s;	
		KMovingFoodGui f;
		double check;
		int foodPickup;
		
		MyCustomer(KCustomer c, int table, customerstate s) {
			this.c = c;
			this.table = table;
			this.s = s;
			f = null;
			check = 0;
			choice = null;
			foodPickup = -1;
		}
	}

	
}

