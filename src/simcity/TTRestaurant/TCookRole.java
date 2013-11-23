package simcity.TTRestaurant;

import agent.Role
;
import simcity.interfaces.TCook;
import simcity.interfaces.TWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.TTRestaurant.THostRole.Table;
import simcity.TTRestaurant.THostRole.myWaiters;
import simcity.TTRestaurant.gui.TCookGui;

/**
 * Restaurant Cook Agent
 */

public class TCookRole extends Role implements TCook {
	Timer timer = new Timer();
	private String name; 
	public boolean buyingFood = false;
	private boolean unFullfilled = false; 
	public TCookGui cookGui = null;  
	public List<Orders> orders
	= Collections.synchronizedList(new ArrayList<Orders>());

	Random randomQuan = new Random();
	private Semaphore atCounter = new Semaphore(0,true);
	Map<String, Integer> Supply = new HashMap<String, Integer>(4);
	//public List<Market> markets
	//= Collections.synchronizedList(new ArrayList<Market>());
	enum MarketState  
	{none, waiting, paying}; 
	
	public TCookRole(PersonAgent p) {
		super(p);
		this.name = name;
	}
	
	class Orders {
		TWaiter thisWaiter; 
		String order; 
		int table;
		OrderStatus status;
		
		public void setTable (int t) {
			table = t; 
		}
		
		public void setOrder (String choice) {
			order = choice;
			status = OrderStatus.pending; 
		}
		
		public void setWaiter (TWaiter headWaiterRoleTT) {
			thisWaiter = headWaiterRoleTT; 
		}
	}

	//private Food food = new Food(); 
	int timeCooked;
	enum OrderStatus 
	{pending, cooking, cooked, removed};

	TWaiterRole waiter;
	
	public void msgHereIsAnOrder(int t, String choice, TWaiter headWaiterRoleTT) {
		Orders o = new Orders(); 
		o.setWaiter(headWaiterRoleTT); 
		o.setTable(t);
		o.setOrder(choice);
		orders.add(o); 
		print("Cook has received customer orders.");
		stateChanged(); 
	}
	
/**
	public void msgCanGive(Map<String, Integer> nS, double check, Market mart) {
		for (Market m:markets) {
			if (m.m == mart) {
				m.state = MarketState.receivedCheck;
				m.bill = check; 
			} 
		}
		Map<String, Integer> newSupply= nS;
		Supply.put("Steak", newSupply.get("Steak") + Supply.get("Steak"));
		Supply.put("Chicken", newSupply.get("Chicken") + Supply.get("Chicken"));
		Supply.put("Salad", newSupply.get("Salad") + Supply.get("Salad"));
		Supply.put("Pizza", newSupply.get("Pizza") + Supply.get("Pizza"));
		print("Restocking supply"); 
		print (Supply.get("Steak") + " Steaks, " + Supply.get("Chicken") + " Chickens, " + Supply.get("Salad") + " Salads, " + Supply.get("Pizza") + " Pizzas"); 
		newSupply.clear();
		
	}
*/
	
	public void msgUnfulfilledStock() {
		buyingFood = false;
		unFullfilled = true;
		print ("Buying stock from different market"); 
		stateChanged(); 
	}
	
	public void msgAtCounter() {
		atCounter.release();
		stateChanged(); 
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!orders.isEmpty()){
			for (int index = 0; index < orders.size(); index++) {
				if (orders.get(index).status == OrderStatus.pending) {
					if (orders.get(index).order == "Steak") {
						if (Supply.get("Steak") <= 0) {
							orders.get(index).status = OrderStatus.removed; 
							callWaiter(index);
							//BuyFood(); 
							return true; 
						}
						else {
							orders.get(index).status = OrderStatus.cooking;
							cookFood(index);
							return true; 
						}
					}
					if (orders.get(index).order == "Chicken") {
						if (Supply.get("Chicken") <= 0) {
							orders.get(index).status = OrderStatus.removed; 
							callWaiter(index);
							//BuyFood(); 
							return true; 
						}
						else {
							orders.get(index).status = OrderStatus.cooking;
							cookFood(index);
							return true; 
						}
					}
					if (orders.get(index).order == "Salad") {
						if (Supply.get("Salad") <= 0) {
							orders.get(index).status = OrderStatus.removed;
							//BuyFood(); 
							callWaiter(index);
							return true; 
						}
						else {
							orders.get(index).status = OrderStatus.cooking;
							cookFood(index);
							return true; 
						}
					}
					if (orders.get(index).order == "Pizza") {
						if (Supply.get("Pizza") <= 0) {
								orders.get(index).status = OrderStatus.removed; 
								callWaiter(index);
								//BuyFood(); 
								return true; 
						}
						else {
							orders.get(index).status = OrderStatus.cooking;
							cookFood(index);
							return true; 
						}
					}
				}
			}
		}
		/**
		for (Markets m:markets) {
			if m.state == MarketState.paying {
				sendCheck(m); 
			}
		}
		*/
		if (unFullfilled == true) {
			//BuyFood(); 
		}
     return false; 
		
	}

	// Actions

	private void cookFood(final int orderNumber) {
		goToFridge(); 
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done cooking food.");
				orders.get(orderNumber).status = OrderStatus.cooked; 
				if (orders.get(orderNumber).order == "Steak") {
					Supply.put("Steak", Supply.get("Steak") - 1);
					print ("There are " + Supply.get("Steak") + " steaks left"); 
				}
				if (orders.get(orderNumber).order == "Chicken") {
					Supply.put("Chicken", Supply.get("Chicken") - 1); 
					print ("There are " + Supply.get("Chicken") + " chickens left"); 
				}
				if (orders.get(orderNumber).order == "Salad") {
					Supply.put("Salad", Supply.get("Salad") - 1); 
					print ("There are " + Supply.get("Salad") + " salads left"); 
				}
				if (orders.get(orderNumber).order == "Pizza") {
					Supply.put("Pizza", Supply.get("Pizza") - 1); 
					print ("There are " + Supply.get("Pizza") + " pizzas left"); 
				}
				leaveFood();
				try {
					atCounter.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				orders.get(orderNumber).thisWaiter.msgOrderIsReady(orders.get(orderNumber).table);
				waitForOrder(); 
				stateChanged(); 
			}
		},
		 8000);
		
	}
	
	private void callWaiter(int orderNumber) {
		print("Out of food");
		orders.get(orderNumber).thisWaiter.msgOutOfFood(orders.get(orderNumber).table); 
	}
	
	private void checkOrders() {
		
	}
	/**
	private void BuyFood() {
		
		if (buyingFood == false) {
			print("Buying food from market."); 
			buyingFood = true;
			int index = 0;
			int checkedMarkets = 0; 
			while (markets.get(index).checked != true && index < markets.size()) {
				index++;
				checkedMarkets++; 
			}
			if (checkedMarkets == markets.size()) {
				print("Checked all markets for supply"); 
				for (index = 0; index < markets.size(); index++) {
					markets.get(index).checked = false;
				}
			}
			else {
				markets.get(index).m.msgBuyFood(Supply, this);
				markets.get(index).checked = true
				markets.get(index).state = MarketState.waiting;;
			}
		}	
	}
	
	private void sendCheck(Market m) {
		cashier.msgPayForSupply(m.m, m.bill); 
		markets.remove(m); 
	}
	
	*/
	
	//animations
	private void goToFridge() {
		cookGui.makeFood(); 
	}
	
	private void leaveFood() {
		cookGui.giveFoodToWaiter(); 
	}
	
	private void waitForOrder() {
		cookGui.goHome(); 
	}
	
	/**
	//utilities 
	public void addMarket(MarketAgent mart) {
		Market m = new Market();
		m.setMarket(mart);
		markets.add(m);
		stateChanged(); 
	}
	*/

	public void addFood() {
		/**
		Supply.put("Steak", 0); 
		Supply.put("Pizza", 0); 
		Supply.put("Salad", 0); 
		Supply.put("Chicken", 0);
		*/
		Supply.put("Steak", randomQuan.nextInt(4)); 
		Supply.put("Pizza", randomQuan.nextInt(4)); 
		Supply.put("Salad", randomQuan.nextInt(4)); 
		Supply.put("Chicken", randomQuan.nextInt(4));
	}

	public void setGui(TCookGui gui) {
        cookGui = gui;
	}
	
	public TCookGui getGui() {
	        return cookGui;
	}

	
	public class OrderStand extends Object {
		private Vector <Orders> rotatingTable;
		
		OrderStand() {
			
		}
		
		synchronized public void insert() {
			
		}
		
		synchronized public void remove() {
			
		}
		
		private void insertOrder() {
			
		}
		
		private void removeOrder() {
			
		}
		
	}


/**	
	public class Market {
		MarketAgent m;
		MarketState state;
		boolean checked;
		double bill; 
		
		public List<String> foodChoices = new ArrayList<String>();

		public void setMarket (MarketAgent mar) {
			m = mar; 
			checked = false;
			state = MarketState.none;
		}

	}
*/

}




