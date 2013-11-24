package simcity.KRestaurant;

import agent.*;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCustomer;
//import restaurant.interfaces.Market;
import simcity.interfaces.KWaiter;
import simcity.interfaces.MarketCashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Cook Agent
 */

public class KCashierRole extends Role implements KCashier{
	
	public List<Order> bills = Collections.synchronizedList(new ArrayList<Order>());
	public List<MarketBill> toPay = Collections.synchronizedList(new ArrayList<MarketBill>());
	public Map<String, Double > prices = new HashMap<String, Double>(); 

	public EventLog log;
	private double myMoney;
	
	private String name;
				
	private Timer timer;
	
	KCookRole cook; 
	public enum orderState 
	{ pending, computed, givenPayment, completed }
	
	public enum paidState
	{ notPaid, halfPaid, paid}
	
	public KCashierRole(PersonAgent p) {
		super(p);
		this.name = name;
		myMoney = 10;
		prices.put("Steak", 15.99);
		prices.put("Chicken",10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		log = new EventLog();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void msgBill(KCustomer c, KWaiter w, String choice ) {
		Do("got bill order from waiter");
		synchronized(bills) {
			for(Order o: bills) {
				if( o.c == c  && o.owesMoney) {
					o.s = orderState.pending;
					Do("this guy was a flake before");
					o.choice = choice;
					stateChanged();
					return;
				}
			}
		}
		LoggedEvent e = new LoggedEvent("Received bill to compute");
		log.add(e);
		bills.add(new Order(c, w, choice, orderState.pending));
		stateChanged();
	}

	public void msgPayment( double payment, KCustomer c, double check) {
		Do("got money from customer $" + payment);
		Order current = null;
		synchronized(bills) {
        for(Order o : bills) {
                if (o.c == c) {
                        current = o;
                }
        }
		}
        current.s = orderState.givenPayment;
        current.price = check;
        current.payment = payment;
        
        LoggedEvent e = new LoggedEvent("Received payment");
        log.add(e);
        
		stateChanged();
	}

	public void msgBillFromMarket(double check, MarketCashier m) {
		toPay.add(new MarketBill(m, check));
		Do("got bill from a market $"+check);
		LoggedEvent e = new LoggedEvent("received bill to pay");
		log.add(e);
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(bills) {
			for( Order o : bills ) {
				if ( o.s == orderState.pending) {
					compute(o);
					return true;
				}
			}		
		}
		synchronized(bills) {
			for( Order o : bills) {
				if ( o.s == orderState.givenPayment) {
					processPayment(o);
					return true;
				}
			}
		}
		synchronized(toPay) {
			for(MarketBill b : toPay) {
				if(b.paid == paidState.notPaid) {
					payMarket(b);
					return true;
				}
			}
		}
		return false;
	}

	// Actions

	public void compute(final Order o) {
		o.s = orderState.computed;
		Do("computing check and giving to waiter");
		LoggedEvent e = new LoggedEvent("computing check and giving it to waiter");
		log.add(e);
		o.price = prices.get(o.choice) + o.moneyOwed;
		Do("customer owes $" + o.price);
		o.w.msgHereIsCheck(o.c, o.price);
	}
	
	public void processPayment(Order o) {
		Do("processing payment");
		if ( o.payment >= o.price) {
			o.c.msgChange(o.payment - o.price);
			myMoney+=o.price;
			o.moneyOwed = 0;
			Do("customer has paid");
			LoggedEvent e = new LoggedEvent("customer has paid");
			log.add(e);
		}
		else if(o.payment < o.price) {
			Do("didn't give me enough money....");
			o.owesMoney = true;
			o.moneyOwed = o.price-o.payment;
			myMoney+=o.payment;
			LoggedEvent e = new LoggedEvent("customer is a flake and didn't have enough money");
			log.add(e);
		}
		synchronized(toPay) {
			for(MarketBill b : toPay) {
				if(b.paid == paidState.halfPaid) {
					b.paid = paidState.notPaid;
				}
			}
		}
		o.s = orderState.completed;
	}
	
	public void payMarket(MarketBill b) {
		Do("paying market");
		if(myMoney>=b.bill) {
			b.m.msgHereIsPayment(cook, b.bill);
			myMoney -= b.bill;
			b.paid = paidState.paid;
			LoggedEvent e = new LoggedEvent("paid Market");
			log.add(e);
		}
		else {  // myMoney < b.bill
			Do("Don't have enough money to pay off bill. Will pay the rest when I get more money");
			b.m.msgHereIsPayment(cook, myMoney);
			b.bill -= myMoney;
			myMoney = 0;
			b.paid = paidState.halfPaid;
			LoggedEvent e = new LoggedEvent("Don't have enough money...");
			log.add(e);
		}
	}
	
	//utilities	
	public void setCook(KCookRole c) {
		cook = c;
	}
	public class Order {
		public KCustomer c;
		KWaiter w;
		String choice;
		public double price;
		public orderState s;
		public double payment;
		public boolean owesMoney;
		public double moneyOwed = 0;
		
		public Order(KCustomer cust, KWaiter waiter, String c, orderState state) {
			w = waiter;
			this.c = cust;
			choice = c;
			s = state;
			owesMoney = false;
		}
		
		public Order(KCustomer c, double price, double payment, orderState s) {
			this.c = c;
			this.price = price;
			this.payment = payment;
			this.s = s;
		}
	}
	public class MarketBill{
		public MarketCashier m;
		public double bill;
		public paidState paid;
		
		public MarketBill(MarketCashier market, double b) {
			m = market;
			bill = b;
			paid = paidState.notPaid;
		}
	}
	
	public void setMoney(double money) {
		myMoney = money;
	}

	
}

