package simcity.BRestaurant;

import agent.Agent;
import agent.Role;
import simcity.PersonAgent;
import simcity.BRestaurant.*;
import simcity.interfaces.*;
import simcity.BRestaurant.gui.*;
import simcity.KRestaurant.KRestaurantOrder;
import simcity.KRestaurant.ProducerConsumerMonitor;
import simcity.KRestaurant.KCookRole.marketOrderState;
import simcity.KRestaurant.KCookRole.orderState;
import simcity.Market.MFoodOrder;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BCookRole extends Role implements BCook {

	private int nextMarket=0;
	int amountNeeded;
	private boolean needtoOrder=false;
	private boolean alreadyOrdered=false;
	boolean goHome=false;
	
	private BCashierRole cashier;
	private BOrderStand theMonitor;
	
	public BCookRole() {// , List<BMarketRole> markets) {
			super();
			
			this.name=name;
			
			foodStock.put("steak",new BFood("steak", 10, 5,4,5,16,0));
			foodStock.put("chicken",new BFood("chicken", 5,10,5,5,11,0));
			foodStock.put("salad",new BFood("salad", 10, 5,10,5,6,0));
			foodStock.put("pizza",new BFood("pizza", 10, 5,10,5,9,0));
			
	    }
	 
	 

	private Map<String,BFood> foodStock = Collections.synchronizedMap(new HashMap<String,BFood>());
	public List<Order> pendingOrders = Collections.synchronizedList(new ArrayList<Order>());
	private List<MFoodOrder> OrdertoMarket = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	List<MarketManager> markets = Collections.synchronizedList(new ArrayList<MarketManager>());
	List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public enum Status
	{done, cooking, pending};

	String name;
	public Status status;
	private BWaiter waiter;
	boolean arrived;
	private BHostRole host;
	
	Timer timer= new Timer();

	public void setWaiter(BWaiter waiter) {
		this.waiter = waiter;
	}


	public class Order{

		BWaiter waiter;
		String choice;
		int tablenumber;
		Status status;
	}

	

	
	
	

	//messages

	public void msgHereisanOrder(BWaiter w, String choice, int tablenumber){

		Order thisOrder = new Order();
		thisOrder.choice=choice;
		thisOrder.tablenumber=tablenumber;
		thisOrder.waiter=w;
		thisOrder.status=Status.pending;

		pendingOrders.add(thisOrder);

		stateChanged();
	}
	
	
	public void msgHereisCompletedOrder(List<BFood> foodOrder){
		synchronized(foodOrder){
		for(int i = 0; i< foodOrder.size(); i++)
    	{
    		foodStock.get(foodOrder.get(i).typeOfFood).quantity = foodStock.get(foodOrder.get(i).typeOfFood).quantity+ foodOrder.get(i).amount;
    		
    	}
		}
		
		alreadyOrdered=false;
		stateChanged();
	}
	
	public void msgCannotCompleteOrder(){
		nextMarket = (nextMarket + 1) % markets.size();
		alreadyOrdered=false;
		stateChanged();
	}
	
	public void msgHereIsDelivery(List<MFoodOrder> canGiveMe, double bill, MarketManager manager, MarketCashier cashier) {
		marketOrders.add(new MarketOrder(bill, cashier));
		for(MFoodOrder f : canGiveMe) {
			Do("got " + f.amount + " of " + f.type);
			BFood currentFood = foodStock.get(f.type);
			currentFood.amount += f.amount;
		}
		stateChanged();
	}
	
	 public void msgGoHome(double paycheck) {
         myPerson.money += paycheck;
         goHome = true;
         stateChanged();
 }
	
	public void foodFinished(Order order){//called by timer
		order.status = Status.done;
		stateChanged();
	    }





	public boolean pickAndExecuteAnAction() {
		
		if (arrived){
			tellHost();
			return true;
		}
		
		synchronized(marketOrders) {
			for(MarketOrder o : marketOrders) {
				giveCashierCheck(o);
				return true;
			}
		}
		
		synchronized(pendingOrders) {
			for (Order order : pendingOrders){
				if(order.status==Status.done)
					placeOrderDown(order);
				return true;
			}
		}
		synchronized(pendingOrders) {
			for (Order order : pendingOrders){
				if(order.status==Status.pending){
					CookFood(order);
					foodFinished(order);
					return true;
				}
			}
		}
		if(alreadyOrdered==false){
			synchronized(foodStock) {
				for(Map.Entry<String, BFood> food : foodStock.entrySet()) {
					if(food.getValue().quantity < food.getValue().threshold){
						
						makeMarketOrder();
						return true;
					}
				}
			}
		}
		if(goHome){
			goHome();
		}
		else{
			checkRotatingStand();
		
		}
		return false;
	}	


//Actions	
	
	private void giveCashierCheck(MarketOrder o) {
		cashier.msgHereisCheckfromMarket(o.check, o.cashier);
		marketOrders.remove(o);
	}
	
	  private void tellHost() {
 	     Do("telling manager I'm here at work");
 	     arrived = false;
 	     host.msgIAmHere(this, "cashier");
 	 }
	  
	private void CookFood(Order order){
		if(foodStock.get(order.choice).quantity >= 1){
			
		foodStock.get(order.choice).quantity--;
		order.status=Status.cooking;
		timer.schedule(new TimerTask() {

			public void run() {
				print("Done cooking");

				}
			},	
		5000);
		}
		else {
			order.waiter.msgOutOfThatFood(order.tablenumber);
			pendingOrders.remove(order);
			
		}
	}

	private void placeOrderDown(Order order){
		//animation to place order
		order.waiter.msgOrderisReady(order.tablenumber, order.choice);
		pendingOrders.remove(order);
	}
	
	private void checkRotatingStand() {
		BRotatingOrders newOrder = theMonitor.remove();
		if(newOrder != null) {
			Order o = new Order();
			o.waiter=newOrder.w;
			o.tablenumber=newOrder.table;
			o.choice=newOrder.choice;
			o.status=Status.pending;
			pendingOrders.add(o);
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
	
	private void makeMarketOrder(){
		needtoOrder=false;
		alreadyOrdered=true;
		
		synchronized(foodStock) {
			for(Map.Entry<String, BFood> food : foodStock.entrySet()) {
				if(food.getValue().quantity < food.getValue().threshold){
					
					amountNeeded=food.getValue().capacity-food.getValue().quantity;
					MFoodOrder thisOrder=new MFoodOrder(food.getValue().typeOfFood,amountNeeded);
					OrdertoMarket.add(thisOrder);
					markets.get(nextMarket).msgIAmHere((Role)this, OrdertoMarket, "BRestaurant", "cook");
					
				}
			}
		}
	}
	
    private void goHome() {
        Do("going home");
        isActive = false;
        goHome = false;
       
}
	
	//utilities

	class MarketOrder {
		double check;
		MarketCashier cashier;
		
		public MarketOrder(double check, MarketCashier cashier) {
			this.check = check;
			this.cashier = cashier;
		}
	}
	
	public void setCashier(BCashierRole c) {
		this.cashier = c;
	}
	
	public void setMonitor(BOrderStand m) {
		theMonitor = m;
	}
	
	private void msgTimeToCheckStand() {
		stateChanged();
	}
}