package simcity.DRestaurant;

import simcity.PersonAgent;
//import simcity.DRestaurant.DCookRole.InventoryOrder.InventoryOrderState;
import simcity.DRestaurant.DOrder.OrderState;
import simcity.DRestaurant.gui.DCookGui;
import simcity.DRestaurant.DWaiterRole;
import agent.Role;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
//import simcity.interfaces.DMarket;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class DCookRole extends Role implements DCook{

	private DProducerConsumerMonitor theMonitor;
	
	Timer timer = new Timer();
	private String name;
	private Map<String, DFood> myFood = new HashMap<String, DFood>();
	//public HostGui hostGui = null;
	private int initialFoodAmnt= 2;
	private static final int MAXCAPACITY=10;
	private int threshold = 1;
	//private final int NUM_MARKETS = 3;
    
    public DCookGui CookGui = null;
	
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	DCashier myCashier;
	private DWaiterRole waiter;
	private DHostRole host;

	private Map<String, Double> prices = new HashMap<String, Double>();
	private boolean waitingForInventory; // order at once, don't order when waiting for an order to arrive
	//private boolean reOrdering;
	
	Map<String, Boolean> grillOccupied = new HashMap<String, Boolean>();
	
	ArrayList<ArrayList<DFoodOrder>> delivery= new ArrayList<ArrayList<DFoodOrder>>();
	List<DOrder> orders =  Collections.synchronizedList(new ArrayList<DOrder>());
	//ArrayList<DMarketRole> markets = new ArrayList<DMarketRole>();
	//List<InventoryOrder> myOrders =  Collections.synchronizedList(new ArrayList<InventoryOrder>());
	private int ORDER_ID;
	ArrayList<DFoodOrder> orderToMarket = new ArrayList<DFoodOrder>();
	//private int marketToSendOrdersTo;

	
	boolean RestaurantIsOpen, CheckedAtFirst, valsAreSet;
	
	public DCookRole(PersonAgent p) {
		super(p);
		
		//this.name = name;
		
		//GRADER: CHANGE KITCHEN VALS HERE
		//myFood.put("Chicken", new Food("Chicken", 5, X));
		// or change initialFoodAmnt variable, threshold to reaorder is 1/2 of that value
		
		//fill up kitchen with food
		myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
		myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
		myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
		myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));
		//System.out.println("INITALIZE VALUES OF KITCHEN AND HIT 'SET' IN ORDER FOR COOK TO BEGIN CHECKING IF READY FOR OPEN");
		//System.out.println("Cook Kitchen Inventory is initialized to "+ initialFoodAmnt+ " and threshold is "+ threshold);
		//System.out.println("Maximum capacity in kitchen is 10");
	
		grillOccupied.put("Pizza", false);
		grillOccupied.put("Steak", false);
		grillOccupied.put("Chicken", false);
		grillOccupied.put("Salad", false);
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
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
	

	@Override
	public String getName() {
		return name;
	}

	// Messages

	//hack!
	
	public void AddHost(DHostRole h) {
		//System.out.println("host added to cook");
		host=h;
	}
	
	public void AddCashier(DCashier h) {
		//System.out.println("host added to cook");
		myCashier=h;
	}
	
	public void msgAddWaiter(DWaiterRole w) {
		waiter=w;
	}
	
//	public void msgAddMarket(DMarketRole m) {
//		//System.out.println("added");
//		markets.add(m);
//	}
	
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
			myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
			myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
			myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
			myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));
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
			myFood.put("Chicken", new DFood("Chicken", 5, initialFoodAmnt));
			myFood.put("Steak", new DFood("Steak", 10, initialFoodAmnt));
			myFood.put("Salad", new DFood("Salad", 3, initialFoodAmnt));
			myFood.put("Pizza", new DFood("Pizza", 6, initialFoodAmnt));

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
	
	public void msgTimeToCheckStand() {
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
	
//	public void msgShouldIPayThisBill(double amt, DMarket ma) {
//		System.out.println("cashier received shouldipaythisbill");
//		for(InventoryOrder o: myOrders){
//			if(o.market==ma) {
//				if(o.billExpected==amt)
//					o.state=InventoryOrderState.approved;
//				else 
//					o.state=InventoryOrderState.denied;	
//				stateChanged();
//				break;
//			}
//		}
//	}
	
	public void msgHereIsAnOrder(DOrder order) {
		orders.add(new DOrder(order.getChoice(), order.getTablenum(), order.getWaiter()));
		System.out.println("cook received order of "+ order.getChoice()+ " from table "+order.tablenum);
		stateChanged();
	}
	
//	public void msgHereIsYourFoodOrder(String food, int val) {
//		delivery.add(new FoodOrder(food, val));
//		System.out.println("received order of "+ food + " for "+ val + " from market");
//		stateChanged();
//	}
//	
//	public void msgCouldNotFulFillFullOrderOf(String foo) {
//		if(InventoryMarketTracker.get(foo)<=markets.size()) // if at last market possible or below
//		InventoryMarketTracker.put(foo, InventoryMarketTracker.get(foo)+1);
//	}
	
	public void msgHereIsYourFoodOrder(ArrayList<DFoodOrder> dlv) {
		delivery.add(dlv);
		System.out.println("**received order from cook");
		waitingForInventory=false;
		stateChanged();
	}
	
//	public void msgCouldNotFulfillThese(ArrayList<DFoodOrder> reorderlist, int ORDERID) {
//		
//		
//		for(InventoryOrder order: myOrders) {
//			if(order.getID()==ORDERID) {
//				System.out.println("got inventory orderback from "+order.getMarketOrderingFrom());
//				order.incMarketOrderingFrom();
//				order.state=InventoryOrderState.needsReorder;
//				order.myorder=reorderlist;
//				stateChanged();
//			}
//		}
//	}
//	
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

	public void setGui(DCookGui gui) {
		CookGui = gui;
	}
	
	public void setMonitor(DProducerConsumerMonitor m) {
		theMonitor= m;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
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
		if(!delivery.isEmpty()) {
			ProcessDelivery(delivery.get(0));
			return true;
		}
//		if(!waitingForInventory) {
//			if((myFood.get("Chicken").getAmount()<=threshold || myFood.get("Pizza").getAmount()<=threshold ||
//					myFood.get("Steak").getAmount()<=threshold || myFood.get("Salad").getAmount()<=threshold)) {
//						OrderFoodFromMarket();
//						return true;
//					}
//		}
//		for(InventoryOrder o: myOrders) {
//			if(o.state==InventoryOrderState.approved || o.state==InventoryOrderState.denied) {
//				NotifyCashierOfVerification(o);
//				return true;
//			}
//		}
//	
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
		
//		for(InventoryOrder i: myOrders) {
//			if(i.state==InventoryOrderState.needsReorder) {
//				ReorderFood(i);
//				return true;
//			}
//		}
//		
//		
//		if(!RestaurantIsOpen && valsAreSet && !waitingForInventory) {
//			CheckIfFullyStocked(); 
//			return true;
//		}
		
		CheckRotatingStand();
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
	
		private void CheckRotatingStand() {
			//System.err.println("checking the stand");
			DOrder o = theMonitor.remove();
			if(o!=null) {
				System.out.println("Order Received from Revolving Table");
				DOrder newOrder = new DOrder(o.getChoice(), o.getTablenum(), o.getWaiter());
				orders.add(newOrder);
			}
			else {
				timer.schedule(new TimerTask() {
					public void run() {
						msgTimeToCheckStand();
					}
				}, 2000);
			}
		}
	
//		private void NotifyCashierOfVerification(InventoryOrder o) {
//			System.out.println("notifying cashier");
//			if(o.state==InventoryOrderState.approved) {
//				myCashier.msgAnswerVerificationRequest(true);
//			}
//			if(o.state==InventoryOrderState.denied) {
//				myCashier.msgAnswerVerificationRequest(false);
//			}
//			myOrders.remove(o);
//		}
		private void ClearPlating(DOrder o) {
//			System.err.println("***");
			DoClearPlating(o.getChoice().substring(0,2));
			orders.remove(o);
		}
//		private void CheckIfFullyStocked() {
//			//System.out.println("called");
//			if(myFood.get("Chicken").getAmount()>=threshold && myFood.get("Steak").getAmount()>=threshold &&
//					myFood.get("Pizza").getAmount()>=threshold && myFood.get("Salad").getAmount()>=threshold) {
//				host.msgKitchenIsReady();
//				RestaurantIsOpen=true;
//			}
//		}
		
		private void CookOrder(final DOrder o) {
//			System.out.println("asdfjlaksdjflkasj");
			DFood food=myFood.get(o.getChoice());
			
			if(food.getAmount()==0) {
					System.out.println("Out of "+ food.getChoice());
					o.getWaiter().msgOutOfFood(o);
					orders.remove(o);
					return;
				}
			
			food.setAmount(food.getAmount()-1);
			myFood.put(o.getChoice(), food);
			
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
				@Override
				public void run() {
					print(o+" is cooked!");
					o.state=OrderState.cooked;
					//isHungry = false;
					stateChanged();
				}
			},
			myFood.get(o.getChoice()).getCookTime()*1000);
			
		}


		private void PlateOrderAndCallWaiter(DOrder o) {
			
			o.state=OrderState.plated;
			//System.err.println("**");
			//if(o.state==OrderState.plated) System.err.println("changed!");
			
			
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
		
//		private void OrderFoodFromMarket() {
//			/*
//			 	if(food.getAmount()==threshold) // made it ==, not <= so won't keep ordering
//				{
//					System.out.println("Out of "+ food.getChoice()+"!");
//					markets.get(InventoryMarketTracker.get(o.getChoice())).msgHereIsAnInventoryOrder(o.getChoice(), threshold);
//					System.out.println("Made an order to mkt#"+ InventoryMarketTracker.get(o.getChoice()) + " for " + threshold);
//					
//				}
//			 */
//			System.out.println("need to order");
//			double billAmnt = 0;
//			orderToMarket.clear(); //restart a new order
//			
//			if(myFood.get("Chicken").getAmount()<=threshold) {
//				orderToMarket.add(new DFoodOrder("Chicken", MAXCAPACITY- myFood.get("Chicken").getAmount()));
//				billAmnt+=(MAXCAPACITY - myFood.get("Chicken").getAmount())*prices.get("Chicken");
//			}
//			
//			if(myFood.get("Steak").getAmount()<=threshold) {
//				orderToMarket.add(new DFoodOrder("Steak", MAXCAPACITY-myFood.get("Steak").getAmount()));
//				billAmnt+=(MAXCAPACITY - myFood.get("Steak").getAmount())*prices.get("Steak");
//			}
//			
//			if(myFood.get("Pizza").getAmount()<=threshold) {
//				orderToMarket.add(new DFoodOrder("Pizza", MAXCAPACITY-myFood.get("Pizza").getAmount()));
//				billAmnt+=(MAXCAPACITY - myFood.get("Pizza").getAmount())*prices.get("Pizza");
//			}
//			
//			if(myFood.get("Salad").getAmount()<=threshold) {
//				orderToMarket.add(new DFoodOrder("Salad", MAXCAPACITY-myFood.get("Salad").getAmount()));
//				billAmnt+=(MAXCAPACITY - myFood.get("Salad").getAmount())*prices.get("Salad");
//			}
////			for(FoodOrder order: orderToMarket) {
////				System.out.println(order.getFood()+"..."+order.getVal());
////			}
//			markets.get(0).msgHereIsAnInventoryOrder(orderToMarket, ORDER_ID, myCashier);
//			myOrders.add(new InventoryOrder(markets.get(0), billAmnt, ORDER_ID));
//			ORDER_ID++;
//			waitingForInventory=true;
//		}
//		
//		private void ReorderFood(InventoryOrder reord) {
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
//			}
//			
//			double billAmnt =0;
//			for(DFoodOrder ord: reord.myorder) {
//				if(ord.getFood().equals("Chicken")) {
//					billAmnt+=ord.getVal()*prices.get("Chicken");
//				}
//				if(ord.getFood().equals("Steak")) {
//					billAmnt+=ord.getVal()*prices.get("Steak");
//				}
//				if(ord.getFood().equals("Salad")) {
//					billAmnt+=ord.getVal()*prices.get("Salad");
//				}
//				if(ord.getFood().equals("Pizza")) {
//					billAmnt+=ord.getVal()*prices.get("Pizza");
//				}
//			}
//			
//			
//			
//			System.out.println("sent reorder");
//			markets.get(reord.mktOrderingFrom-1).msgHereIsAnInventoryOrder(reord.myorder, ORDER_ID, myCashier);
//			for(InventoryOrder ord: myOrders) {
//				if(ord.orderID==ORDER_ID-1) {
//					ord.billExpected-=billAmnt;
//				}
//			}
//			myOrders.add(new InventoryOrder(markets.get(reord.mktOrderingFrom-1), billAmnt, ORDER_ID));
//			waitingForInventory=true;
//			ORDER_ID++;
//			reord.state=InventoryOrderState.ordered;
//		
//			
//			
//		}
		
	
		private void ProcessDelivery(ArrayList<DFoodOrder> groceries) {
			for(DFoodOrder foo: groceries) {
				DFood temp = myFood.get(foo.getFood());
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
		
		
//		static class InventoryOrder {
//			int orderID;
//			ArrayList<DFoodOrder> myorder;
//			int mktOrderingFrom;
//			DMarket market;
//			double billExpected;
//			enum InventoryOrderState {ordered, needsReorder, approved, denied};
//			InventoryOrderState state;
//			InventoryOrder (ArrayList<DFoodOrder> mo, int orderid) {
//				myorder=mo;
//				mktOrderingFrom=1;
//				orderID=orderid;
//				state=InventoryOrderState.ordered;
//				//reorder=false;
//				//sent=false;
//			}
//			
//			InventoryOrder(DMarket mkt, double billAmt, int ord) {
//				market= mkt;
//				billExpected=billAmt;
//				//sent=true;
//				orderID = ord;
//			}
//			
//			DMarket getMarket() {
//				return market;
//			}
//			int getMarketOrderingFrom() {
//				return mktOrderingFrom;
//			}
//			
//			void incMarketOrderingFrom(){
//				mktOrderingFrom++;
//			}
//			
//			int getID() {
//				return orderID;
//			}
//			}
//			boolean getReorder() {
//				return reorder;
//			}
//			void setReorder(boolean r) {
//				reorder=r;
//			}
//			
//		}

		
}



