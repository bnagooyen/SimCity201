package simcity.DRestaurant;

import agent.Role;
import agent.Agent;
import simcity.DRestaurant.DHostRole.MyCustomer.CustState;
import simcity.DRestaurant.DHostRole.MyWaiter.MyWaiterState;
import simcity.DRestaurant.DWaiterRole.WaiterState;
import simcity.DRestaurant.gui.DHostGui;
import simcity.restaurant.interfaces.Customer;
import simcity.restaurant.interfaces.Waiter;
import simcity.interfaces.Host;
import simcity.PersonAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class DHostRole extends Role implements Host{
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<MyCustomer>());

	DCustomerRole custLeavingWaitlist;
	DCustomerRole sendFullMsgTo;

	public List<MyWaiter> waiters =  Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;

	private int customersInRST;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore customerAtFront = new Semaphore(0,true);
	public DHostGui hostGui = null;

	boolean KitchenReadyForOpen;

	public DHostRole(PersonAgent p) {
		super(p);

		//name = p.getName();
		
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		synchronized(tables) {
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
		}
		KitchenReadyForOpen=false;

		customersInRST=0;
		
		custLeavingWaitlist=null;
		sendFullMsgTo=null;
	}
	
	public String getMaitreDName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgKitchenIsReady() {
		System.out.println("received msg from cook that kitchen is fully stocked!");
		KitchenReadyForOpen=true;
		stateChanged();
	}
	public void msgIWantFood(DCustomerRole cust) { //telling agent i want food (once seated)
		if(customersInRST<NTABLES) {
			Do("adding "+cust+" to host customer list");
			waitingCustomers.add(new MyCustomer(cust));
			
			stateChanged();
		}
		else {
			sendFullMsgTo = cust;
			System.out.println("adding "+cust+" to host customer list and telling them is full");
			waitingCustomers.add(new MyCustomer(cust));
			stateChanged();
		}
	}
	public void msgIDontWantToWait(DCustomerRole cust) {
		synchronized(waitingCustomers) {
		for(MyCustomer c: waitingCustomers) {
			if(c.c==cust) {
				custLeavingWaitlist=cust;
				stateChanged();
			}
		}
		}
	}

	public void msgAddWaiter(DWaiterRole w) {
		
		waiters.add(new MyWaiter(w));
		
		System.out.println("waiter "+ w.getName() +" added to host list");
		stateChanged();
	}
	
	public void msgHereToTakeMyCustomer(Waiter w) {
		synchronized(waiters) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.atFront;
				stateChanged();
			}
		}
		}
	}

	public void msgHereToGetSeated(Customer c) {
		System.out.println("received here to get seated from customer");
		customerAtFront.release();
		stateChanged();
	}
	public void msgTableIsClear(int t, DWaiterRole wa)
	{
		synchronized(waiters) {
		for(MyWaiter waiter: waiters) {
			if (waiter.w==wa) {
				waiter.numCustomers--;
				break;
			}
		}
		}
		synchronized(tables) {
		for(Table table: tables)
		{
			if (table.getTableNum()==t)
			{
				table.setUnoccupied();
				customersInRST--;
				stateChanged();
			}
		}
		}
	}
	
	public void msgGoOnBreakPlease(DWaiterRole w) {
		synchronized(waiters) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.requestedBreak;
				stateChanged();
			}
		}
		}
	}
	
	public void msgBackToWork(DWaiterRole w) {
		synchronized(waiters) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.working;
				stateChanged();
			}
		}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(custLeavingWaitlist!=null) {
			RemoveCustomerFromList();
			return true;
		}
		
		if(sendFullMsgTo!=null) {
			RestaurantIsFull();
			return true;
		}
		synchronized(waitingCustomers) {
		for(MyCustomer cust: waitingCustomers) {
			if(cust.state==CustState.justArrived) {
				TellCustomerToHangout(cust);
				return true;
			}
		}
		}
		synchronized(waitingCustomers) {
		for(MyCustomer cust: waitingCustomers) {
			if(cust.state==CustState.assignedWaiter && cust.w.state==MyWaiterState.atFront) {
				CallCustomerToFront(cust);
				return true;
			}
		}
		}
		if(!waiters.isEmpty() && KitchenReadyForOpen)
		{
		
			MyWaiter w=waiters.get(0); 
			int minCustomers=waiters.get(0).numCustomers; //dummy value for initialization.. theoretically if only 1 waiter will be the one at the top
			synchronized(waiters) {
			for(MyWaiter waiter: waiters)
			{
				if(waiter.state==MyWaiterState.working)
					if (waiter.numCustomers<minCustomers)
						{	
							w=waiter; 
							minCustomers=waiter.numCustomers; 
						}
			}
			}
			synchronized(tables) {
			for (Table table : tables) {
				for(MyCustomer cust: waitingCustomers) {
					if (!(table.isOccupied()) && cust.state==CustState.waiting) {
						print("Assigning waiter to cust");
						TellWaiterToSeat(cust, w, table);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
			}
			synchronized(waiters) {
			for(MyWaiter waiter: waiters) {
				if(waiter.state==MyWaiterState.requestedBreak) {
					AnswerWaiterBreakRequest(waiter);
					return true;
				}
			}
			}	
			
		}

		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void TellCustomerToHangout(MyCustomer cu) {
		customersInRST++;
		cu.c.msgGoToHangout();
		cu.state=CustState.waiting;
	}
	
	private void CallCustomerToFront(MyCustomer cu) {
		cu.c.msgYourTableIsReady();

		try {
			customerAtFront.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cu.w.w.msgYourCustomerHasArrived();
		cu.w.state=MyWaiterState.working;
		waitingCustomers.remove(cu);
		
	}
	
	private void RestaurantIsFull() {
		sendFullMsgTo.msgNoRoomForYou();
		sendFullMsgTo=null;
	}

	private void RemoveCustomerFromList() {
		
		System.out.println("removed "+ custLeavingWaitlist.getName());
		waitingCustomers.remove(custLeavingWaitlist);
		custLeavingWaitlist=null;
		
	}
	private void TellWaiterToSeat(MyCustomer cust, MyWaiter w, Table t) {
		t.occupiedBy=(DCustomerRole) cust.c;
		w.numCustomers++;
		cust.w=w;
		w.w.msgHereIsAWaitingCustomer(cust.c, t.getTableNum());
		cust.state=CustState.assignedWaiter;
		
	}
	

	private void AnswerWaiterBreakRequest(MyWaiter w) {
		int waitersOnDuty=0;
		synchronized(waiters) {
		for(MyWaiter waiter: waiters) {
			if(waiter.state==MyWaiterState.working) {
				waitersOnDuty++;
			}
		}
		}
		if(waitersOnDuty>=1) {
			Do("yes "+ w.w.getName()+", you can take a break!");
			w.w.msgBreakReply(true);
			w.state=MyWaiterState.onBreak;
		}
		else {
			Do("no "+ w.w.getName()+", you can't take a break!");
			w.w.msgBreakReply(false);
			w.state=MyWaiterState.working;
		}
	}

	//utilities

	public void setWaiter(DWaiterRole r){
		waiters.add(new MyWaiter(r));
	}
	
	public void setGui(DHostGui gui) {
		hostGui = gui;
	}

	public DHostGui getGui() {
		return hostGui;
	}
	
	static public class MyWaiter {
		
		DWaiterRole w;
		enum MyWaiterState {working, onBreak, atFront, requestedBreak};
		MyWaiterState state;
		int numCustomers;
		public MyWaiter(DWaiterRole w) {
			this.w=w;
			state=MyWaiterState.working;
			numCustomers=0;
		}
	}
	
	static public class MyCustomer {
		Customer c;
		enum CustState {justArrived, waiting, assignedWaiter, atFront, comingToFront};
		CustState state;
		MyWaiter w;
		
		MyCustomer(Customer cu) {
			c=cu;
			state=CustState.justArrived;
		}
	}

	public class Table {
		DCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		int getTableNum() { return tableNumber; }

		void setOccupant(DCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		DCustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		@Override
		public String toString() {
			return "table " + tableNumber;
		}
	}
}

