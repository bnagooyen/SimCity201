package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
//import simcity.restaurant.interfaces.Cashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
//import simcity.Transportation.CarAgent;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
//import simcity.Transportation.CarAgent;
import simcity.interfaces.Car;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.Market.MarketCashierRole.orderState;
import simcity.Market.gui.MCashierGui;
//import simcity.PersonAgent.EnergyState;
//import simcity.PersonAgent.LocationState;
import agent.Role;


public class MarketCashierRole extends Role implements MarketCashier{

	public List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	
	public EventLog log;
	
	public InventoryBoy ib;
	public MarketManager manager; 
	
	public PersonAgent p;
	public int instance; //paints in correct frame
	
	public enum orderState{pending, inquiring, ready, given, paid, done};
	public enum myState{arrived, working, goHome, unavailable};
	
	public myState state;
	
	private MCashierGui cashierGui;

	private SimCityGui gui;
	
	public MarketCashierRole(SimCityGui gui, int num) {
		super();
		this.gui = gui;
		instance=num;
		//this.p = p;

		log = new EventLog();
		state = myState.arrived;
		startHour = 11;
	}
	
	//Messages
	public void msgOrder(MarketCustomer c, List<MFoodOrder> foods, String building){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Received an order");
		Do("Received an order");
		orders.add(new MOrder(foods, building, c, orderState.pending));
		stateChanged();
	}
	
	public void msgCarOrder(MarketCustomer c, String building){ 
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Received an order");
		Do("Received an order");
		orders.add(new MOrder(building, c, orderState.pending));
		stateChanged();
	}
	
	public void msgOrder(Cook cook, List<MFoodOrder> foods, String building, RestaurantCashier cashier){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Received an order");
		Do("Received an order");
		orders.add(new MOrder(foods, building, cook, orderState.pending, cashier));
		stateChanged();
	}
	
	public void msgCanGive(MOrder o){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Received fulfilled order");
		Do("Received fulfilled order");
		MOrder current = find(o, orders);
		current.state = orderState.ready;
		stateChanged();
	}
	
	public void msgCanGive(Car car, MOrder o){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Received fulfilled order");
		Do("Received fulfilled order");
		MOrder current = find(o, orders);
		current.car = car;
		current.state = orderState.ready;
		stateChanged();
	}
	
	public void msgHereIsPayment(Role r, double payment){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Receiving payment");
		Do("Receiving payment");
		MOrder current = find(r,orders);
		System.out.println("Current: "+current);
		current.state = orderState.paid;
		manager.msgHereIsMoney(payment);
		stateChanged();
	}
	
	public void msgGoHome(double paycheck){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Told to go home");
		Do("Told to go home");
//		myPerson.money += paycheck;
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
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Giving order to inventory boy");
		Do("Giving order to inventory boy.");
		ib.msgCheckInventory(o);
		o.state = orderState.inquiring;
	}
	
	private void giveOrder(MOrder o){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Giving fulfilled order");
		Do("Giving fulfilled order");
		double check = calculateCheck(o);
		o.state = orderState.given;
		Do("o.building is " + o.building);
		if(o.building.equals("")){
			//Customer is at the market
			DoGiveFood();
			o.c.msgHereIsOrderAndCheck(o.canGive, check);
			//msg rest cashier
		}
		else if(o.cook == null){
			//phone order from customer
			o.c.msgHereIsOrder(o.canGive);
//			o.cashier.msgBillFromMarket(check, this, manager);
		}
		else if(o.foodsNeeded == null){
			o.c.msgHereIsCarAndCheck(o.car, check);
		}
		else{
			//phone order from cook
//			o.cashier.msgBillFromMarket(check, this);
			manager.msgLoadDeliveryTruck(this, o.canGive, o.building, check, o.cook);
			//o.cook.msgHereIsDelivery(o.canGive, check, manager, this);
		}
	}
	
	private void goHome(){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Going home");
		Do("Going home");
		state = myState.arrived;
		DoGoHome();
		isActive = false;
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
	}

	private void tellManager(){
		if(cashierGui == null) {
			cashierGui = new MCashierGui(this);
			if(gui != null) {
				gui.myPanels.get("Market "+instance).panel.addGui(cashierGui);
			}
		}
		cashierGui.setPresent(true);
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Telling manager that I can work");
		Do("Telling manager that I can work");
		state = myState.working;
		manager.msgIAmHere(this, "cashier");
	}
	
	private void updateManager(MOrder o){
		AlertLog.getInstance().logInfo(AlertTag.Market, "MarketCashierRole", "Finished order");
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
		synchronized(orders) {
			for(MOrder o: orders){
				if(o.c == r){
					order = o;
				}
				else if(o.cook == r){
					order = o;
				}
				else if (o.cashier == r) {
					order = o;
				}
			}
		}
		return order;
	}
	
	private MOrder find(MOrder m, List<MOrder> orders){
		MOrder order = null;
		synchronized(orders) {
			for(MOrder o: orders){
				if(o.c == m.c){
					order = o;
				}
				else if(o.cook == m.cook){
					order = o;
				}
			}
		}
		return order;
	}
	
//	public class MCarOrder extends MOrder{
//		String building;
//		orderState state;
//		MarketCustomer c;
//		
//		MCarOrder(String b, MarketCustomer cust, orderState s){
//			super(b,cust,s);
//		}
//	}
	
	private double calculateCheck(MOrder o) {
		double cost = 0;
		
		if(o.foodsNeeded == null){
			return 1000;
		}
		
		else{
			for(int i =0; i < o.foodsNeeded.size(); i++){
				cost += (o.foodsNeeded.get(i).price * o.foodsNeeded.get(i).amount);
			}
		}
		
		return cost;
	}
	
	private void DoGiveFood() {
		// TODO Auto-generated method stub
		
	}
	
	private void DoGoHome() {
		if(gui!=null) {
			cashierGui.DoGoHome();
		}
	}
	
	// utilities
	public void setInventoryBoy(InventoryBoy ib) {
		this.ib = ib;
	}
	public void setMarketManager(MarketManager m) {
		this.manager = m;
	}
}




