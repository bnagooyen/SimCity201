package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.restaurant.CashierRole;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import agent.Role;


public class MarketManagerRole extends Role {
	
	List<MyMarketCashier> cashiers = Collections.synchronizedList(new ArrayList<MyMarketCashier>());
	List<InventoryBoy> inventoryBoys = Collections.synchronizedList(new ArrayList<InventoryBoy>());
	List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());

	int hour;
	boolean isClosed;
	enum workerState{justArrived, available, occupied};
	
	protected MarketManagerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	//Messages
	public void msgTimeUpdate(int hour){
		this.hour = hour;
	}
	
	public void msgIAmHere(Role r, String type){
		if(type.equals("cahsier")){
			cashiers.add(new MyMarketCashier(r, workerState.justArrived));
		}
		else if(type.equals("inventory boy")){
			inventoryBoys.add((InventoryBoy) r);
		}
		else{
			customers.add(new MyCustomer(r));
		}
		
		if(!cashiers.isEmpty() && !inventoryBoys.isEmpty()){
			isClosed = false;
		}
		else{
			isClosed = true;
		}
		
		stateChanged();
	}
	
	public void msgCustomerDone(MarketCashier mc, MarketCustomer c){
		MyMarketCashier current = ((MyMarketCashier) cashiers).find(mc);
		current.state = workerState.available;
		MyCustomer cust = ((MyCustomer) customers).find(c);
		customers.remove(cust);
		stateChanged();
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		
		if(hour == 20){
			closeMarket();
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
		
		return false;
	}
	
	//Actions
	private void closeMarket(){
		for(MyMarketCashier c: cashiers){
			c.c.msgGoHome();
		}
		for(InventoryBoy b: inventoryBoys){
			b.msgGoHome();
		}
		cashiers.clear();
		inventoryBoys.clear();
		isClosed = true;
	}
	
	private void marketClosed(){
		for(MyCustomer c: customers){
			c.c.msgMarketClosed();
			customers.remove(c);
		}
	}
	
	private void swapCashiers(){
		cashiers.get(0).c.msgGoHome();
		cashiers.get(1).state = workerState.available;
		cashiers.remove(0);
	}
	
	private void swapInventoryBoys(){
		inventoryBoys.get(0).msgGoHome();
		inventoryBoys.remove(0);
	}
	
	private void handleCustomer(MyCustomer c, MyMarketCashier mc){
		c.waiting = false;
		mc.state = workerState.occupied;
		c.c.msgGoToCashier(mc.c);
	}
	
	//Utilities
	class MyMarketCashier{
		MarketCashierRole c;
		workerState state;
		
		public MyMarketCashier(Role r, workerState w){
			c = (MarketCashierRole) r;
			state = w;
		}

		public MyMarketCashier find(MarketCashierRole mc) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	class MyCustomer{
		MarketCustomer c;
		boolean waiting;
		
		MyCustomer(Role r){
			c = (MarketCustomer) r;
			waiting = true;
		}

	}
	
}
