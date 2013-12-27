/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant;

import simcity.DRestaurant.DOrder;
import simcity.DRestaurant.DOrder.OrderState;
import agent.Role;
import simcity.gui.SimCityGui;
import simcity.DRestaurant.DGui.DWaiterGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Head Waiter
 */

public abstract class DWaiterRole extends Role implements DWaiter {
	static final int NTABLES = 4;//a global for the number of tables.

	public List<DCustomerRole> waitingCustomers
	=  Collections.synchronizedList(new ArrayList<DCustomerRole>());

	public List<DOrder> orders=  Collections.synchronizedList(new ArrayList<DOrder>());
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<DCheck> myChecks=  Collections.synchronizedList(new ArrayList<DCheck>());

	Timer timer = new Timer();
	
	DCookRole cook;
	DHostRole host;
	DCashierRole cashier;

	protected String type;
	boolean checksWaiting=false;
	private MyCustomer takingOrderFrom = null;
	private int startPos;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atFront = new Semaphore(0,true);
	private Semaphore atTheDoor = new Semaphore(0, true);
	private Semaphore customerArrived = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0, true);
	public DWaiterGui WaiterGui = null;

	public enum WaiterState {arrived, working, takingOrder, 
			goingToCook, servingFood, onBreak, onDuty, waitingForOnDuty, offDuty};
	
	public boolean onBreak;
	public boolean canTakeBreak; // to hold response from host + make sure host doesn't assign new customers while waiter is finishing up
	private boolean checkBoxReset= false;
	
	private boolean wantBreakChecked;
	private boolean returnFromWorkChecked;
	private boolean disableBoxTillBreak;
	
	public WaiterState state;
	SimCityGui gui;
	
	public DWaiterRole(SimCityGui gui) {
		super();
		this.gui=gui;
		
		state = WaiterState.arrived;
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
	public void msgAddCook(DCookRole c) {
		cook=c;
	}
	
	public void msgAddCashier(DCashierRole ca) {
		cashier= ca;
	}
	
	public void msgAddHost(DHostRole h) {
		host=h;
	}
	
	public void msgPosition(int pos) {
		startPos=pos;
		state=WaiterState.onDuty;
		stateChanged();
	}
	
    public void msgOffDuty(double money){
    	myPerson.money+=money;
    	state=WaiterState.offDuty;
    	stateChanged();
    }
	
	public void msgHereIsAWaitingCustomer(DCustomer c, int t) {
		System.out.println("waiter: adding "+c+ " to my customers list");
		customers.add(new MyCustomer(c, t, simcity.DRestaurant.DWaiterRole.MyCustomer.MyCustomerState.waiting));
		stateChanged();
	}
	
	public void msgYourCustomerHasArrived() {
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Customer arrived");
		System.out.println("received cust arrive");
		customerArrived.release();
		stateChanged();
	}

	
	@Override
	public void msgImReadyToOrder(DCustomerRole cust) {
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Customer is ready to order");
		System.out.println("waiter received ready message");
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=simcity.DRestaurant.DWaiterRole.MyCustomer.MyCustomerState.readyToOrder;
				System.out.println("found customer on list!");
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgHereIsMyChoice(DCustomerRole cust, String choice) {
		
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				System.out.println(cust + " ordered: "+ choice);
				customer.setChoice(choice);
				customer.state=MyCustomer.MyCustomerState.ordered;
				DOrder newOrder = new DOrder(choice, customer.getTablenum(), this);
				orders.add(newOrder);
				System.out.println("waiter added order of "+ choice);
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgOutOfFood(DOrder o) {
		for(MyCustomer customer: customers) {
			if(customer.getTablenum()==o.getTablenum()) {
				customer.state=MyCustomer.MyCustomerState.needsToReorder;
			}
		}
		for(DOrder order: orders) {
			if(order == o) {
				o.state=OrderState.needsReOrder;
			}
		}
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Out of food");
		System.out.println("out of food");
		stateChanged();
	}
	
	@Override
	public void msgOrderIsReady(int tablenum) {
		for(DOrder order: orders) {
			if(order.tablenum==tablenum) {
				System.out.println("order upddated to cooked");
				order.state=DOrder.OrderState.cooked; // probably redundant but watevz
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgDoneEatingAndLeaving(DCustomerRole cust) {
		//System.out.println("received done eating");
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=MyCustomer.MyCustomerState.gone;
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgCantAffordNotStaying(DCustomerRole cust) {
		//System.out.println("received done eating");
		for(MyCustomer customer: customers) {
			if(customer.getCustomer()==cust) {
				customer.state=MyCustomer.MyCustomerState.couldNotAffordAndLeaving;
				stateChanged();
			}
		}
	}

	@Override
	public void msgAtTable() {//from animation
		stateChanged();
	}
	
	@Override
	public void msgIWantABreak() { // from GUI
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "I want a break");
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
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Check is ready");
		System.out.println("received check ready");
		checksWaiting=true;
		stateChanged();
	}
	
	@Override
	public void msgHereIsABill(DCheck bill) {
			myChecks.add(new DCheck(bill.getCustomer(), bill.getTablenum(), bill.getBillAmnt()));
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Got bill");
			System.out.println("waiter added bill for " + bill.getCustomer()+ " at table "+bill.getTablenum()+ "!");	
	
			stateChanged();
	}
	
	public void msgAnimationLeftRestaurant() {
		atTheDoor.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtFront() {
//		System.out.println("waiter made it to the front");
		atFront.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtCashier() {
//		System.out.println("waiter made it to cashier!");
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

		atTable.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtKitchen() {

		atCook.release();
		stateChanged();
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
		//`System.out.println("in waiter scheduler");
		if(!onBreak)
		{
			
			if(state==WaiterState.arrived) {
				tellHost();
				return true;
			}
			
			if(state==WaiterState.onDuty) {
				goToPosition();
				return true;
			}
			if(state==WaiterState.offDuty) {
				leaveRestaurant();
				return true;
			}
			
			
			if(wantBreakChecked) {
				AskHostForBreak();
				return true;
			}
			

			
			synchronized(customers) {
			for (MyCustomer customer : customers) {
				if(customer.state==DWaiterRole.MyCustomer.MyCustomerState.gone) {
					//System.out.println("customer " + customer.getCustomer() +" gone");
					UpdateHostOnClearTable(customer);
					return true;
				}
			}
			}
			
			if(!myChecks.isEmpty()) {
				DistributeCheck(myChecks.get(0));
				return true;
			}
			
			
			synchronized(customers) {
			for (MyCustomer customer : customers) {
				if(customer.state==DWaiterRole.MyCustomer.MyCustomerState.couldNotAffordAndLeaving) {
					//System.out.println("customer " + customer.getCustomer() +" gone");
					UpdateHostOnClearTableAndLeave(customer);
					return true;
				}
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
				synchronized(customers) {
				for (MyCustomer customer : customers) {
					if (customer.state==DWaiterRole.MyCustomer.MyCustomerState.waiting) {
						//GoToFront();
						SeatCustomer(customer);
						return true;
					}
				}
				}
				synchronized(customers) {
				for (MyCustomer customer : customers) {
					if(customer.state==DWaiterRole.MyCustomer.MyCustomerState.readyToOrder) {
						//temp comment out until fix everything else
						
						GoToCustomer(customer);
						return true;
						//return false;
					}
				}
				}

				synchronized(customers) {
				for (MyCustomer customer : customers) {
					if(customer.state==DWaiterRole.MyCustomer.MyCustomerState.gone) {
						//System.out.println("customer " + customer.getCustomer() +" gone");
						UpdateHostOnClearTable(customer);
						return true;
					}
				}
				}
					
				if(checksWaiting) {
					GoToCashier();
					return true;
					
				}
				synchronized(orders) {
				for(DOrder order: orders) {
					if(order.state==DOrder.OrderState.cooked || order.state==OrderState.pending) {
						System.out.println("going to cook");
						GoToCook();
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
	
	protected abstract void tellHost();
	
	private void goToPosition() {
		DoGoToWaiterPosition();
		state=WaiterState.working;
	}
	
	private void leaveRestaurant() {
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Off duty. Leaving restaurant.");
		Do("Off duty... leaving restaurant");
		DoLeaveRestaurant();
		try {
			atTheDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state=WaiterState.arrived;
		isActive=false;
		
	}
	
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
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Asked host for break");
		System.out.println("asked host for break");
		host.msgGoOnBreakPlease(this);
		wantBreakChecked=false; //so doesn't call this message again

	}
	
	/*******break functionality implemented in separate restaurants, not reimplemented for integration ******/
//	private void TakeBreak (final WaiterRole w) {
//		onBreak=true;
//		Do("Taking break...");
//		canTakeBreak=false;
//		/*
//		timer.schedule(new TimerTask() {
//			public void run() {
//				print("Done with break!");
//				host.msgBackToWork(w);
//				DoResetCheckBox();
//				//isHungry = false;
//				stateChanged();
//			}
//		},
//		6000);*/
//		DoSetBoxToReturn();
//		
//	}
//	
//	private void CouldNotTakeBreak() {
//		DoResetCheckBox();
//		checkBoxReset=false;
//	}
//	
//	private void ReturnToWork() {
//		print("Done with break!");
//		onBreak=false;
//		canTakeBreak=false;
//		host.msgBackToWork(this);
//		DoResetCheckBox();
//		returnFromWorkChecked=false;
//	}
	


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

		cust.getCustomer().msgFollowMe(new DMenu(), cust.tablenum, this);
		DoSeatCustomer((DCustomerRole) cust.getCustomer(), cust.getTablenum());

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DoGoHangAtTheFront();

	}
	
	private void GoToCustomer(MyCustomer cust) {
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Going to customer");
		Do("Going to customer");
		DoGoToTable(cust.getCustomer(), cust.getTablenum());
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
	
	
	private void GoToCook() {
		//state=WaiterState.goingToCook;
		DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(DOrder order: orders) {
			if(order.state==DOrder.OrderState.pending) {
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
	
	
	//the method that both types of waiters will override (all that is different between the waiters)
	protected abstract void GiveCookOrder(DOrder o);
	
	private void ServeCustomer(DOrder o) {
		DoDisplayCookedLabel(o.getChoice(), o.tablenum);
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Serving customer");
		Do("serving customer");
		o.state=DOrder.OrderState.serving;
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
				cashier.msgComputeBill(o.getChoice(), (customer.getCustomer()), (((DCustomerRole)(customer.getCustomer())).getName()), o.tablenum, this);
				DoGoHangAtTheFront();
				orders.remove(o);
				
			}
		}
	}
	
	
	private void UpdateHostOnClearTable(MyCustomer cust) {
		host.msgTableIsClear(cust.getTablenum(), this);
		DoGoHangAtTheFront();
		customers.remove(cust);
	}
	
	private void UpdateHostOnClearTableAndLeave(MyCustomer cust) { // so waiter doesn't hang out at a table if customer can't afford
		//System.out.println("going to clear table");
		host.msgTableIsClear(cust.getTablenum(), this);
		DoGoHangAtTheFront();
		customers.remove(cust);
	}
	
//	private void DisableCheckbox() {
//		DoDisableCheckbox();
//		disableBoxTillBreak=false;
//	}
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
	
	private void DistributeCheck(DCheck bill) {
	
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
		
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DWaiterRole", "Giving customer bill");
		System.out.println("Distributing the following bill: \n"+ bill.customer+"  "+ bill.tablenum + "  " + bill.getBillAmnt());
		bill.customer.msgHereIsYourBill(bill.tablenum, bill.getBillAmnt());
		myChecks.remove(bill);
		DoGoHangAtTheFront();
	}

	// The animation DoXYZ() routines
	private void DoGoHangAtTheFront() {
		WaiterGui.DoGoToHangout();
	}
	private void DoGoToWaiterPosition() {
		//System.err.println(WaiterGui);
		WaiterGui.DoGoToWaiterPosition(startPos);
	}
	private void DoLeaveRestaurant() {
		WaiterGui.DoLeaveRestaurant();
	}
	private void DoGoToCashier() {
		Do("going to cashier to get bill..");
		WaiterGui.DoGoToCashier();
	}

	private void DoGoToFront() {
		WaiterGui.DoGoToFrontLine();
	}
	private void DoSeatCustomer(DCustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		WaiterGui.DoBringToTable(customer,table); 
		//send info to customerGui
		//customer.getGui().DoGoToSeat(table, 1);

	}
	
	private void DoGoToTable(DCustomer customer, int table) {

		WaiterGui.DoGoToTable((DCustomerRole)customer, table);
	}
	

	private void DoGoToCook() {
		WaiterGui.DoGoToCook();
	}
	
	private void DoDisplayCookedLabel(String choice, int tablenum) {
		WaiterGui.DoShowCookedLabel(choice, tablenum);
	}
	
	/***** do functions not utilized during integration *****/
	
//	private void DoDisableCheckbox() {
//	WaiterGui.DoDisableCheckbox();
//}

//	private void DoClearTable(int t) {
//		WaiterGui.DoClearTable(t);
//	}
	
//	private void DoResetCheckBox() {
//		WaiterGui.DoResetCheckBox();
//	}
//	
//	private void DoSetBoxToReturn() {
//		WaiterGui.DoSetBoxToReturn();
//	}
	
	//utilities

	public void setGui(DWaiterGui gui) {
		WaiterGui = gui;
	}

	public DWaiterGui getGui() {
		return WaiterGui;
	}

	public static class MyCustomer {
		
		private DCustomer c;
		private int tablenum;
		enum MyCustomerState {none, waiting, seated, readyToOrder, ordered, needsToReorder, waitingForFood, serveMe, served, gone, billed, couldNotAffordAndLeaving };
		MyCustomerState state;
		private String choice;
		
		MyCustomer(DCustomer c2, int t, MyCustomerState s) {
			c=c2;
			tablenum=t;
			state=s;
		}
		
		public DCustomer getCustomer() {
			return c;
		}
		
		public void setCustomer(DCustomerRole c) {
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
	public void msgSitAtTable(int t, DCustomerRole cust) {
		// TODO Auto-generated method stub
		
	}
	
}

