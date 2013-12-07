package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.Market.gui.MManagerGui;
//import simcity.test.mock.MockMarketCustomer;
import simcity.Transportation.DeliveryTruckAgent;
import simcity.gui.SimCityGui;
//import simcity.Transportation.DeliveryTruckAgent;
import simcity.interfaces.Cook;
import simcity.interfaces.DeliveryTruck;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.Market.gui.MManagerGui;
//import simcity.PersonAgent.EnergyState;
//import simcity.PersonAgent.LocationState;
import agent.Role;


public class MarketManagerRole extends Role implements MarketManager{
	
	public List<MyMarketCashier> cashiers = Collections.synchronizedList(new ArrayList<MyMarketCashier>());
	public List<InventoryBoy> inventoryBoys = Collections.synchronizedList(new ArrayList<InventoryBoy>());
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public DeliveryTruck dTruck;
	public List<MyDeliveryOrder> dOrders = Collections.synchronizedList(new ArrayList<MyDeliveryOrder>());
	
	public double marketMoney;
	public int hour;
	public boolean isClosed;
	public enum workerState{justArrived, available, occupied, out};
	public enum orderState{waiting, done};
	workerState dState;
	public EventLog log;
		
	MManagerGui managerGui;

	private SimCityGui gui;
	
	public MarketManagerRole(SimCityGui gui) {
		super();
		this.gui = gui;
		marketMoney = 50000.0; //***********threshold all the rest deposit to the bank
		log = new EventLog();
		startHour = 10;

	}

	//Messages
	public void msgRestaurantClosed(Role r){
		//cannot deliver order delete customer order
		
		synchronized(dOrders){
			for(MyDeliveryOrder d : dOrders){
				if(d.cook.equals(r)){
					d.restClosed = true;
				}
			}
		}
		
//		synchronized(customers){
//			for(MyCustomer cust : customers){
//				if(cust.c.equals(r)){
//					customers.remove(r);
//				}
//			}
//		}
	}
	
	public void msgRestaurantOpen(Role r){
		
		synchronized(dOrders){
			for(MyDeliveryOrder d : dOrders){
				if(d.cook.equals(r)){
					d.restClosed = false;
				}
			}
		}
		
	}
	
	public void msgHereIsMoney(double money){
		marketMoney += money;
	}
	
	public void msgTimeUpdate(int hour){
		this.hour = hour;
	}
	
	//Customer is visiting market to set an order
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
	
	//Cook is calling in an order
	public void msgIAmHere(Role r, List<MFoodOrder>need, String building, String type, RestaurantCashier cashier){
		LoggedEvent e = new LoggedEvent("Received msgIAmHere.");
		log.add(e);
		
		Do("Cook is calling");
		if(type.equals("cook")) {
			customers.add(new MyCustomer(r, need, building, "cook", cashier, false));
		}
		stateChanged();
	}
	
	public void msgBackFromDelivery(){
		dState = workerState.available;
	}
	
	public void msgCustomerDone(MarketCashier mc, Role r){
		Do("Cashier finished order");
		MyMarketCashier current = find(mc, cashiers);
		current.state = workerState.available;
		MyCustomer cust = find(r, customers);
		customers.remove(cust);
		stateChanged();
	}
	
	public void msgLoadDeliveryTruck(MarketCashier cashier, List<MFoodOrder>deliver, String location, double bill, Cook c){
		Do("Loading delivery truck");
		dState = workerState.occupied;
		
		
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.building == location) {
					dOrders.add(new MyDeliveryOrder(cashier, deliver, location, bill, c ,mc.cashier));
				}
			}
		}
		
		
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
		}
		
		if(cashiers.size() > 1){
			swapCashiers();
			return true;
		}
		
		if(inventoryBoys.size() > 1){
			swapInventoryBoys();
			return true;
		}
		
		if(dState.equals(workerState.occupied)){
			synchronized(dOrders){
				for(MyDeliveryOrder d : dOrders){
					if(d.state.equals(orderState.waiting) && !d.restClosed){
						sendOverTruck(d);
					}
				}
			}
			
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
	
	private void sendOverTruck(MyDeliveryOrder d){
		dState = workerState.out;
		Do("Sending delivery truck over");
		dTruck.msgGoToDestination(d.mc, d.supply, d.destination, d.check, d.cook, d.cashier);
	}
	
	private void closeMarket(){ //pay employees 50
		Do("Closing market. It is "+hour);
		synchronized(cashiers){
			for(MyMarketCashier c: cashiers){
				marketMoney -= 50;
				c.c.msgGoHome(50);
			}
		}
		synchronized(inventoryBoys){
			for(InventoryBoy b: inventoryBoys){
				marketMoney -= 50;
				b.msgGoHome(50);
			}
		}   
		cashiers.clear();
		inventoryBoys.clear();
		isClosed = true;
		managerGui.DoGoHome();
		
		isActive = false;
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;

	}
	
	private void marketClosed(){
//		Do("Telling market is closed");
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
		marketMoney -= 50;
		cashiers.get(0).c.msgGoHome(50);
		cashiers.get(1).state = workerState.available;
		cashiers.remove(0);
	}
	
	private void swapInventoryBoys(){
		Do("Switching out inventory boys");
		marketMoney -= 50;
		inventoryBoys.get(0).msgGoHome(50);
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
			mc.c.msgOrder((Cook)c.c, c.need, c.building,c.cashier);
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

	}
	
	public void setDeliveryTruck(DeliveryTruck d){
		dTruck = d;
	}
	
	public class MyDeliveryOrder{
//		public DeliveryTruck d;
//		public workerState state;
		public MarketCashier mc;
		public List<MFoodOrder>supply;
		public String destination;
		public double check;
		public Cook cook;//restaurant's cook
		public RestaurantCashier cashier;
		public orderState state;
		public boolean restClosed; 
		
		MyDeliveryOrder(MarketCashier mCash, List<MFoodOrder>deliver, String loc, double bill, Cook c,RestaurantCashier rc){
			mc = mCash;
			supply = deliver;
			destination = loc;
			bill = check;
			cook = c;
			cashier = rc;
			state = orderState.waiting;
			restClosed = false;
		}
	}
	
	public class MyCustomer{
		Role c;
		boolean waiting;
		String type;
		List<MFoodOrder>need;
		String building;
		public RestaurantCashier cashier;
//		public boolean restClosed; 
		
		//For the customers who visit the market
		MyCustomer(Role r, String s){
			c = r;
			waiting = true;
			type = s;
			building = null;
			cashier = null;
		
		}
		
		//For the cook who calls in an order
		MyCustomer(Role r, List<MFoodOrder>n, String b, String s, RestaurantCashier cash, boolean closed){
			c = r;
			type = s;
			waiting = true;
			need = n;
			building = b;
			cashier = cash;
//			restClosed = closed;
		}

	}
	public void setGui(MManagerGui g) {
		managerGui = g;
	}

}
