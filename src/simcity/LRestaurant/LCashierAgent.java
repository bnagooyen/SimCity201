package simcity.LRestaurant;

import agent.Agent;
//import restaurant.gui.HostGui;


import java.util.*;

import simcity.LRestaurant.LCustomerRole.AgentEvent;
//import simcity.LRestaurant.interfaces.Market;
import simcity.test.mock.EventLog;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;

/**
 * Restaurant Cashier Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LCashierAgent extends Agent implements LCashier {
	Timer timer = new Timer();
	String name;
	int restMoney;
	public EventLog log = new EventLog();
	
	public List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Transaction>transactions = Collections.synchronizedList(new ArrayList<Transaction>());
//	public List<Bill>bills = Collections.synchronizedList(new ArrayList<Bill>());
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
	public enum OrderState {pending, computing, done};
	public enum TransState {pending, computing, done};
	//private CookGui cookGui;
	
	public LCashierAgent(String name){
		super();
		this.name = name;
		restMoney = 100;
		foods.put("P", new Food("P", 8)); //choice, cookTime, amount, capacity, threshold
		foods.put("St", new Food("St", 15));
		foods.put("S", new Food("S", 5));
		foods.put("Ch", new Food("Ch", 10));
		
		
		}
	
	
	// Messages
	
	public void msgComputeCheck(String choice, LCustomer c, LWaiter w) {//from animation
		//print("Received order from " + w);
		orders.add(new Order(choice,c,w,OrderState.pending));
		stateChanged();
	}
	
	public void msgHereIsPayment(LCustomer c, int money, int check){
		transactions.add(new Transaction(c, money, check, TransState.pending));
		stateChanged();
	}
	
//	public void msgHereIsSupplyCheck(int bill, LMarket market) {
//		bills.add(new Bill(bill, market, false));
//	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
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
		
//		synchronized(bills){
//			for(Bill b : bills){
//				if(!b.didPay){
//					PayMarket(b);
//					return true;
//				}
//			}
//		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	// Actions

//	private void PayMarket(final Bill b) {
//		if(restMoney >= b.amount){
//			Do("Paying Market "+b.amount);
//			restMoney -= b.amount;
//			b.didPay = true;
//			b.m.msgHereIsMoney(b.amount);
//			bills.remove(b);
//		}
//		else{
//			Do("Need to run to the bank to pay market bill");
//			goToBank();
//			 timer.schedule(new TimerTask() {
//                 public void run() {
//                     b.didPay = true;
//                 }
//	         },
//	         5000);
//			Do("Paying Market "+b.amount);
//			restMoney -= b.amount;
//			b.m.msgHereIsMoney(b.amount);
//			bills.remove(b);
//		}
//	}

	private void CreateCheck(Order o){
		Do("Creating Check");
		o.w.msgHereIsCheck(foods.get(o.choice).price, o.c);
		Do("Check is "+foods.get(o.choice).price);
		orders.remove(o);	
	}
	
	private void ChargeAction(Transaction t){
		Do("Charging Customer");
		
		if(t.custMoney <= t.check){
			Do("Customer is unable to pay");
			t.c.msgHereIsChange(-1);
			transactions.remove(t);	
		}
		else{
			int giveBack = t.custMoney-t.check;
			restMoney += t.check;
			t.c.msgHereIsChange(giveBack);
			Do("Customer's change "+giveBack);
			transactions.remove(t);	
		}
	}
	
	private void goToBank(){
        restMoney += 500;
	}

	//utilities

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
//	
//	public class Bill{
//		int amount;
//		public LMarket m;
//		boolean didPay;
//		
//		Bill(int a, LMarket mark, boolean p){
//			amount = a;
//			m = mark;
//			didPay = p;
//		}
//	}

}

