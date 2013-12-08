package simcity.DRestaurant;

import simcity.DRestaurant.DProducerConsumerMonitor;
import simcity.DRestaurant.DHostRole.MyCustomer.CustState;
import simcity.DRestaurant.DHostRole.MyWaiter.MyWaiterState;
import agent.Agent;
import agent.Role;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
//import simcity.gui.HostGui;
import simcity.interfaces.DCustomer;
import simcity.interfaces.Host;
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
public class DHostRole extends Role implements Host {
	
	private DProducerConsumerMonitor theMonitor;
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<MyCustomer>());
	private double register;
	DCustomerRole custLeavingWaitlist;
	DCustomerRole sendFullMsgTo;

	boolean cookArrived =false;
	boolean cashierArrived = false;
	
	DCookRole myCook = null;
	DCashierRole myCashier=null;
	//list of waiters
	public List<MyWaiter> waiters =  Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public static final double cashierPay = 90;
	public static final double waiterPay = 120;
	public static final double cookPay= 100;
	public static final double hostPay = 130;
	private int customersInRST;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore customerAtFront = new Semaphore(0,true);
	private Semaphore registerRestocked = new Semaphore(0, true);
	//public HostGui hostGui = null;
	boolean waitingForNoCustomers=false;
	boolean KitchenReadyForOpen;
	int hour;
	
	
	public DHostRole() {
		super();

		//this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		register=300;
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

	public void msgKitchenIsReady() {
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Received msg kitchen is fully stocked");
		System.out.println("received msg from cook that kitchen is fully stocked!");
		KitchenReadyForOpen=true;
		stateChanged();
	}
	public void msgRegisterMoney(double amt) {
		register=amt;
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Received register amount");
		System.out.println("host received update in register amount: "+ register);
		registerRestocked.release();
	}
	public void msgTimeUpdate(int hr) {
		hour = hr;
		stateChanged();
	}
	public void msgIAmHere(Role role, String type) {
		if(type.equals("cook")) {
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Cook is here");
			Do("Cook is here");
			myCook=(DCookRole)role;
			cookArrived=true;
		}
		else if(type.equals("cashier")) {
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Cashier is here");
			Do("Cashier is here");
			myCashier=(DCashierRole)role;
			cashierArrived=true;
		}
		else if(type.equals("waiterShared")) {
			waiters.add(new MyWaiter((DWaiterRole)role));
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Waiter is added");
			System.out.println("waiter "+ ((DWaiterRole)role).getName() +" added to host list");
				((DWaiterSharedDataRole) role).setMonitor(theMonitor);
		}
		else if (type.equals("waiterNormal")) {
			waiters.add(new MyWaiter((DWaiterRole)role));
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Waiter is added");
			System.out.println("waiter "+ ((DWaiterRole)role).getName() +" added to host list");
		}
			
		stateChanged();
	}
	
	public void msgIWantFood(DCustomerRole cust) { //telling agent i want food (once seated)
		//System.err.println("received iwantfood");
		if(customersInRST<NTABLES) {
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Adding customer to list");
			System.out.println("adding "+cust+" to host customer list");
			waitingCustomers.add(new MyCustomer(cust));
			
			stateChanged();
		}
		else {
			sendFullMsgTo = cust;
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Customer list is full");
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
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Customer wants to be seated");
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
		if(hour==23 && !waitingForNoCustomers) {
			RestaurantIsClosed();
		}
		if(waitingForNoCustomers) {
			boolean restaurantEmpty=true;
			for(MyWaiter w: waiters) {
				if(w.numCustomers!=0) {
					restaurantEmpty=false;
				}
			}
			if(restaurantEmpty) {
				CloseRestaurant();
				return true;
			}
		}
		if(cookArrived){
			CookOnDuty();
			return true;
		}
		if(cashierArrived) {
			CashierOnDuty();
			return true;
		}
		synchronized(waiters) {
		for(int i=1; i<=waiters.size(); i++) {
			if(waiters.get(i).state==MyWaiterState.justArrived) {
				TellWaiterPosition(waiters.get(i), i);
				return true;
			}
		}
		}
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
		
		if(!waiters.isEmpty())
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
	private void CloseRestaurant() {
		
		myCashier.msgOffDuty(cashierPay);
		try {
			registerRestocked.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCook.msgOffDuty(cookPay);
		int numWaitersPaid=0;
		for(MyWaiter w: waiters) {
			w.w.msgOffDuty(waiterPay);
			numWaitersPaid++;
		}
		waiters.clear();
		myPerson.money+=hostPay;
		register-=(cookPay+cashierPay+hostPay+numWaitersPaid*waiterPay);
		waitingForNoCustomers=false;
		isActive=false;
	}
	private void RestaurantIsClosed() {
		for(MyCustomer c: waitingCustomers) {
			c.c.msgRestaurantIsClosed();
		}
		waitingForNoCustomers=true;
	}
	private void CookOnDuty() {
		myCook.msgOnDuty();
		cookArrived=false;
	}
	private void CashierOnDuty() {
		myCashier.msgRegisterAmount(register);
		register=0;
		cashierArrived=false;
	}
	private void TellWaiterPosition(MyWaiter w, int pos) {
		w.w.msgPosition(pos);
		w.state=MyWaiterState.working;
	}
	
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
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Waiter can take a break");
			Do("yes "+ w.w.getName()+", you can take a break!");
			w.w.msgBreakReply(true);
			w.state=MyWaiterState.onBreak;
			//w.requestedBreak=false;
		}
		else {
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DHostRole", "Waiter cannot take a break");
			Do("no "+ w.w.getName()+", you can't take a break!");
			w.w.msgBreakReply(false);
			w.state=MyWaiterState.working;
			//w.requestedBreak=false;
		}
	}

	//utilities

	public DProducerConsumerMonitor getMonitor() {
		return theMonitor;
	}
//	public void setGui(HostGui gui) {
//		hostGui = gui;
//	}
//
//	public HostGui getGui() {
//		return hostGui;
//	}
	
	public void msgAddWaiter(DWaiterRole w) {
		//waiters.add(w);
		waiters.add(new MyWaiter(w));
		System.out.println("waiter "+ w.getName() +" added to host list");
		if(w instanceof DWaiterSharedDataRole) {
			((DWaiterSharedDataRole) w).setMonitor(theMonitor);
		}
		
	}
	
//	public void addCook(DCookRole c) {
//		myCook = c;
//		c.setMonitor(theMonitor);
//		cookArrived=true;
//	}
	
	static public class MyWaiter {
		
		DWaiterRole w;
		enum MyWaiterState {justArrived, working, onBreak, atFront, requestedBreak};
		MyWaiterState state;
		int numCustomers;
		public MyWaiter(DWaiterRole w2) {
			// TODO Auto-generated constructor stub
			w=w2;
			state=MyWaiterState.justArrived;
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

	public void addCook(DCookRole cook) {
		// TODO Auto-generated method stub
		myCook=cook;
	}
}

