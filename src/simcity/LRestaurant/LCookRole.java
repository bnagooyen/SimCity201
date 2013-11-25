package simcity.LRestaurant;

import agent.Agent;
//import restaurant.gui.HostGui;


import agent.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.LRestaurant.LRestaurantOrder;
import simcity.LRestaurant.ProducerConsumerMonitor;
import simcity.LRestaurant.LCookRole.Order;
import simcity.LRestaurant.LCustomerRole.AgentEvent;
import simcity.LRestaurant.gui.LCookGui;
import simcity.Market.MFoodOrder;
import simcity.interfaces.LCashier;
//import simcity.LRestaurant.interfaces.Market;
import simcity.interfaces.LCook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;

/**
 * Restaurant Cook Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LCookRole extends Role implements LCook {
	Timer timer = new Timer();
	String name;
	LCashier cashier;
	private ProducerConsumerMonitor theMonitor;
	//private List<MarketAgent> markets = new ArrayList<MarketAgent>();
//	private List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	private List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
//	private Map<String, Boolean> marketOrders = new HashMap<String, Boolean>(); //integer holds the amount reordered
	public enum OrderState {pending, cooking, cooked};
	public enum MarketState{noOrder, order, firstOrder, reOrder, waiting, arrived, done};
	public LCookGui cookGui;
	public List<MarketManager> markets =Collections.synchronizedList( new ArrayList<MarketManager>()); 
	public List<MarketOrder> marketOrders =Collections.synchronizedList( new ArrayList<MarketOrder>()); 
	private Semaphore task = new Semaphore(0,true);

	public LCookRole(PersonAgent p){
		super(p);
		this.name = p.getName();
		foods.put("P", new Food("P", 700, 2, 5, 2)); //choice, cookTime, amount, capacity, threshold
		foods.put("St", new Food("St", 1000, 2, 5, 2));
		foods.put("S", new Food("S", 500, 1, 5, 2));
		foods.put("Ch", new Food("Ch", 900, 2, 5, 2));
		}

//	public void addMarket(LMarket m){
//		markets.add(new MyMarket(m));
//	}


	// Messages

	public void msgHereIsAnOrder(int table, String choice, LWaiterRole w) {//from animation
		//print("Received order from " + choice);
		orders.add(new Order(table, choice,w,OrderState.pending));
		stateChanged();
	}
	
	private void msgTimeToCheckStand() {
		stateChanged();
	}

//	public void msgSupplyUnfulfilled(String choice, int stillNeed, MarketAgent market){
//		print("Failed to fulfill order. Need "+stillNeed);
//		//marketOrders.put(choice, false);
//		//foods.get(o.choice).needMarket = true;
//
//		for(MyMarket m : markets){
//			if(m.market == market){
//				m.supply.put(choice, false);
//			}
//		}
//
//		foods.get(choice).state = MarketState.reOrder;
//		foods.get(choice).need = stillNeed;
//		stateChanged();
//	}
//
//	public void msgHereIsFoodSupply(String choice, int a, Market mark){
//		print("Got food from "+ mark);
//		foods.get(choice).amount += a;
//
//		if(foods.get(choice).amount == foods.get(choice).capacity){
//			foods.get(choice).state = MarketState.noOrder;
//		}
//
//		stateChanged();
//	}
	
	public void msgHereIsDelivery(List<MFoodOrder> canGiveMe, double bill, MarketManager manager, MarketCashier cashier) {
		print("Got food from "+ manager);
		
		for(MarketOrder m : marketOrders) {
			if( m.m == manager) {
				m.state = MarketState.arrived;
				m.cashier = cashier;
			}
		}
		
		for(MFoodOrder f : canGiveMe) {
			Do("got " + f.amount + " of " + f.type);
			Food currentFood = foods.get(f.type);
			foods.get(f.type).state = MarketState.noOrder;
			currentFood.amount += f.amount;
		}
		
		
//		foods.get(choice).amount += a;
//
//		if(foods.get(choice).amount == foods.get(choice).capacity){
//			foods.get(choice).state = MarketState.noOrder;
//		}

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

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
//		synchronized(foods){
//			for(Food choice : foods.values()){
//				if(choice.state.equals(MarketState.reOrder)){
//					reorderFromMarket(choice.choice, choice.need);
//					return true;
//				}
//			}
//		}
		
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
		
			checkRotatingStand();
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions

	private void checkRotatingStand() {
		LRestaurantOrder newOrder = theMonitor.remove();
		if(newOrder != null) {
			Order o = new Order(newOrder.table, newOrder.choice,newOrder.w, OrderState.pending);
			orders.add(o);
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
		Do("Handing market check over to cashier");
		cashier.msgHereIsSupplyCheck(m.check, m.cashier);
		m.state = MarketState.done;	
	}

	private void orderFromMarket(String choice, int orderAmount){
//		int orderAmount = foods.get(o.choice).capacity - foods.get(o.choice).amount;
//		foods.get(o.choice).state = MarketState.noOrder;

		List<MFoodOrder> needed = new ArrayList<MFoodOrder>();
		needed.add(new MFoodOrder(choice, orderAmount));
		Random rand = new Random();
		int select = rand.nextInt(markets.size());
		MarketManager m = markets.get(select);
		marketOrders.add(new MarketOrder(m));
		m.msgIAmHere((Role) this, needed, "LRestaurant", "cook");
		
//		synchronized(markets){
//			for(MyMarket m : markets){
//				if(m.supply.get(o.choice)){
//					print("Ordering " +orderAmount+ " " +o.choice+" from market "+ m.market);
//					m.market.msgNeedFood(orderAmount, o.choice);
//					break;
//				}
//			}
//		}
		
		stateChanged();
	}
//
//	private void reorderFromMarket(String choice, int need){
//		int orderAmount = need;
//		foods.get(choice).state = MarketState.noOrder;
//
//		synchronized(markets){	
//			for(MyMarket m : markets){
//				if(m.supply.get(choice)){
//					print("Reordering " +need+ " " +choice+" from market "+ m.market);
//					m.market.msgNeedFood(orderAmount, choice);
//					break;
//				}
//			}
//		}
//		
//		stateChanged();
//	}
	


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
//			foods.get(o.choice).state = MarketState.firstOrder;
			foods.get(o.choice).state = MarketState.order;
			int orderAmount = foods.get(o.choice).capacity - foods.get(o.choice).amount;
			foods.get(o.choice).need = orderAmount;
			orderFromMarket(o.choice,orderAmount);

		}
		print("Cooking order");
		/**
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
		*/
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
		print("Plating food");
		print("Finished order.");
		/*
		cookGui.setPlateFood();
		cookGui.DoPlating();
		try {
			task.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.DoHome();
		*/
		o.w.msgOrderIsReady(o.table, o.choice);
		orders.remove(o);
	}

	//utilities

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
//			state = MarketState.noOrder;
			need = 0;

		}
	}

//	public class MyMarket{
//		Map<String, Boolean> supply = Collections.synchronizedMap(new HashMap<String, Boolean>()); //true when in stock
//		LMarket market;
//
//		MyMarket(LMarket m){
//			supply.put("Ch", true);
//			supply.put("S", true);
//			supply.put("St", true);
//			supply.put("P", true);
//			market = m;
//		}
//	}
	
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

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
