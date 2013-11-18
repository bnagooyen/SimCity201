package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.restaurant.CashierRole;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import agent.Role;


public class MarketCashierRole extends Role implements MarketCashier{

	List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	
	boolean active;
	double marketMoney;
	
	InventoryBoy ib;
	MarketManager manager; 
	
	public enum orderState{pending, inquiring, ready, given, paid, done};
	enum myState{arrived, working, goHome, unavailable};
	
	myState state;
	
	public MarketCashierRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
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
		MOrder current = ((MOrder) orders).find(o);
		current.state = orderState.ready;
		stateChanged();
	}
	
	public void msgHereIsPayment(Role r, double payment){
		MOrder current = ((MOrder) orders).find(r);
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
		
		for(MOrder o: orders){
			if(o.state == orderState.ready){
				giveOrder(o);
				return true;
			}
		}
		
		for(MOrder o: orders){
			if(o.state == orderState.pending){
				tryToFulFillOrder(o);
				return true;
			}
		}
		
		for(MOrder o: orders){
			if(o.state == orderState.paid){
				updateManager(o);
				return true;
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




