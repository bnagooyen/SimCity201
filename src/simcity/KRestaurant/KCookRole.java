package simcity.KRestaurant;

import agent.*;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KCookGui;
import simcity.KRestaurant.gui.KMovingFoodGui;
import simcity.KRestaurant.gui.KRestaurantGui;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.Market.MFoodOrder;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCook;
//import simcity.interfaces.Market;


import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Cook Agent
 */

public class KCookRole extends Role implements KCook{
	
	private ProducerConsumerMonitor theMonitor;
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public Map<String, Food > foods = Collections.synchronizedMap( new HashMap<String, Food>()); 
	public List<MarketManager> markets =Collections.synchronizedList( new ArrayList<MarketManager>());
	public List<Integer> grills = Collections.synchronizedList( new ArrayList<Integer>());
	public List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
//	public MarketAgent currentMarket;
	public KCashier cashier;
	
	private KCookGui cookGui;
	private String name;
	
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	private boolean needToOrder;
	private KRestaurantGui gui;

	private Timer timer = new Timer(); 
		
	public enum orderState 
	{ pending, cooking, done, finished }
	
	public enum marketOrderState 
	{ waiting, arrived, done}
	
	public KCookRole(PersonAgent p) {
		super(p);
		
		foods.put("Steak", new Food("Steak", 5000, 3, 3, 1,10));
		foods.put("Chicken", new Food("Chicken", 4500, 2, 3,1,7));
		foods.put("Salad", new Food("Salad", 2000, 3, 3, 1, 3));
		foods.put("Pizza", new Food("Pizza", 4000, 1, 3, 1, 5));
		
		for(int i = 0; i<4; i++) {
			grills.add(0);
		}
		
		needToOrder = true;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void msgHereIsAnOrder(KWaiterRole w, String choice, int table) {
		Do("got order from waiter");
		orders.add(new Order(w, choice, table, orderState.pending));
		stateChanged();
	}

	// from normal waiter
	public void msgCanGive(List<MFoodOrder> canGiveMe) {
		for(MFoodOrder f : canGiveMe) {
			Do("Told they can give me " + f.amount + " of " + f.type);
			Food currentFood = foods.get(f.type);
			currentFood.stillNeed -= f.amount;
			Do("amount still need of " + f.type + " is " + foods.get(f.type).stillNeed);
			
			if(currentFood.stillNeed > 0) {
				needToOrder = true;
			}
		}
		stateChanged();
	}
	
	public void msgHereIsDelivery(List<MFoodOrder> canGiveMe, double bill, MarketManager manager, MarketCashier cashier) {
		for(MarketOrder m : marketOrders) {
			if( m.m == manager) {
				m.state = marketOrderState.arrived;
				m.cashier = cashier;
			}
		}
		
		for(MFoodOrder f : canGiveMe) {
			Do("got " + f.amount + " of " + f.type);
			Food currentFood = foods.get(f.type);
			currentFood.amount += f.amount;
		}
		stateChanged();
	}
	private void msgTimeToCheckStand() {
		stateChanged();
	}
	public void msgAtGrill() {
		atGrill.release();
	}
	
	public void msgAtFridge() {
		atFridge.release();
	}
	
	public void msgAtPlating() {
		atPlating.release();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(marketOrders) {
			for(MarketOrder m : marketOrders) {
				if(m.state == marketOrderState.arrived) {
					giveCashierCheck(m);
				}
			}
		}
		
		synchronized(orders) {
			for( Order o : orders ) {
				if ( o.s == orderState.done) {
					plateIt(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for( Order o : orders ) {
				if (o.s == orderState.pending) {
					Do("gonna cook order now");
					cookIt(o);
					return true;
				}
			}
		}
		if(needToOrder) {
			synchronized(foods) {
				for(Map.Entry<String, Food> f : foods.entrySet()) {
					Food food = f.getValue();
					if ( food.stillNeed > 0) {
						orderFoodThatIsLow();
						return true;
					}
				}
			}
		}
		else {
			checkRotatingStand();
		}
		return false;
	}

	
	// Actions

	private void giveCashierCheck(MarketOrder m) {
		Do("giving check to cashier");
		cashier.msgBillFromMarket(m.check, m.cashier);
		m.state = marketOrderState.done;
	}
	
	private void checkRotatingStand() {
		KRestaurantOrder newOrder = theMonitor.remove();
		if(newOrder != null) {
			Order o = new Order(newOrder.w, newOrder.choice, newOrder.table, orderState.pending);
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

	public void cookIt( final Order o) {
		Food food = foods.get(o.choice);
		if( food.amount == 0) {
			o.w.msgOutOf(o.table, o.choice);
			Do("sent msg to waiter that out of " + food.type);
			orders.remove(o);
			return;
		}
		o.s = orderState.cooking;
		DoCooking(o); 
		timer.schedule(new TimerTask() {
			public void run() {
					print("Done cooking");
					o.s = orderState.done;
					grills.set(o.grill, 0);
					stateChanged();
				}
			},
			food.cookingTime);
		food.amount--;
		if ( food.amount == food.low) {
			Do("need to order food");
			food.stillNeed = food.capacity - food.low;
			orderFoodThatIsLow();
		}
		
		Do(food.type + " = " + food.amount);
	}
	
	public void orderFoodThatIsLow() {
		needToOrder = false;
		
		List<MFoodOrder> lowFoods = new ArrayList<MFoodOrder>();
		
		synchronized(foods) {
			for (Map.Entry<String, Food> food : foods.entrySet()) {
				Food f = food.getValue();
				if(f.stillNeed > 0) {
					lowFoods.add(new MFoodOrder(f.type, f.stillNeed));
					
				}
			}
		}
		Do("ordering from a market");
		MarketManager m = pickAMarket();
		marketOrders.add(new MarketOrder(m));
		m.msgIAmHere((Role) this, lowFoods, "KRestaurant", "cook");
	}
	
	public MarketManager pickAMarket() {
		Do("picking a market to order from");
		Random rand = new Random();
		int choice = rand.nextInt(markets.size());
		return markets.get(choice);
	}
	
	public void DoCooking(Order o) {
		for(int i = 0; i<4;i++) {
			if(grills.get(i) == 0) {
				grills.set(i, 1);
				o.grill = i;
				break;
			}
		}
		Do("going to fridge to acquire materials");
		cookGui.DoGoToFridge();
		try{
			atFridge.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("going to grill " +o.grill);
		o.gui = new KMovingFoodGui(this, o.w, gui, (o.grill *20) + 5, o.choice);
		gui.animationPanel.addGui(o.gui);
		cookGui.DoCookFood(o.grill);
		try{
			atGrill.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoGoHome();
	}
	
	public void plateIt( Order o ) {
		DoPlating(o);
		o.w.msgOrderIsReady(o.choice, o.table, o.gui, o.grill);
		cookGui.DoGoHome();
		o.s = orderState.finished;
	}

	public void DoPlating( Order o ) {
		Do("plating order "+o.choice);
		cookGui.DoCookFood(o.grill);
		try{
			atGrill.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoPLateFood(o.grill);
		o.gui.GoToPlating(o.grill);
		try{
			atPlating.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	//utilities
	
	public void setInitial(KRestaurantGui gui) {
		this.gui = gui;
	}
	public void setCashier(KCashier c) {
		cashier = c;
	}
	public void setMonitor(ProducerConsumerMonitor m) {
		theMonitor = m;
	}
	
	public void setGui(KCookGui gui) {
		cookGui = gui;

	}
//	public void addMarket(MarketAgent m) {
//		markets.add(m);
//	}
	
	private class Order {
		KWaiterRole w;
		String choice;
		int table;
		orderState s;
		int grill;
		KMovingFoodGui gui;

		public Order(KWaiterRole waiter, String c, int t, orderState state) {
			w = waiter;
			choice = c;
			table = t;
			s = state;
			grill = -1;
		}
	}
	private class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity;
		int stillNeed;
		int price;
		
		public Food(String s, int t, int a, int c, int l, int p) {
			type = s;
			cookingTime = t;
			amount = a;
			low = l;
			capacity = c;
			stillNeed = 0;
			price = p;
			if(low == amount) {
				stillNeed = capacity - amount;
			}
		}
	}
	private class MarketOrder {
		MarketManager m;
		MarketCashier cashier;
		double check;
		marketOrderState state;
		
		public MarketOrder(MarketManager m) {
			this.m = m;
			check = 0;
			state = marketOrderState.waiting;
		}
	}
	
}

