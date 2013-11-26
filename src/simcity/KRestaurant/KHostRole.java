package simcity.KRestaurant;

import agent.Agent;
import agent.Role;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.KCustomer;
import simcity.interfaces.Host;
import simcity.interfaces.MarketCustomer;
import simcity.test.mock.LoggedEvent;
import simcity.KRestaurant.gui.KRestaurantGui;
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
	
	KRestaurantGui gui;

	
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
	
	public void msgHereIsMoney(double money){
		restaurantMoney += money;
	}
	
	public void msgTimeUpdate(int hour){
		this.hour = hour;
		if(hour == 9) {
			restaurantMoney = 50000.0;
		}
	}
	public void msgIAmHere(Role r, String type){
		
		if(type.equals("waiter")){
			Do("Waiter is here");
			waiters.add(new MyWaiter((KWaiterRole) r));
		}
		else if(type.equals("cook")){
			Do("Cook is here");
			myCook = (KCookRole)r;
		}
		else if(type.equals("cashier")){
			Do("Cashier is here");
			myCashier = (KCashierRole) r;
		}
		
		if(!waiters.isEmpty() && myCook != null && myCashier != null){
			isClosed = false;
		}
		else{
			isClosed = true;
		}
		
		stateChanged();
	}
	
	
	public void msgIWantFood(KCustomerRole cust) {
		System.out.println("got msg from customer, he's hungry");
		customers.add(new MyCustomer(cust, state.waiting));
		stateChanged();
	}

	public void msgDecideToWait(KCustomerRole cust, boolean decision) {
		Do("customer is telling me if he'll wait");
		if(!decision) {
			synchronized(customers) {
				for(MyCustomer c : customers) {
					if(c.c == cust) {
						c.s = state.done;
						waitingCustomers.set(c.waitingPos, 0);
						Do("cust said he's leaving");
					}
				}
			}
		}
		else{
			Do("cust said he's staying!");
		}
	}
	public void msgLeavingTable(KCustomer cust) {
		synchronized(tables) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
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
		System.out.println("table is free");
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
		Do("waiter wants to go on break");
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
		
		if(hour == 21 && !isClosed){
			closeRestaurant();
			return true;
		}
		
		if(hour == 20 || isClosed){
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
		Do("Closing restaurant. It is "+hour);
		synchronized(waiters){
			for(MyWaiter w: waiters){
				restaurantMoney -= 50;
				w.w.msgGoHome(50);
			}
		}
		myCashier.msgGoHome(50);
		myCook.msgGoHome(50);
		
		waiters.clear();
		myCashier = null;
		myCook = null;
		isClosed = true;
	}
	
	private void restaurantClosed() {
		Do("Telling market is closed");
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
		if (workingWaiters < 1) {
			w.state = WaiterState.working;
			gui.uncheckCB();
		}
		else {
			w.state = WaiterState.onBreak;
			gui.setBackToWorkCB();
		}
		if (w.state == WaiterState.onBreak) {
			w.w.msgYesBreak(true);
			Do("told " + w.w.getName() + " he can go on break");
		}
		else if (w.state == WaiterState.working) {
			w.w.msgYesBreak(false);
			Do("told waiter can't go on break");
		}
	}
		
	private void seatCustomer(MyCustomer customer, Table table) {
		System.out.println(this.name+" sending msg to waiter to seat cust");
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
	public void addWaiter(KWaiterRole w) {
		waiters.add(new MyWaiter(w));
		if(w instanceof KWaiterSharedDataRole) {
			((KWaiterSharedDataRole) w).setMonitor(theMonitor);
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

