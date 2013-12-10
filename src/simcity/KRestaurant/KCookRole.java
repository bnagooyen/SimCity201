package simcity.KRestaurant;

import agent.*;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KCookGui;
import simcity.KRestaurant.gui.KMovingFoodGui;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.Market.MFoodOrder;
import simcity.Market.gui.IBGui;
import simcity.gui.Gui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Cook;
import simcity.interfaces.KCashier;
//import simcity.interfaces.Market;


import simcity.interfaces.KWaiter;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Cook Agent
 */

public class KCookRole extends Role implements Cook{
	
	private ProducerConsumerMonitor theMonitor;
	
	private KHostRole host;
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public Map<String, Food > foods = Collections.synchronizedMap( new HashMap<String, Food>()); 
	public List<MarketManager> markets =Collections.synchronizedList( new ArrayList<MarketManager>());
	public List<Integer> grills = Collections.synchronizedList( new ArrayList<Integer>());
	public List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
//	public MarketAgent currentMarket;
	public RestaurantCashier cashier;
	
	private KCookGui cookGui;
	private String name;
	
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	public boolean arrived; 
	public boolean goHome = false;
	public boolean needToOrder;

	public EventLog log;
	private Timer timer = new Timer(); 
		
	public enum orderState 
	{ pending, cooking, done, finished }
	
	public enum marketOrderState 
	{ waiting, arrived, done, confirm}
	
	private SimCityGui gui;
	
	public KCookRole(SimCityGui gui) {
		super();
		this.gui = gui;
		
		log = new EventLog();
		foods.put("Steak", new Food("Steak", 5000, 3, 3, 1,10));
		foods.put("Chicken", new Food("Chicken", 4500, 3, 3,1,7));
		foods.put("Salad", new Food("Salad", 2000, 3, 3, 1, 3));
		foods.put("Pizza", new Food("Pizza", 4000, 1, 3, 1, 5));
		
		for(int i = 0; i<4; i++) {
			grills.add(0);
		}
		
		needToOrder = true;
		arrived = true;
		startHour = 11;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages


	@Override
	public void msgSetInventory(int val) {
		foods.put("Steak", new Food("Steak", 5000, val, 3, 1, 10));
		foods.put("Chicken", new Food("Chicken", 4500, val, 3, 1, 7));
		foods.put("Salad", new Food("Salad", 2000, val, 3, 1,3));
		foods.put("Pizza", new Food("Pizza", 4000, val, 3, 1, 5)); 

	}
	
	public void msgConfirmCheck( double check, MarketManager manager) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "got check to confirm");
		Do("got check to confirm");
		synchronized(marketOrders) {
				for(MarketOrder m : marketOrders) {
					if( m.m == manager) {
						m.state = marketOrderState.confirm;
						m.check = check;
					}
				}
			}
		stateChanged();
	}
	
	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}
	public void msgGoHome(double payment) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "told to go home");
		System.out.println(myPerson.getName() + ": " +"told to go home");
		myPerson.money += payment;
		goHome = true;
		stateChanged();
	}
	public void msgHereIsAnOrder(KWaiter w, String choice, int table) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "got order from waiter");
		System.out.println(myPerson.getName() + ": " +"got order from waiter");
		LoggedEvent e = new LoggedEvent("got order from waiter");
		log.add(e);
		orders.add(new Order(w, choice, table, orderState.pending));
		stateChanged();
	}

	// from normal waiter
	public void msgCanGive(List<MFoodOrder> canGiveMe) {
		for(MFoodOrder f : canGiveMe) {
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "Told they can give me " + f.amount + " of " + f.type);
			System.out.println(myPerson.getName() + ": " +"Told they can give me " + f.amount + " of " + f.type);
			Food currentFood = foods.get(f.type);
			currentFood.stillNeed -= f.amount;
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "amount still need of " + f.type + " is " + foods.get(f.type).stillNeed);
			System.out.println(myPerson.getName() + ": " +"amount still need of " + f.type + " is " + foods.get(f.type).stillNeed);
			
			if(currentFood.stillNeed > 0) {
				needToOrder = true;
			}
		}
		stateChanged();
	}
	
	public void msgHereIsDelivery(List<MFoodOrder> canGiveMe, double bill, MarketManager manager, MarketCashier cashier) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "got delivery from market");
		Do("got delivery from market");
		LoggedEvent e = new LoggedEvent("got delivery from market");
		log.add(e);
		synchronized(marketOrders) {
		for(MarketOrder m : marketOrders) {
			if( m.m == manager) {
				m.state = marketOrderState.arrived;
				m.cashier = cashier;
				m.foodGiven = canGiveMe;
			}
		}
		}
		for(MFoodOrder f : canGiveMe) {
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "got " + f.amount + " of " + f.type);
			System.out.println(myPerson.getName() + ": " +"got " + f.amount + " of " + f.type);
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
		if(arrived){
			tellHost();
			return true;
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
					System.out.println(myPerson.getName() + ": " +"gonna cook order now");
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
		synchronized(marketOrders) {
			for(MarketOrder m : marketOrders) {
				if(m.state == marketOrderState.confirm) {
					confirmBill(m);
				}
			}
		}
		if(goHome) {
			goHome();
		}
		else {
			checkRotatingStand();
		}
		return false;
	}

	// Actions

	private void confirmBill(MarketOrder m) {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "confirming check");
		Do("confirming check");
		double billShouldBe = 0;
		synchronized(m.foodGiven) {
			for(MFoodOrder o : m.foodGiven) {
				billShouldBe += o.amount * o.price;
			}
		}
		if(billShouldBe == m.check) {
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "check is correct");
			Do("check is correct");
			 ((KCashierRole)cashier).msgConfirmingMarketCheck(false, m.m);
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "market is scamming us");
			Do("market is scamming us");
			((KCashier) cashier).msgConfirmingMarketCheck(false, m.m);
		}
		m.state = marketOrderState.done;
	}

	private void tellHost() {
		if(cookGui == null) {
			cookGui = new KCookGui(this, gui);
			gui.myPanels.get("Restaurant 4").panel.addGui(cookGui);
		}
		cookGui.setPresent(true);
		cookGui.DoGoToWork();

		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "telling manager I'm here to work");
		System.out.println(myPerson.getName() + ": " +"telling manager I'm here to work");
		arrived = false;
		host.msgIAmHere(this, "cook");
	}

	private void goHome() {
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "going home");
		System.out.println(myPerson.getName() + ": " +"going home");
		isActive = false;
		goHome = false;
		leaveRestaurant();
		
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
		
		arrived = true;
	}

	private void leaveRestaurant() {
		cookGui.DoLeaveRestaurant();
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
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "sent msg to waiter that out of " + food.type);
			System.out.println(myPerson.getName() + ": " +"sent msg to waiter that out of " + food.type);
			orders.remove(o);
			return;
		}
		o.s = orderState.cooking;
		DoCooking(o); 
		timer.schedule(new TimerTask() {
			public void run() {
					AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "Done cooking");
					print("Done cooking");
					o.s = orderState.done;
					grills.set(o.grill, 0);
					stateChanged();
				}
			},
			food.cookingTime);
		food.amount--;
		if ( food.amount == food.low) {
			LoggedEvent e = new LoggedEvent("need to order food");
			log.add(e);
			AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "need to order food");
			System.out.println(myPerson.getName() + ": " +"need to order food");
			food.stillNeed = food.capacity - food.low;
			orderFoodThatIsLow();
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", food.type + " = " + food.amount);
		System.out.println(myPerson.getName() + ": " +food.type + " = " + food.amount);
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
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "ordering from a market");
		System.out.println(myPerson.getName() + ": " +"ordering from a market");
		MarketManager m = pickAMarket();
		marketOrders.add(new MarketOrder(m));
		m.msgIAmHere((Role) this, lowFoods, "KRestaurant", "cook", cashier);
	}
	
	public MarketManager pickAMarket() {
		System.out.println(myPerson.getName() + ": " +"picking a market to order from");
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
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "going to fridge to acquire materials");
		System.out.println(myPerson.getName() + ": " +"going to fridge to acquire materials");
		cookGui.DoGoToFridge();
		try{
			atFridge.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "going to grill " +o.grill);
		System.out.println(myPerson.getName() + ": " +"going to grill " +o.grill);
		o.gui = new KMovingFoodGui(this, (KWaiter) o.w, gui, (o.grill *20) + 5, o.choice);
		if(gui != null) {
		gui.myPanels.get("Restaurant 4").panel.addGui((Gui) o.gui);
		}
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
		AlertLog.getInstance().logMessage(AlertTag.KRestaurant, "KCook", "plating order "+o.choice);
		System.out.println(myPerson.getName() + ": " +"plating order "+o.choice);
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
	public void addMarket(MarketManager m) {
		markets.add(m);
	}
	public void setHost(KHostRole h) {
		host = h;
	}
	
	public void setInitial(SimCityGui gui) {
		this.gui = gui;
	}
	public void setCashier(RestaurantCashier c) {
		cashier =  c;
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
		KWaiter w;
		String choice;
		int table;
		orderState s;
		int grill;
		KMovingFoodGui gui;

		public Order(KWaiter waiter, String c, int t, orderState state) {
			w = waiter;
			choice = c;
			table = t;
			s = state;
			grill = -1;
		}
	}
	public class Food {
		String type;
		int cookingTime;
		public int amount;
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
	public class MarketOrder {
		MarketManager m;
		MarketCashier cashier;
		public double check;
		public marketOrderState state;
		public List<MFoodOrder> foodGiven;
		
		public MarketOrder(MarketManager m) {
			this.m = m;
			check = 0;
			state = marketOrderState.waiting;
		}
	}
	
	@Override
	public void msgGoToCashier(MarketCashier c) {
		// TODO Auto-generated method stub
		
	}


	
	
}

