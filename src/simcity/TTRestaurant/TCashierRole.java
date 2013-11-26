package simcity.TTRestaurant;

import agent.Role;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import simcity.interfaces.TCashier;







import java.text.DecimalFormat;
import java.util.*;

import simcity.PersonAgent;
import simcity.TTRestaurant.TCookRole.Market;
import simcity.TTRestaurant.TCookRole.OrderStatus;
import simcity.TTRestaurant.THostRole.myWaiters;
import simcity.TTRestaurant.TWaiterRole.customers;

/**
 * Restaurant Cashier Agent
 */

public class TCashierRole extends Role implements TCashier{
	private String name;
	public double budget = 100;
	public double debt = 0; 
	public List<Customers> payingCustomers
	= Collections.synchronizedList(new ArrayList<Customers>());
	public List<Waiters> waiters
	= Collections.synchronizedList(new ArrayList<Waiters>());
	public List<Markets> markets 
	= new ArrayList<Markets>(); 

	private TCookRole cook;
	Map<String, Double> Menu = new HashMap<String, Double>(4);
	
	class Waiters {
		TWaiter thisWaiter; 
		TCustomer thisCust; 
		String order;
		
		public void setWaiter (TWaiter w) {
			thisWaiter = w;
		}
		
		public void setCust (TCustomer c) {
			thisCust = c; 
		}
		public void setOrder (String o) {
			order = o; 
		}
	}
	
	class Customers {
		TCustomer cust; 
		double custMon;
		double check; 
		
		public void setCustomer(TCustomer c) {
			cust = c;
		}
		
		public void setMoney(double money) {
			custMon = money; 
		}
		
		public void setCost(double cost) {
			check = cost; 
		}
			
	}
	
	
	class Markets {
		MarketCashier mart; 
		double bill; 
		
		public void setMarket(MarketCashier m) {
			mart = m;
		}
		public void setBill(double b) {
			bill = b; 
		}
	}
	
	
	public TCashierRole(){
		super();
		this.name = name;
	}

	public void msgComputeBill(TWaiter w, TCustomer c, String order) {
		Waiters wait = new Waiters();
		wait.setWaiter(w);
		wait.setCust(c); 
		wait.setOrder(order); 
		waiters.add(wait); 
		stateChanged();
	}
	

	public void msgHereIsMyMoney(TCustomer c, double m, double cost) {
		Customers cust = new Customers();
		cust.setCustomer(c); 
		cust.setMoney(m); 
		cust.setCost(cost); 
		payingCustomers.add(cust); 
		stateChanged(); 
	}
	
	
	public void msgPayForSupply(MarketCashier m, double b) {
		Markets market = new Markets(); 
		market.mart = m;
		market.bill = b; 
		markets.add(market); 
		stateChanged(); 
	}

	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!waiters.isEmpty()) {
			calculateBill(waiters.get(0).thisWaiter); 
			return true; 
		}
		
		if (!payingCustomers.isEmpty()) {
			sendChange(payingCustomers.get(0).cust); 
			return true; 
		}
		
		
		if (!markets.isEmpty()) {
			payForStock(); 
			return true; 
		}
		

     return false; 
		
	}

	// Actions

	private void calculateBill(TWaiter wait) {
		print("Calculating bill for waiter."); 
		int index = 0; 
		while (waiters.get(index).thisWaiter != wait && index < waiters.size()) {
			index++; 
		}
		waiters.get(index).thisWaiter.msgHereIsCheck(waiters.get(index).thisCust, Menu.get(waiters.get(index).order));
		waiters.remove(index); 
	}
	
	private void sendChange(TCustomer c) {
		print("Giving change back to customer"); 
		int index = 0; 
		while (payingCustomers.get(index).cust != c && index < payingCustomers.size()) {
			index++; 
		}
		if (payingCustomers.get(index).custMon >= payingCustomers.get(index).check) {
			budget += payingCustomers.get(index).check;
			budget = budget * 100; 
			Math.round(budget); 
			budget = budget/100; 
		}
		else {
			print("I'm adding " + budget + " and " + payingCustomers.get(index).custMon); 
			budget += payingCustomers.get(index).custMon; 
		}
		print ("The budget is " + budget); 
		double x = payingCustomers.get(index).custMon - payingCustomers.get(index).check; 
		payingCustomers.get(index).cust.msgChange(x);
		payingCustomers.remove(index); 
	}
	
	
	private void payForStock() {
		print("The bill is " + markets.get(0).bill);
		if (budget < markets.get(0).bill) {
			print("Don't have enough money to pay for supplies. Borrowing money from bank.");
			double x = markets.get(0).bill - budget;
			budget += x; 
			debt += x; 
			print("We now owe "+ debt); 
		}
		budget -= markets.get(0).bill;
		if (budget > debt && debt != 0) {
			budget -= debt;
			debt = 0; 
			print("We don't owe money anymore!"); 
		}
		markets.get(0).mart.msgHereIsPayment(cook, markets.get(0).bill); 
		markets.remove(0); 
		print("Paying for food from the market. Our budget is now $" + budget); 
		
	}
	
	

	public void addFood() {
		Menu.put("Steak", 15.99); 
		Menu.put("Pizza", 10.99); 
		Menu.put("Salad", 5.99); 
		Menu.put("Chicken", 8.99);
	}

	public void setCook(TCookRole cook) {
		this.cook = cook;
	}
	
}




