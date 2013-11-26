package simcity.Drew_restaurant;

import agent.Role;
import simcity.PersonAgent;
import simcity.Drew_restaurant.gui.WaiterGui;
import simcity.Drew_restaurant.gui.Menu;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.Drew_restaurant.interfaces.*;

/**
 * Restaurant Waiter Agent
 */

public class Drew_WaiterRole extends Role implements Drew_Waiter{
	
	
	//Data
	public List<MyCustomer> customers
	= new ArrayList<MyCustomer>();
	
	private String name;
	private Semaphore atDest = new Semaphore(-1,true);
	private Drew_Host host;
	private Drew_Cook cook;
	private Drew_Cashier cashier; 
	
	public enum CustomerState 
	{waiting, seated,readyToOrder,askedToOrder, ordered, waitingForFood, foodReady, eating, billReady,paying, done};

	public WaiterGui waitergui=null;
	
	public void addCashier(Drew_Cashier c){
		cashier=c;
	}
	
	public void setHost(Drew_Host host) {
		this.host = host;
	}
	
	public void setCook(Drew_Cook cook) {
		this.cook = cook;
	}
	
	public Drew_WaiterRole() {
		//super(p);
		//this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<MyCustomer> getWaitingCustomers() {
		return customers;
	}

	// Messages
	
	//Host to waiter to seat customer
	public void sitAtTable(Drew_Customer c, int table){
		MyCustomer mc = new MyCustomer(c,table);
		customers.add(mc);
		print("Calling sitAtTable");
		stateChanged();
	}
	
	//From customer when customer is ready to order
	public void readyToOrder(Drew_Customer c){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.getCustomer() == c) {
				mc=customer;
				//stateChanged();
			}
		}
		mc.s=CustomerState.readyToOrder;
		stateChanged();
	}
	
	public void heresMyChoice(Drew_Customer c, String ch){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.getCustomer() == c) {
				mc=customer;
				//stateChanged();
			}
		}
		if(ch=="leaving"){
			mc.s=CustomerState.seated;
		}
		else{
			mc.s=CustomerState.ordered;
			mc.choice = ch;
		}
		stateChanged();
	}
	
	public void orderDone(int table, String choice){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.t == table) {
				mc=customer;
			}
		}
		mc.s=CustomerState.foodReady;
		stateChanged();
	}
	
	public void DoneEating(Drew_Customer c){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.getCustomer() == c) {
				mc=customer;
			}
		}
		mc.s=CustomerState.done;
		//waitergui.clearTable(mc.t);
		stateChanged();
	}  
	
	//Called if the cooks out of ordered food
	public void outOf(String choice, int Table){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.t == Table) {
				mc=customer;
			}
		}
		tellCustomerOutOfFood(mc, choice);
	}

	public void msgAtDest() {//from animation
		//print("msgAtTable() called");
		atDest.release();
		stateChanged();
	}

	
	public void breakResponse(boolean Response){
		waitergui.setOnBreak(Response);
		waitergui.setWantsBreak(false);
	}

	public void heresBill(Double bill, int table){
		MyCustomer mc = null;
		for (MyCustomer customer : customers) {
			if (customer.t == table) {
				mc=customer;
			}
		}
		mc.b=bill;
		mc.s=CustomerState.billReady;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		waitergui.idle=false;
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.done){
					closeOutTable(customer.getTable(), customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.askedToOrder) {
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.foodReady) {
					deliverFood(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.billReady) {
					giveCheck(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.eating && !customer.hasCheck) {
					getCheck(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.waiting) {
					seatCustomer(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.readyToOrder) {
					takeOrder(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		try{
		//synchronized(customers){
			for (MyCustomer customer : customers) {
				if(customer.getState()==CustomerState.ordered) {
					putInOrder(customer);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e) {
			return false;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void seatCustomer(MyCustomer customer){
		if(customer.c.getName().equals("Break")){
			print("May I go on break?");
			host.iWantToGoOnBreak(this);
		}
		
		print("Seating " + customer.c + " at " + customer.getTable());
		//waitergui.getCustomer();
		finishTask();
		waitergui.setGui(customer.c.getGui());	//Tells WaiterGui which customerGui it needs
		//waitergui.DoBringToTable(customer.getTable());	//WaiterGui sends Location to CustomerGui
		customer.getCustomer().followMeToTable(new Menu()); 
		finishTask();		//Utilizes AtDest semaphore to allow waiter to complete task
		//customer.getCustomer().followMeToTable(new Menu()); 
		customer.s=CustomerState.seated;
	} 
	
	private void tellCustomerOutOfFood(MyCustomer mc, String choice){
		//waitergui.goToTable(mc.t);
		finishTask();
		mc.s=CustomerState.seated;
		mc.c.outOfChoice(choice);
		print("OUT OF "+choice+" Customer" + mc.c);
		stateChanged();
	}
	
	private void takeOrder(MyCustomer customer){
		//waitergui.goToTable(customer.getTable());
		finishTask();
		customer.c.whatWouldYouLike();
		customer.s=CustomerState.askedToOrder;
	}
	
	private void putInOrder(MyCustomer c){
		//waitergui.pickUpOrder();
		//waitergui.goToKitchen();
		finishTask();
		//waitergui.dropOff();
		cook.hereIsOrder(this, c.choice, c.t);
		c.s=CustomerState.waitingForFood;
		stateChanged();
	}
	
	private void deliverFood(MyCustomer c){
		//waitergui.goToKitchen();
		finishTask();
		cook.getGui().onWindow--;
		//waitergui.pickUpFood(c.choice);
		//waitergui.goToTable(c.t);
		finishTask();
		waitergui.dropOffFood(c.t, c.choice);
		c.c.hereIsYourFood();
		c.s=CustomerState.eating;
	}
	
	private void closeOutTable(int tablenumber, MyCustomer customer){
		host.tableIsFree(tablenumber);
		customers.remove(customer);
		if(customers.isEmpty()){
			//waitergui.goHome();
			finishTask();
		}
	}

	public void askForBreak(){
		print("May I go on break?");
		host.iWantToGoOnBreak(this);
	}
	
	public void doneWithBreak(){
		print("I'm back at work!");
		host.backFromBreak(this);
	}
	
	private void getCheck(MyCustomer c){
		waitergui.goToCashier();
		finishTask();
		cashier.calculateBill(this, c.choice, c.t, c.c.getDebt());
		print("Told Cashier to Calculate Bill");
		c.hasCheck=true;
		//waitergui.pickUpCheck();
	}
	
	private void giveCheck(MyCustomer c){
		waitergui.goToTable(c.t);
		finishTask();
		c.c.giveCheck(c.b, cashier);
		print("Customer Has been given the Bill "+ c.b);
		c.s=CustomerState.paying;
		//waitergui.dropOff();
	}

	//utilities

	private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setGui(WaiterGui gui) {					
		waitergui = gui;
	}

	public WaiterGui getGui() {
		return waitergui;
	}
	
	public class MyCustomer {
		Drew_Customer c;
		int t;
		String choice;
		CustomerState s;
		Double b;
		boolean hasCheck=false;
		
		MyCustomer(Drew_Customer customer, int table){
			c=customer;
			t=table;
			s=CustomerState.waiting;
		}
		
		
		Drew_Customer getCustomer(){
			return c;
		}
		CustomerState getState(){
			return s;
		}
		int getTable(){
			return t;
		}
	}
}


