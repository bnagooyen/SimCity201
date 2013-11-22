package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.test.mock.MockMarketCustomer;
import simcity.DRestaurant.DCashierRole;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import agent.Role;


public class MarketManagerRole extends Role implements MarketManager{
	
	public List<MyMarketCashier> cashiers = Collections.synchronizedList(new ArrayList<MyMarketCashier>());
	public List<InventoryBoy> inventoryBoys = Collections.synchronizedList(new ArrayList<InventoryBoy>());
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());

	public int hour;
	public boolean isClosed;
	public enum workerState{justArrived, available, occupied};
	public EventLog log;
	
	public MarketManagerRole(PersonAgent p) {
		super(p);
		log = new EventLog();
	}

	//Messages
	public void msgTimeUpdate(int hour){
		this.hour = hour;
	}
	
	public void msgIAmHere(Role r, String type){
		LoggedEvent e = new LoggedEvent("Received msgIAmHere.");
		log.add(e);
		
		if(type.equals("cashier")){
			Do("Cashier is here");
			cashiers.add(new MyMarketCashier(r, workerState.available));
		}
		else if(type.equals("inventory boy")){
			Do("Inventory boy is here");
			inventoryBoys.add((InventoryBoy) r);
		}
		else if(type.equals("customer")){
			Do("Customer is here");
			customers.add(new MyCustomer(r, "customer"));
		}
		else if(type.equals("cook")) {
			Do("Cook is here");
			customers.add(new MyCustomer(r, "cook"));
		}
		
		if(!cashiers.isEmpty() && !inventoryBoys.isEmpty()){
			isClosed = false;
		}
		else{
			isClosed = true;
		}
		
		stateChanged();
	}
	
	public void msgIAmHere(Role r, List<MFoodOrder>need, String building, String type){
		Do("Cook is here");
		if(type.equals("cook")) {
			customers.add(new MyCustomer(r, need, building, "cook"));
		}
		stateChanged();
	}
	
	public void msgCustomerDone(MarketCashier mc, Role r){
		Do("Cashier finished order");
		MyMarketCashier current = find(mc, cashiers);
		current.state = workerState.available;
		MyCustomer cust = find(r, customers);
		customers.remove(cust);
		stateChanged();
	}


	//Scheduler

	public boolean pickAndExecuteAnAction() {
		
		if(hour == 20 && !isClosed){
			closeMarket();
			return true;
		}
		
		if(isClosed){
			marketClosed();
			return true;
		}
		
		if(cashiers.size() > 1){
			swapCashiers();
			return true;
		}
		
		if(inventoryBoys.size() > 1){
			swapInventoryBoys();
			return true;
		}
		
		synchronized(customers){
			for(MyCustomer c: customers){
				if(c.waiting == true){
					for(MyMarketCashier mc: cashiers){
						if(mc.state == workerState.available){
							handleCustomer(c,mc);
						}
					}
				}
				return true;
			}
		}
		
		return false;
	}
	
	//Actions
	private void closeMarket(){
		Do("Closing market");
		synchronized(cashiers){
			for(MyMarketCashier c: cashiers){
				c.c.msgGoHome();
			}
		}
		synchronized(inventoryBoys){
			for(InventoryBoy b: inventoryBoys){
				b.msgGoHome();
			}
		}   
		cashiers.clear();
		inventoryBoys.clear();
		isClosed = true;
	}
	
	private void marketClosed(){
		Do("Telling market is closed");
		synchronized(customers){
			for(MyCustomer c: customers){
				if(c.type.equals("customer")) {
					((MarketCustomer) c.c).msgMarketClosed();
				}
				else {
					((Cook) c.c).msgMarketClosed();
				}
			}
			customers.clear();
		}
	}
	
	private void swapCashiers(){
		Do("Switching out cashiers");
		cashiers.get(0).c.msgGoHome();
		cashiers.get(1).state = workerState.available;
		cashiers.remove(0);
	}
	
	private void swapInventoryBoys(){
		Do("Switching out inventory boys");
		inventoryBoys.get(0).msgGoHome();
		inventoryBoys.remove(0);
	}
	
	private void handleCustomer(MyCustomer c, MyMarketCashier mc){
		Do("Assigning order to cashier");
		c.waiting = false;
		mc.state = workerState.occupied;
		if( c.type.equals("customer")) {
			((MarketCustomer) c.c).msgGoToCashier((MarketCashier) mc.c);
		}
		else { // type must be cook
			mc.c.msgOrder((Cook)c.c, c.need, c.building);;
//			((Cook) c.c).msgGoToCashier((MarketCashier) mc.c);

		}
	}
	
	//Utilities
	private MyCustomer find(Role r, List<MyCustomer> custs) {
		MyCustomer c = null;
		synchronized(custs){
			for(MyCustomer cust : custs) {
				if(cust.c == r) {
					c = cust;
				}
			}
		}
		return c;
	}
	
	private MyMarketCashier find(MarketCashier m, List<MyMarketCashier> cash){
		MyMarketCashier mc = null;
		synchronized(cash){
			for(MyMarketCashier cashier: cash){
				if(cashier.c == m){
					mc = cashier;
				}
			}
		}
		return mc;
	}
	
	public class MyMarketCashier{
		MarketCashier c;
		public workerState state;
		
		public MyMarketCashier(Role r, workerState w){
			c = (MarketCashier) r;
			state = w;
		}

//		public MyMarketCashier find(MarketCashier mc) {
//			// TODO Auto-generated method stub
//			return null;
//		}
	}
	
	public class MyCustomer{
		Role c;
		boolean waiting;
		String type;
		List<MFoodOrder>need;
		String building;
		
		MyCustomer(Role r, String s){
			c = r;
			type = s;
			waiting = true;
		}
		
		MyCustomer(Role r, List<MFoodOrder>n, String b, String s){
			c = r;
			type = s;
			waiting = true;
			need = n;
			building = b;
		}

	}

}
