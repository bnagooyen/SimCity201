package restaurant;

import agent.Role;
import restaurant.CustomerRole.AgentEvent;
import restaurant.Order.OrderState;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import simcity.PersonAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookRole extends Role {

	Timer timer = new Timer();
	private String name;
	private Map<String, Food> myFood = new HashMap<String, Food>();
	//public HostGui hostGui = null;
	private int initialFoodAmnt= 2;
	private static final int MAXCAPACITY=10;
	private int threshold = 1;
	//private final int NUM_MARKETS = 3;
    
    public CookGui CookGui = null;
	
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	private WaiterRole waiter;
	private HostRole host;

	private boolean waitingForInventory; // order at once, don't order when waiting for an order to arrive
	//private boolean reOrdering;
	
	Map<String, Boolean> grillOccupied = new HashMap<String, Boolean>();
	
	ArrayList<ArrayList<FoodOrder>> delivery= new ArrayList<ArrayList<FoodOrder>>();
	List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
//	ArrayList<MarketAgent> markets = new ArrayList<MarketAgent>();
	List<InventoryOrder> myOrders =  Collections.synchronizedList(new ArrayList<InventoryOrder>());
	private int ORDER_ID;
	ArrayList<FoodOrder> orderToMarket = new ArrayList<FoodOrder>();
	//private int marketToSendOrdersTo;

	Cashier myCashier;
	boolean RestaurantIsOpen, CheckedAtFirst, valsAreSet;
	
	public CookRole(PersonAgent p) {
		super(p);
		
		this.name = name;
		
		//GRADER: CHANGE KITCHEN VALS HERE
		//myFood.put("Chicken", new Food("Chicken", 5, X));
		// or change initialFoodAmnt variable, threshold to reaorder is 1/2 of that value
		
		//fill up kitchen with food
		myFood.put("Chicken", new Food("Chicken", 5, initialFoodAmnt));
		myFood.put("Steak", new Food("Steak", 10, initialFoodAmnt));
		myFood.put("Salad", new Food("Salad", 3, initialFoodAmnt));
		myFood.put("Pizza", new Food("Pizza", 6, initialFoodAmnt));
		System.out.println("INITALIZE VALUES OF KITCHEN AND HIT 'SET' IN ORDER FOR COOK TO BEGIN CHECKING IF READY FOR OPEN");
		System.out.println("Cook Kitchen Inventory is initialized to "+ initialFoodAmnt+ " and threshold is "+ threshold);
		System.out.println("Maximum capacity in kitchen is 10");
	
		grillOccupied.put("Pizza", false);
		grillOccupied.put("Steak", false);
		grillOccupied.put("Chicken", false);
		grillOccupied.put("Salad", false);
		
		
		waitingForInventory=false;
		//reOrdering=false;
		//marketToSendOrdersTo=1;
		
		ORDER_ID=1;
		
		RestaurantIsOpen=false; CheckedAtFirst=false; valsAreSet=false;
		
	}


	// The animation DoXYZ() routines
	
	private void DoClearPlating(String foo) {
		CookGui.DoClearPlating(foo);
	}
	
	private void DoGoToRefrigerator() {
		CookGui.DoGoToRefrigerator();
	}
	
	private void DoGoToPlating() {
		CookGui.DoGoToPlating();
	}
	private void DoGoToPizzaGrill() {
		CookGui.DoGoToPizzaGrill();
	}
	private void DoGoToSteakGrill() {
		CookGui.DoGoToSteakGrill();		
	}
	private void DoGoToChickenGrill() {
		CookGui.DoGoToChickenGrill();	
	}
	private void DoGoToSaladGrill() {
		CookGui.DoGoToSaladGrill();	
	}

	private void DoGoToPizzaGrill2() {
		CookGui.DoGoToPizzaGrill2();
	}
	private void DoGoToSteakGrill2() {
		CookGui.DoGoToSteakGrill2();		
	}
	private void DoGoToChickenGrill2() {
		CookGui.DoGoToChickenGrill2();	
	}
	private void DoGoToSaladGrill2() {
		CookGui.DoGoToSaladGrill2();	
	}
	

	public String getName() {
		return name;
	}

	// Messages

	//hack!
	
	public void AddHost(HostRole h) {
		//System.out.println("host added to cook");
		host=h;
	}
	
	public void AddCashier(Cashier h) {
		//System.out.println("host added to cook");
		myCashier=h;
	}
	
	public void msgAddWaiter(WaiterRole w) {
		waiter=w;
	}
	
//	public void msgAddMarket(MarketAgent m) {
//		//System.out.println("added");
//		markets.add(m);
//	}
//	
	public void msgIncKitchenThreshold() {
		threshold++;
		System.out.println("Kitchen threshold increased to "+ threshold);
	}
	public void msgDecKitchenThreshold() {
		if(threshold>1) {
			threshold--;
			System.out.println("Kitchen threshold decresed to "+ threshold);
		}
		else {
			System.out.println("Cannot drop Kitchen threshold below 1");
		}
	}
	
	public void msgIncKitchenAmnt() {
		if(initialFoodAmnt<MAXCAPACITY) {
		initialFoodAmnt++;
		//fill up kitchen with food
			myFood.put("Chicken", new Food("Chicken", 5, initialFoodAmnt));
			myFood.put("Steak", new Food("Steak", 10, initialFoodAmnt));
			myFood.put("Salad", new Food("Salad", 3, initialFoodAmnt));
			myFood.put("Pizza", new Food("Pizza", 6, initialFoodAmnt));
			System.out.println("Cook Kitchen Inventory is updated to "+ initialFoodAmnt);
		}
		else {
			System.out.println("kitchen filled to capacity!");
		}
	}
	
	public void msgDecKitchenAmnt() {
		if(initialFoodAmnt>0) {
			initialFoodAmnt--;
			//fill up kitchen with food
			myFood.put("Chicken", new Food("Chicken", 5, initialFoodAmnt));
			myFood.put("Steak", new Food("Steak", 10, initialFoodAmnt));
			myFood.put("Salad", new Food("Salad", 3, initialFoodAmnt));
			myFood.put("Pizza", new Food("Pizza", 6, initialFoodAmnt));

			System.out.println("Cook Kitchen Inventory is updated to "+ initialFoodAmnt);
		}
		else {
			System.out.println("Kitchen Inventory can not be lower than 0");
		}
	}
	
	public void msgCheckInventoryValsForOpen() {
		//System.out.println("received msg");
		valsAreSet=true;
		stateChanged();
	}
	
//	public void msgClearPlatingForOrder(String order) {
//		System.err.println("received clear plating");
//		for(Order ord: orders) {
//			if(ord.tablenum==o.tablenum) {
//				ord.state=OrderState.clearPlating;
//				stateChanged();
//			}
//		}
//	}
	
	public void msgHereIsAnOrder(Order order) {
		orders.add(new Order(order.getChoice(), order.getTablenum(), order.getWaiter()));
		System.out.println("cook received order of "+ order.getChoice()+ " from table "+order.tablenum);
		stateChanged();
	}
	/*
	public void msgHereIsYourFoodOrder(String food, int val) {
		delivery.add(new FoodOrder(food, val));
		System.out.println("received order of "+ food + " for "+ val + " from market");
		stateChanged();
	}
	
	public void msgCouldNotFulFillFullOrderOf(String foo) {
		if(InventoryMarketTracker.get(foo)<=markets.size()) // if at last market possible or below
		InventoryMarketTracker.put(foo, InventoryMarketTracker.get(foo)+1);
	}*/
	
	public void msgHereIsYourFoodOrder(ArrayList<FoodOrder> dlv) {
		delivery.add(dlv);
		System.out.println("received order from cook");
		waitingForInventory=false;
		stateChanged();
	}
	
	public void msgCouldNotFulfillThese(ArrayList<FoodOrder> reorderlist, int ORDERID) {
		
		
		for(InventoryOrder order: myOrders) {
			if(order.getID()==ORDERID) {
				System.out.println("got inventory orderback from "+order.getMarketOrderingFrom());
				order.incMarketOrderingFrom();
				order.reorder=true;
				order.myorder=reorderlist;
				stateChanged();
			}
		}
	}
	
	public void msgAnimationArrivedAtFridge() {
		atFridge.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtGrill() {
		atGrill.release();
		stateChanged();
	}
	
	public void msgAnimationArrivedAtPlating() {
		atPlating.release();
		stateChanged();
	}
	
	//utilities

	public void setGui(CookGui gui) {
		CookGui = gui;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.clearPlating) {
				ClearPlating(orders.get(i));
				return true;
			}
		}
	
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.grillInUse && !grillOccupied.get(orders.get(i).getChoice())) {
//				CookOrder(orders.get(i));
//				System.err.println("****");
				CookOrder(orders.get(i));
				return true;
			}
		}
		
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.pending) {
//				CookOrder(orders.get(i));
//				System.err.println("*****");
				CookOrder(orders.get(i));
				return true;
			}
		}
		
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.cooked) {
				PlateOrderAndCallWaiter(orders.get(i));
				//CallWaiter(orders.get(i));
				return true;
			}
		}
		if(!delivery.isEmpty()) {
			ProcessDelivery(delivery.get(0));
			return true;
		}
		
		for(InventoryOrder i: myOrders) {
			if(i.reorder) {
				ReorderFood(i);
				return true;
			}
		}
		
		if(!waitingForInventory) {
			if((myFood.get("Chicken").getAmount()<=threshold || myFood.get("Pizza").getAmount()<=threshold ||
					myFood.get("Steak").getAmount()<=threshold || myFood.get("Salad").getAmount()<=threshold)) {
						OrderFoodFromMarket();
						return true;
					}
		}
		
		if(!RestaurantIsOpen && valsAreSet && !waitingForInventory) {
			CheckIfFullyStocked(); 
			return true;
		}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
	
		private void ClearPlating(Order o) {
//			System.err.println("***");
			DoClearPlating(o.getChoice().substring(0,2));
			orders.remove(o);
		}
		private void CheckIfFullyStocked() {
			//System.out.println("called");
			if(myFood.get("Chicken").getAmount()>=threshold && myFood.get("Steak").getAmount()>=threshold &&
					myFood.get("Pizza").getAmount()>=threshold && myFood.get("Salad").getAmount()>=threshold) {
				host.msgKitchenIsReady();
				RestaurantIsOpen=true;
			}
		}
		
		private void CookOrder(final Order o) {
//			System.out.println("asdfjlaksdjflkasj");
			Food food=myFood.get(o.getChoice());
			
			if(food.getAmount()==0) {
					System.out.println("Out of "+ food.getChoice());
					o.getWaiter().msgOutOfFood(o);
					orders.remove(o);
					return;
				}
			
			if(grillOccupied.get(o.getChoice())) {
				o.state=OrderState.grillInUse;
				return;
			}
			
			grillOccupied.put(o.getChoice(), true);
			DoGoToRefrigerator();
			
			try {
				atFridge.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.err.println("made to fridge... "+ o.getChoice());
			
			if(o.getChoice().equals("Pizza")) {
				DoGoToPizzaGrill();
//				System.err.println("called dofuncpizza");
			}
			else if(o.getChoice().equals("Steak")) {
				DoGoToSteakGrill();
//				System.err.println("called dofuncsteak");
			}
			else if(o.getChoice().equals("Chicken")) {
				DoGoToChickenGrill();
//				System.err.println("called dofuncChicken");
			}
			else if(o.getChoice().equals("Salad")) {
				DoGoToSaladGrill();
//				System.err.println("called dofuncsalad");
			}
			
			
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			System.err.println("made to grill");
			
			o.state=OrderState.cooking;
			
			timer.schedule(new TimerTask() {
				public void run() {
					print(o+" is cooked!");
					o.state=OrderState.cooked;
					//isHungry = false;
					stateChanged();
				}
			},
			myFood.get(o.getChoice()).getCookTime()*1000);
			
		}


		private void PlateOrderAndCallWaiter(Order o) {
			
			o.state=OrderState.plated;
			System.err.println("**");
			if(o.state==OrderState.plated) System.err.println("changed!");
			
			
			if(o.getChoice().equals("Pizza"))
				DoGoToPizzaGrill2();
			else if(o.getChoice().equals("Steak"))
				DoGoToSteakGrill2();
			else if(o.getChoice().equals("Chicken"))
				DoGoToChickenGrill2();
			else if(o.getChoice().equals("Salad"))
				DoGoToSaladGrill2();
			
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			grillOccupied.put(o.getChoice(), false);
//			
//			System.out.println("at Grill!");
			
			DoGoToPlating();
			
			try {
				atPlating.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("order plated");
			o.getWaiter().msgOrderIsReady(o.getTablenum());
			orders.remove(o);
		}
		
//		private void CallWaiter(Order o) {
//			o.getWaiter().msgOrderIsReady(o);
//			orders.remove(o);
//		}
		
		private void OrderFoodFromMarket() {
			/*
			 	if(food.getAmount()==threshold) // made it ==, not <= so won't keep ordering
				{
					System.out.println("Out of "+ food.getChoice()+"!");
					markets.get(InventoryMarketTracker.get(o.getChoice())).msgHereIsAnInventoryOrder(o.getChoice(), threshold);
					System.out.println("Made an order to mkt#"+ InventoryMarketTracker.get(o.getChoice()) + " for " + threshold);
					
				}
			 */
			System.out.println("need to order");
			
			orderToMarket.clear(); //restart a new order
			
			if(myFood.get("Chicken").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Chicken", MAXCAPACITY- myFood.get("Chicken").getAmount()));
			}
			
			if(myFood.get("Steak").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Steak", MAXCAPACITY-myFood.get("Steak").getAmount()));
			}
			
			if(myFood.get("Pizza").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Pizza", MAXCAPACITY-myFood.get("Pizza").getAmount()));
			}
			
			if(myFood.get("Salad").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Salad", MAXCAPACITY-myFood.get("Salad").getAmount()));
			}
			
//			markets.get(0).msgHereIsAnInventoryOrder(orderToMarket, ORDER_ID, myCashier);
			myOrders.add(new InventoryOrder(orderToMarket, ORDER_ID));
			ORDER_ID++;
			waitingForInventory=true;
		}
		
		private void ReorderFood(InventoryOrder reord) {
//			if(reord.getMarketOrderingFrom()>markets.size()) {
//				for(int i=0; i< reord.myorder.size(); i++) {
//					System.out.println("all markets are out of "+ reord.myorder.get(i).getFood());
//				}
//				if(!RestaurantIsOpen && reord.getMarketOrderingFrom()>markets.size()) { //markets don't have it.. should just open anyway
//					RestaurantIsOpen=true;
//					host.msgKitchenIsReady();
//				}
//				myOrders.remove(reord);
//				return;
			//}
			System.out.println("sent reorder");
//			markets.get(reord.mktOrderingFrom-1).msgHereIsAnInventoryOrder(reord.myorder, reord.getID(), myCashier);
			reord.reorder=false;
			
		}
		
	
		private void ProcessDelivery(ArrayList<FoodOrder> groceries) {
			for(FoodOrder foo: groceries) {
				Food temp = myFood.get(foo.getFood());
				temp.setAmount(temp.getAmount()+foo.getVal());
				myFood.put(foo.getFood(), temp);
				System.out.println("update on " + temp.getChoice() + ": "+ myFood.get(temp.getChoice()).getAmount());
				//myFood.put(f.getFood(), myFood.get(f.getVal()+));
				
			}
			waitingForInventory=false;
			delivery.remove(groceries);
			if(!RestaurantIsOpen) {
				if(myFood.get("Chicken").getAmount()==initialFoodAmnt && myFood.get("Steak").getAmount()==initialFoodAmnt &&
						myFood.get("Pizza").getAmount()==initialFoodAmnt && myFood.get("Salad").getAmount()==initialFoodAmnt) {
					host.msgKitchenIsReady();
					RestaurantIsOpen=true;
					//System.out.println("ready!");
				}
			}
		}
		
		
		class InventoryOrder {
			int orderID;
			ArrayList<FoodOrder> myorder;
			int mktOrderingFrom;
			boolean reorder;
			
			InventoryOrder(ArrayList<FoodOrder> mo, int orderid) {
				myorder=mo;
				mktOrderingFrom=1;
				orderID=orderid;
				reorder=false;
			}
			
			int getMarketOrderingFrom() {
				return mktOrderingFrom;
			}
			
			void incMarketOrderingFrom(){
				mktOrderingFrom++;
			}
			
			int getID() {
				return orderID;
			}
			boolean getReorder() {
				return reorder;
			}
			void setReorder(boolean r) {
				reorder=r;
			}
			
		}

		
}



