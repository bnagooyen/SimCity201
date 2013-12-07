package simcity.KRestaurant;

import agent.*;
import simcity.PersonAgent;
import simcity.KRestaurant.gui.KWaiterGui;
import simcity.Market.MFoodOrder;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCustomer;
//import restaurant.interfaces.Market;
import simcity.interfaces.KWaiter;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Cook Agent
 */

public class KCashierRole extends Role implements RestaurantCashier{
	
	public List<Order> bills = Collections.synchronizedList(new ArrayList<Order>());
	public List<MarketBill> toPay = Collections.synchronizedList(new ArrayList<MarketBill>());
	public Map<String, Double > prices = new HashMap<String, Double>(); 

	public EventLog log;
	private double myMoney;
	
	boolean goHome = false;
	public boolean arrived;
	
	private String name;
				
	private Timer timer;
	
	KCookRole cook; 
	KHostRole host;
	
	public enum orderState 
	{ pending, computed, givenPayment, completed }
	
	public enum paidState
	{ notPaid, halfPaid, paid, confirmed, inquiring}
	
	public KCashierRole() {
		super();
		this.name = name;
		myMoney = 10;
		prices.put("Steak", 15.99);
		prices.put("Chicken",10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		log = new EventLog();
		
		arrived = true;
		startHour = 11;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void msgConfirmingMarketCheck(boolean scammed, MarketManager m) {
		Do("check has been confirmed by cook");
		synchronized(toPay) {
			for(MarketBill b: toPay) {
				if(b.manager == m) {
					b.paid = paidState.confirmed;
					b.scammed = scammed;
				}
			}
		}
		stateChanged();
	}
	
	public void msgGoHome(double paycheck) {
		myPerson.money += paycheck;
		goHome = true;
		stateChanged();
	}	
	
	public void msgPayment(double payment, KCustomerRole c, double check) {
		System.out.println(myPerson.getName() + ": " +"got money from customer $" + payment);
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

	public void msgBill(KCustomer c, KWaiterRole w, String choice) {
		System.out.println(myPerson.getName() + ": " +"got bill order from waiter");
		synchronized(bills) {
			for(Order o: bills) {
				if( o.c == c  && o.owesMoney) {
					o.s = orderState.pending;
					System.out.println(myPerson.getName() + ": " +"this guy was a flake before");
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
	
	@Override
	public void msgBillFromMarket(double check, MarketCashier marketCashier,
			MarketManager manager) {
		toPay.add(new MarketBill(marketCashier, manager, check));
		System.out.println(myPerson.getName() + ": " +"got bill from a market $"+check);
		LoggedEvent e = new LoggedEvent("received bill to pay");
		log.add(e);
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
					confirmWithCook(b);
					return true;
				}
			}
		}
		synchronized(toPay) {
			for(MarketBill b : toPay) {
				if(b.paid == paidState.confirmed) {
					payMarket(b);
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

	private void confirmWithCook(MarketBill b) {
		b.paid = paidState.inquiring;
		cook.msgConfirmCheck(b.bill, b.manager);
	}

	private void tellHost() {
		System.out.println(myPerson.getName() + ": " +"telling manager I'm here at work");
		arrived = false;
		host.msgIAmHere(this, "cashier");
	}

	private void goHome() {
		System.out.println(myPerson.getName() + ": " +"Going home");
		isActive = false;
		goHome = false;
		host.msgHereIsMoney(myMoney);
		
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
		
		arrived = true;
	}

	public void compute(final Order o) {
		o.s = orderState.computed;
		System.out.println(myPerson.getName() + ": " +"computing check and giving to waiter");
		LoggedEvent e = new LoggedEvent("computing check and giving it to waiter");
		log.add(e);
		o.price = prices.get(o.choice) + o.moneyOwed;
		System.out.println(myPerson.getName() + ": " +"customer owes $" + o.price);
		o.w.msgHereIsCheck(o.c, o.price);
	}
	
	public void processPayment(Order o) {
		System.out.println(myPerson.getName() + ": " +"processing payment");
		if ( o.payment >= o.price) {
			o.c.msgChange(o.payment - o.price);
			myMoney+=o.price;
			o.moneyOwed = 0;
			System.out.println(myPerson.getName() + ": " +"customer has paid");
			LoggedEvent e = new LoggedEvent("customer has paid");
			log.add(e);
		}
		else if(o.payment < o.price) {
			System.out.println(myPerson.getName() + ": " +"didn't give me enough money....");
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
		System.out.println(myPerson.getName() + ": " +"paying market");
		if(b.scammed) {
			Do("we were scammed by market, but still paying them anyways");
		}
		if(myMoney>=b.bill) {
			b.cashier.msgHereIsPayment(cook, b.bill);
			myMoney -= b.bill;
			b.paid = paidState.paid;
			LoggedEvent e = new LoggedEvent("paid Market");
			log.add(e);
		}
		else {  // myMoney < b.bill
			System.out.println(myPerson.getName() + ": " +"Don't have enough money to pay off bill. Will pay the rest when I get more money");
			b.cashier.msgHereIsPayment(cook, myMoney);
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
		public MarketManager manager;
		public MarketCashier cashier;
		public double bill;
		public paidState paid;
		public boolean scammed;
		
		public MarketBill(MarketCashier cashier, MarketManager manager, double b) {
			this.cashier = cashier;
			this.manager = manager;
			bill = b;
			paid = paidState.notPaid;
		}
	}
	
	public void setMoney(double money) {
		myMoney = money;
	}

	public void setHost(KHostRole h) {
		host = h;
	}




	
}

