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
public class CookRole extends Role {

	Timer timer = new Timer();
	private String name;
	private Map<String, Food> myFood = new HashMap<String, Food>();
	private Map<String, Double> prices = new HashMap<String, Double>();
	private int initialFoodAmnt= 2;
	private static final int MAXCAPACITY=10;
	private int threshold = 1;
    
    public CookGui CookGui = null;
	
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	private WaiterRole waiter;
	private HostRole host;

	private boolean waitingForInventory; 
	
	Map<String, Boolean> grillOccupied = new HashMap<String, Boolean>();
	
	ArrayList<ArrayList<FoodOrder>> delivery= new ArrayList<ArrayList<FoodOrder>>();
	List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	List<InventoryOrder> myOrders =  Collections.synchronizedList(new ArrayList<InventoryOrder>());
	private int ORDER_ID;
	ArrayList<FoodOrder> orderToMarket = new ArrayList<FoodOrder>();

	Cashier myCashier;
	boolean RestaurantIsOpen, CheckedAtFirst, valsAreSet;
	
	public CookRole(PersonAgent p) {
		super(p);
		
		this.name = name;
		
		myFood.put("Chicken", new Food("Chicken", 5, initialFoodAmnt));
		myFood.put("Steak", new Food("Steak", 10, initialFoodAmnt));
		myFood.put("Salad", new Food("Salad", 3, initialFoodAmnt));
		myFood.put("Pizza", new Food("Pizza", 6, initialFoodAmnt));
//		System.out.println("INITALIZE VALUES OF KITCHEN AND HIT 'SET' IN ORDER FOR COOK TO BEGIN CHECKING IF READY FOR OPEN");
//		System.out.println("Cook Kitchen Inventory is initialized to "+ initialFoodAmnt+ " and threshold is "+ threshold);
//		System.out.println("Maximum capacity in kitchen is 10");
	
		grillOccupied.put("Pizza", false);
		grillOccupied.put("Steak", false);
		grillOccupied.put("Chicken", false);
		grillOccupied.put("Salad", false);
		
		
		waitingForInventory=false;
		
		ORDER_ID=1;
		
		RestaurantIsOpen=false; CheckedAtFirst=false; valsAreSet=false;
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
	}
	
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

	
	public void msgHereIsAnOrder(Order order) {
		orders.add(new Order(order.getChoice(), order.getTablenum(), order.getWaiter()));
		System.out.println("cook received order of "+ order.getChoice()+ " from table "+order.tablenum);
		stateChanged();
	}

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
	public boolean pickAndExecuteAnAction() {
	
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.clearPlating) {
				ClearPlating(orders.get(i));
				return true;
			}
		}
	
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.grillInUse && !grillOccupied.get(orders.get(i).getChoice())) {
				CookOrder(orders.get(i));
				return true;
			}
		}
		
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.pending) {
				CookOrder(orders.get(i));
				return true;
			}
		}
		
		for(int i=0; i<orders.size(); i++) {
			if(orders.get(i).state==OrderState.cooked) {
				PlateOrderAndCallWaiter(orders.get(i));
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
	}


	// Actions
	
		private void ClearPlating(Order o) {
			DoClearPlating(o.getChoice().substring(0,2));
			orders.remove(o);
		}
		private void CheckIfFullyStocked() {
			if(myFood.get("Chicken").getAmount()>=threshold && myFood.get("Steak").getAmount()>=threshold &&
					myFood.get("Pizza").getAmount()>=threshold && myFood.get("Salad").getAmount()>=threshold) {
				host.msgKitchenIsReady();
				RestaurantIsOpen=true;
			}
		}
		
		private void CookOrder(final Order o) {
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
			}
			else if(o.getChoice().equals("Steak")) {
				DoGoToSteakGrill();
			}
			else if(o.getChoice().equals("Chicken")) {
				DoGoToChickenGrill();
			}
			else if(o.getChoice().equals("Salad")) {
				DoGoToSaladGrill();
			}
			
			
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			o.state=OrderState.cooking;
			
			timer.schedule(new TimerTask() {
				public void run() {
					print(o+" is cooked!");
					o.state=OrderState.cooked;
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

		
		private void OrderFoodFromMarket() {
			
			double billAmnt=0;
			
			System.out.println("need to order");
			
			orderToMarket.clear(); 
			
			if(myFood.get("Chicken").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Chicken", MAXCAPACITY- myFood.get("Chicken").getAmount()));
				billAmnt+=(MAXCAPACITY - myFood.get("Chicken").getAmount())*prices.get("Chicken");
			}
			
			if(myFood.get("Steak").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Steak", MAXCAPACITY-myFood.get("Steak").getAmount()));
				billAmnt+=(MAXCAPACITY - myFood.get("Steak").getAmount())*prices.get("Steak");
			}
			
			if(myFood.get("Pizza").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Pizza", MAXCAPACITY-myFood.get("Pizza").getAmount()));
				billAmnt+=(MAXCAPACITY - myFood.get("Pizza").getAmount())*prices.get("Pizza");
			}
			
			if(myFood.get("Salad").getAmount()<=threshold) {
				orderToMarket.add(new FoodOrder("Salad", MAXCAPACITY-myFood.get("Salad").getAmount()));
				billAmnt+=(MAXCAPACITY - myFood.get("Salad").getAmount())*prices.get("Salad");
			}
			
			myOrders.add(new InventoryOrder(orderToMarket, ORDER_ID));
			ORDER_ID++;
			waitingForInventory=true;
			
			//msg to market to call in and ask for order
			
			myCashier.msgMadeInventoryOrder(ORDER_ID, billAmnt);
			
			
		}
		
		private void ReorderFood(InventoryOrder reord) {
			System.out.println("sent reorder");
			reord.reorder=false;
			
		}
		
	
		private void ProcessDelivery(ArrayList<FoodOrder> groceries) {
			for(FoodOrder foo: groceries) {
				Food temp = myFood.get(foo.getFood());
				temp.setAmount(temp.getAmount()+foo.getVal());
				myFood.put(foo.getFood(), temp);
				System.out.println("update on " + temp.getChoice() + ": "+ myFood.get(temp.getChoice()).getAmount());
				
			}
			waitingForInventory=false;
			delivery.remove(groceries);
			if(!RestaurantIsOpen) {
				if(myFood.get("Chicken").getAmount()==initialFoodAmnt && myFood.get("Steak").getAmount()==initialFoodAmnt &&
						myFood.get("Pizza").getAmount()==initialFoodAmnt && myFood.get("Salad").getAmount()==initialFoodAmnt) {
					host.msgKitchenIsReady();
					RestaurantIsOpen=true;
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



