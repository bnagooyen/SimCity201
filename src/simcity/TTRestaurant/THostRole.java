package simcity.TTRestaurant;

import agent.Role;
import simcity.interfaces.Host;
import simcity.interfaces.TCustomer; 
import simcity.interfaces.TWaiter;
import simcity.PersonAgent;
import simcity.TTRestaurant.gui.THostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */

public class THostRole extends Role implements Host {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<TCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<TCustomer>());
	public Collection<Table> tables;
	public List<myWaiters> waiters
	= Collections.synchronizedList(new ArrayList<myWaiters>());


	enum WaiterState 
	{ready, wantsBreak, onBreak};
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private int waiterNum = 0;
	private int waitersOnBreak = 0;
	private int occupiedTable = 0; 

	public THostGui hostGui = null;

	public THostRole(PersonAgent p) {
		super(p);

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getName() {
		return name;
	}



	// Messages

	public void msgIWantFood(TCustomer cust) {
		waitingCustomers.add(cust);
		print("Adding new customer"); 
		stateChanged();
	}

	public void msgCustomerLeft(TCustomer cust) {
		synchronized(tables) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				occupiedTable--; 
				stateChanged();
			}
		}
		}
	}
	
	public void msgTiredOfWaiting(TCustomer cust) {
		print("" + waitingCustomers.size()); 
		if (!waitingCustomers.isEmpty()) {
		int i = 0;
		while (waitingCustomers.get(i) != cust && i < waitingCustomers.size()) {
			i++;
		}
		if (waitingCustomers.get(i) == cust) {
			waitingCustomers.remove(i);
		}
		}
	}
	
	public void msgBreakPlease(TWaiterRole waiter) {
		int index = 0; 
		while (waiters.get(index).w != waiter) {
			index++; 
			}
		waiters.get(index).state = WaiterState.wantsBreak;
		stateChanged(); 
	}
	
	public void msgBreakPlease(TWaiter waiter) {
		int index = 0; 
		while (waiters.get(index).hw != waiter) {
			index++; 
			}
		waiters.get(index).state = WaiterState.wantsBreak;
		stateChanged(); 
		
	}
	
	public void msgOffBreak(TWaiterRole waiter) {
		int index = 0; 
		while (waiters.get(index).w != waiter) {
			index++; 
			}
		waiters.get(index).state = WaiterState.ready;
		waitersOnBreak -= 1; 
		stateChanged(); 
	}
	
	public void msgOffBreak(THeadWaiterRole waiter) {
		int index = 0; 
		while (waiters.get(index).hw != waiter) {
			index++; 
			}
		waiters.get(index).state = WaiterState.ready;
		waitersOnBreak -= 1; 
		stateChanged(); 
	}

	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized(waiters) {
		for (int index = 0; index < waiters.size(); index++) {
			if (waiters.get(index).state == WaiterState.wantsBreak) {
				if (waitersOnBreak < waiters.size() - 1) {
					print("should be giving break"); 
					giveBreak(index);
					return true; 
				}
				else {
					noBreak(index); 
					return true; 
				}
			}
		}
		}
		synchronized(tables) {
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty() && !waiters.isEmpty()) {
					table.setOccupant(waitingCustomers.get(0));
					boolean assigned = false; 
					while (assigned == false) {
						if (waiters.get(waiterNum).state == WaiterState.ready){
							callWaiter(table.tableNumber, waiterNum);
							waiters.get(waiterNum).w.setHost(this);
							assigned = true;
							occupiedTable++; 
						}
						waiterNum++; 
						if (waiterNum == waiters.size()) {
							waiterNum = 0; 
						}
					}
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}

		}
		}
		if (occupiedTable >= 3) {
			if (!waitingCustomers.isEmpty()) {
				AskCustWait();
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void callWaiter(int table, int index) { 
		waiters.get(index).w.msgSeatAtTable(waitingCustomers.get(0), table); //messaging waiter to seat customer;
		print("Telling waiter to seat customer."); 
		waitingCustomers.remove(0);
	}
	
	private void giveBreak(int i) {
		waiters.get(i).w.msgOnBreak(); 
		waiters.get(i).state = WaiterState.onBreak;
		waitersOnBreak++; 
	}
	
	private void noBreak (int i) {
		waiters.get(i).w.msgNoBreak(); 
		waiters.get(i).state = WaiterState.ready; 
	}
	
	private void AskCustWait() {
		synchronized(waitingCustomers) {
		for (int i = 0; i < waitingCustomers.size(); i++) { 
			waitingCustomers.get(i).msgPleaseWait(); 
		}
		}
	}


	// The animation DoXYZ() routines
	
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}

	//utilities
	
	public void setWaiter(TWaiterRole wait) {
		myWaiters waitList = new myWaiters();
		waitList.setWaiter(wait);
		waiters.add(waitList);
		wait.setHomePosition(waiters.size() - 1);
		stateChanged(); 
	}

	public void setGui(THostGui gui) {
		hostGui = gui;
	}

	public THostGui getGui() {
		return hostGui;
	}
	
	class myWaiters {
		TWaiterRole w;
		THeadWaiterRole hw; 
		WaiterState state;
		
		public void setWaiter(TWaiterRole wait) {
			w = wait;
			state = WaiterState.ready; 
		}
		
		public void setHeadWaiter(THeadWaiterRole wait) {
			hw = wait;
			state = WaiterState.ready; 
		}
		
	}

	public class Table {
		TCustomer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(TCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		TCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

}
