package simcity.LRestaurant;

import agent.Agent;
//import restaurant.gui.HostGui;


import agent.Role;

import java.util.*;

import simcity.PersonAgent;
import simcity.LRestaurant.LCustomerRole.AgentEvent;
//import simcity.LRestaurant.interfaces.Market;
import simcity.test.mock.EventLog;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCook;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LHost;
import simcity.interfaces.LWaiter;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;

/**
 * Restaurant Cashier Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LCashierRole extends Role implements LCashier, RestaurantCashier {
	Timer timer = new Timer();
	String name;
	int restMoney;
	public EventLog log = new EventLog();
	boolean goHome = false;
	boolean here;
	
	LHost host;
	LCook cook;
	
	public List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Transaction>transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public List<Bill>bills = Collections.synchronizedList(new ArrayList<Bill>());
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
	public enum OrderState {pending, computing, done};
	public enum TransState {pending, computing, done};
	//private CookGui cookGui;
	
	public LCashierRole(){
		super();
		//this.name = p.getName();
		here = true;
		restMoney = 100;
		foods.put("P", new Food("P", 8)); //choice, cookTime, amount, capacity, threshold
		foods.put("St", new Food("St", 15));
		foods.put("S", new Food("S", 5));
		foods.put("Ch", new Food("Ch", 10));
		
		
		}
	
	
	// Messages
	
	public void msgGoHome(int cash) {
		myPerson.money += cash;
		goHome = true;
		stateChanged();		
	}
	
	
	public void msgBillFromMarket(double check, MarketCashier marketCashier,MarketManager manager) {
		bills.add(new Bill(check, marketCashier, false));
	}

	
	public void msgComputeCheck(String choice, LCustomer c, LWaiter w) {//from animation
		//print("Received order from " + w);
		orders.add(new Order(choice,c,w,OrderState.pending));
		stateChanged();
	}
	
	public void msgHereIsPayment(LCustomer c, int money, int check){
		transactions.add(new Transaction(c, money, check, TransState.pending));
		stateChanged();
	}
	
	public void msgHereIsSupplyCheck(double check, MarketCashier mc){ 
		//old interaction with market
		bills.add(new Bill(check, mc, false));
	}
	
//	public void msgHereIsSupplyCheck(int bill, LMarket market) {
//		bills.add(new Bill(bill, market, false));
//	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
		if(here){
			tellHost();
			return true;
		}
		
		synchronized(orders){
			for (Order order : orders) {
				if(order.state == OrderState.pending){
					CreateCheck(order);
					order.state = OrderState.computing;
					return true;
				}
			}
		}
		
		synchronized(transactions){
			for (Transaction trans : transactions) {
				if(trans.state == TransState.pending){
					ChargeAction(trans);
					trans.state = TransState.computing;
					return true;
				}
			}
		}
		
		synchronized(bills){
			for(Bill b : bills){
				if(!b.didPay){
					PayMarket(b);
					return true;
				}
			}
		}
		
		if(goHome) {
			goHome();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	// Actions
	
	private void tellHost(){
		host.msgIAmHere(this, "cashier");
		here = false;
	}
	
	private void goHome(){
		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Going home");
		Do("Going home");
		isActive = false;
		here = true;
		goHome = false;
		host.msgHereIsMoney(restMoney);
	}
	
	private void PayMarket(final Bill b) {
		cook.msgMarketCheck(b.amount);
		if(restMoney >= b.amount){
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Paying market");
			Do("Paying Market "+b.amount);
			restMoney -= b.amount;
			b.didPay = true;
			b.mc.msgHereIsPayment(this,b.amount);
			bills.remove(b);
		}
		else{
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Need to run to the bank to pay market bill");
			Do("Need to run to the bank to pay market bill");
			goToBank();
			 timer.schedule(new TimerTask() {
                 public void run() {
                     b.didPay = true;
                 }
	         },
	         5000);
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Paying market");
			Do("Paying Market "+b.amount);
			restMoney -= b.amount;
			b.mc.msgHereIsPayment(this,b.amount);
			bills.remove(b);
		}
	}

	private void CreateCheck(Order o){
		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Creating check");
		Do("Creating Check");
		o.w.msgHereIsCheck(foods.get(o.choice).price, o.c);
		Do("Check is "+foods.get(o.choice).price);
		orders.remove(o);	
	}
	
	private void ChargeAction(Transaction t){
		AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Charging customer");
		Do("Charging Customer");
		
		if(t.custMoney <= t.check){
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Customer is unable to pay");
			Do("Customer is unable to pay");
			t.c.msgHereIsChange(-1);
			transactions.remove(t);	
		}
		else{
			int giveBack = t.custMoney-t.check;
			restMoney += t.check;
			t.c.msgHereIsChange(giveBack);
			AlertLog.getInstance().logInfo(AlertTag.LRestaurant, "LCashierRole", "Here is customer's change");
			Do("Customer's change "+giveBack);
			transactions.remove(t);	
		}
	}
	
	private void goToBank(){
        restMoney += 500;
	}

	//utilities

	public void setHost(LHost host){
		this.host = host;
	}
	
	public void setCook(LCook cook){
		this.cook = cook;
	}
	
	public class Food{
		String choice;
		int price;
		
		Food(String c, int p){
			choice = c;
			price = p;
		
		}
	}
	
	public class Order{
		LCustomer c;
		String choice;
		public OrderState state;
		public LWaiter w;
		
		//Additional for V2.1
		//int cookTime = 2;
		
		Order(String c, LCustomer cust, LWaiter w, OrderState o){
			choice = c;
			this.w = w;
			this.c = cust;
			state = o;
			
		}
		
	}
	
	public class Transaction{
		public LCustomer c;
		int custMoney;
		int check;
		TransState state;
		
		Transaction(LCustomer cust, int cm, int ch, TransState t){
			c = cust;
			custMoney = cm;
			check = ch;
			state = t;
		
		}
	}

	public class Bill{
		double amount;
		public MarketCashier mc;
		boolean didPay;
		
		Bill(double a, MarketCashier mark, boolean p){
			amount = a;
			mc = mark;
			didPay = p;
		}
	}

}

