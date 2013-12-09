package simcity.Drew_restaurant;

import agent.Role;
import simcity.PersonAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.*;
import simcity.Drew_restaurant.gui.Drew_CookGui;
import simcity.Market.MFoodOrder;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;

/**
 * Restaurant Cook Agent
 */

public class Drew_CookRole extends Role implements Drew_Cook {
	
	//Data
	private ProducerConsumerMonitor theMonitor=new ProducerConsumerMonitor();
	
	public List<Order> orders
	= Collections.synchronizedList(new ArrayList<Order>());
	
	public List<marketOrder> marketorders
	= Collections.synchronizedList(new ArrayList<marketOrder>());
	
	public List<Delivery> deliveries
	= Collections.synchronizedList( new ArrayList<Delivery>());
	
	private Timer timer= new Timer();
	
	Drew_CashierRole cashier;
	Drew_HostRole host;
	
	//Foods
	Food chicken;
	Food steak;
	Food salad;
	Food pizza;
	 
	private  Map<String,Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
	private  Map<String,Integer> startingFoodAmount = new HashMap<String, Integer>();
	
	private Semaphore atDest = new Semaphore(0,true);
	private Drew_CookGui gui= null;


	private String name;
	private enum CookState {arrived, working, leave};
	private CookState cookstate;
	
	public List<MarketManager> markets
	= new ArrayList<MarketManager>();
	
	public List<MarketBill> marketBills = new ArrayList<MarketBill>();
	
	public void addMarket(MarketManager m){
		markets.add(m);
	}
	
	public Drew_CookGui getGui(){
		return gui;
	}
		
	public enum State 
	{pending, cooking, done, finished};

	public Drew_CookRole() {
		super();
		this.name = name;
		cookstate=CookState.arrived;
		
		
		//Initialize food amount
		startingFoodAmount.put("chicken", 5);
		startingFoodAmount.put("steak", 10);
		startingFoodAmount.put("salad", 5);
		startingFoodAmount.put("pizza", 7);
		
		//Initialize Types of foods
		chicken = new Food("chicken");
		steak = new Food("steak");
		pizza = new Food("pizza");
		salad = new Food("salad");
		
		//Initialize foods Map
		foods.put("chicken", chicken);
		foods.put("steak", steak);
		foods.put("salad", salad);
		foods.put("pizza", pizza);
		
		checkInventory();
	}

	public String getName() {
		return name;
	}

	public List<Order> getOrders() {
		return orders;
	}
	
	public void setGui(Drew_CookGui CG){
		gui=CG;
	}

	// Messages
	
	@Override
	public void msgSetInventory(int val) {
		// TODO Auto-generated method stub
		
		Iterator it = foods.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry obj = (Map.Entry)it.next();
			Food f = (Food) obj.getValue();
			f.changeAmt(val);
		}
		
	}
	
	@Override
	public void msgGoHome(double pay) {
		myPerson.money+=pay;
		cookstate=CookState.leave;
		stateChanged();
	}
	
	public void hereIsOrder(Drew_Waiter w, String choice, int table){
		orders.add(new Order(w, choice,table));
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCook", "Got Order");
		print("Got Order");
		stateChanged();
	}
	
	private void msgTimeToCheckStand() {
		Do("time to check stand");
		stateChanged();
	}
	
	public void TimerDone(Order O){
		O.s=State.done;
	}
	
	public void remainingStock(String type, int quantityAvailable, int quantityOrdered, String name){
		marketorders.add(new marketOrder(name, type, quantityOrdered-quantityAvailable,false));
		stateChanged();
	}
	
	public void deliver(String type, int quantity, boolean fullOrder){
//		Delivery d = new Delivery(type, quantity, fullOrder);
		Delivery d = new Delivery(type, quantity, true);
		deliveries.add(d);
		stateChanged();
	}

	
	public void msgHereIsDelivery(List<MFoodOrder> canGiveMe, double bill, MarketManager manager, MarketCashier cashier) {
		for(MFoodOrder o: canGiveMe) {
		/********************************** just put true for now for bool fullOrder ***************/
			Delivery d = new Delivery(o.type, o.amount, true);
			deliveries.add(d);
		}
		marketBills.add(new MarketBill(bill, cashier));
		stateChanged();
		
	}
	public void msgAtDest() {
		atDest.release();
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next   rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(cookstate==CookState.arrived){
			tellHostIAmHere();
		}
		synchronized(orders) {
		for (Order order : orders) {
			if(order.getState()==State.done){
				plateIt(order);
				return true;
			}
		}
		}
		synchronized(deliveries) {
		for(Delivery delivery : deliveries){
			if(!delivery.stored){
				storeDelivery(delivery);
			}
		}
		}
		synchronized(marketorders) {
		for(marketOrder marketOrder : marketorders){
			if(marketOrder.s==State.pending){
				//orderFood(marketOrder);					ORDER FOOD!!!!!!!!!!!!!
			}
		}
		}
		synchronized(orders) {
		for (Order order : orders) {
			if(order.getState()==State.pending){
				cookIt(order);
				return true;
			}
		}
		}
		synchronized(marketBills) {
		for(MarketBill b: marketBills) {
			giveCashierBill(b);
		}
		}
		if(cookstate==CookState.leave){
			leaveBank();
		}
		checkRotatingStand();
		return false;
	}

	// Actions

	private void giveCashierBill(MarketBill b) {
		cashier.marketBill(b.bill, b.cashier);
		marketBills.remove(b);
	}

	private void plateIt(Order O){
		gui.goToGrill();
		finishTask();
		gui.onGrill--;
		gui.windowCount++;
		gui.goToWindow();
		finishTask();
		gui.onWindow++;
		O.w.orderDone(O.t, O.choice.toLowerCase());
		O.s=State.finished;
	} 
	
	private void cookIt(final Order O){
		//doCooking(O); Will be used for gui
		Food f=foods.get(O.choice.toLowerCase());
		if(f.amount==0){
			O.w.outOf(O.choice, O.t);
			orders.remove(O);
			print("OUT OF "+ O.choice);
			return;
		}
		//gui.goToGrill();
		finishTask();
		gui.onGrill++;
		O.s=State.cooking;
		f.amount--;
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCook", "Cooking " + O.choice+ "-------"+f.amount+" Left" );
		print("Cooking " + O.choice+ "-------"+f.amount+" Left" );
		/*if(f.amount<=1 && !f.ordered){					//Cook constantly keeps track of inventory
			marketorders.add(new marketOrder(f.type, startingFoodAmount.get(f.type)-f.amount,true));
			f.ordered=true;
		}*/
		checkInventory();
		O.timer.schedule(new TimerTask() {
			public void run() {
				AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCook", "Done Cooking "+ O.choice );
				print("Done Cooking "+ O.choice);
				TimerDone(O);
				stateChanged();				
				}
		},
		f.cookingTime);//how long to wait before running task
	}
	
	private void orderFood(marketOrder MO){;
		MarketManager market=markets.get(0);
		
//		//RUN WHEN FIRST MARKET DID NOT HAVE EVERYTHING NEEDED
//		if(!MO.firstOrder){
//		synchronized(markets) {
//			for(int i=markets.size()-2;i>=0;i--){
//				if(markets.get(i).getName().equals(MO.market)){
//					market=markets.get(i+1);
//				}
//			}
//			if(market==markets.get(0)) print("All markets out of "+ MO.type);
//			else{
//				print("Ordering "+ MO.quantity+ " "+ MO.type + "s From "+ market.getName());
//				market.orderFood(this, MO.type, MO.quantity);
//			}
//		}
//		}
		//CALLED WHEN TRYING THE FIRST MARKET
		//else{				
			List<MFoodOrder> toOrder = new ArrayList<MFoodOrder>();
			toOrder.add(new MFoodOrder(MO.type, MO.quantity));
			//print("Ordering "+ MO.quantity+ " "+ MO.type + "s From "+ market.getName());
Do("UNCOMMENT 289");//			market.msgIAmHere(this, toOrder, "Drew_restaurant", "cook");
		//}
		MO.s=State.done;
	}
	
	private void checkInventory(){
		synchronized(foods) {
		for(Food f : foods.values()){
			if(f.amount< 3 && !f.ordered){					//Cook constantly keeps track of inventory
				marketorders.add(new marketOrder(f.type, startingFoodAmount.get(f.type)-f.amount,true));
				f.ordered=true;
			}
		}
		}
	}
	
	private void storeDelivery(Delivery D){
		Food food = foods.get(D.Type);
		if(D.fullDelivery) food.ordered=false;
		food.amount=food.amount+D.quantity;
		D.stored=true;
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCook", "Delivery Stocked" );
		AlertLog.getInstance().logMessage(AlertTag.DrewRestaurant, "DrewCook", "Current "+ food.type+ " inventory: "+food.amount );
		print("Delivery Stocked");
		print("Current "+ food.type+ " inventory: "+food.amount);
		
	}
	
	private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void checkRotatingStand() {
		Do("Checking Stand");
		Drew_RestaurantOrder newOrder = theMonitor.remove();
		if(newOrder != null) {
			Order o = new Order(newOrder.w, newOrder.choice, newOrder.table);
			orders.add(o);
			stateChanged();
			}
		else{
			timer.schedule(new TimerTask() {
				public void run() {
						msgTimeToCheckStand();
						Do("timer Done!");
					}
				},
				2000);
		}
	}
	
	private void tellHostIAmHere(){
		host.msgIAmHere(this);
		cookstate=CookState.working;
	}
	
	private void leaveBank(){
		gui.goOffScreen();
		this.isActive=false;
		cookstate=CookState.arrived;
	}


	//utilities

	public void setMonitor(ProducerConsumerMonitor w) {
		theMonitor = w;
	}
	/*public void setGui(WaiterGui gui) {					GUI
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}*/
	
	public void setCashier(Drew_CashierRole c) {
		cashier = c;
	}
	
	public class Order {
		Drew_Waiter w;
		int t;
		String choice;
		State s;
		Timer timer = new Timer();
		
		Order(Drew_Waiter waiter, String ch, int table){
			w=waiter;
			t=table;
			choice=ch;
			s=State.pending;
		}	
		
		Drew_Waiter getCustomer(){
			return w;
		}
		State getState(){
			return s;
		}
	}
		
	public class marketOrder {
		String market;
		int quantity;
		String type;
		boolean firstOrder;
		State s;
			
		marketOrder(String m, String ch, int q, boolean first){
			market=m;
			type=ch;
			quantity=q;
			firstOrder=first;
			s=State.pending;
		}
		marketOrder(String ch, int q, boolean first){
			type=ch;
			quantity=q;
			firstOrder=first;
			s=State.pending;
			market=null;
		}
	}
		
		public class Delivery {
			String Type;
			int quantity;
			boolean fullDelivery;
			boolean stored;
			
			Delivery(String t, int q, boolean fd){
				Type=t;
				quantity=q;
				fullDelivery=fd;
				stored=false;
			}
		}
		
		
	public class Food {					
		int cookingTime;
		String type;
		int amount;
		boolean ordered;
		private  Map<String,Integer> cookTimes = new HashMap<String, Integer>();
		 {
			cookTimes.put("chicken",1);
			cookTimes.put("steak",1);
			cookTimes.put("salad",1);
			cookTimes.put("pizza",1);
		}
		
		Food(String ch){
			type=ch;
			cookingTime=cookTimes.get(ch)* 1000;
			amount=3;
			ordered=false;
		}
		
		public void changeAmt(int val){
			amount = val;
		}
	}

	public class MarketBill {
		double bill;
		MarketCashier cashier;
		
		public MarketBill (double bill, MarketCashier c){
			this.bill = bill;
			cashier = c;
		}
	}

	public void setHost(Drew_HostRole drew_host) {
		// TODO Auto-generated method stub
		host=drew_host;
	}

}




