package simcity.TRestaurant;

import agent.Role;
import simcity.TRestaurant.gui.TCookGui;
import simcity.TRestaurant.gui.TWaiterGui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import simcity.PersonAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public class TWaiterRole extends Role implements TWaiter{
	public List<customers> myCustomers
	= new ArrayList<customers>(); 

	enum CustomerState 
	{waiting, seated, ordering, askingForOrder, waitingForOrder, orderPlaced, orderDone, Eating, wantsCheck, needsCheck, Paying, gaveCheck, Leaving};

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore withCustomer = new Semaphore(-1, true);
	private Semaphore atCook = new Semaphore(0, true);
	private THostRole host;
	private TCookRole cook;
	private TCashierRole cashier; 
	public boolean onBreak = false;
	public boolean askingForBreak = false; 
	boolean goHome = false;
	Timer timer = new Timer();
	private Menu menu = new Menu();
	private OrderStand orders; 
	SimCityGui gui;
    boolean arrived;



	public TWaiterGui waiterGui = null;


	public TWaiterRole(SimCityGui gui) {
		super();
		this.gui = gui; 
		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}


	// Messages
	

	public void msgSeatAtTable(TCustomer cust, int t) {
		customers c = new customers();
		c.setCustomer(cust);
		c.setTable(t);
		myCustomers.add(c);
		stateChanged();
	}

	public void msgReadyToOrder(TCustomer cust) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != cust && index < myCustomers.size()) {
				index++; 
			}
			myCustomers.get(index).state = CustomerState.ordering;
		}
		stateChanged(); 
	}

	public void msgHeresMyChoice(TCustomer cust, String order) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != cust && index < myCustomers.size()) {
				index++; 
			}
			setOrder(myCustomers.get(index).c, order); 
			myCustomers.get(index).state = CustomerState.waitingForOrder;
		}
		stateChanged();
	}

	public void msgOutOfFood(int t) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).table != t && index < myCustomers.size()) {
				index++;
			}
			myCustomers.get(index).state = CustomerState.ordering;
		}
		stateChanged();
	}

	public void msgOrderIsReady(int t) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).table != t) {
				index++; 
			}
			myCustomers.get(index).state = CustomerState.orderDone;
		}
		stateChanged(); 
	}

	public void msgReadyForCheck(TCustomer cust) {
		int index = 0; 
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != cust) {
				index++; 
			}
			myCustomers.get(index).state = CustomerState.wantsCheck; 
		}
		stateChanged(); 
	}
	
	public void msgHereIsCheck(TCustomer cust, double c) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != cust) {
				index++; 
			}
			AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Recieved check from cashier");
			Do("Received check from cashier"); 
			myCustomers.get(index).check = c; 
			myCustomers.get(index).state = CustomerState.Paying;
		}
		stateChanged();
		
	}
	
	public void msgLeavingTable(TCustomer cust) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != cust) {
				index++; 
			}
			myCustomers.get(index).state = CustomerState.Leaving;
		}
		stateChanged(); 

	}
	
	@Override
	public void msgGoHome(double moneys) {
		myPerson.money += moneys;
		goHome = true;
		stateChanged();
		
	}

	public void msgNoBreak() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "No break for me");
		Do("No break for me.");
		askingForBreak = false; 
		stateChanged(); 
	}

	public void msgOnBreak() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "I can have a break! :)");
		Do("I can have a break! :)");
		onBreak = true; 
		stateChanged(); 
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}

	public void msgWithCustomer() {
		withCustomer.release();
		stateChanged(); 
	}

	public void msgAtKitchen() {
		atCook.release(); 
		stateChanged(); 
	}

	public void msgAskForBreak () {
		askingForBreak = true; 
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
		if (askingForBreak == true) {
			askingForBreak = false; 
			AskForBreak(); 
			return true; 
		}
		try {
			if (!myCustomers.isEmpty()) {
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.Paying) {
						print("Giving customer his check");
						giveCheck(myCustomers.get(index).c, myCustomers.get(index).table, myCustomers.get(index).check); 
						myCustomers.get(index).state = CustomerState.gaveCheck; 
						return true; 
					}
				}
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.Leaving) {
						callHost(myCustomers.get(index).c);
						return true; 
					}
				}
				
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.ordering) {
						myCustomers.get(index).state = CustomerState.askingForOrder;
						print("Going to take customer's order");
						try {
							goToTable(myCustomers.get(index).c, myCustomers.get(index).table);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true; 
					}
				}
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.wantsCheck) {
						print("Calling cashier for check");
						callCashier(myCustomers.get(index).c); 
						myCustomers.get(index).state = CustomerState.needsCheck; 
						return true; 
					}
				}
	
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.waitingForOrder) {
						print("Calling cook to give order");
						callCook(myCustomers.get(index)); 
						myCustomers.get(index).state = CustomerState.orderPlaced; 
						return true; 
					}
				}
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.orderDone) {
						//goToCook();
						giveOrder(myCustomers.get(index).c, myCustomers.get(index).table, myCustomers.get(index).choice);
						myCustomers.get(index).state = CustomerState.Eating; 
						return true; 
					}
				}
				for (int index = 0; index < myCustomers.size(); index++) {
					if (myCustomers.get(index).state == CustomerState.waiting) {
						//waiterGui.getCustomer();
						try {
							withCustomer.acquire();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						seatCustomer(myCustomers.get(index));
						return true; 
					}
				}
			}
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
		
		if(goHome) {
			goHome();
			return true;
		}

		if (onBreak == true && myCustomers.isEmpty()) {
			onBreak = false; 
			takeBreak();
			return true; 
		}
		return false;

	}

	// Actions
	

	private void tellHost() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Telling manager I can work");
		Do("Telling manager I can work");
		arrived = false;
		if (waiterGui == null) {
			waiterGui = new TWaiterGui(this);
			gui.myPanels.get("Restaurant 6").panel.addGui(waiterGui);
		}
		host.msgIAmHere(this, "Waiter");
	}

	private void seatCustomer(customers customer) {
		DoGetCustomer(); 
		menu.setMenu(); 
		customer.c.msgSitAtTable(customer.table, menu.foodChoices);
		customer.state = CustomerState.seated;
		customer.c.setWaiter(this);
		customer.c.setCashier(cashier); 
		DoSeatCustomer(customer.c, customer.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
	}

	private void goToTable (TCustomer c, int table) throws InterruptedException {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Going to table");
		Do("Going to table " + table + " to take order");
		DoGoToTable(table);
		atTable.acquire(); 
		c.msgWhatWouldYouLike();
		waiterGui.DoLeaveCustomer();
	}

	private void setOrder(TCustomer c, String o) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != c) {
				index ++; 
			}
			myCustomers.get(index).choice = o;
		}
		myCustomers.get(index).state = CustomerState.waitingForOrder;
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Recieved customer's orders");
		Do("Received customer's orders");
	}

	private void callCook(customers c) {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "in callCook");
		Do("in callCook");
		goToCook(); 
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RotatingOrders o = new RotatingOrders(this, c.choice, c.table); 
		orders.insert(o);
		waiterGui.DoLeaveCustomer(); 
	}

	private void giveOrder(TCustomer c, int t, String f) {
		
		goToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.holdingFood = true; 
		waiterGui.setFood(f);
		DoGoToTable(t);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Giving customer their order.");
		Do("Giving customer their order.");
		c.msgHeresYourOrder(); 
		waiterGui.holdingFood = false; 
		waiterGui.DoLeaveCustomer();
	}
	
	private void callCashier(TCustomer c) {
		int index = 0;
		while (myCustomers.get(index).c != c) {
			index ++; 
		}
		cashier.msgComputeBill(this, myCustomers.get(index).c, myCustomers.get(index).choice);
	}
	
	private void giveCheck(TCustomer c, int tab, double check) {
		
		DoGoToTable(tab);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Gave check to customer");
		Do("Gave check to customer"); 
		c.msgHereIsYourCheck(check); 
		waiterGui.DoLeaveCustomer();
	}
	

	private void callHost(TCustomer c) {
		int index = 0;
		synchronized (myCustomers) {
			while (myCustomers.get(index).c != c) {
				index ++; 
			}
			myCustomers.remove(index);
		}
		host.msgCustomerLeft(c);
	}

	private void AskForBreak() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Asking host for a break");
		Do("Asking host for a break"); 
		host.msgBreakPlease(this); 
	}

	private void takeBreak() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Taking my break");
		Do("Taking my break"); 
		waiterGui.DoGoOnBreak();
		waiterGui.setBreak();
		timer.schedule(new TimerTask() {
			public void run() {
				DoneWithBreak(); 
			}
		},
		 10000);
	}

	public void DoneWithBreak() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Done with my break");
		Do("Done with my break");
		waiterGui.DoLeaveCustomer();
		waiterGui.offBreak(); 
		host.msgOffBreak(this); 
	}
	
	private void goHome() {
		AlertLog.getInstance().logInfo(AlertTag.TRestaurant, "TWaiterRole", "Going home");
		Do("Going home");
		isActive = false;
		goHome = false;
	}
	
	
	// The animation DoXYZ() routines
	private void DoGetCustomer() {
		waiterGui.getCustomer();  
	}
	
	private void DoSeatCustomer(TCustomer c, int tab) {
		print("Seating " + c + " at table " + tab);
		waiterGui.DoBringToTable(tab);

	}

	private void DoGoToTable (int tab) {
		waiterGui.DoBringToTable(tab);
	}

	private void goToCook () {
		waiterGui.DoGoToCook();
	}

	
	//utilities

	public void setGui(TWaiterGui gui) {
		waiterGui = gui;
	}
	
	public void setHomePosition (int l) {
		waiterGui.setHome(l); 
	}

	public TWaiterGui getGui() {
		return waiterGui;
	}

	public void setHost(THostRole h) {
		host = h;
	}

	public void setCook(TCookRole c) {
		cook = c;
	}
	
	public void setCashier (TCashierRole c) {
		cashier = c; 
	}



	class customers {
		TCustomer c;
		int table; 
		String choice;
		Double check; 
		CustomerState state;		

		public void setCustomer(TCustomer cust) {
			c = cust;
			state = CustomerState.waiting; 
		}

		public void setTable (int t) {
			table = t; 
		}

		TCustomer getC(){
			return c; 
		}


	}

	public class Menu {
		//public String[] foodChoices = {"Steak", "Chicken", "Salad", "Pizza"};
		public List<String> foodChoices = new ArrayList<String>();

		public void setMenu () {
			foodChoices.add("Steak"); 
			foodChoices.add("Salad"); 
			foodChoices.add("Chicken"); 
			foodChoices.add("Pizza"); 
		}

		public void removeItem(String item) {
			int i = 0; 
			while (foodChoices.get(i) != item) {
				i++; 
			}
			foodChoices.remove(i); 

		}

		public void addItem(String item) {
			foodChoices.add(item); 
		}

	}



}