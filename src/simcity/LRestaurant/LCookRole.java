package simcity.LRestaurant;

import agent.Agent;
//import restaurant.gui.HostGui;


import agent.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.LRestaurant.LRestaurantOrder;
import simcity.LRestaurant.ProducerConsumerMonitor;
//import simcity.LRestaurant.LCookRole.Order;
import simcity.LRestaurant.LCustomerRole.AgentEvent;
import simcity.LRestaurant.gui.LCookGui;
import simcity.Market.MFoodOrder;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Cook;
import simcity.interfaces.LCashier;
//import simcity.LRestaurant.interfaces.Market;
import simcity.interfaces.LCook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;

/**
 * Restaurant Cook Role
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LCookRole extends Role implements LCook, Cook {
	
	String name;
	Timer timer = new Timer();
	LCashierRole cashier;
	LHostRole host;
	SimCityGui gui;
	public LCookGui cookGui;
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
	public enum MarketState{noOrder, order, firstOrder, reOrder, waiting, arrived, done};
	public enum OrderState {pending, cooking, cooked};
	private List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	boolean goHome;
	private Semaphore task = new Semaphore(0,true);
	private ProducerConsumerMonitor theMonitor;
	public List<MarketOrder> marketOrders =Collections.synchronizedList( new ArrayList<MarketOrder>()); 
	public List<MarketManager> markets =Collections.synchronizedList( new ArrayList<MarketManager>()); 
	boolean here;
	
	
	public LCookRole(SimCityGui g){
		super();
		//this.name = p.getName();
		foods.put("P", new Food("P", 700, 2, 5, 2)); //choice, cookTime, amount, capacity, threshold
		foods.put("St", new Food("St", 1000, 2, 5, 2));
		foods.put("S", new Food("S", 500, 1, 5, 2));
		foods.put("Ch", new Food("Ch", 900, 2, 5, 2));
		goHome = false;
		here = true;
		gui = g;
	}
	
	public void addMarket(MarketManager m){
		markets.add(m);
	}
	
	public void setCashier(LCashierRole c){
		cashier = c;
	}
	
	public void setSimCityGui(SimCityGui g){
		gui = g;
	}
	
	
	// Messages
	
	
		@Override
		public void msgSetInventory(int val) {
			// TODO Auto-generated method stub
			foods.put("P", new Food("P", 700, val, 5, 2)); //choice, cookTime, amount, capacity, threshold
			foods.put("St", new Food("St", 1000, val, 5, 2));
			foods.put("S", new Food("S", 500, val, 5, 2));
			foods.put("Ch", new Food("Ch", 900, val, 5, 2));
		}
	
		public void msgGoHome(int cash) {
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Told to go home");
			Do("Told to go home");
			myPerson.money += cash;
			goHome = true;
			stateChanged();	
		}
		
		public void msgHereIsAnOrder(int table, String choice,LWaiterNormalRole w) {//from animation
			//print("Received order from " + choice);
			orders.add(new Order(table, choice,w,OrderState.pending));
			stateChanged();
		}
		
		private void msgTimeToCheckStand() {
			stateChanged();
		}

//		public void msgSupplyUnfulfilled(String choice, int stillNeed, MarketAgent market){
//			print("Failed to fulfill order. Need "+stillNeed);
//			//marketOrders.put(choice, false);
//			//foods.get(o.choice).needMarket = true;
	//
//			for(MyMarket m : markets){
//				if(m.market == market){
//					m.supply.put(choice, false);
//				}
//			}
	//
//			foods.get(choice).state = MarketState.reOrder;
//			foods.get(choice).need = stillNeed;
//			stateChanged();
//		}
	//
//		public void msgHereIsFoodSupply(String choice, int a, Market mark){
//			print("Got food from "+ mark);
//			foods.get(choice).amount += a;
	//
//			if(foods.get(choice).amount == foods.get(choice).capacity){
//				foods.get(choice).state = MarketState.noOrder;
//			}
	//
//			stateChanged();
//		}
		
		public void msgMarketCheck(double check){
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Checked market check");
			Do("Checked market check");
		}
		
		public void msgHereIsDelivery(List<MFoodOrder> delivery, double bill, MarketManager manager, MarketCashier cashier) {
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Got delivery from market");
			Do("Got food from "+ manager);
			
			for(MarketOrder m : marketOrders) {
				if( m.m == manager) {
					m.state = MarketState.arrived;
					m.cashier = cashier;
				}
			}
			
			for(MFoodOrder f : delivery) {
				Do("got " + f.amount + " of " + f.type);
				Food currentFood = foods.get(f.type);
				foods.get(f.type).state = MarketState.noOrder;
				currentFood.amount += f.amount;
			}
			
			
//			foods.get(choice).amount += a;
	//
//			if(foods.get(choice).amount == foods.get(choice).capacity){
//				foods.get(choice).state = MarketState.noOrder;
//			}

			stateChanged();
		}
		

		public void msgDone(Order o) {
			o.state = OrderState.cooked;
			stateChanged();
		}
		
		public void msgTask() {
			task.release();
			stateChanged();
		}

	
	@Override
	public boolean pickAndExecuteAnAction() {
		if(here){
			tellHost();
			return true;
		}
		
//		synchronized(foods){
//		for(Food choice : foods.values()){
//			if(choice.state.equals(MarketState.reOrder)){
//				reorderFromMarket(choice.choice, choice.need);
//				return true;
//			}
//		}
//	}
	
	synchronized(marketOrders) {
		for(MarketOrder m : marketOrders) {
			if(m.state == MarketState.arrived) {
				giveCashierCheck(m);
			}
		}
	}

	synchronized(orders){
		for (Order order : orders) {
			if(order.state == OrderState.pending){
				print("THE ORDER: "+order.choice);
				cookOrder(order);
				order.state = OrderState.cooking;
				return true;
			}
		}

		for (Order order : orders) {
			if(order.state == OrderState.cooked){
				FinishAndTellWaiter(order);
				return true;
			}
		}
	}
	
	synchronized(foods){
		for(Food choice : foods.values()){
			if(choice.state.equals(MarketState.order)){
				orderFromMarket(choice.choice, choice.need);
				return true;
			}
		}
	}
	
	if(goHome) {
		goHome();
	}
	else {
		checkRotatingStand();
	}
	

	//we have tried all our rules and found
	//nothing to do. So return false to main loop of abstract agent
	//and wait
	return false;
	}
	
	// Actions
	
	private void tellHost(){

		host.msgIAmHere(this, "cook");
		//host.msgIAmHere(this, "cashier");
		if(cookGui == null){
			cookGui = new LCookGui(this, "LCookGui");
			gui.myPanels.get("Restaurant 1").panel.addGui(cookGui);
		}

		here = false;
	}

		private void goHome() {
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Going home");
			Do("going home");
			isActive = false;
			here = true;
			goHome = false;
			cookGui.DoLeaveRestaurant();		
		}

		private void checkRotatingStand() {
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Checking rotating stand");
			LRestaurantOrder newOrder = theMonitor.remove();
			if(newOrder != null) {
				Order o = new Order(newOrder.table, newOrder.choice,newOrder.w, OrderState.pending);
				orders.add(o);
				msgTimeToCheckStand();
				}
			else{
				timer.schedule(new TimerTask() {
					public void run() {
							msgTimeToCheckStand();
						}
					},
					2000);
			}
		}
		
		private void giveCashierCheck(MarketOrder m) {
//			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Handing market check over to cashier");
//			Do("Handing market check over to cashier");
//			cashier.msgHereIsSupplyCheck(m.check, m.cashier);
//			m.state = MarketState.done;	
		}

		private void orderFromMarket(String choice, int orderAmount){
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Ordering from market");
			
			//inserting food that needs to be ordered
			List<MFoodOrder> needed = new ArrayList<MFoodOrder>();
			needed.add(new MFoodOrder(choice, orderAmount));
			
			//choosing market to order from randomly
			Random rand = new Random();
			int select = rand.nextInt(markets.size());
			MarketManager m = markets.get(select);
			
			//accounting for which market ordering from
			marketOrders.add(new MarketOrder(m));
			
			//Calling order into specific market
			m.msgIAmHere((Role) this, needed, "LRestaurant", "cook", cashier);
			
			
			
//			int orderAmount = foods.get(o.choice).capacity - foods.get(o.choice).amount;
//			foods.get(o.choice).state = MarketState.noOrder;
//			synchronized(markets){
//				for(MyMarket m : markets){
//					if(m.supply.get(o.choice)){
//						print("Ordering " +orderAmount+ " " +o.choice+" from market "+ m.market);
//						m.market.msgNeedFood(orderAmount, o.choice);
//						break;
//					}
//				}
//			}
			
			stateChanged();
		}
		


		private void cookOrder(final Order o) {

			if(foods.get(o.choice).amount == 0){
				print("Ran out of " + o.choice);
				//msg waiter that food is out
				o.w.msgRanOutofFood(o.table, o.choice);
				orders.remove(o);
				//remove order from list of orders
				return;
			}

			foods.get(o.choice).amount--;

			//at a certain amount want to order from market
			if(foods.get(o.choice).amount <= foods.get(o.choice).threshold && foods.get(o.choice).state.equals(MarketState.noOrder)){

				//call an individual action to implement all below
				//foods.get(o.choice).needMarket = true;
//				foods.get(o.choice).state = MarketState.firstOrder;
				
				
				foods.get(o.choice).state = MarketState.order;
				int orderAmount = foods.get(o.choice).capacity - foods.get(o.choice).amount;
				foods.get(o.choice).need = orderAmount;
				orderFromMarket(o.choice,orderAmount);

			}
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Cooking order");
			print("Cooking order");
			
			cookGui.setFood(o.choice);
			cookGui.DoGetFood();
			try {
				task.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cookGui.DoCooking();
			try {
				task.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			timer.schedule(new TimerTask() {
				public void run() {
					o.state = OrderState.cooked;
					stateChanged();
				}

			},
			foods.get(o.choice).cookingTime);
			//stateChanged();

		}

		private void FinishAndTellWaiter(Order o) {
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Plating food");
			print("Plating food");
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCookRole", "Finished order");
			print("Finished order");
			
			cookGui.setPlateFood();
			cookGui.DoPlating();
			try {
				task.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cookGui.DoHome();
			
			o.w.msgOrderIsReady(o.table, o.choice);
			orders.remove(o);
		}

		//utilities

		public void setHost(LHostRole host){
			this.host = host;
		}
		
		public void setGui(LCookGui gui){
			cookGui = gui;
		}
		
		public void setMonitor(ProducerConsumerMonitor m) {
			theMonitor = m;
		}
	private class Food{
		String choice;
		int amount; //how much there is at the moment
		int cookingTime;
		int capacity; //how much the cook can hold
		int threshold; //signals to order from market
		int need; //food supply still needed
		MarketState state;

		Food(String c, int ct, int a, int cap, int thres){
			choice = c;
			cookingTime = ct;
			amount = a;
			capacity = cap;		
			threshold = thres;
//			needMarket = false;
			state = MarketState.noOrder;
			need = 0;

		}
	}
	
	
	private class MarketOrder {
		MarketManager m;
		MarketCashier cashier;
		double check;
		MarketState state;
		
		public MarketOrder(MarketManager m) {
			this.m = m;
			check = 0;
			state = MarketState.waiting;
		}
	}

	public class Order{
		int table;
		String choice;
		OrderState state;
		LWaiterRole w;

		//Additional for V2.1
		//int cookTime = 2;

		Order(int t, String c, LWaiterRole w, OrderState o){
			table = t;
			choice = c;
			this.w = w;
			state = o;

		}

	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}


}
