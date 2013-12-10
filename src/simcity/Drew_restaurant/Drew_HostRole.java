package simcity.Drew_restaurant;

import agent.Role;
import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.BankState;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
//import restaurant.Customer.AgentState;
//import restaurant.gui.WaiterGui;
import simcity.interfaces.*;

import java.util.*;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class Drew_HostRole extends Role implements Drew_Host, Host {//Drew_Host{
	private ProducerConsumerMonitor theMonitor;
	public static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	
	public int count=0;		//Keeps track of total # seated
	public int hour=7;
	
	public boolean restaurantOpen;
	
	public List<Drew_Customer> waitingCustomers
	=Collections.synchronizedList( new ArrayList<Drew_Customer>());
	
	public List<MyWaiter> waiters
	= Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Drew_Cook cook;
	public Drew_Cashier cashier;
	
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	//private Semaphore atTable = new Semaphore(0,true);

	public Drew_HostRole() {
		super();
		// make some tables
		startHour=11;
		restaurantOpen=false;
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			Table t = new Table(ix);			
			tables.add(t);//how you add to a collections
		}
		theMonitor=new ProducerConsumerMonitor();
	}
	
	public void timeUpdate(int hr) {
		hour=hr;
		stateChanged();
	}
	
	public void addWaiter(Drew_Waiter w){
		waiters.add(new MyWaiter(w));
		//stateChanged();
	}
	
	public void msgIAmHere(Role person){
		if(person instanceof Drew_Waiter){
			waiters.add(new MyWaiter((Drew_Waiter) person));
			if(person instanceof Drew_WaiterSharedDataRole){
				((Drew_WaiterSharedDataRole) person).setMonitor(theMonitor);
			}
		}
		else if(person instanceof Drew_Cook){
			cook=(Drew_Cook) person;
			cook.setMonitor(theMonitor);
		}
		else if(person instanceof Drew_Cashier){
			cashier=(Drew_Cashier)person;
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Drew_Customer> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection<Table> getTables() {
		return tables;
	}
	// Messages
	
	public void whatIsWait(Drew_Customer cust) {
		cust.wait(waitingCustomers.size());
		stateChanged();
	}
	
	public void msgIWantFood(Drew_Customer cust) {
		waitingCustomers.add(cust);
		count++;
		cust.wait(waitingCustomers.size()-1);
		Do("Recieved msgiwantfood");
		stateChanged();
	}

	public void tableIsFree(int table){
		synchronized(tables) {
		for(Table t : tables){
			if(t.tableNumber==table) t.setUnoccupied();
		}
		}
		stateChanged();
	}
	
	public void iWantToGoOnBreak(Drew_Waiter wait){
		MyWaiter mw=null;
		synchronized(waiters) {
		for(MyWaiter w : waiters){
			if(w.waiter.equals(wait)) mw=w;
		}
		}
		checkForBreak(mw);
	}
	
	public void backFromBreak(Drew_Waiter wait){
		MyWaiter mw=null;
		synchronized(waiters) {
		for(MyWaiter w : waiters){
			if(w.waiter.equals(wait)) mw=w;
		}
		}
		mw.onBreak=false;
	}
	
	public void leaving(Drew_Customer cust){
		waitingCustomers.remove(cust);
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
		if(hour>startHour && !restaurantOpen){
			openRestaurant();
			return true;
		}		
		if(hour>22 && restaurantOpen){
			closeRestaurant();
			return true;
		}
		if(!restaurantOpen && !waitingCustomers.isEmpty()){
			sendHome();
			return true;
		}
		synchronized(tables) {
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty() & !waiters.isEmpty()) {
					seatCustomer(waitingCustomers.get(0), table);
					return true;//return true to the abstract agent to reinvoke the scheduler.
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

	private void openRestaurant(){
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "Opening Restaurant");
		Do("Opening Restaurant");
		restaurantOpen=true;
	}
	
	private void closeRestaurant(){
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "Closing Restaurant");
		Do("Closing Restaurant");
		restaurantOpen=false;
		for(MyWaiter w:waiters){
			w.waiter.msgGoHome(100.0);
			waiters.remove(w);
		}
		cook.msgGoHome(100.0);
		cook=null;
		cashier.msgGoHome(100.0);
		cashier=null;
	}
	
	private void sendHome(){
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "We're Closed, Go Home");
		Do("We're Closed, Go Home");
		waitingCustomers.get(0).msgGoHome();
	}
	
	private void seatCustomer(Drew_Customer customer, Table table) {
		MyWaiter MW=waiters.get(count%waiters.size());
		while(MW.onBreak){
			count++;
			MW=waiters.get(count%waiters.size());
		}
		DoSeatCustomer(customer, table, MW);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	

	private void DoSeatCustomer(Drew_Customer customer, Table table, MyWaiter MW) {
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "Telling waiter " + MW.waiter.getName() + " to seat " + customer + " at " + table);
		print("Telling waiter " + MW.waiter.getName() + " to seat " + customer + " at " + table);   //ADD WHICH WAITER YOUR TELLING		
		customer.setWaiter(MW.waiter);
		MW.waiter.sitAtTable(customer, table.tableNumber); 
	}
	
	private void checkForBreak(MyWaiter mw){
		int workingWaiters=0;
		synchronized(waiters) {
		for(MyWaiter waiter : waiters){
			if(!waiter.onBreak) workingWaiters++;
		}
		}
		if (workingWaiters<=1){
			mw.waiter.breakResponse(false);
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "You can not go on break right now");
			print("You can not go on break right now");
		}
		else{
			mw.waiter.breakResponse(true);
			mw.onBreak=true;
			AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewHost", "Go on break when you finish with current customers");
			print("Go on break when you finish with current customers");
		}
	}

	//utilities
	
	public void setCook(Drew_Cook cook2){
		cook=cook2;
		cook.setMonitor(theMonitor);
	}

	private class MyWaiter{
		Drew_Waiter waiter;
		boolean onBreak;
		
		MyWaiter(Drew_Waiter w){
			waiter=w;
			onBreak=false;
		}
	}
	
	public class Table {
		Drew_Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Drew_Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Drew_Customer getOccupant() {
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

