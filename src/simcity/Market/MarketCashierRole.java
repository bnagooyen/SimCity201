package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.restaurant.CashierRole;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import agent.Role;


public class MarketCashierRole extends Role implements MarketCashier{

	public List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	
	boolean active;
	public double marketMoney;
	public EventLog log;
	
	public InventoryBoy ib;
	public MarketManager manager; 
	
	public enum orderState{pending, inquiring, ready, given, paid, done};
	enum myState{arrived, working, goHome, unavailable};
	
	myState state;
	
	public MarketCashierRole(PersonAgent p) {
		super(p);
		marketMoney = 0.0;
		log = new EventLog();
	}
	
	//Messages
	public void msgOrder(MarketCustomer c, List<MFoodOrder> foods, String building){
		orders.add(new MOrder(foods, building, c, orderState.pending));
		stateChanged();
	}
	
	public void msgOrder(Cook cook, List<MFoodOrder> foods, String building, RestaurantCashier c){
		orders.add(new MOrder(foods, building, cook, c, orderState.pending));
		stateChanged();
	}
	
	public void msgCanGive(MOrder o){
		MOrder current = find(o, orders);
		current.state = orderState.ready;
		LoggedEvent e = new LoggedEvent("Received msgCanGive from inventory boy.");
		log.add(e);
//		if(orders.get(0).state.equals(orderState.ready)){
//			Do("HERE");
//		}
		stateChanged();
	}
	
	public void msgHereIsPayment(Role r, double payment){
		MOrder current = find(r,orders);
		current.state = orderState.paid;
		marketMoney += payment;
		stateChanged();
	}
	
	public void msgGoHome(){
		state = myState.goHome;
		stateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
		synchronized(orders){
			for(MOrder o: orders){
				Do("State: "+o.state);
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
		
		if(state == myState.goHome){
			goHome();
			return true;
		}
		
		if(state == myState.arrived){
			tellManager();
			return true;
		}
		
		return false;
	}
	
	//Actions
	private void tryToFulFillOrder(MOrder o){
		ib.msgCheckInventory(o);
		o.state = orderState.inquiring;
	}
	
	private void giveOrder(MOrder o){
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
			o.cashier.msgBillFromMarket(check, this);
			DoDeliverFood();
			o.cook.msgHereIsDelivery(o.canGive);
		}
	}
	
	private void goHome(){
		state = myState.unavailable;
		DoGoHome();
		active = false;
	}

	private void tellManager(){
		state = myState.working;
		manager.msgIAmHere(this, "cashier");
	}
	
	private void updateManager(MOrder o){
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
}




