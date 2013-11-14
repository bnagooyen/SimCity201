package restaurant;

import agent.Role;
import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.HostRole.MyCustomer.CustState;
import restaurant.HostRole.MyWaiter.MyWaiterState;
import restaurant.WaiterRole.WaiterState;
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
public class HostRole extends Role {
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<MyCustomer>());

	CustomerRole custLeavingWaitlist;
	CustomerRole sendFullMsgTo;

	public List<MyWaiter> waiters =  Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;

	private int customersInRST;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore customerAtFront = new Semaphore(0,true);
	public HostGui hostGui = null;

	boolean KitchenReadyForOpen;

	public HostRole(PersonAgent p) {
		super(p);

		name = p.getName();
		
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
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
	public void msgIWantFood(CustomerRole cust) { //telling agent i want food (once seated)
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
	public void msgIDontWantToWait(CustomerRole cust) {
		for(MyCustomer c: waitingCustomers) {
			if(c.c==cust) {
				custLeavingWaitlist=cust;
				stateChanged();
			}
		}
	}

	public void msgAddWaiter(WaiterRole w) {
		
		waiters.add(new MyWaiter(w));
		
		System.out.println("waiter "+ w.getName() +" added to host list");
		stateChanged();
	}
	
	public void msgHereToTakeMyCustomer(Waiter w) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.atFront;
				stateChanged();
			}
		}
		
	}

	public void msgHereToGetSeated(Customer c) {
		System.out.println("received here to get seated from customer");
		customerAtFront.release();
		stateChanged();
	}
	public void msgTableIsClear(int t, WaiterRole wa)
	{
		for(MyWaiter waiter: waiters) {
			if (waiter.w==wa) {
				waiter.numCustomers--;
				break;
			}
		}
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
	
	public void msgGoOnBreakPlease(WaiterRole w) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.requestedBreak;
				stateChanged();
			}
		}
	}
	
	public void msgBackToWork(WaiterRole w) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.working;
				stateChanged();
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
		
		for(MyCustomer cust: waitingCustomers) {
			if(cust.state==CustState.justArrived) {
				TellCustomerToHangout(cust);
				return true;
			}
		}
		
		for(MyCustomer cust: waitingCustomers) {
			if(cust.state==CustState.assignedWaiter && cust.w.state==MyWaiterState.atFront) {
				CallCustomerToFront(cust);
				return true;
			}
		}
		
		if(!waiters.isEmpty() && KitchenReadyForOpen)
		{
		
			MyWaiter w=waiters.get(0); int minCustomers=waiters.get(0).numCustomers; //dummy value for initialization.. theoretically if only 1 waiter will be the one at the top
			for(MyWaiter waiter: waiters)
			{
				if(waiter.state==MyWaiterState.working)
					if (waiter.numCustomers<minCustomers)
						{ w=waiter; minCustomers=waiter.numCustomers; }
			}

			for (Table table : tables) {
				for(MyCustomer cust: waitingCustomers) {
					if (!(table.isOccupied()) && cust.state==CustState.waiting) {
						TellWaiterToSeat(cust, w, table);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
			
			for(MyWaiter waiter: waiters) {
				if(waiter.state==MyWaiterState.requestedBreak) {
					AnswerWaiterBreakRequest(waiter);
					return true;
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
		t.occupiedBy=(CustomerRole) cust.c;
		w.numCustomers++;
		cust.w=w;
		w.w.msgHereIsAWaitingCustomer(cust.c, t.getTableNum());
		cust.state=CustState.assignedWaiter;
		
	}
	

	private void AnswerWaiterBreakRequest(MyWaiter w) {
		int waitersOnDuty=0;
		for(MyWaiter waiter: waiters) {
			if(waiter.state==MyWaiterState.working) {
				waitersOnDuty++;
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

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	static public class MyWaiter {
		
		WaiterRole w;
		enum MyWaiterState {working, onBreak, atFront, requestedBreak};
		MyWaiterState state;
		int numCustomers;
		public MyWaiter(WaiterRole w2) {
			// TODO Auto-generated constructor stub
			w=w2;
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
		CustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		int getTableNum() { return tableNumber; }

		void setOccupant(CustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerRole getOccupant() {
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

