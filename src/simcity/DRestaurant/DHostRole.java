package simcity.DRestaurant;

import simcity.PersonAgent;
import simcity.DRestaurant.DProducerConsumerMonitor;
import simcity.DRestaurant.DHostRole.MyCustomer.CustState;
import simcity.DRestaurant.DHostRole.MyWaiter.MyWaiterState;
import simcity.DRestaurant.gui.DHostGui;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LHostRole.myWaiter;
import agent.Agent;
import agent.Role;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DHost;
import simcity.interfaces.DWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class DHostRole extends Role implements DHost {
	
	private DProducerConsumerMonitor theMonitor;
	DCook cook;
	DCashier cashier;
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<MyCustomer>());

	DCustomerRole custLeavingWaitlist;
	DCustomerRole sendFullMsgTo;
	
	DCook myCook = null;
	
	//list of waiters
	public List<MyWaiter> waiters =  Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private int customersInRST;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore customerAtFront = new Semaphore(0,true);
	public DHostGui hostGui = null;
	public boolean isClosed = false;
	boolean KitchenReadyForOpen;

	public DHostRole() {
		//super(p);

		//this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		KitchenReadyForOpen=false;
		//adding one waiter.. to be changed
		//waiters.add(new WaiterAgent("Joe"));

		customersInRST=0;
		
		custLeavingWaitlist=null;
		sendFullMsgTo=null;
		
		theMonitor = new DProducerConsumerMonitor();
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

	public void msgIAmHere(Role r){
		
		if(r instanceof DWaiter){
			System.out.println(myPerson.getName()+ ": Waiter is here");
			waiters.add(new MyWaiter((DWaiterRole)r));
		}
		else if(r instanceof DCook){
			System.out.println(myPerson.getName()+ ": Cook is here");
			cook = (DCookRole)r;
		}
		else if(r instanceof DCashier ){
			System.out.println(myPerson.getName()+ ": Cashier is here");
			cashier = (DCashierRole) r;
		}
		
		if(!waiters.isEmpty() && cook != null && cashier != null){
			isClosed = false;
		}
		else{
			isClosed = true;
		}
		
		stateChanged();
	}
	
	public void msgKitchenIsReady() {
		System.out.println("received msg from cook that kitchen is fully stocked!");
		KitchenReadyForOpen=true;
		stateChanged();
	}
	public void msgIWantFood(DCustomerRole cust) { //telling agent i want food (once seated)
		if(customersInRST<NTABLES) {
			System.out.println("adding "+cust+" to host customer list");
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
		for(MyCustomer c: waitingCustomers) {
			if(c.c==cust) {
				custLeavingWaitlist=cust;
				stateChanged();
			}
		}
	}

	
	public void msgHereToTakeMyCustomer(DWaiter w) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.atFront;
				stateChanged();
			}
		}
		
	}

	public void msgHereToGetSeated(DCustomer c) {
		System.out.println("received here to get seated from customer");
		customerAtFront.release();
		stateChanged();
	}
	public void msgTableIsClear(int t, DWaiterRole wa)
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
	
	public void msgGoOnBreakPlease(DWaiterRole w) {
		for(MyWaiter waiter: waiters) {
			if(waiter.w==w) {
				waiter.state=MyWaiterState.requestedBreak;
				stateChanged();
			}
		}
	}
	
	public void msgBackToWork(DWaiterRole w) {
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
				//System.out.println("checking waiter "+ waiter.getName());
				if(waiter.state==MyWaiterState.working)
					if (waiter.numCustomers<minCustomers)
						{ w=waiter; minCustomers=waiter.numCustomers; }
			}
//			System.err.println("chose waiter "+ w);
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
//		System.err.println(customerAtFront.availablePermits());
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
		//waitingCustomers.remove(cust);
		cust.w=w;
		w.w.msgHereIsAWaitingCustomer(cust.c, t.getTableNum());
		cust.state=CustState.assignedWaiter;
		
	}
	
//	private void AssignToTable(MyCustomer cust, MyWaiter w, Table t)
//	{
//		t.occupiedBy=(CustomerAgent) cust.c;
//		waitingCustomers.remove(cust);
//		w.w.msgSitAtTable(t.getTableNum(), cust.c);
//		Do(((CustomerAgent) (cust.c)).getName() +" assigned waiter: " + w.w.getName());
//		w.numCustomers++;
//		System.out.println(name+": sent sitAtTable msg");
//		customersInRST++;
//		
//	}
//	
	private void AnswerWaiterBreakRequest(MyWaiter w) {
		int waitersOnDuty=0;
		for(MyWaiter waiter: waiters) {
			if(waiter.state==MyWaiterState.working) {
				waitersOnDuty++;
			}
		}
		//System.out.println("num waiters :: " + waitersOnDuty );
		if(waitersOnDuty>=1) {
			Do("yes "+ w.w.getName()+", you can take a break!");
			w.w.msgBreakReply(true);
			w.state=MyWaiterState.onBreak;
			//w.requestedBreak=false;
		}
		else {
			Do("no "+ w.w.getName()+", you can't take a break!");
			w.w.msgBreakReply(false);
			w.state=MyWaiterState.working;
			//w.requestedBreak=false;
		}
	}

	//utilities

	public void setGui(DHostGui gui) {
		hostGui = gui;
	}

	public DHostGui getGui() {
		return hostGui;
	}
	
	public void msgAddWaiter(DWaiter w) {
		//waiters.add(w);
		waiters.add(new MyWaiter(w));
		System.out.println("waiter "+ w +" added to host list");
		if(w instanceof DWaiterSharedRole) {
			((DWaiterSharedRole) w).setMonitor(theMonitor);
		}
		
	}
	
	public void addCook(DCook c) {
		myCook = c;
		c.setMonitor(theMonitor);
	}
	
	static public class MyWaiter {
		
		DWaiter w;
		enum MyWaiterState {working, onBreak, atFront, requestedBreak};
		MyWaiterState state;
		int numCustomers;
		public MyWaiter(DWaiter w2) {
			// TODO Auto-generated constructor stub
			w=w2;
			state=MyWaiterState.working;
			numCustomers=0;
		}
	}
	
	static public class MyCustomer {
		DCustomer c;
		enum CustState {justArrived, waiting, assignedWaiter, atFront, comingToFront};
		CustState state;
		MyWaiter w;
		
		MyCustomer(DCustomer cu) {
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

	@Override
	public void msgIWantFood(DCustomer dCustomerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIDontWantToWait(DCustomer dCustomerRole) {
		// TODO Auto-generated method stub
		
	}
}

