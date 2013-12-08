package simcity.TRestaurant;

import agent.Role;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;
import simcity.interfaces.TCashier;

import java.text.DecimalFormat;
import java.util.*;

import simcity.PersonAgent;

/**
 * Restaurant Cashier Agent
 */

public class TCashierRole extends Role implements TCashier, RestaurantCashier{
	private String name;
	public double budget = 100;
	public double debt = 0; 
    boolean arrived;
	public List<Customers> payingCustomers
	= Collections.synchronizedList(new ArrayList<Customers>());
	public List<Waiters> waiters
	= Collections.synchronizedList(new ArrayList<Waiters>());
	public List<Markets> markets 
	= new ArrayList<Markets>(); 

	THostRole host;
	boolean goHome = false;
	private TCookRole cook;
	Map<String, Double> Menu = new HashMap<String, Double>(4);
	
	enum BillState  
	{received, waitingForConfirmation, paying}; 
	
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
		BillState billState; 
		
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
	
	
	public void msgBillFromMarket(double b, MarketCashier m, MarketManager manager) {
		Markets market = new Markets(); 
		market.mart = m;
		market.bill = b;
		market.billState = BillState.received; 
		markets.add(market); 
		stateChanged(); 
	}

	public void msgGoHome(double moneys) {
		myPerson.money += moneys;
		goHome = true;
		stateChanged();
	}
	
	public void msgBillIsCorrect (MarketCashier mc) {
		for (Markets m: markets) {
			if (m.mart == mc) {
				m.billState = BillState.paying; 
			}
		}
		stateChanged(); 
	}
	
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(arrived) {
			tellHost();
			return true;
		}
		if (!waiters.isEmpty()) {
			calculateBill(waiters.get(0).thisWaiter); 
			return true; 
		}
		
		if (!payingCustomers.isEmpty()) {
			sendChange(payingCustomers.get(0).cust); 
			return true; 
		}
		
		
		if (!markets.isEmpty()) {
			for (Markets m: markets) {
				if (m.billState == BillState.received) {
					AskForConfirmation(m);
					return true; 
				}
				if (m.billState == BillState.paying) {
					payForStock(m); 
					return true; 
				}
			}
		}
		
		if(goHome) {
			goHome();
			return true;
		}
		

     return false; 
		
	}

	// Actions

	private void tellHost() {
		AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Telling manager I can work");
		Do("Telling manager I can work");
		arrived = false;
		host.msgIAmHere(this, "Cashier");
	}
	
	private void calculateBill(TWaiter wait) {
		AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Calculating bill for waiter");
		Do("Calculating bill for waiter."); 
		int index = 0; 
		while (waiters.get(index).thisWaiter != wait && index < waiters.size()) {
			index++; 
		}
		waiters.get(index).thisWaiter.msgHereIsCheck(waiters.get(index).thisCust, Menu.get(waiters.get(index).order));
		waiters.remove(index); 
	}
	
	private void sendChange(TCustomer c) {
		AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Giving change back to customer");
		Do("Giving change back to customer"); 
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
			AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Adding to budget");
			Do("I'm adding " + budget + " and " + payingCustomers.get(index).custMon); 
			budget += payingCustomers.get(index).custMon; 
		}
		print ("The budget is " + budget); 
		double x = payingCustomers.get(index).custMon - payingCustomers.get(index).check; 
		payingCustomers.get(index).cust.msgChange(x);
		payingCustomers.remove(index); 
	}
	
	private void AskForConfirmation(Markets m) {
		cook.msgPleaseConfirmBill(m.mart);
		m.billState = BillState.waitingForConfirmation; 
		
	}
	
	
	private void payForStock(Markets m) {
		print("The bill is " + m.bill);
		if (budget < m.bill) {
			AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Don't have enough money to pay for supplies. Borrowing money from bank");
			Do("Don't have enough money to pay for supplies. Borrowing money from bank.");
			double x = m.bill - budget;
			budget += x; 
			debt += x; 
			Do("We now owe "+ debt); 
		}
		budget -= m.bill;
		if (budget > debt && debt != 0) {
			budget -= debt;
			debt = 0; 
			AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "We don't owe money anymore!");
			Do("We don't owe money anymore!"); 
		}
		m.mart.msgHereIsPayment(cook, m.bill); 
		markets.remove(m); 
		AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Paying for food from the market.");
		Do("Paying for food from the market. Our budget is now $" + budget); 
		
	}
	
	private void goHome() {
		AlertLog.getInstance().logInfo(AlertTag.Market, "TCashierRole", "Going home");
		Do("Going home");
		isActive = false;
		goHome = false;
	}
	
	public void setHost(THostRole h) {
		host = h;
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




