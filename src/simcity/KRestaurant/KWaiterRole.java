package simcity.KRestaurant;

import agent.Agent;
import agent.Role;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KMovingFoodGui;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCook;
import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.KRestaurant.KCustomerRole.AgentEvent;
import simcity.KRestaurant.KHostRole.state;
import simcity.KRestaurant.KMenu;
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
	private KRestaurantGui gui;

	private Timer timer;
    public EventLog log;

    boolean arrived;
	int homepos;
	
	public WaiterState mystate;
	
	public enum WaiterState 
	{
		working, wantToGoOnBreak, askingToGoOnBreak, canGoOnBreak, onBreak, backFromBreak, leave, unavailable
	}
	public enum customerstate 
	{ waiting, seated, readyToOrder, askedToOrder, ordered, needToOrderAgain, waitingForFood,
		orderIsReady, eating, leaving, gone , checkReady, givenCheck, cantPay}
	
	public KWaiterRole() {
		super();
		
		mystate = WaiterState.working;
		menu = new KMenu();
		log = new EventLog();


	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	// Messages
	public void msgGoHome(double paycheck) {
		Do("told to go home");
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
		Do(": got msg from host to seat customer");
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
		Do(": customer said he's leaving");
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
		Do( ": customer says he's ready to order");
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
		Do("cook said we're out of " + choice);
		stateChanged();
	}
	
	public void msgHereIsChoice(KCustomer cust, String choice) {
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
		Do(": cook said order is ready");
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
		Do("got check from cashier");
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
				Do("Customer is leaving cuz food's too expensive");
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
					Do("in scheduler and cantPay");
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
		Do("telling manager I can work");
		arrived = false;
		host.msgIAmHere(this, "waiter");
	}

	private void goHome() {
		Do("going home");
		isActive = false;
		mystate = WaiterState.unavailable;
		DoGoHome();
	}

	private void DoGoHome() {
		waiterGui.DoGoHome();
	}

	private void askToGoOnBreak() {
		mystate = WaiterState.askingToGoOnBreak;
		host.msgWouldLikeToGoOnBreak(this);
		Do("asked to go on break");
	}
	
	private void tellHostICanWorkAgain() {
		mystate = WaiterState.working;
		host.msgBackFromBreak(this);
		Do("told host I'm back from break");
	}
	
	private void serveFood(MyCustomer c) {
		Do("going to cook to get order");
//		DoGetFood(c.foodPickup);
//		try {
//			pickUpFood.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		c.f.DoGoToSeat(c.table);
		
		Do("got food, going to table");
		DoGoToTable(c.table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		c.c.msgHereIsYourFood();
		c.s = customerstate.eating;
		Do(": gave food to customer");
//		waiterGui.DoLeaveCustomer();
		cashier.msgBill(c.c, this, c.choice);
	}
	
//	private void DoGetFood(int place){
//		waiterGui.DoGetFood(place);
//	}
	
	private void giveNewMenu(MyCustomer c) {
		c.s = customerstate.seated;
//		DoGoToTable(c.table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		KMenu newm = new KMenu();
		for(int i = 0; i< newm.foods.size(); i++) {
			if(newm.foods.get(i).type.equals(c.choice)) {
				newm.foods.remove(i);
			}
		}
		c.c.msgOutOfChoice(newm);
//		waiterGui.DoLeaveCustomer();
	}
	
	private void seatCustomer(MyCustomer customer, int table) {
		Do("seating customer");
		LoggedEvent ee = new LoggedEvent("seating customer");
		log.add(ee);
//		DoPickUpCustomer();
//		try {
//			canGetCustomer.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		customer.c.msgSitAtTable(table, this, menu);
//		DoSeatCustomer(customer.c, table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		customer.s = customerstate.seated;
//		waiterGui.DoLeaveCustomer();
	}

	private void DoPickUpCustomer() {
		waiterGui.DoGetCustomer();
		Do("getting customer");
	}
	
	private void DoSeatCustomer(KCustomer customer, int table) {
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(table); 
	}
	
	private void takeOrder(MyCustomer c) {
		Do(": going to table to ask for his order");
		;
//		DoGoToTable(c.table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		c.c.msgWhatWouldYouLike();
		c.s = customerstate.askedToOrder;
//		try {
//			takeOrder.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		if(!c.choice.equals("TooExpensive")) {
			waiterGui.DoLeaveCustomer();
		}
	}
	
	private void DoGoToTable(int table) {
		print("Going to " + table);
		waiterGui.DoGoToTable(table);
	}
	
	protected abstract void giveCookOrder(MyCustomer c); //{
//		Do(": giving cook order of " + c.choice);
//		c.s = customerstate.waitingForFood;
//		cook.msgHereIsAnOrder(this, c.choice, c.table);
//		waiterGui.DoLeaveCustomer();
	//}
	
	private void DoGoToCook() {
		waiterGui.DoGoToCook();
	}
	
	public void giveCheck(MyCustomer c) {
		DoGoToTable(c.table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		Do("Gave customer check");
		c.c.msgHereIsCheck(c.check);
		c.s = customerstate.givenCheck;
//		waiterGui.DoLeaveCustomer();
	}
	
	private void cleanTable(MyCustomer c, int table) {
		Do(": customer told me he's leaving. telling host");
		c.s = customerstate.gone;
		host.msgLeavingTable(c.c);
//		DoGoToTable(table);
//		try {
//			atTable.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		if(!c.choice.equals("TooExpensive")) {
			c.f.setPresent(false);
		}
//		waiterGui.DoLeaveCustomer();
	}
	
	public void tellCustomerToLeave(MyCustomer c) {
		c.c.msgOkLeave();
	}

	//utilities

	public void setInitial( KCookRole c, KRestaurantGui g, KHostRole h, KCashierRole cashier, int homepos) {
		cook = c;
		gui = g;
		host = h;
		this.cashier = cashier;
		this.homepos = homepos;
	}
	
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
		Do("i wanna go on break");
		mystate = WaiterState.wantToGoOnBreak;
		stateChanged();
	}
	
	public void setBackToWork() {
		Do("going back to work");
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

