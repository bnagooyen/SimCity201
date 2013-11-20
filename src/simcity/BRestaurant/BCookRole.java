package simcity.BRestaurant;

import agent.Agent;
import restaurant.BCustomerRole.AgentEvent;
import restaurant.BWaiterRole.Table;
import restaurant.gui.BHostGui;
import restaurant.interfaces.BCook;
import restaurant.interfaces.BWaiter;
import restaurant.BWaiterRole;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BCookRole extends Agent implements BCook {

	private int nextMarket=0;
	int amountNeeded;
	private boolean needtoOrder=false;
	private boolean alreadyOrdered=false;
	
	public BCookRole(String name, List<BMarketRole> markets) {
			super();
			
			this.name=name;
			this.markets=markets;
			
			foodStock.put("steak",new BFood("steak", 10, 5,4,5,16,0));
			foodStock.put("chicken",new BFood("chicken", 5,10,5,5,11,0));
			foodStock.put("salad",new BFood("salad", 10, 5,10,5,6,0));
			foodStock.put("pizza",new BFood("pizza", 10, 5,10,5,9,0));
			
	    }
	 
	 

	private Map<String,BFood> foodStock = new HashMap<String,BFood>();
	public List<Order> pendingOrders = new ArrayList<Order>();
	private List<BFood> OrdertoMarket = new ArrayList<BFood>();
	List<BMarketRole> markets = new ArrayList<BMarketRole>();

	public enum Status
	{done, cooking, pending};

	String name;
	public Status status;
	private BWaiter waiter;

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

	

	public void setToPause(){
		pauseChange();
		stateChanged();
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
		for(int i = 0; i< foodOrder.size(); i++)
    	{
    		foodStock.get(foodOrder.get(i).typeOfFood).quantity = foodStock.get(foodOrder.get(i).typeOfFood).quantity+ foodOrder.get(i).amount;
    		
    	}
		
		
		alreadyOrdered=false;
		stateChanged();
	}
	
	public void msgCannotCompleteOrder(){
		nextMarket = (nextMarket + 1) % markets.size();
		alreadyOrdered=false;
		stateChanged();
	}
	
	
	public void foodFinished(Order order){//called by timer
		order.status = Status.done;
		stateChanged();
	    }





	protected boolean pickAndExecuteAnAction() {

		for (Order order : pendingOrders){
			if(order.status==Status.done)
				placeOrderDown(order);

		}

		for (Order order : pendingOrders){
			if(order.status==Status.pending){
				CookFood(order);
				foodFinished(order);
			}
		}

		if(alreadyOrdered==false){
		for(Map.Entry<String, BFood> food : foodStock.entrySet()) {
			if(food.getValue().quantity < food.getValue().threshold){
				
				makeMarketOrder();
				
			}
		}
		}

		return false;
	}	


//Actions	
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
	
	private void makeMarketOrder(){
		needtoOrder=false;
		alreadyOrdered=true;
		
		for(Map.Entry<String, BFood> food : foodStock.entrySet()) {
			if(food.getValue().quantity < food.getValue().threshold){
				
				amountNeeded=food.getValue().capacity-food.getValue().quantity;
				BFood thisOrder=new BFood(food.getValue().typeOfFood, 0, 0, 0, 0, 0, amountNeeded);
				OrdertoMarket.add(thisOrder);
				markets.get(nextMarket).msgHereisCookOrder(this, OrdertoMarket);
				
			}
		}
	}

}