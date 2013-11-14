//aaaah

package restaurant;

import agent.Agent;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.Order;
import restaurant.Order.OrderState;
import restaurant.WaiterAgent.MyCustomer.MyCustomerState;
/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<CustomerRole> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<CustomerRole>());

	public List<Order> orders=  Collections.synchronizedList(new ArrayList<Order>());
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<Check> myChecks=  Collections.synchronizedList(new ArrayList<Check>());
	//public Check myBill= null; //typically carrying only one
	//public MyCustomer billRecipient = null; //again typically only one
	//public List<Order> ReadyOrders = new ArrayList<Order>();
	
	Timer timer = new Timer();
	
	CookRole cook;
	HostRole host;
	CashierRole cashier;
	
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	//private boolean inMotion = false;
	
	boolean checksWaiting=false;
	private MyCustomer takingOrderFrom = null;
	//private Order orderDelivering = null;
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atFront = new Semaphore(0,true);
	private Semaphore customerArrived = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0, true);
	public WaiterGui WaiterGui = null;

	public enum WaiterState {working, takingOrder, 
			goingToCook, servingFood, onBreak};
	
	public boolean onBreak;
	//public boolean requestedBreak; // for host to respond to break request
	public boolean canTakeBreak; // to hold response from host + make sure host doesn't assign new customers while waiter is finishing up
	private boolean checkBoxReset= false;
	
	private boolean wantBreakChecked;
	private boolean returnFromWorkChecked;
	private boolean disableBoxTillBreak;
	
	public WaiterState state;
	
	public WaiterAgent(String name) {
		super();
		
		this.name = name;
		
		state = WaiterState.working;
		onBreak=false;
		wantBreakChecked=false;
		//requestedBreak=false;
		canTakeBreak=false;
		returnFromWorkChecked=false;
		disableBoxTillBreak=false;
		
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

	
	// Messages

	//hack!
	public void msgAddCook(CookRole c) {
		cook=c;
	}
	
	public void msgAddCashier(CashierRole ca) {
		cashier= ca;
	}
	
	public void msgAddHost(HostRole h) {
		host=h;
	}
	
	public void msgHereIsAWaitingCustomer(Customer c, int t) {
		System.out.println("waiter: adding "+c+ " to my customers list");
		customers.add(new MyCustomer(c, t, restaurant.WaiterAgent.MyCustomer.MyCustomerState.waiting));
		stateChanged();
	}
	
	public void msgYourCustomerHasArrived() {
		System.out.println("received cust arrive");
		customerArrived.release();
		stateChanged();
	}
//	
//	public void msgSitAtTable(int t, Customer c) {
//		System.out.println("waiter: adding "+c+ " to my customers list");
//		customers.add(new MyCustomer(c, t, restaurant.WaiterAgent.MyCustomer.MyCustomerState.waiting));
//		stateChanged();
//	}
	
	@Override
	public void msgImReadyToOrder(CustomerRole cust) {
		System.out.println("waiter received ready message");
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=restaurant.WaiterAgent.MyCustomer.MyCustomerState.readyToOrder;
				System.out.println("found customer on list!");
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgHereIsMyChoice(CustomerRole cust, String choice) {
		
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				System.out.println(cust + " ordered: "+ choice);
				customer.setChoice(choice);
				customer.state=restaurant.WaiterAgent.MyCustomer.MyCustomerState.ordered;
				Order newOrder = new Order(choice, customer.getTablenum(), this);
				orders.add(newOrder);
				System.out.println("waiter added order of "+ choice);
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgOutOfFood(Order o) {
		for(MyCustomer customer: customers) {
			if(customer.getTablenum()==o.getTablenum()) {
				customer.state=MyCustomer.MyCustomerState.needsToReorder;
			}
		}
		for(Order order: orders) {
			if(order == o) {
				o.state=OrderState.needsReOrder;
			}
		}
		System.out.println("out of food");
		stateChanged();
	}
	
	@Override
	public void msgOrderIsReady(int tablenum) {
		for(Order order: orders) {
			if(order.tablenum==tablenum) {
				System.out.println("order upddated to cooked");
				order.state=Order.OrderState.cooked; // probably redundant but watevz
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgDoneEatingAndLeaving(CustomerRole cust) {
		//System.out.println("received done eating");
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=restaurant.WaiterAgent.MyCustomer.MyCustomerState.gone;
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgCantAffordNotStaying(CustomerRole cust) {
		//System.out.println("received done eating");
		//state=WaiterState.working;
		//DoGoHangAtTheFront();
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=restaurant.WaiterAgent.MyCustomer.MyCustomerState.couldNotAffordAndLeaving;
				stateChanged();
			}
		}
	}

	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		//atTable.release();// = true;
//		state=WaiterState.working;
		stateChanged();
	}
	
	@Override
	public void msgIWantABreak() { // from GUI
		Do("I want break!");
		wantBreakChecked=true;
		stateChanged();
	}
	
	@Override
	public void msgBreakReply(Boolean yn) {
		//requestedBreak=false;
		if (yn) {
			canTakeBreak=true;
			disableBoxTillBreak=true;
			stateChanged();
		}
		else {
			checkBoxReset=true;
			stateChanged();
		}
		
	}
	
	@Override
	public void msgOutOfBreak() {
		//System.out.println("received out of break!");
		returnFromWorkChecked=true;
		onBreak=false;
		stateChanged();
	}
	
	@Override
	public void msgCheckIsReady() {
		System.out.println("received check ready");
		checksWaiting=true;
		stateChanged();
	}
	
	@Override
	public void msgHereIsABill(Check bill) {
			myChecks.add(new Check(bill.getCustomer(), bill.getTablenum(), bill.getBillAmnt()));
			System.out.println("waiter added bill for " + bill.getCustomer()+ " at table "+bill.getTablenum()+ "!");	
	
			stateChanged();
	}
	
	
	public void msgAnimationArrivedAtFront() {
//		System.out.println("made it");
		atFront.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtCashier() {
//		System.out.println("made it to cashier!");
		//state=WaiterState.atCashier;
		
		atCashier.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedForOrder() {
		atTable.release();
		stateChanged();
		
	}
	
	public void msgAnimationDoneSeating() {
		state= WaiterState.working; // using working as default state
		//inMotion=false;
		//System.out.println("madeit");
		atTable.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtKitchen() {
		//System.out.println("made it to kitchen");
		//state= WaiterState.atKitchen;
		atCook.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//`System.out.println("in watier scheduler");
		if(!onBreak)
		{
			//System.out.println("waiter is on duty!");
			//if gui is still seating a customer, cannot schedule any other task
			
			//notifying clear table to host on the radio
			
			if(wantBreakChecked) {
				AskHostForBreak();
				return true;
			}
			
			if(returnFromWorkChecked) {
				ReturnToWork();
				return true;
			}
			
			if(disableBoxTillBreak){
				DisableCheckbox();
				return true;
			}
			
			
			for (MyCustomer customer : customers) {
				if(customer.state==WaiterAgent.MyCustomer.MyCustomerState.gone) {
					//System.out.println("customer " + customer.getCustomer() +" gone");
					UpdateHostOnClearTable(customer);
					return true;
				}
			}
		
			
			if(!myChecks.isEmpty()) {
				DistributeCheck(myChecks.get(0));
				return true;
			}
			
			
			
			for (MyCustomer customer : customers) {
				if(customer.state==WaiterAgent.MyCustomer.MyCustomerState.couldNotAffordAndLeaving) {
					//System.out.println("customer " + customer.getCustomer() +" gone");
					UpdateHostOnClearTableAndLeave(customer);
					return true;
				}
			}
				synchronized(customers) {
				for(MyCustomer customer: customers) {
					if(customer.state== MyCustomer.MyCustomerState.needsToReorder) {
						GoToCustomer(customer);
						System.out.println("going for reorder");
						return true;
					}
				}
				}
				for (MyCustomer customer : customers) {
					if (customer.state==WaiterAgent.MyCustomer.MyCustomerState.waiting) {
						//GoToFront();
						SeatCustomer(customer);
						return true;
					}
				}
				for (MyCustomer customer : customers) {
					if(customer.state==WaiterAgent.MyCustomer.MyCustomerState.readyToOrder) {
						//temp comment out until fix everything else
						
						GoToCustomer(customer);
						return true;
						//return false;
					}
				}
				/*
				for (MyCustomer customer : customers) {
					if(customer.state==MyCustomer.MyCustomerState.ordered) {
						//System.out.println("found customer who ordered!");
						customer.state= MyCustomer.MyCustomerState.waitingForFood;
						GoToCook();
						return true;
					}
				}*/
			
				for (MyCustomer customer : customers) {
					if(customer.state==WaiterAgent.MyCustomer.MyCustomerState.gone) {
						//System.out.println("customer " + customer.getCustomer() +" gone");
						UpdateHostOnClearTable(customer);
						return true;
					}
				}
					
				if(checksWaiting) {
					GoToCashier();
					return true;
					
				}
				for(Order order: orders) {
					if(order.state==Order.OrderState.cooked || order.state==OrderState.pending) {
						System.out.println("going to cook");
						GoToCook();
						return true;
					}
				}
				
				
				if(checkBoxReset) {
					CouldNotTakeBreak();
					return true;
				}
				
				
				
				if(customers.size()==0 && canTakeBreak) {
					TakeBreak(this);
					return true;
				}
				
	
		
				
		}
		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void GoToCashier() {
		checksWaiting=false;
		DoGoToCashier();
//		System.out.println(atCashier.availablePermits());
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("made it");
		cashier.msgHereForMyBills(this);
	}

	private void AskHostForBreak() {
		System.out.println("asked host for break");
		host.msgGoOnBreakPlease(this);
		wantBreakChecked=false; //so doesn't call this message again

	}
	private void TakeBreak (final WaiterAgent w) {
		onBreak=true;
		Do("Taking break...");
		canTakeBreak=false;
		/*
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done with break!");
				host.msgBackToWork(w);
				DoResetCheckBox();
				//isHungry = false;
				stateChanged();
			}
		},
		6000);*/
		DoSetBoxToReturn();
		
	}
	
	private void CouldNotTakeBreak() {
		DoResetCheckBox();
		checkBoxReset=false;
	}
	
	private void ReturnToWork() {
		print("Done with break!");
		onBreak=false;
		canTakeBreak=false;
		host.msgBackToWork(this);
		DoResetCheckBox();
		returnFromWorkChecked=false;
	}
	


	private void SeatCustomer(MyCustomer cust) {
		//FIX HERE
		
		//state=WaiterState.goingToSeatCustomer;
		DoGoToFront();

		try {
			atFront.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("sending message to host that I have arrived");
		host.msgHereToTakeMyCustomer(this);
		
		try {
			customerArrived.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		cust.state=MyCustomer.MyCustomerState.seated;

		cust.getCustomer().msgFollowMe(new Menu(), cust.tablenum, this);
		DoSeatCustomer((CustomerRole) cust.getCustomer(), cust.getTablenum());

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DoGoHangAtTheFront();

	}
	
	private void GoToCustomer(MyCustomer cust) {
		Do("Going to customer");
		//takingOrderFrom=cust;
		//state=WaiterState.goingToTakeOrder;
		DoGoToTable(cust.getCustomer(), cust.getTablenum());
//		System.out.println(atTable.availablePermits());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cust.state==MyCustomer.MyCustomerState.needsToReorder)
		{
			cust.getCustomer().msgWhatWouldYouLike(cust.getChoice());
			state=WaiterState.takingOrder;
		}
		else {
			cust.getCustomer().msgWhatWouldYouLike();
			state=WaiterState.takingOrder;
		}
	}
	
	/*private void ReturnToFront() {
		System.out.println("going to front...");
		DoGoToFront();
	}*/
	
	private void GoToCook() {
		//state=WaiterState.goingToCook;
		DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Order order: orders) {
			if(order.state==Order.OrderState.pending) {
				GiveCookOrder(order);
			}
		}
		for(int i=0; i<orders.size(); i++) {
			//System.out.println(orders.get(i));
			if(orders.get(i).state==OrderState.cooked) {
//				TellCookToClearPlating(orders.get(i));
				ServeCustomer(orders.get(i));
				break; // bc won't be at kitchen by the time is done
			}
		}
		DoGoHangAtTheFront();
		
	}
	
	private void GiveCookOrder(Order o) { //start here!
		Do("giving order to cook");
		cook.msgHereIsAnOrder(o);
		
		o.state=OrderState.ordered;
		state=WaiterState.working; 
	}
	
//	private void TellCookToClearPlating(Order o) {
//		System.err.println(o.tablenum);
//		cook.msgClearPlatingForOrder(o);
//	}
	
	private void ServeCustomer(Order o) {
		DoDisplayCookedLabel(o.getChoice(), o.tablenum);
		Do("serving customer");
		o.state=Order.OrderState.serving;
		for(MyCustomer customer: customers) {
			if(customer.getTablenum()==o.tablenum) {
				//customer.state=MyCustomer.MyCustomerState.serveMe;
				
				DoGoToTable(customer.getCustomer(), o.tablenum);
				
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(customer.getCustomer() + " here is you order of : " + o.getChoice());
				customer.getCustomer().msgFoodIsServed();
				customer.state=MyCustomer.MyCustomerState.served;
//				System.err.println((CustomerAgent)(customer.getCustomer()));
				cashier.msgComputeBill(o.getChoice(), (CustomerRole)(customer.getCustomer()), (((CustomerRole)(customer.getCustomer())).getName()), o.tablenum, this);
//				System.err.println("requested bill for " + (((CustomerAgent) (customer.getCustomer())).getName()) + " at table "+  o.tablenum);
				DoGoHangAtTheFront();
//				o.state=OrderState.billPending;
				orders.remove(o);
				
			}
		}
	}
	
	
	private void UpdateHostOnClearTable(MyCustomer cust) {
		//System.out.println("going to clear table");
		host.msgTableIsClear(cust.getTablenum(), this);
//		DoClearTable(cust.getTablenum());
		//DoGoToFront();
		DoGoHangAtTheFront();
		customers.remove(cust);
	}
	
	private void UpdateHostOnClearTableAndLeave(MyCustomer cust) { // so waiter doesn't hang out at a table if customer can't afford
		//System.out.println("going to clear table");
		host.msgTableIsClear(cust.getTablenum(), this);
//		DoClearTable(cust.getTablenum());
		DoGoHangAtTheFront();
		//DoGoToFront();
		customers.remove(cust);
	}
	
	private void DisableCheckbox() {
		DoDisableCheckbox();
		disableBoxTillBreak=false;
	}
	/*
	private void TakeBillToCustomer() {
		state=WaiterState.goingToGiveBill;
		DoGoToTable(billRecipient, myBill.tablenum);
	}
	
	private void GiveBillToCustomer() {
		//System.out.println(myBill);
		billRecipient.getCustomer().msgHereIsYourBill(myBill.getTablenum(), myBill.getBillAmnt()); // instead of passing object, just get val
		myBill=null;
		billRecipient.state=WaiterAgent.MyCustomer.MyCustomerState.billed;
		billRecipient=null;
		state=WaiterState.working;
		for(int i=0; i<orders.size(); i++) {
			if (orders.get(i).state==OrderState.billProcessed) {
				orders.remove(orders.get(i));
			}
		}
		DoGoHangAtTheFront();
	}*/
	
	private void DistributeCheck(Check bill) {
	
//		System.err.println(bill.customer);
		DoGoToTable(bill.customer, bill.tablenum);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(MyCustomer customer: customers) {
			if(customer.tablenum==bill.tablenum)
				bill.customer=customer.getCustomer();
		}
		System.out.println("Distributing the following bill: \n"+ bill.customer+"  "+ bill.tablenum + "  " + bill.getBillAmnt());
		bill.customer.msgHereIsYourBill(bill.tablenum, bill.getBillAmnt());
		myChecks.remove(bill);
		DoGoHangAtTheFront();
	}

	// The animation DoXYZ() routines
	private void DoGoHangAtTheFront() {
		WaiterGui.DoGoToHangout();
	}
	private void DoGoToCashier() {
		Do("going to cashier to get bill..");
		WaiterGui.DoGoToCashier();
	}
	private void DoDisableCheckbox() {
		WaiterGui.DoDisableCheckbox();
	}
	private void DoGoToFront() {
		WaiterGui.DoGoToFrontLine();
	}
	private void DoSeatCustomer(CustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		WaiterGui.DoBringToTable(customer,table); 
		//send info to customerGui
		//customer.getGui().DoGoToSeat(table, 1);

	}
	
	private void DoGoToTable(Customer customer, int table) {
		/*
		if(customer.state==MyCustomer.MyCustomerState.serveMe) {
			System.out.println("Serving "+ customer.getCustomer() + " at "+ table);
			WaiterGui.DoGoToTable(customer.getCustomer(), table);
			//state=WaiterState.servingFood;
		}
		else if(state==WaiterState.goingToGiveBill) {
			//System.out.println("going to give "+ customer.getCustomer() + "bill!");
			WaiterGui.DoGoToTable(customer.getCustomer(), table);
		}
		else {
			System.out.println("Going to "+ customer.getCustomer() + " at " + table);
			WaiterGui.DoGoToTable(customer.getCustomer(), table);
			state=WaiterState.goingToTakeOrder;
		}*/
		WaiterGui.DoGoToTable((CustomerRole)customer, table);
	}

//	private void DoClearTable(int t) {
//		WaiterGui.DoClearTable(t);
//	}
	private void DoGoToCook() {
		WaiterGui.DoGoToCook();
	}
	
	private void DoDisplayCookedLabel(String choice, int tablenum) {
		WaiterGui.DoShowCookedLabel(choice, tablenum);
	}
	
	private void DoResetCheckBox() {
		WaiterGui.DoResetCheckBox();
	}
	
	private void DoSetBoxToReturn() {
		WaiterGui.DoSetBoxToReturn();
	}
	//utilities

	public void setGui(WaiterGui gui) {
		WaiterGui = gui;
	}

	public WaiterGui getGui() {
		return WaiterGui;
	}

	public static class MyCustomer {
		
		private Customer c;
		private int tablenum;
		enum MyCustomerState {none, waiting, seated, readyToOrder, ordered, needsToReorder, waitingForFood, serveMe, served, gone, billed, couldNotAffordAndLeaving };
		MyCustomerState state;
		private String choice;
		
		MyCustomer(Customer c2, int t, MyCustomerState s) {
			c=c2;
			tablenum=t;
			state=s;
		}
		
		public Customer getCustomer() {
			return c;
		}
		
		public void setCustomer(CustomerRole c) {
			this.c = c;
		}

		public int getTablenum() {
			return tablenum;
		}

		public void setTablenum(int tablenum) {
			this.tablenum = tablenum;
		}

		public String getChoice() {
			return choice;
		}

		public void setChoice(String choice) {
			this.choice = choice;
		}
		
	}

	@Override
	public void msgHereIsACheck(int tnum, double amnt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(int t, CustomerRole cust) {
		// TODO Auto-generated method stub
		
	}
	
	
	/* private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;
		
		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		
		int getTableNum() { return tableNumber; }

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}*/
}

