package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.restaurant.interfaces.Cashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.DRestaurant.DCashierRole;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import agent.Role;


public class MarketCashierRole extends Role implements MarketCashier{

	public List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	
	public boolean active;
	public double marketMoney;
	public EventLog log;
	
	public InventoryBoy ib;
	public MarketManager manager; 
	
	public enum orderState{pending, inquiring, ready, given, paid, done};
	public enum myState{arrived, working, goHome, unavailable};
	
	public myState state;
	
	public MarketCashierRole(PersonAgent p) {
		super(p);
		marketMoney = 0.0;
		log = new EventLog();
		state = myState.arrived;
	}
	
	//Messages
	public void msgOrder(MarketCustomer c, List<MFoodOrder> foods, String building){
		Do("Received an order");
		orders.add(new MOrder(foods, building, c, orderState.pending));
		stateChanged();
	}
	
	public void msgOrder(Cook cook, List<MFoodOrder> foods, String building){
		Do("Received an order");
		orders.add(new MOrder(foods, building, cook, orderState.pending));
		stateChanged();
	}
	
	public void msgCanGive(MOrder o){
		Do("Received fulfilled order");
		MOrder current = find(o, orders);
		current.state = orderState.ready;
		stateChanged();
	}
	
	public void msgHereIsPayment(Role r, double payment){
		Do("Receiving payment");
		MOrder current = find(r,orders);
		System.out.println("Current: "+current);
		current.state = orderState.paid;
		marketMoney += payment;
		stateChanged();
	}
	
	public void msgGoHome(){
		Do("Told to go home");
		state = myState.goHome;
		stateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
		if(state == myState.goHome){
			goHome();
			return true;
		}
		
		if(state == myState.arrived){
			tellManager();
			return true;
		}
		
		synchronized(orders){
			for(MOrder o: orders){
				if(o.state == orderState.ready){
					giveOrder(o);
					return true;
				}
			}
		}
		
		synchronized(orders){
			for(MOrder o: orders){
				if(o.state == orderState.pending){
					tryToFulFillOrder(o);
					return true;
				}
			}
		}
		
		synchronized(orders){
			for(MOrder o: orders){
				if(o.state == orderState.paid){
					updateManager(o);
					return true;
				}
			}
		}
		
		return false;
	}
	
	//Actions
	private void tryToFulFillOrder(MOrder o){
		Do("Giving order to inventory boy.");
		ib.msgCheckInventory(o);
		o.state = orderState.inquiring;
	}
	
	private void giveOrder(MOrder o){
		Do("Giving fulfilled order");
		double check = calculateCheck(o);
		
		if(o.building.equals("")){
			DoGiveFood();
			o.c.msgHereIsOrderAndCheck(o.canGive, check);
		}
		else if(o.cook == null){
			DoDeliverFood();
			o.c.msgHereIsOrderAndCheck(o.canGive, check);
		}
		else{
//			o.cashier.msgBillFromMarket(check, this);
			DoDeliverFood();
			o.cook.msgHereIsDelivery(o.canGive, check, manager, this);
		}
	}
	
	private void goHome(){
		Do("Going home");
		state = myState.unavailable;
		DoGoHome();
		active = false;
	}

	private void tellManager(){
		Do("Telling manager that I can work");
		state = myState.working;
		manager.msgIAmHere(this, "cashier");
	}
	
	private void updateManager(MOrder o){
		Do("Finished order");
		if(o.cook == null) {
			manager.msgCustomerDone(this, (Role) o.c);
		}
		else {
			manager.msgCustomerDone(this, (Role) o.cook);
		}
		o.state = orderState.done;
	
	}

	//Utilities
	
	private MOrder find(Role r, List<MOrder> orders){
		MOrder order = null;
		for(MOrder o: orders){
			if(o.c == r){
				order = o;
			}
			else if(o.cook == r){
				order = o;
			}
		}
		
		return order;
	}
	
	private MOrder find(MOrder m, List<MOrder> orders){
		MOrder order = null;
		for(MOrder o: orders){
			if(o.c == m.c){
				order = o;
			}
			else if(o.cook == m.cook){
				order = o;
			}
		}
		
		return order;
	}
	private double calculateCheck(MOrder o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void DoGiveFood() {
		// TODO Auto-generated method stub
		
	}
	
	private void DoDeliverFood() {
		// TODO Auto-generated method stub
		
	}
	
	private void DoGoHome() {
		// TODO Auto-generated method stub
		
	}
	
	// utilities
	public void setInventoryBoy(InventoryBoy ib) {
		this.ib = ib;
	}
	public void setMarketManager(MarketManager m) {
		this.manager = m;
	}
}




