package simcity.KRestaurant;

import agent.Agent;
import agent.Role;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.KCustomer;
import simcity.interfaces.Host;
import simcity.interfaces.MarketCustomer;
import simcity.test.mock.LoggedEvent;
import simcity.Market.MarketManagerRole.MyCustomer;
import simcity.Market.MarketManagerRole.MyMarketCashier;
import simcity.Market.MarketManagerRole.workerState;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */

public class KHostRole extends Role implements Host{
	
	// monitor for cook and waiters to share
	private ProducerConsumerMonitor theMonitor;
	
	static final int NTABLES = 4;

	private int hour;
	private boolean isClosed;
	private double restaurantMoney;
	
	private KCookRole myCook;
	private KCashierRole myCashier;
	public List<MyCustomer> customers= Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public List<Integer> waitingCustomers = Collections.synchronizedList(new ArrayList<Integer>());
	public Collection<Table> tables;
	
//	KRestaurantGui gui;

	
	public enum state 
	{waiting, atTable, done};
	
	public enum WaiterState
	{
		working, wantToGoOnBreak, onBreak, backFromBreak
	}
	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public int hostStatus = 0; // 0 is host is free, 1 is host is busy
	
	public KWaiterGui hostGui = null;
	

	public KHostRole() {
		//super(p);
		//this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized(tables) {
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
		}
		synchronized(waitingCustomers) {
			for(int i = 0; i<10; i++) {
				waitingCustomers.add(0);
			}
		}
		
		theMonitor = new ProducerConsumerMonitor();
		startHour = 9;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return customers;
	}

	public Collection getTables() {
		return tables;
	}
	
	public void msgTimeUpdate(int hour){
		this.hour = hour;
		if(hour == 9) {
			restaurantMoney = 50000.0;
		}
		
		if(hour == myPerson.directory.get(myPerson.jobLocation).openHour) {
			isClosed = false;
		}
		stateChanged();
	}
	
	public void msgHereIsMoney(double money){
		restaurantMoney += money;
	}
	
	public void msgIAmHere(Role r, String type){
		
		if(type.equals("waiter")){
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "Waiter is here");
			System.out.println(myPerson.getName() + ": " + "Waiter is here");
			waiters.add(new MyWaiter((KWaiterRole) r));
		}
		else if(type.equals("cook")){
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "Cook is here");
			System.out.println(myPerson.getName() + ": " +"Cook is here");
			myCook = (KCookRole)r;
		}
		else if(type.equals("cashier")){
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "Cashier is here");
			System.out.println(myPerson.getName() + ": " +"Cashier is here");
			myCashier = (KCashierRole) r;

		}
		
//		if(!waiters.isEmpty() && myCook != null && myCashier != null){
//			isClosed = false;
//		}
//		else{
//			isClosed = true;
//		}
		
		stateChanged();
	}
	
	
	public void msgIWantFood(KCustomerRole cust) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "got msg from customer, he's hungry");
		System.out.println(myPerson.getName() + ": " +"got msg from customer, he's hungry");
		customers.add(new MyCustomer(cust, state.waiting));
		stateChanged();
	}

	public void msgDecideToWait(KCustomerRole cust, boolean decision) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "customer is telling me if he'll wait");
		System.out.println(myPerson.getName() + ": " +"customer is telling me if he'll wait");
		if(!decision) {
			synchronized(customers) {
				for(MyCustomer c : customers) {
					if(c.c == cust) {
						c.s = state.done;
						waitingCustomers.set(c.waitingPos, 0);
						AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "customer said he's leaving");
						System.out.println(myPerson.getName() + ": " +"cust said he's leaving");
					}
				}
			}
		}
		else{
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "customer said he's staying");
			System.out.println(myPerson.getName() + ": " +"cust said he's staying!");
		}
	}
	public void msgLeavingTable(KCustomer cust) {
		synchronized(tables) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", cust + " leaving " + table);
				print(cust + " leaving " + table);
				table.setUnoccupied();
				synchronized(customers) {
					for(MyCustomer c : customers) {
						if(c.c == cust) {
							c.s = state.done;
						}
					}
				}
			}
		}
		}
		stateChanged();
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "table is free");
		System.out.println(myPerson.getName() + ": " +"table is free");
	}

	
	public void msgBackFromBreak(KWaiterRole w) {
		MyWaiter waiter = null;
		synchronized(waiters) {
			for (MyWaiter currentWaiter : waiters) {
				if( currentWaiter.w == w) {
					waiter = currentWaiter;
					waiter.state = WaiterState.working;
				}
			}
		}
		stateChanged();
	}
	
	public void msgWouldLikeToGoOnBreak(KWaiterRole w) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "waiter wants to go on break");
		System.out.println(myPerson.getName() + ": " +"waiter wants to go on break");
		synchronized(waiters) {
			for (MyWaiter currentWaiter : waiters) {
				if( currentWaiter.w == w) {
					currentWaiter.state = WaiterState.wantToGoOnBreak;
				}
			}
		}
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(hour == myPerson.directory.get(myPerson.jobLocation).closeHour && !isClosed){
			closeRestaurant();
			return true;
		}
		
		if(hour == myPerson.directory.get(myPerson.jobLocation).closeHour-1){
			restaurantClosed();
			return true;
		}
		
		synchronized(waiters) {
			for(MyWaiter w: waiters) {
				if ( w.state == WaiterState.wantToGoOnBreak) {
					tellWaiterYesOrNo(w);
					return true;
				}
			}
		}
		synchronized(tables) {
		for (Table table : tables) {
			if (!table.isOccupied()) {
				synchronized(customers) {
					for(MyCustomer c : customers) {
						if(c.s == state.waiting) {
							seatCustomer(c, table);
							return true;
						}
					}
				}
			}
		}
		}
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == state.waiting && !c.inWaitingArea) {
					sayRestaurantFull(c);
				}
			}
		}
		return false;
	}
	
	// Actions
	private void closeRestaurant(){ //pay employees 50
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "Closing restaurant. It is "+hour);
		System.out.println(myPerson.getName() + ": " +"Closing restaurant. It is "+hour);
		synchronized(waiters){
			for(MyWaiter w: waiters){
				restaurantMoney -= 50;
				w.w.msgGoHome(50);
			}
		}
		myCashier.msgGoHome(50.0);
		myCook.msgGoHome(50.0);
		
		myPerson.money +=50;
		restaurantMoney -= 150;

		waiters.clear();
		myCashier = null;
		myCook = null;
		isClosed = true;
		isActive = false;
		
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
				
	}
	
	private void restaurantClosed() {
		synchronized(customers){
			for(MyCustomer c: customers){
				c.c.msgRestaurantClosed();
			}
			customers.clear();
		}
	}
	
	private void sayRestaurantFull(MyCustomer c) {
		synchronized(waitingCustomers) {
			for(int i = 0; i<10; i++) {
				if(waitingCustomers.get(i) == 0) {
					waitingCustomers.set(i, 1) ;
					c.inWaitingArea = true;
					c.waitingPos = i;
					c.c.msgRestaurantFull(i);
					return;
				}
			}
		}
	}
	
	private void tellWaiterYesOrNo(MyWaiter w) {
		int workingWaiters = 0;
		synchronized(waiters) {
			for (MyWaiter currentWaiter : waiters) {
				if(currentWaiter.state == WaiterState.working) {
					workingWaiters++;
				}
			}
		}
//		if (workingWaiters < 1) {
//			w.state = WaiterState.working;
//			gui.uncheckCB();
//		}
//		else {
//			w.state = WaiterState.onBreak;
//			gui.setBackToWorkCB();
//		}
		if (w.state == WaiterState.onBreak) {
			w.w.msgYesBreak(true);
			System.out.println(myPerson.getName() + ": " +"told " + w.w.getName() + " he can go on break");
		}
		else if (w.state == WaiterState.working) {
			w.w.msgYesBreak(false);
			System.out.println(myPerson.getName() + ": " +"told waiter can't go on break");
		}
	}
		
	private void seatCustomer(MyCustomer customer, Table table) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KHost", "sending msg to waiter to seat cust");
		System.out.println(myPerson.getName() + ": " +"sending msg to waiter to seat cust");
		if(customer.inWaitingArea) {
			customer.inWaitingArea = false;
			synchronized(waitingCustomers) {
				waitingCustomers.set(customer.waitingPos, 0);
			}
		}
		customer.c.msgGoToSeeWaiter();
		MyWaiter waiter = waiters.get(0); 
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if ((w.customers < waiter.customers) && (w.state == WaiterState.working)) {
					waiter = w;
				}	
			}
		}
		waiter.w.msgSeatCustomer(customer.c, table.tableNumber);
		waiter.customers++; 
		table.setOccupant(customer.c);
		customer.s = state.atTable;
	}


	//utilities
	
	public void addWaiter(KWaiterRole kw) {
		waiters.add(new MyWaiter(kw));
		if(kw instanceof KWaiterSharedDataRole) {
			((KWaiterSharedDataRole) kw).setMonitor(theMonitor);
		}
	}

	public void setCook(KCookRole c) {
		myCook = c;
		c.setMonitor(theMonitor);
	}
	private class Table {
		KCustomer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(KCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		KCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	private class MyCustomer {
		KCustomer c;
		state s;
		boolean inWaitingArea;
		int waitingPos;
		
		public MyCustomer( KCustomer cust, state s) {
			c = cust;
			this.s = s;
			inWaitingArea = false;
			waitingPos = -1;
		}
	}
	
	private class MyWaiter {
		KWaiterRole w;
		int customers;
		WaiterState state;
		
		public MyWaiter(KWaiterRole waiter) {
			w = waiter;
			customers = 0;
			state = WaiterState.working;
		}
	}
	
	public void setCashier(KCashierRole c) {
		myCashier = c;
	}
}

